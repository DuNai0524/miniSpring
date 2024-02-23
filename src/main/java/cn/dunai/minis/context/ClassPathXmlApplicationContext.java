package cn.dunai.minis.context;

import cn.dunai.minis.beans.BeansException;
import cn.dunai.minis.beans.factory.BeanFactory;
import cn.dunai.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import cn.dunai.minis.beans.factory.config.AutowireCapableBeanFactory;
import cn.dunai.minis.beans.factory.config.BeanFactoryPostProcessor;
import cn.dunai.minis.beans.factory.support.SimpleBeanFactory;
import cn.dunai.minis.beans.factory.xml.XmlBeanDefinitionReader;
import cn.dunai.minis.core.ClassPathXmlResource;
import cn.dunai.minis.core.Resource;

import java.util.ArrayList;
import java.util.List;

public class ClassPathXmlApplicationContext implements BeanFactory {

    AutowireCapableBeanFactory beanFactory;

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    public ClassPathXmlApplicationContext(String fileName) throws BeansException {
        this(fileName,true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) throws BeansException {
        Resource resource = new ClassPathXmlResource(fileName);
        AutowireCapableBeanFactory beanFactory = new AutowireCapableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
        if (isRefresh){
            refresh();
        }
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        return this.beanFactory.getBean(beanName);
    }

    @Override
    public Boolean containsBean(String name) {
        return this.beanFactory.containsBean(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    public Class<?> getType(String name) {
        return null;
    }

    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return this.beanFactoryPostProcessors;
    }

    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor) {
        this.beanFactoryPostProcessors.add(beanFactoryPostProcessor);
    }

    public void refresh() throws BeansException,IllegalStateException {
        registerBeanPostProcessors(this.beanFactory);
        onRefresh();
    }

    public void registerBeanPostProcessors (AutowireCapableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    private void onRefresh() {
        this.beanFactory.refresh();
    }
}
