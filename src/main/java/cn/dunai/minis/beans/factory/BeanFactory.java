package cn.dunai.minis.beans.factory;

import cn.dunai.minis.beans.factory.config.BeanDefinition;
import cn.dunai.minis.beans.BeansException;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
    Boolean containsBean(String name);
    boolean isSingleton(String name);
    boolean isPrototype(String name);
}
