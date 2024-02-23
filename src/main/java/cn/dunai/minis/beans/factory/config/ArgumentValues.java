package cn.dunai.minis.beans.factory.config;

import java.util.*;

public class ArgumentValues {
    private final Map<Integer,ArgumentValue> indexdArgumentValues = new HashMap<>(0);
    private final List<ArgumentValue> genericArgumentValues = new LinkedList<>();

    public ArgumentValues(){

    }

    private void addArgumentValue(Integer key, ArgumentValue newValue){
        this.indexdArgumentValues.put(key, newValue);
    }

    public boolean hasIndexedArgumentValue(int index){
        return this.indexdArgumentValues.containsKey(index);
    }

    public ArgumentValue getIndexedArgumentValue(int index) {
        return this.indexdArgumentValues.get(index);
    }

    public void addGenericArgumentValue(Object value,String type){
        this.genericArgumentValues.add(new ArgumentValue(value, type));
    }

    private void addGenericArgumentValue(ArgumentValue newValue) {
        if(newValue.getName()!=null){
            for(Iterator<ArgumentValue> it = this.genericArgumentValues.iterator(); it.hasNext();){
                ArgumentValue av = it.next();
                if(newValue.getName().equals(av.getName())){
                    it.remove();
                }
            }
        }
        this.genericArgumentValues.add(newValue);
    }

    public ArgumentValue getGenericArgumentValue(String requiredName) {
        for(ArgumentValue valueHolder : this.genericArgumentValues) {
            if(valueHolder.getName() != null && (requiredName == null || !valueHolder.getName().equals(requiredName))){
                continue;
            }
            return valueHolder;
        }
        return null;
    }

    public int getArgumentCount() {
        return this.genericArgumentValues.size();
    }

    public boolean isEmpty() {
        return this.genericArgumentValues.isEmpty();
    }





}
