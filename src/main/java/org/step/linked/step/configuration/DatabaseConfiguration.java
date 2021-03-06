package org.step.linked.step.configuration;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

/*
1. Environment
2. BeanDefinitionReader - читает все бины в classpath
3. BeanFactory - создает beans из конструктора по умолчанию
4. BeanPostProcessor (before init) - модернизация бина процессором
5. PostConstruct - допольнительная логика, связанная с предварительным injection
6. BeanPostProcessor (after init) - создание прокси и дополнительная конфигурация
7. PreDestroy - когда закрывается контекст
 */
@Configuration
@ComponentScan(basePackages = "org.step.linked.step")
@PropertySources(value = {
        @PropertySource(value = "classpath:db.properties"),
        @PropertySource(value = "classpath:secret.properties")
})
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "org.step.linked.step.repository",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
@EnableAsync
@EnableJpaAuditing
public class DatabaseConfiguration {

    private final Environment environment;

    @Autowired
    public DatabaseConfiguration(Environment environment) {
        this.environment = environment;
    }

    /*
    Injection - @Autowired
    1. Field (@Autowired private Field field)
    2. Setter - final (@Autowired public void setField(Field field) { setter logic })
    3. Constructor - preferable type of injection
     */

    /*
    Абстракции!!!!
     */

    /*
    @Component
    @Repository
    @Service
    @Controller
    @RestController
     */

    /*
    Scope
    singleton - создается 1 раз за весь цикл жизни приложения (контекста) (по умолчанию активен)
    prototype - создается по требованию (injection), запрос бина
    request - создается для каждого http request'a
    session - для сессии пользователя (30 минут)
    globalSession - для глобальный сессии
     */
    /*
    По умолчанию ID bean'a = названию метода justEasyBean
    Но!!! Если мы записываем ID bean'a в аннотацию - ID берется из аннотации
     */

    @Bean("dataSource")
    @Profile({"default"})
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl(environment.getProperty("db.url"));
        dataSource.setUsername(environment.getProperty("db.username"));
        dataSource.setPassword(environment.getProperty("db.password"));
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("db.driver")));

        return dataSource;
    }

    @Bean("testDataSource")
    @Profile(value = {"test"})
    public DataSource testDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl(environment.getProperty("db.test.url"));
        dataSource.setUsername(environment.getProperty("db.test.username"));
        dataSource.setPassword(environment.getProperty("db.test.password"));
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("db.test.driver")));

        return dataSource;
    }

    @Bean
//    @DependsOn(value = {"dataSource"})
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource ds) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();

        emf.setDataSource(ds);
        emf.setPackagesToScan("org.step.linked.step.model");
        emf.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setJpaProperties(jpaProperties());

        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();

        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "none");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.generate_statistics", "true");
        properties.setProperty("hibernate.max_fetch_depth", "2");
        properties.setProperty("hibernate.jdbc.batch_size", "50");
        properties.setProperty("hibernate.jdbc.fetch_size", "50");
        properties.setProperty("hibernate.order_updates", "true");
        properties.setProperty("hibernate.order_inserts", "true");

        return properties;
    }
}
