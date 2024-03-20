package accountmanager.supporttool.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class JDBCTemplateConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcTemplate JdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

}
