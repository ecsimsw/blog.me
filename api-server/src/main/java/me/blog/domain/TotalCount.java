package me.blog.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
    @Index(name = "idx_cnt", columnList = "count")
})
@Entity
public class TotalCount {

    @Id
    private Integer articleId;

    private int count = 0;

    public void addCount(int count) {
        this.count+=count;
    }
}
