package cn.dunai.minis.beans.factory.config;

import java.util.ArrayList;
import java.util.List;

public class ConstructorPropertyValues {
    private final List<ConstructorPropertyValue> constructorPropertyValueList;

    public ConstructorPropertyValues() {
        this.constructorPropertyValueList = new ArrayList<>(0);
    }

    public List<ConstructorPropertyValue> getPropertyValueList() {
        return this.constructorPropertyValueList;
    }

    public int size() {
        return this.constructorPropertyValueList.size();
    }

    public void addPropertyValue(ConstructorPropertyValue pv) {
        this.constructorPropertyValueList.add(pv);
    }

    public void removePropertyValue(ConstructorPropertyValue pv) {
        this.constructorPropertyValueList.remove(pv);
    }

    public void removePropertyValue(String propertyName) {
        this.constructorPropertyValueList.remove(getPropertyValue(propertyName));
    }

    public ConstructorPropertyValue[] getPropertyValues() {
        return this.constructorPropertyValueList.toArray(new ConstructorPropertyValue[this.constructorPropertyValueList.size()]);
    }

    public ConstructorPropertyValue getPropertyValue(String propertyName) {
        for (ConstructorPropertyValue pv : this.constructorPropertyValueList) {
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }

    public Object get(String propertyName) {
        ConstructorPropertyValue pv = getPropertyValue(propertyName);
        return (pv != null ? pv.getValue() : null);
    }

    public boolean contains(String propertyName) {
        return getPropertyValue(propertyName) != null;
    }

    public boolean isEmpty() {
        return this.constructorPropertyValueList.isEmpty();
    }
}
