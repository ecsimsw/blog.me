package me.blog.content;

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

    @GetMapping("/api/article/{id}")
    public ResponseEntity<Article> findArticle(@PathVariable int id) {
        var result = articles.findById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/article")
    public ResponseEntity<List<Article>> findAllArticle(Pageable pageable, @RequestParam(required = false) String category) {
        if(category == null) {
            var results = articles.findAll(pageable.getPageSize(), pageable.getPageNumber());
            return ResponseEntity.ok(results);
        } else {
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/api/article/search")
    public ResponseEntity<List<Article>> searchArticle(
        @RequestParam(required = false, defaultValue = "") String title,
        Pageable pageable
    ) {
        var results = articles.search(title, pageable.getPageSize(), pageable.getPageNumber());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/api/category")
    public ResponseEntity<List<Category>> findAllCategory() {
        return ResponseEntity.ok(categories.findAll());
    }
}
