## blog.me

내 첫번째 보물은 가족 사진, 두번째는 일기장, 그리고 세번째 보물은 티스토리 블로그이다.        
블로그 글을 백업하는 프로그램 제작에서 시작해서, 직접 서버를 운영해보고 싶었다.         
별거 없고, 못생겼지만, 절대 잃어선 안될 자료들

## 기능

1. 블로그 글을 수집할 수 있다.
2. 카테고리 별 글을 확인할 수 있다.
3. 게시물 별 조회수를 확인할 수 있다.
4. 전체 혹은 날짜 별 최다 조회수 게시물을 집계 할 수 있다.

## 구조 

![image](https://github.com/ecsimsw/ecsimsw.blog/assets/46060746/dd2a3161-b905-412d-bb93-f2edcac2230f)

## 기록 

### 1. 글 수집

Jsoup 으로 티스토리 작성 글의 html 을 수집한다.        
게시물의 html 을 파싱하여 글 제목과 본문을 수집하고, 미리 만들어둔 html 템플릿과 css 파일에 이를 삽입하고 파일로 저장한다.          

    
### 2. 조회수 캐시 

조회수를 메모리에 기록해두었다가, 10초에 한번 그 기간동안 발생한 수를 DB에 기록한다.        
게시물 별 조회수를 기록해야 했기에 Map 을 사용했다.
동시성 문제를 고려하여 ConcurrentMap, 원자성이 보장된 연산을 사용하여 조회수를 담는다. 


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

DB 저장시에 조회에 락을 걸어 동시성 문제를 처리한다.    
{articleId, date}로 인덱스를 설정하고 row lock 처리하여, 트랜잭션의 {게시물:날짜}에 해당하는 데이터 외에 다른 row 처리에서 영향이 없도록 하였다.     
DB 데이터 업데이트가 잦지 않아 동시에 발생할 일이 극히 드물고, 그 트랜잭션의 처리 시간이 적기에 재시도 로직이 불필요하고 처리가 확실한 비관적 락을 선택했다.    

``` java
public interface DailyCountRepository extends JpaRepository<DailyCount, Long> {
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<DailyCount> findByArticleIdAndDate(int articleId, LocalDate date);
}
```

### 3. 집계 정보 캐시

최다 조회 게시글 순위, 어제 전체 조회수, 가장 최신 등록 글 등, 집계에 많은 비용이 들지만 매번 계산할 필요가 없는 데이터가 많았다.      
이를 메모리에 캐시로 저장해두고 사용하고 싶었다.      

조회수 갱신이 아니라 2시간에 한번 최다 조회 계시물 집계, 일에 한번 어제 전체 조회수 집계 등 데이터 업데이트가 아니라, 캐시의 수명이 명확했으면 했다.     
TTL에 의해 자동으로 캐시가 만기되고, Cache miss 가 났을 때 계산으로 재집계가 처리되었으면 했다.    
ConccurentHashMap은 TTL은 지원하지 않기에, Caffeine cache를 CacheManager로 사용하였다.     


### 4. 






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

