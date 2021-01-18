package org.step.linked.step.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return Persistence.createEntityManagerFactory(environment.getProperty("PERSISTENCE_UNIT_NAME_DEV"));
    }
}
