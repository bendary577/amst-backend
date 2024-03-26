package accountmanager.supporttool.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableJpaRepositories(basePackages = "accountmanager.supporttool.repository",
        entityManagerFactoryRef = "entityManager",
        transactionManagerRef = "transactionManager")
public class DynamicDatabaseRouter {

    public static final String PROPERTY_PREFIX = "spring.datasource.";

    @Autowired
    private Environment environment;

    @Bean
    @Primary
    @Scope("prototype")
    public AbstractRoutingDataSourceImpl dataSource() {
        Map<Object, Object> targetDataSources = getTargetDataSources();
        return new AbstractRoutingDataSourceImpl((DataSource)targetDataSources.get("default"), targetDataSources);
    }

    @Bean(name = "entityManager")
//    @Scope("prototype")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource()).packages("accountmanager.supporttool.model.app").build();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryBean(builder).getObject());
    }

    private Map<Object,Object> getTargetDataSources() {

        //loading the database names to a list from application.properties file
        List<String> databaseNames = environment.getProperty("spring.database-names.list",List.class);
        Map<Object,Object> targetDataSourceMap = new HashMap<>();

        for (String dbName : databaseNames) {

            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(environment.getProperty(PROPERTY_PREFIX + dbName + ".driver-class-name"));
            dataSource.setUrl(environment.getProperty(PROPERTY_PREFIX + dbName + ".url"));
            dataSource.setUsername(environment.getProperty(PROPERTY_PREFIX + dbName + ".username"));
            dataSource.setPassword(environment.getProperty(PROPERTY_PREFIX + dbName + ".password"));
            targetDataSourceMap.put(dbName,dataSource);

        }
        targetDataSourceMap.put("default",targetDataSourceMap.get(databaseNames.get(0)));
        return targetDataSourceMap;
    }
}
