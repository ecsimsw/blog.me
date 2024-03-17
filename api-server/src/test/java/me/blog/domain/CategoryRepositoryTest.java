package me.blog.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryTest {

    private static final List<Category> MOCK_CATEGORY_DATA = List.of(
        new Category(1, "A"),
        new Category(2, "B"),
        new Category(3, "C"),
        new Category(4, "D"),
        new Category(5, "E")
    );

    private CategoryRepository categoryRepository;

    @BeforeEach
    public void initArticles() {
        this.categoryRepository = new CategoryRepository(MOCK_CATEGORY_DATA);
    }

    @DisplayName("전체 카테고리 목록을 반환한다.")
    @Test
    void findAll() {
        var result = categoryRepository.findAll();
        assertEquals(MOCK_CATEGORY_DATA, result);
    }

    @DisplayName("반환 목록은 방어적 복사되어 변경이 원본 리스트에 전달되지 않는다.")
    @Test
    void testDefensiveCopy() {
        var result = categoryRepository.findAll();
        result.remove(0);
        assertEquals(MOCK_CATEGORY_DATA, categoryRepository.findAll());
    }

    @DisplayName("카테고리 이름에 해당하는 카테고리를 반환한다.")
    @Test
    void getByName() {
        var result = categoryRepository.getByName("A");
        var expected = MOCK_CATEGORY_DATA.get(0);
        assertEquals(expected, result);
    }

    @DisplayName("전체 카테고리 개수를 반환한다.")
    @Test
    void size() {
        var result = categoryRepository.size();
        var expected = MOCK_CATEGORY_DATA.size();
        assertEquals(expected, result);
    }
}