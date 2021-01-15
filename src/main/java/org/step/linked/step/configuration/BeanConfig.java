package org.step.linked.step.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.step.linked.step.study.EasyBean;
import org.step.linked.step.study.SuperBean;

@Configuration
public class BeanConfig {

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
    @Bean(name = "blablaBean")
    @Scope(scopeName = "prototype")
    public EasyBean justEasyBean() {
        return new EasyBean(3, "good message");
    }

    @Bean
    public SuperBean justSuperBean() {
        return new SuperBean(justEasyBean());
    }
}
