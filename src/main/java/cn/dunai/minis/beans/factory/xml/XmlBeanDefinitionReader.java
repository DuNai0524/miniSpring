package cn.dunai.minis.beans.factory.xml;

import cn.dunai.minis.beans.factory.BeanFactory;
import cn.dunai.minis.beans.factory.SimpleBeanFactory;
import cn.dunai.minis.beans.factory.config.*;
import cn.dunai.minis.core.Resource;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

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
            // 处理属性
            List<Element> propertyElements = element.elements("property");
            PropertyValues PVS = new PropertyValues();
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
                PVS.addPropertyValue(new PropertyValue(pType,pName,pV,isRef));
            }
            beanDefinition.setPropertyValues(PVS);

            // 处理构造器参数
            List<Element> constructorsElements = element.elements("constructor-arg");
            ArgumentValues AVS = new ArgumentValues();
            for(Element e : constructorsElements){
                String aType = e.attributeValue("type");
                String aValue = e.attributeValue("value");
                String aName = e.attributeValue("name");
                AVS.addArgumentValue(new ArgumentValue(aType,aName,aValue));
            }
            beanDefinition.setConstructorArgumentValues(AVS);

            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);

            this.simpleBeanFactory.registerBeanDefinition(beanID,beanDefinition);
        }
    }
}
