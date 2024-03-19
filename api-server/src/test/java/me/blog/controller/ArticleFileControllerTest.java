package me.blog.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import me.blog.alert.AlertManagerChain;
import me.blog.service.ContentService;
import me.blog.service.ViewCountCacheService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ArticleFileController.class)
class ArticleFileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContentService contentService;

    @MockBean
    private ViewCountCacheService cacheService;

    @MockBean
    private AlertManagerChain alertManagerChain;

    @DisplayName("GET::/article/{id}, 요청한 게시물의 컨텐츠 파일을 응답한다.")
    @Nested
    class ServeFile {

        @DisplayName("게시물 ID 에 해당하는 컨텐츠 파일 경로로 forwarding 한다.")
        @Test
        void serveArticleFile() throws Exception {
            var articleId = 1;
            var mockPath = "mockPath.html";
            when(contentService.getPathById(articleId))
                .thenReturn(mockPath);
            mockMvc
                .perform(get("/article/" + articleId))
                .andExpect(forwardedUrl("/" + mockPath));
        }

        @DisplayName("요청된 게시물의 조회 횟수를 기록한다.")
        @Test
        void verifyCount() throws Exception {
            var articleId = 1;
            var mockPath = "mockPath.html";
            when(contentService.getPathById(articleId))
                .thenReturn(mockPath);
            mockMvc
                .perform(get("/article/" + articleId))
                .andExpect(forwardedUrl("/" + mockPath));

            verify(cacheService, times(1))
                .count(articleId, 1);
        }
    }
}
