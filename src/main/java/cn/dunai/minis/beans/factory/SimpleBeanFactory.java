package cn.dunai.minis.beans.factory;

import cn.dunai.minis.beans.BeansException;
import cn.dunai.minis.beans.factory.config.BeanDefinition;
import cn.dunai.minis.beans.factory.support.DefaultSingletonBeanRegistry;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    private List<String> beanDefinitionNames = new ArrayList<>();

    public SimpleBeanFactory() {

    }

    public void registerBeanDefinition(String name,BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(name, beanDefinition);
        this.beanDefinitionNames.add(name);
        if(!beanDefinition.isLazyInit()){
            try{
                getBean(name);
            }catch (BeansException e){
                e.printStackTrace();
            }
        }
    }

    // 容器核心方法
    @Override
    public Object getBean(String beanName) throws BeansException {
        // 先尝试获取 Bean 实例
        Object singleton = this.getSingleton(beanName);
        // 如果此时没有这个 Bean 的实例，就要获取定义来创造实例
        if (singleton == null) {
            // 获取 bean 的定义
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if(beanDefinition == null) {
                throw new BeansException("No such bean.");
            }
            try{
                singleton = Class.forName(beanDefinition.getClassName()).newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            // 新注册这个 bean 实例
            this.registerSingleton(beanName, singleton);
        }
        return singleton;
    }

    public void removeBeanDefinition(String name){
        this.beanDefinitionMap.remove(name);
        this.beanDefinitionNames.remove(name);
        this.removeSingleton(name);
    }

    @Override
    public Boolean containsBean(String name){
        return containsSingleton(name);
    }

    public BeanDefinition getBeanDefinition(String name){
        return this.beanDefinitionMap.get(name);
    }

    public boolean containsBeanDefinition(String name){
        return this.beanDefinitionMap.containsKey(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return this.beanDefinitionMap.get(name).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) {
        return this.beanDefinitionMap.get(name).isPrototype();
    }

    public Class<?> getType(String name) {
        return this.beanDefinitionMap.get(name).getClass();
    }
}
