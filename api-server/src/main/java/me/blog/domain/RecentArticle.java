package me.blog.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class RecentArticle {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotNull
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String link;

    private LocalDateTime updated = LocalDateTime.now();

    public RecentArticle(String title, String link) {
        this.title = title;
        this.link = link;
    }
}
