package org.step.linked.step.study.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.stereotype.Component;
import org.step.linked.step.study.Insert;
import org.step.linked.step.study.SpecialAnnotation;
import org.step.linked.step.study.impl.SpecialThingImpl;

import java.lang.reflect.Field;

@Component
public class SpecialAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final BeanFactory factory;

    @Autowired
    public SpecialAnnotationBeanPostProcessor(BeanFactory factory) {
        this.factory = factory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        boolean isPresent = beanClass.isAnnotationPresent(SpecialAnnotation.class);
        if (isPresent) {
//            SpecialThingImpl bean1 = factory.getBean(beanName, SpecialThingImpl.class);
            Field[] declaredFields = beanClass.getDeclaredFields();
            for (Field f : declaredFields) {
                if (f.isAnnotationPresent(Insert.class)) {
                    f.setAccessible(true);
                    try {
                        f.set(bean, beanClass.getAnnotation(SpecialAnnotation.class).secretMessage());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Bad things happened");
                    }
                }
            }
            System.out.println("SECOND STEP: BeanPostProcessor - before init " + beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        boolean isPresent = bean.getClass().isAnnotationPresent(SpecialAnnotation.class);
        if (isPresent) {
            Object proxyBean = Proxy.newProxyInstance(
                    bean.getClass().getClassLoader(),
                    bean.getClass().getInterfaces(),
                    (proxy, method, methodArgs) -> {
                        System.out.println("This is method");
                        if (method.getName().equals("specialThing")) {
                            return "New value";
                        } else {
                            return method.invoke(bean, methodArgs);
                        }
                    }
            );
            System.out.println("THIRD STEP: BeanPostProcess - after init " + beanName);
            return proxyBean;
        }
        return bean;
    }
}
