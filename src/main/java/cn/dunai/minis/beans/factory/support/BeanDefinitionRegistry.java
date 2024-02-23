package cn.dunai.minis.beans.factory.support;

import cn.dunai.minis.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, BeanDefinition bd);
    void removeBeanDefinition(String name);
    BeanDefinition getBeanDefinition(String name);
    boolean containsBeanDefinition(String name);
}
