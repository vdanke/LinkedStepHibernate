package org.step.linked.step.study.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import org.step.linked.step.study.NotBeanClass;

@Component
public class SpecialAnnotationBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        System.out.println("FIRST STEP: Factory is called");
        NotBeanClass bean = configurableListableBeanFactory.createBean(NotBeanClass.class);
        configurableListableBeanFactory.registerSingleton("notBeanClass", bean);
    }
}
