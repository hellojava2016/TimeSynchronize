package com.shtitan.timesynchronize.entity.eqm.bo;
import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


public abstract class Bo implements Serializable {

private static final long serialVersionUID = 3796628986958434482L;

/**
* 记录修改的属性和值
*/
Map<String, Object> modifiedValueMap = new HashMap<String, Object>();


public void clearModifiedValueMap() {
modifiedValueMap.clear();
}

@Override
public Object clone() throws CloneNotSupportedException {
return super.clone();
}

public void copyBasicPropToNewInst(Bo newInst) {
}

public void copyPropertyToNewInst(Bo newInst) {
if (newInst == null || !this.getClass().equals(newInst.getClass())) {
    return;
}
try {
    PropertyUtils.copyProperties(newInst, this);
} catch (Exception e) {
    e.printStackTrace();
}
}

/**
* 验证两个实例是否相等
* 
* @param o
*            被验证的实例
* @return 验证结果,true相等 false 不相等
*/
@Override
public boolean equals(Object o) {
return EqualsBuilder.reflectionEquals(this, o);
}




public Map<String, Object> getModifiedValueMap() {
return modifiedValueMap;
}

/**
* 对实例做hashcode编码
* 
* @param o
*            被操作的实例
* @return 编码值
*/
public int hashCode(Object o) {
return HashCodeBuilder.reflectionHashCode(this);
}


public void setModifiedValueMap(Map<String, Object> modifiedValueMap) {
this.modifiedValueMap = modifiedValueMap;
}

public void setToModifiedValueMap(String fieldName, Object fieldValue) {
modifiedValueMap.put(fieldName, fieldValue);
}

/**
* 得到修改过的字段的值
* 
* @param fieldName
* @return
*/
public Object getModifiedValue(String fieldName) {
Object fieldValue = modifiedValueMap.get(fieldName);
return fieldValue;
}

/**
* 将实例序列化成string类型,便于显示
* 
* @return Description of the Return Value
*/
@SuppressWarnings("unchecked")
@Override
public String toString() {
StringBuffer results = new StringBuffer();
Class clazz = getClass();

results.append(clazz + "\n");

while (clazz != Bo.class) {
    Field[] fields = clazz.getDeclaredFields();

    try {
        AccessibleObject.setAccessible(fields, true);

        for (Field field : fields) {
            results.append("\t" + field.getName() + "="
                    + field.get(this) + "\n");
        }
    } catch (Exception e) {
        //System.out.print("Bo.toString faild,"+ExceptionUtils.getCommonExceptionInfo(e));
    }
    clazz = clazz.getSuperclass();
}

return results.toString();
}


}
