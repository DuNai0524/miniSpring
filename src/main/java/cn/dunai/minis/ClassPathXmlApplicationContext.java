package cn.dunai.minis;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.*;

public class ClassPathXmlApplicationContext {
    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private Map<String, Object> singletons = new HashMap<>();

    // 构造器获取外部配置，解析出 Bean 的定义，形成内存映像
    public ClassPathXmlApplicationContext(String fileName) {
        this.readXml(fileName);
        this.instanceBeans();
    }

    private void readXml(String fileName){
        SAXReader saxReader = new SAXReader();
        try{
            URL xmlPath = this.getClass().getClassLoader().getResource(fileName);
            Document document = saxReader.read(xmlPath);
            Element rootElement = document.getRootElement();
            // 对配置文件中每一个 bean 进行处理
            for(Element element: (List<Element>) rootElement.elements()){
                // 获取 Bean 的基本信息
                String beanID = element.attributeValue("id");
                String beanClassName = element.attributeValue("class");
                BeanDefinition beanDefinition = new BeanDefinition(beanID,beanClassName);
                // 将 Bean 的定义存放到 beanDefinition
                beanDefinitions.add(beanDefinition);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 利用反射创建 Bean 实例，并存储在 singletons 中
    private void instanceBeans(){
        for(BeanDefinition beanDefinition: beanDefinitions){
            try{
                singletons.put(beanDefinition.getId(),Class.forName(beanDefinition.getClassName()).newInstance());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    // 对外方法，让外部程序从容器中获取 Bean 实例，会逐步演化成核心方法
    public Object getBean(String beanName) {
        return singletons.get(beanName);
    }
}
