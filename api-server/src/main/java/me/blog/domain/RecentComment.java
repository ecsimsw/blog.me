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
public class RecentComment {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotNull
    private String comment;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String link;

    private LocalDateTime updated = LocalDateTime.now();

    public RecentComment(String comment, String link) {
        this.comment = comment;
        this.link = link;
    }
}
