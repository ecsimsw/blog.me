package me.blog.data;

import me.blog.domain.Articles;
import me.blog.domain.Categories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig {

    @Bean
    public Articles articles(ArticleDao articleDao) {
        var data = articleDao.readDataFile();
        return new Articles(data);
    }

    @Bean
    public Categories categories(CategoryDao categoryDao) {
        var data = categoryDao.readDataFile();
        return new Categories(data);
    }
}
