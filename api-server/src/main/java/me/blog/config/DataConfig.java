package me.blog.config;

import java.util.List;
import me.blog.content.Article;
import me.blog.content.Articles;
import me.blog.content.Categories;
import me.blog.data.ArticleDao;
import me.blog.data.CategoryDao;
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
