package org.step.linked.step;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.step.linked.step.configuration.DatabaseConfiguration;
import org.step.linked.step.model.User;
import org.step.linked.step.service.IDGenerator;
import org.step.linked.step.service.UserService;
import org.step.linked.step.study.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Runner {

    public static void main(String[] args) throws IOException {
        // BeanDefinitionReader - считывает все классы на наличие аннотаций
//        ApplicationContext fromAnnotation = new AnnotationConfigApplicationContext("org.step.linked.step");
        AnnotationConfigApplicationContext fromAnnotation = new AnnotationConfigApplicationContext(DatabaseConfiguration.class);

        SpecialService specialThing = fromAnnotation.getBean("specialThing", SpecialService.class);

        System.out.println(specialThing);

        System.out.println("This is very special thing: " + specialThing.specialThing());

        IDGenerator<String> uuidGenerator = fromAnnotation.getBean("baseIdGenerator", IDGenerator.class);
        UserService userService = fromAnnotation.getBean("userServiceImpl", UserService.class);

        NotBeanClass notBeanClass = fromAnnotation.getBean("notBeanClass", NotBeanClass.class);

        System.out.println(notBeanClass);

        User user = User.builder().username("foo-username@mail.ru").password("bla-password").age(25).build();
        userService.save(user);
        List<User> all = userService.findAll();
        userService.update(User.builder().id(all.get(0).getId()).username("hop@mail.ru").build());

        userService.findAll().forEach(u -> System.out.printf("User with username %s%n", u.getUsername()));

        System.out.println("The list is " + all);

        String id = uuidGenerator.generate();

        String resource = getResource(fromAnnotation);
        String javaHome = getEnvironment(fromAnnotation, "PERSISTENCE_UNIT_NAME_DEV");

        System.out.println(javaHome);
        System.out.println(resource);

        System.out.printf("Generated ID is %s%n", id);

        fromAnnotation.close();
    }

    public static String getEnvironment(ApplicationContext context, String propertyName) {
        Environment environment = context.getEnvironment();
        boolean isContains = environment.containsProperty(propertyName);
        if (isContains) {
            return environment.getProperty(propertyName);
        }
        return null;
    }

    public static String getResource(ApplicationContext context) throws IOException {
        Resource resource = context.getResource("classpath:beans.xml");
        boolean isExists = resource.exists();
        if (isExists) {
            InputStream inputStream = resource.getInputStream();
            byte[] bytes = inputStream.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return null;
    }

    public static void example() {
        ApplicationContext context = new ClassPathXmlApplicationContext("src/main/webapp/WEB-INF/beans.xml");

        EasyBean easyBean = context.getBean("easyBean", EasyBean.class);
        EasyBean easyBeanSecond = context.getBean("easyBeanSecond", EasyBean.class);
        SuperBean superBean = context.getBean("superBean", SuperBean.class);

        System.out.println(easyBean.equals(easyBeanSecond));

        System.out.println(easyBeanSecond.getMessage());
        System.out.println(easyBean.getMessage());
        System.out.println(superBean.getBean().getMessage());

        HashBean first = context.getBean("publicHashBean", HashBean.class);
        HashBean second = context.getBean("publicHashBean", HashBean.class);
        System.out.printf("Result: %b%n", first.equals(second));
    }
}