package me.blog.config;

import me.blog.data.ArticleDao;
import me.blog.data.CategoryDao;
import me.blog.data.SafeBoxDao;
import me.blog.domain.ArticleRepository;
import me.blog.domain.CategoryRepository;
import me.blog.domain.SafeBoxRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig {

    @Bean
    public ArticleRepository articles(ArticleDao articleDao) {
        var entities = articleDao.readDataFile();
        return new ArticleRepository(entities);
    }

    @Bean
    public CategoryRepository categories(CategoryDao categoryDao) {
        var entities = categoryDao.readDataFile();
        return new CategoryRepository(entities);
    }

    @Bean
    public SafeBoxRepository safeBoxes(SafeBoxDao safeBoxDao) {
         var entities = safeBoxDao.readDataFile();
         return new SafeBoxRepository(entities);
    }
}
