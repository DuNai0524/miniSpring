package cn.dunai.minis.test;

import cn.dunai.minis.beans.factory.annotation.Autowired;

public class BaseService {

    @Autowired
    private BaseBaseService bbs;

    public BaseBaseService getBbs() {
        return bbs;
    }

    public void setBbs(BaseBaseService bbs) {
        this.bbs = bbs;
    }

    public void sayHello() {
        System.out.println("Base Service Say Hello!");
        bbs.sayHello();
    }
}
