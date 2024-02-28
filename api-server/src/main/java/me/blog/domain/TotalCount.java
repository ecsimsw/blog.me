package me.blog.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TotalCount {

    @Id
    private Integer articleId;

    private int count = 0;

    public void addCount(int count) {
        this.count+=count;
    }
}
