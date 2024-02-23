package cn.dunai.minis.beans.factory.xml;

import cn.dunai.minis.beans.factory.BeanFactory;
import cn.dunai.minis.beans.factory.SimpleBeanFactory;
import cn.dunai.minis.beans.factory.config.BeanDefinition;
import cn.dunai.minis.core.Resource;
import org.dom4j.Element;

public class XmlBeanDefinitionReader {
    SimpleBeanFactory simpleBeanFactory;
    public XmlBeanDefinitionReader(SimpleBeanFactory simpleBeanFactory){
        this.simpleBeanFactory = simpleBeanFactory;
    }
    public void loadBeanDefinitions(Resource resource) {
        while(resource.hasNext()){
            Element element = (Element) resource.next();
            String beanID =element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanID,beanClassName);
            this.simpleBeanFactory.registerBeanDefinition("",beanDefinition);
        }
    }
}
