package cn.dunai.minis.context;

import cn.dunai.minis.beans.BeansException;
import cn.dunai.minis.beans.factory.BeanFactory;
import cn.dunai.minis.beans.factory.SimpleBeanFactory;
import cn.dunai.minis.beans.factory.config.BeanDefinition;
import cn.dunai.minis.beans.factory.xml.XmlBeanDefinitionReader;
import cn.dunai.minis.core.ClassPathXmlResource;
import cn.dunai.minis.core.Resource;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.*;

public class ClassPathXmlApplicationContext implements BeanFactory {

    BeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName) {
        Resource resource = new ClassPathXmlResource(fileName);
        BeanFactory beanFactory = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        return this.beanFactory.getBean(beanName);
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanFactory.registerBeanDefinition(beanDefinition);
    }
}
