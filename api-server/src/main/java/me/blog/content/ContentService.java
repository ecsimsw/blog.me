package me.blog.content;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContentService {

    private final Articles articles;
    private final Categories categories;

    public List<Article> search(String keyword, Pageable pageable) {
        var pageSize = pageable.getPageSize();
        var pageNumber = pageable.getPageNumber();
        return articles.findAllByTitleContains(keyword, pageSize, pageNumber);
    }

    public List<Article> articlesInCategory(Optional<String> optCategory, Pageable pageable) {
        var pageSize = pageable.getPageSize();
        var pageNumber = pageable.getPageNumber();
        if(optCategory.isEmpty()) {
            return articles.findAll(pageSize, pageNumber);
        }
        var categoryName = optCategory.orElseThrow();
        var category = categories.getByName(categoryName);
        return articles.findAllByCategory(category.index(), pageSize, pageNumber);
    }
}
