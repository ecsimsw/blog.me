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

    public static final String DATA_ARTICLE_FILE_PATH = "database/data-article.txt";
    public static final String DATA_CATEGORY_FILE_PATH = "database/data-category.txt";
    public static final String DATA_SAFEBOX_FILE_PATH = "database/data-safebox.txt";

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
