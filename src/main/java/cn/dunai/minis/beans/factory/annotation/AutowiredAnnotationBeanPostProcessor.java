package cn.dunai.minis.beans.factory.annotation;

import cn.dunai.minis.beans.BeansException;
import cn.dunai.minis.beans.factory.config.AutowireCapableBeanFactory;
import cn.dunai.minis.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    private AutowireCapableBeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object result = bean;

        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if(fields!=null){
            // 对每一个属性进行判断，如果带有 @Autowired 注解，则进行处理
            for(Field field:fields){
                boolean isAutowired = field.isAnnotationPresent(Autowired.class);
                if(isAutowired) {
                    String fieldName = field.getName();
                    Object autowiredObj = this.getBeanFactory().getBean(fieldName);
                    // 设置属性值，完成注入
                    try{
                        field.setAccessible(true);
                        field.set(bean,autowiredObj);
                        System.out.println("autowired " + fieldName + " for bean " + beanName);
                    }catch (Exception e){

                    }
                }
            }
        }
        return result;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    public AutowireCapableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

}
