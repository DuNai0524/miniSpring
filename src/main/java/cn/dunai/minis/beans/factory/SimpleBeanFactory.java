package cn.dunai.minis.beans.factory;

import cn.dunai.minis.beans.BeansException;
import cn.dunai.minis.beans.factory.config.BeanDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleBeanFactory implements BeanFactory{
    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private List<String> beanNames = new ArrayList<>();
    private Map<String, Object> singletons = new HashMap<>();

    public SimpleBeanFactory() {

    }

    // 容器核心方法
    @Override
    public Object getBean(String beanName) throws BeansException {
        // 先尝试获取 Bean 实例
        Object singleton = singletons.get(beanName);
        // 如果此时没有这个 Bean 的实例，就要获取定义来创造实例
        if (singleton == null) {
            int i = beanNames.indexOf(beanName);
            if (i == -1){
                throw new BeansException("Not found bean");
            }else{
                // 获取 Bean 的定义
                BeanDefinition beanDefinition = beanDefinitions.get(i);
                try{
                    singleton = Class.forName(beanDefinition.getClassName()).newInstance();
                } catch (Exception ignored){

                }
                // 注册 bean 实例
                singletons.put(beanDefinition.getId(),singleton);
            }
        }
        return singleton;
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {

    }
}
