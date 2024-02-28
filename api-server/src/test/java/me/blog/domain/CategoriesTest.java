package me.blog.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import me.blog.domain.Categories;
import me.blog.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoriesTest {

    private static final List<Category> MOCK_CATEGORY_DATA = List.of(
        new Category(1, "A"),
        new Category(2, "B"),
        new Category(3, "C"),
        new Category(4, "D"),
        new Category(5, "E")
    );

    private Categories categories;

    @BeforeEach
    public void initArticles() {
        this.categories = new Categories(MOCK_CATEGORY_DATA);
    }

    @DisplayName("전체 카테고리 목록을 반환한다.")
    @Test
    void findAll() {
        var result = categories.findAll();
        assertEquals(MOCK_CATEGORY_DATA, result);
    }

    @DisplayName("반환 목록은 방어적 복사되어 변경이 원본 리스트에 전달되지 않는다.")
    @Test
    void testDefensiveCopy() {
        var result = categories.findAll();
        result.remove(0);
        assertEquals(MOCK_CATEGORY_DATA, categories.findAll());
    }

    @DisplayName("카테고리 이름에 해당하는 카테고리를 반환한다.")
    @Test
    void getByName() {
        var result = categories.getByName("A");
        var expected = MOCK_CATEGORY_DATA.get(0);
        assertEquals(expected, result);
    }

    @DisplayName("전체 카테고리 개수를 반환한다.")
    @Test
    void size() {
        var result = categories.size();
        var expected = MOCK_CATEGORY_DATA.size();
        assertEquals(expected, result);
    }
}