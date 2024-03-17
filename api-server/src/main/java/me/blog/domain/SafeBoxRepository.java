package me.blog.domain;

import java.util.ArrayList;
import java.util.List;

public class SafeBoxRepository {

    private final List<SafeBox> safeBoxes;

    public SafeBoxRepository(List<SafeBox> safeBoxes) {
        this.safeBoxes = new ArrayList<>(safeBoxes);
    }

    public boolean isExistsByArticleId(int id) {
        return safeBoxes.stream()
            .anyMatch(it -> it.articleId() == id);
    }
}
