package cn.dunai.minis.beans.factory.config;

public interface SingletonBeanRegistry {
    void registerSingleton(String beanName, Object singletoneObject);
    Object getSingleton(String beanName);
    boolean containsSingleton(String beanName);
    String[] getSingletonNames();
}
