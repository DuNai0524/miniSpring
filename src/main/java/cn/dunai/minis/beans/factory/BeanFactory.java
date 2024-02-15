package cn.dunai.minis.beans.factory;

import cn.dunai.minis.beans.factory.config.BeanDefinition;
import cn.dunai.minis.beans.BeansException;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
    void registerBeanDefinition(BeanDefinition beanDefinition);
}
