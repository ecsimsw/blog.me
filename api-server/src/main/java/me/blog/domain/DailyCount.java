package me.blog.domain;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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