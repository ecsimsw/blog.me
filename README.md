## blog.me

내 첫번째 보물은 가족 사진, 두번째는 일기장, 그리고 세번째 보물은 티스토리 블로그이다.        
블로그 글을 백업하는 프로그램 제작에서 시작해서, 직접 서버를 운영해보고 싶었다.         
별거 없고, 못생겼지만, 절대 잃어선 안될 자료들

## 기능

1. 블로그 글을 수집할 수 있다.
2. 카테고리 별 글을 확인할 수 있다.
3. 게시물 별 조회수를 확인할 수 있다.
4. 전체 혹은 날짜 별 최다 조회수 게시물을 집계 할 수 있다.

## 기록 

### 1. 글 수집

- Jsoup 으로 티스토리 작성 글의 html 을 수집한다.        
- 게시물의 html 을 파싱하여 글 제목과 본문을 수집하고, 미리 만들어둔 html 템플릿과 css 파일에 이를 삽입하고 파일로 저장한다.          
    
### 2. 조회수 캐시 

- 조회수를 메모리에 기록해두었다가, 10초에 한번 그 기간동안 발생한 수를 DB에 기록한다.        
- 게시물 별 조회수를 기록해야 했기에 Map 을 사용했다.    
- 동시성 문제를 고려하여 ConcurrentMap, 원자성이 보장된 연산을 사용하여 조회수를 담는다. 


``` java
private final ConcurrentMap<Integer, Integer> countCachePerArticle = new ConcurrentHashMap<>();

public void count(int articleId, int count) {
    countCachePerArticle.compute(articleId, (k, v) -> {
        if (v == null) {
            return count;
        }
        return v + count;
    });
}
```

- DB 저장시에 조회에 락을 걸어 동시성 문제를 처리한다.    
- {articleId, date}로 인덱스를 설정하고 row lock 처리하여, 트랜잭션의 {게시물:날짜}에 해당하는 데이터 외에 다른 row 처리에서 영향이 없도록 하였다.     
- DB 데이터 업데이트가 잦지 않아 동시에 발생할 일이 극히 드물고, 그 트랜잭션의 처리 시간이 적기에 재시도 로직이 불필요하고 처리가 확실한 비관적 락을 선택했다.    

``` java
public interface DailyCountRepository extends JpaRepository<DailyCount, Long> {
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<DailyCount> findByArticleIdAndDate(int articleId, LocalDate date);
}
```

### 3. 집계 정보 캐시
- 최다 조회 게시글 순위, 어제 전체 조회수, 가장 최신 등록 글 등, 집계에 많은 비용이 들지만 매번 계산할 필요가 없는 데이터가 많았다. 이를 메모리에 캐시로 저장해두고 사용하고 싶었다.
- 조회수 갱신이 아니라 2시간에 한번 최다 조회 계시물 집계, 일에 한번 어제 전체 조회수 집계 등 데이터 업데이트가 아니라, Miss가 될 때 집계하는 방식을 택했다.     
- TTL에 의해 자동으로 캐시가 만기되고, Cache miss 가 났을 때 계산으로 재집계가 처리되었으면 했다.    
- ConccurentHashMap은 TTL은 지원하지 않기에, Caffeine cache를 CacheManager로 사용한다.    

``` java
@Cacheable(value = Cached.DAILY_VIEW_COUNT, key = "#date")
@Transactional
public int viewCountAt(LocalDate date) {
    return dailyCountRepository.findAllByDate(date).stream()
        .mapToInt(DailyCount::getCount)
        .sum();
}
```

- 분산 환경을 고려하지 않았다. WAS가 여러개라면 캐시에 싱크가 어긋나 응답이 WAS마다 다른 상황이 발생할 것이다.           
- 당장은 WAS 한개로 충분하다고 생각했고, 분산 환경을 고려하여 WAS 간 공유를 위한 자원, 인프라를 추가하고 싶지 않았다.     


### 4. 게시물 파일 포워딩

- 사용자가 게시물을 조회하는 경우 게시물 html 파일에 직접 접근을 피하고, 파일 경로를 숨기고 싶었다.
- 게시물 조회를 원할 경우 WAS에 조회하는 게시물 Id 를 전달하면, WAS는 그 Id로 DB에서 해당 게시물의 물리 경로를 찾고 그 파일 응답으로 Forwarding 한다.
- 사용자로부터 파일 경로를 숨기고, 파일 조회시 로직을 추가할 수 있었다. (ex, 비공개 게시물, 게시물 별 조회수 계산)     

