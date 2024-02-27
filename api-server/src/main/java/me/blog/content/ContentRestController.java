package me.blog.content;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.blog.dto.CategoryResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ContentRestController {

    private final ContentService contentService;

    @GetMapping("/api/article")
    public ResponseEntity<List<Article>> findAllArticle(@RequestParam Optional<String> category, Pageable pageable) {
        var results = contentService.articlesInCategory(category, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/api/article/search")
    public ResponseEntity<List<Article>> searchArticle(
        @RequestParam(required = false, defaultValue = "") String keyword, Pageable pageable
    ) {
        var results = contentService.search(keyword, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/api/category")
    public ResponseEntity<List<CategoryResponse>> findAllCategory() {
        return ResponseEntity.ok(contentService.categories());
    }

    @GetMapping("/api/article/count")
    public ResponseEntity<Integer> count(@RequestParam Optional<String> category) {
        var results = contentService.countArticleIn(category);
        return ResponseEntity.ok(results);
    }
}
