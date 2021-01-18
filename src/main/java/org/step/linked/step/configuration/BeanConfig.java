package org.step.linked.step.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.step.linked.step.study.EasyBean;
import org.step.linked.step.study.SuperBean;

@Configuration
public class BeanConfig {

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
