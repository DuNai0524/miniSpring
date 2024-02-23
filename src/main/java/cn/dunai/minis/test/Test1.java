package cn.dunai.minis.test;

import cn.dunai.minis.beans.BeansException;
import cn.dunai.minis.context.ClassPathXmlApplicationContext;

public class Test1 {
    public static void main(String[] args) throws BeansException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        AService aService = (AService) context.getBean("aservice");
        aService.sayHello();
    }
}
