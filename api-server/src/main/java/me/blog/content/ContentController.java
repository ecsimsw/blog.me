package me.blog.content;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ContentController {

    private final Articles articles;
    private final Categories categories;
    private final ContentService contentService;

    @GetMapping("/api/article/{id}")
    public ResponseEntity<Article> findArticle(@PathVariable int id) {
        var result = articles.getById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/article")
    public ResponseEntity<List<Article>> findAllArticle(Pageable pageable, @RequestParam Optional<String> category) {
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
    public ResponseEntity<List<Category>> findAllCategory() {
        return ResponseEntity.ok(categories.findAll());
    }
}
