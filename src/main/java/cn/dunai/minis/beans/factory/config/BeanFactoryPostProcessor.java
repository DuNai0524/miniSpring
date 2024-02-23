package cn.dunai.minis.beans.factory.config;

import cn.dunai.minis.beans.BeansException;
import cn.dunai.minis.beans.factory.BeanFactory;

public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException;
}
