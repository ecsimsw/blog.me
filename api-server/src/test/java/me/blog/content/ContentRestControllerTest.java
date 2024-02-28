package me.blog.content;

import static me.blog.fixture.Dummies.MOCK_ARTICLE_DATA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import me.blog.alert.AlertManagerChain;
import me.blog.dto.CategoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ContentRestController.class)
class ContentRestControllerTest {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContentService contentService;

    @MockBean
    private AlertManagerChain alertManagerChain;

    @DisplayName("GET::/api/article, 글 목록을 페이지 정보에 맞춰 조회한다.")
    @Nested
    class GetArticleWithPagination {

        private final int page = 0;
        private final int pageSize = MOCK_ARTICLE_DATA.size();

        @DisplayName("글 목록을 반환한다.")
        @Test
        void findAllArticle() throws Exception {
            when(contentService.articlesInCategory(Optional.empty(), PageRequest.of(0, pageSize)))
                .thenReturn(MOCK_ARTICLE_DATA);
            mockMvc
                .perform(get("/api/article")
                    .param("pageNumber", String.valueOf(page))
                    .param("pageSize", String.valueOf(pageSize))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(MOCK_ARTICLE_DATA)));
        }

        @DisplayName("카테고리별로 필터링 할 수 있다.")
        @Test
        void findAllArticleInCategory() throws Exception {
            var categoryName = "A";
            when(contentService.articlesInCategory(Optional.of(categoryName), PageRequest.of(page, pageSize)))
                .thenReturn(MOCK_ARTICLE_DATA);
            mockMvc
                .perform(get("/api/article")
                    .param("category", categoryName)
                    .param("pageNumber", String.valueOf(page))
                    .param("pageSize", String.valueOf(pageSize))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(MOCK_ARTICLE_DATA)));
        }

        @DisplayName("페이지 정보 없이 요청하는 경우 기본 페이지 값으로 요청된다.")
        @Test
        void findAllWithoutPagination() throws Exception {
            when(contentService.articlesInCategory(Optional.empty(),
                PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)))
                .thenReturn(MOCK_ARTICLE_DATA);
            mockMvc
                .perform(get("/api/article"))
                .andExpect(status().isOk())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(MOCK_ARTICLE_DATA)));
        }
    }

    @DisplayName("GET::/api/article/search, 제목으로 글을 검색할 수 있다.")
    @Nested
    class Search {

        @DisplayName("검색어가 제목에 포함된 글 목록을 반환한다.")
        @Test
        void searchArticle() throws Exception {
            var searchKeyword = "A";
            when(contentService.search(searchKeyword, PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)))
                .thenReturn(MOCK_ARTICLE_DATA);
            mockMvc
                .perform(get("/api/article/search")
                    .param("keyword", searchKeyword)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(MOCK_ARTICLE_DATA)));
        }

        @DisplayName("검색어가 없는 요청은 기본 키워드가 포함된 글 목록을 반환한다.")
        @Test
        void searchArticleWithoutKeyword() throws Exception {
            var defaultKeyword = "";
            when(contentService.search(defaultKeyword, PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)))
                .thenReturn(MOCK_ARTICLE_DATA);
            mockMvc
                .perform(get("/api/article/search"))
                .andExpect(status().isOk())
                .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(MOCK_ARTICLE_DATA)));
        }
    }

    @DisplayName("GET::/api/category, 카테고리 목록과 포함된 글 개수를 반환한다.")
    @Test
    void findAllCategory() throws Exception {
        var expected = List.of(
            CategoryResponse.of(new Category(1, "A"), 1),
            CategoryResponse.of(new Category(2, "B"), 2),
            CategoryResponse.of(new Category(3, "C"), 3)
        );
        when(contentService.categories()).thenReturn(expected);
        mockMvc
            .perform(get("/api/category"))
            .andExpect(status().isOk())
            .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(expected)));
    }

    @DisplayName("GET::/api/article/count, 글의 개수를 반환한다.")
    @Nested
    class Count {

        @DisplayName("전체 글 개수를 반환한다.")
        @Test
        void count() throws Exception {
            var mockArticleCount = 5;
            when(contentService.countArticleIn(Optional.empty()))
                .thenReturn(mockArticleCount);
            mockMvc
                .perform(get("/api/article/count"))
                .andExpect(status().isOk())
                .andExpect(content().json(String.valueOf(mockArticleCount)));
        }

        @DisplayName("카테고리에 포함되는 글 개수를 반환한다.")
        @Test
        void countInCategory() throws Exception {
            var category = "A";
            var mockArticleCount = 5;
            when(contentService.countArticleIn(Optional.of(category)))
                .thenReturn(mockArticleCount);
            mockMvc
                .perform(get("/api/article/count")
                    .param("category", category)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(String.valueOf(mockArticleCount)));
        }
    }
}