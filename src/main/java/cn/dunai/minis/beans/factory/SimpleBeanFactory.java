package cn.dunai.minis.beans.factory;

import cn.dunai.minis.beans.BeansException;
import cn.dunai.minis.beans.factory.config.*;
import cn.dunai.minis.beans.factory.support.DefaultSingletonBeanRegistry;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    private List<String> beanDefinitionNames = new ArrayList<>();

    private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>(16);

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
            // 获取 bean 的定义，如果没有实例，则尝试从毛胚模型中获取
            singleton = this.earlySingletonObjects.get(beanName);
            if (singleton == null) {
                BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
                if (beanDefinition == null) {
                    throw new BeansException("No such bean.");
                }
                try {
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
        }
        return singleton;
    }

    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clz = null;
        // 创建毛胚 bean 实例
        Object obj = doCreateBean(beanDefinition);
        Constructor<?> con = null;
        // 存放到毛胚实例缓存中
        this.earlySingletonObjects.put(beanDefinition.getId(), obj);
        try {
            clz = Class.forName(beanDefinition.getClassName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        handleProperties(beanDefinition, clz, obj);
        return obj;
    }

    private void handleProperties(BeanDefinition bd, Class<?> clz,Object obj) {
        // 处理属性
        System.out.println("handle properties for bean:" + bd.getId());
        PropertyValues propertyValues = bd.getPropertyValues();
        if (!propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String pType = propertyValue.getType();
                String pName = propertyValue.getName();
                Object pValue = propertyValue.getValue();
                boolean isRef = propertyValue.isRef();
                Class<?>[]paramTypes = new Class<?>[1];
                Object[] paramValues = new Object[1];
                if(!isRef){ // 如果不是 ref，只是普通属性
                    // 对每一个属性，分数据分类型处理
                    if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                        paramTypes[0] = String.class;
                    } else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
                        paramTypes[0] = Integer.class;
                    } else if ("int".equals(pType)) {
                        paramTypes[0] = int.class;
                    } else { // 默认 String
                        paramTypes[0] = String.class;
                    }
                    paramValues[0] = pValue;
                }else{
                    try{
                        paramTypes[0] = Class.forName(pType);
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }
                    try{
                        paramValues[0] = getBean((String)pValue);
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }
                }

                // 按照 setXxxx 规范查找 setter 方法，调用 setter 方法设置属性
                String methodName = "set" + pName.substring(0,1).toUpperCase() + pName.substring(1);
                Method method = null;
                try{
                    method = clz.getMethod(methodName,paramTypes);
                }catch (Exception e){

                }
                try{
                    method.invoke(obj,paramValues);
                }catch (Exception e){

                }
            }
        }
    }

    // doCreateBean 创建毛胚实例，仅仅调用构造方法，没有进行属性处理
    private Object doCreateBean(BeanDefinition bd) {
        Class<?> clz = null;
        Object obj = null;
        Constructor<?> con = null;

        try{
            clz = Class.forName(bd.getClassName());

            // handler constructor
            ArgumentValues argumentValues = bd.getConstructorArgumentValues();
            // 如果有参数
            if (!argumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
                Object[] paramValues = new Object[argumentValues.getArgumentCount()];
                // 对于每一个参数，分数据类型分别处理
                for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
                    ArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
                    if ("String".equals(argumentValue.getType()) || "java.lang.String".equals(argumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    } else if ("Integer".equals(argumentValue.getType()) || "java.lang.Integer".equals(argumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.parseInt((String) argumentValue.getValue());
                    } else if ("int".equals(argumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.parseInt((String) argumentValue.getValue());
                    } else { // 默认 String
                        paramTypes[i] = Class.forName(argumentValue.getType());
                        paramValues[i] = argumentValue.getValue();

                    }
                }
                try {
                    con = clz.getConstructor(paramTypes);
                    obj = con.newInstance(paramValues);
                } catch (Exception e) {

                }
            } else {
                obj = clz.newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(bd.getId() + "bean created." + bd.getClassName() + " : " + obj.toString());
        return obj;
    }

    public void refresh() {
        for (String beanName : beanDefinitionNames) {
            try{
                getBean(beanName);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
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
