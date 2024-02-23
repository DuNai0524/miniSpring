package cn.dunai.minis.beans.factory.xml;

import cn.dunai.minis.beans.factory.support.SimpleBeanFactory;
import cn.dunai.minis.beans.factory.config.*;
import cn.dunai.minis.core.Resource;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class XmlBeanDefinitionReader {
    AutowireCapableBeanFactory beanFactory;
    public XmlBeanDefinitionReader(AutowireCapableBeanFactory beanFactory){
        this.beanFactory = beanFactory;
    }
    public void loadBeanDefinitions(Resource resource) {
        while(resource.hasNext()){
            Element element = (Element) resource.next();
            String beanID =element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanID,beanClassName);
            // 处理属性
            List<Element> propertyElements = element.elements("property");
            ConstructorPropertyValues PVS = new ConstructorPropertyValues();
            List<String> refs = new ArrayList<>();
            for( Element e: propertyElements ){
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                String pRef = e.attributeValue("ref");
                boolean isRef = false;
                String pV = "";
                if(pValue != null && !pValue.equals("")){
                    isRef = false;
                    pV = pValue;
                } else if (pRef != null && !pRef.equals("")){
                    isRef = true;
                    pV = pRef;
                    refs.add(pRef);
                }
                PVS.addPropertyValue(new ConstructorPropertyValue(pType,pName,pV,isRef));
            }
            beanDefinition.setPropertyValues(PVS);

            // 处理构造器参数
            List<Element> constructorsElements = element.elements("constructor-arg");
            ConstructorArgumentValues AVS = new ConstructorArgumentValues();
            for(Element e : constructorsElements){
                String aType = e.attributeValue("type");
                String aValue = e.attributeValue("value");
                String aName = e.attributeValue("name");
                AVS.addArgumentValue(new ConstructorArgumentValue(aType,aName,aValue));
            }
            beanDefinition.setConstructorArgumentValues(AVS);

            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);

            this.beanFactory.registerBeanDefinition(beanID,beanDefinition);
        }
    }
}
