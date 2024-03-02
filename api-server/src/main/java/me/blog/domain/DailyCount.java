package me.blog.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@Table(indexes = {
    @Index(name = "idx_art_date_cnt", columnList = "articleId, date, count", unique = true)
})
@Entity
public class DailyCount {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotNull
    private int articleId;

    @NotNull
    private LocalDate date;

    private int count = 0;

    public DailyCount(int articleId, int count, LocalDate date) {
        this.articleId = articleId;
        this.date = date;
        this.count = count;
    }

    public void addCount(int addCount) {
        this.count += addCount;
    }
}