``` java
@GetMapping("/api/article/{id}")
public String serveArticleFile(@PathVariable int id) {
    cacheService.count(id, 1);
    var filePath = contentService.getPathById(id);
    return "forward:/" + fileRootDirectory + filePath;
}
```

### 5. 리버스 프록시와 Nginx
- WAS 전면에 Nginx 를 리버스 프록시로 두었다. 이 프로젝트에서 리버스 프록시의 역할은 다음과 같다.
1. TLS 설정을 WAS 전면에 한다. Http 요청을 Https 요청으로 넘긴다.
2. 정적 자원을 응답한다. WAS까지 가지 않아도 되는 기본적인 html, css, js 파일을 Nginx 에서 직접 응답한다.
3. Rate limit을 설정한다. 5req/1sec, burst=5, nodelay 로 처리한다.
4. WAS에서 정확한 사용자 ip 로깅을 위해 라우팅하는 요청 Header에 사용자 Ip를 포함시켜 전달한다.

```
# route api
location /api/ {
   proxy_pass http://api-service;
   proxy_set_header Host            $host;          
   proxy_set_header X-Real-IP $remote_addr;
   proxy_set_header X-Forwarded-For $remote_addr;  

   limit_req zone=default_rate_limit burst=5 nodelay;
   limit_req_status 429;
}

# static files
location /static/ {
    root /;
}
```

### 5. Failover 서버와 GSLB
- 비용을 아끼고자 메인 서버는 홈 서버로 하고, 가장 저렴한 클라우드 pc에 임시 서버를 운영한다.
- 게이트웨이로 메인 서버가 다운되면 임시 서버로 라우팅되도록 처리하려고 했으나, 게이트웨이의 노드가 다운되면 결국 전체 서비스 장애가 나는 것은 똑같았다.
- GSLB 를 키워드로 공부하고 Route53, 상태 검사를 적용했다.
- Route53의 기본 상태 검사 프로토콜(Http)을 사용하기 위해 80번 포트에 헬스 체크용 API를 열고, 나머지 요청은 https로 리다이렉트 시킨다.

``` 
server {
  listen 80;

  location /health {
    access_log off;
    add_header 'Content-Type' 'application/json';
    return 200 '{"status":"UP"}';
  }

  location / {
    return 301 https://www.ecsimsw.com$request_uri;
  }
}
```

### 6. ShutDown 라이브러리

- 임시 서버에선 DB를 사용하지 않는다. 메인 서버와 DB가 같은 노드에 있기 때문에 메인 서버가 다운되면 DB도 함께 다운되어 의미가 없기 떄문이다.
- 최소한의 주요 서비스 (게시물 조회, 카테고리 조회)를 남기고 나머지 API는 "사용 불가"를 요청한다.
- 임시 서버 API를 위한 코드 변경과 하드 코딩을 최소화하기 위해 ShutDown 필터를 개발했다.

``` java
@ShutDown(
    conditionOnActiveProfile = "failover",
    message = "service unavailable",
    status = HttpStatus.SERVICE_UNAVAILABLE,
    contentType = "application/json"
)
@RequiredArgsConstructor
@RestController
public class ViewCountRestController {
```

- 제작한 @ShutDown 어노테이션에 조건과 응답 내용을 정의하면, 그 조건에 따라 해당 Controller 의 API 핸들러를 읽고 임시 응답을 반환하는 필터가 등록된다.
- 프로젝트에선 failover profile 일 경우, ViewCountRestController의 핸들러에 요청이 들어올 경우, 503 과 응답 메시지로 사용 불가를 알린다.




## 구조 

![image](https://github.com/ecsimsw/ecsimsw.blog/assets/46060746/dd2a3161-b905-412d-bb93-f2edcac2230f)



## 기술 

Java17, Spring boot2.7.9, Hibernate, Jsoup    
Mysql, H2, Caffeine    
JUnit5, Mockito     
Nginx, Docker, Route53        
JIB, Github actions, GHCR    


## 스크린 샷
![image](https://github.com/ecsimsw/ecsimsw.blog/assets/46060746/3e1a8731-d8ca-4411-8696-3fbcb946a9e5)


![image](https://github.com/ecsimsw/ecsimsw.blog/assets/46060746/744dcf12-05b1-4b2f-90a0-ffff09c0e356)


![image](https://github.com/ecsimsw/ecsimsw.blog/assets/46060746/a44f631e-bc21-4d03-ab32-bc14050a9c50)

