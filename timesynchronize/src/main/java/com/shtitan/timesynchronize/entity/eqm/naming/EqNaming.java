package com.shtitan.timesynchronize.entity.eqm.naming;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.shtitan.timesynchronize.enums.DeviceType;
import com.shtitan.timesynchronize.enums.EqObjectType;
import com.shtitan.timesynchronize.util.EqmStringUtil;

public class EqNaming implements Serializable, Comparable<EqNaming> {
	private static final long serialVersionUID = -1799755167843771614L;

    // Eq在系统内的唯一标识
    protected String name;
    // 设备对象分隔符
    public static final char PRIMARY_DELIMETER = '/';
    // 实例标识
    public static final char SECONDARY_DELIMETER = '=';
    //辅助信息
    public static final char THIRD_DELIMETER=':';

    /**
     * @param name
     */
    public EqNaming(String name) {
        if (name == null)
            throw new IllegalArgumentException(" name is a null argument");
        this.name = name;
    }

    /**
     * 
     * @param parent
     * @param instance
     * @param eqType
     */
    protected EqNaming(String parent, Long instance, EqObjectType objectType) {
        if (objectType == null)
            throw new NullPointerException();
        StringBuffer sb = new StringBuffer();
        sb.append(parent);
        sb.append(objectType);
        sb.append(SECONDARY_DELIMETER);
        sb.append(instance);
        sb.append(THIRD_DELIMETER);
        sb.append("");
        sb.append(PRIMARY_DELIMETER);
        name = sb.toString();
    }
    
    
    /**
     * 
     * @param parent
     * @param instance
     * @param eqType
     */
    protected EqNaming(String parent, Long instance, EqObjectType objectType,String addition) {
        if (objectType == null)
            throw new NullPointerException();
        StringBuffer sb = new StringBuffer();
        sb.append(parent);
        sb.append(objectType);
        sb.append(SECONDARY_DELIMETER);
        sb.append(instance);
        sb.append(THIRD_DELIMETER);
        sb.append(addition);
        sb.append(PRIMARY_DELIMETER);
        name = sb.toString();
    }
    
    /**
     * 
     * @param parent
     * @param instance
     * @param objectType
     * 
     * @see toString()
     */
    public EqNaming(EqNaming parent, Long instance, EqObjectType objectType) {
    	if (objectType == null)
            throw new NullPointerException();
        StringBuffer sb = new StringBuffer();
        sb.append(parent.toString());
        sb.append(objectType);
        sb.append(SECONDARY_DELIMETER);
        sb.append(instance);
        sb.append(THIRD_DELIMETER);
        sb.append("");
        sb.append(PRIMARY_DELIMETER);
        name = sb.toString();
    }

    /**
     * 是否为null
     * 
     * @return
     */
    public static boolean isNull(EqNaming eqNaming) {
        if (eqNaming == null)
            return true;
        return StringUtils.isEmpty(eqNaming.getName());
    }

    /**
     * 获取当前EqNaming的管理对象类型
     * 
     * @return
     */
    public EqObjectType getEqObjectType() {
        String temp = name.substring(0, name.lastIndexOf(PRIMARY_DELIMETER));
        if(StringUtils.isEmpty(temp)){
        	return null;
        }
        return EqObjectType.getEqObjectType(temp.substring(temp.lastIndexOf(PRIMARY_DELIMETER) + 1, temp
                .lastIndexOf(SECONDARY_DELIMETER)));

    }

    /**
     * 获取当前设备类型实例
     * 
     * @return
     */
    public Long getInstance() {
        Long instance=getValue();
        if(this.isNe()){
            Long value=instance/100000;
            return value;
        }
        return instance;
    }
    
    
    public Long getValue(){
        String temp = name.substring(0, name.lastIndexOf(THIRD_DELIMETER));
        Long value=Long.parseLong(temp.substring(temp.lastIndexOf(SECONDARY_DELIMETER) + 1));
        return value;
    }
    
    
    public DeviceType getDeviceType(){
        if(!this.isNe())
            return null;
        Long value=getValue();
        Long deviceTypeValue=value%100000;
        return DeviceType.getDeviceTypeByCode(deviceTypeValue.intValue());
    }
    
    
    public String getAddition(){
        int index=name.lastIndexOf(THIRD_DELIMETER);
        String addition=name.substring(index+1, name.lastIndexOf(PRIMARY_DELIMETER));
        return addition;
    }
    /**
     * 获取当前设备Holder的父Holder
     * 
     * @return
     */
    public EqNaming getParent() {
        String temp = name.substring(0, name.lastIndexOf(PRIMARY_DELIMETER));
        String dn = temp.substring(0, temp.lastIndexOf(PRIMARY_DELIMETER) + 1);
        if (dn == null || dn.equals(""))
            return null;
        return new EqNaming(dn);
    }
    /**
     * 根据EqObjectType获取其父EqNaming
     * 
     * @param moType
     * @return
     */
    public EqNaming getEqNamingByEqObjectType(EqObjectType objectType) {
        EqNaming EqNaming = this;
        while (true) {
            if (EqNaming.getEqObjectType() == objectType)
                return EqNaming;
            EqNaming = EqNaming.getParent();
            if (EqNaming == null)
                return null;
        }
    }

    /**
     * 获取MO对应的网元
     * 
     * @return
     */
    public EqNaming getNe() {
        if (this.getEqObjectType().isNe())
            return this;
        EqNaming ne = this;
        while (true) {
            ne = ne.getParent();
            if (ne == null)
                return null;
            if (ne.isNe())
                return ne;
        }
    }
    /**
     * 获取MO顶层网元
     * @return
     */
    public EqNaming getTopNe(){
        if (this.getEqObjectType().equals(EqObjectType.ne))
            return this;
        EqNaming ne = this;
        while (true) {
            ne = ne.getParent();
            if (ne == null)
                return null;
            if (ne.getEqObjectType().equals(EqObjectType.ne))
                return ne;
        }
    }
    
    public EqNaming getUpLinkNe(){
        EqNaming ne = this.getNe();
        while (true) {
            ne = ne.getParent();
            if (ne == null)
                return null;
            if (ne.isNe())
                return ne;
        }
    }
    
    public EqNaming getIndepenceNe(){
        if (this.isNe()&&!this.isProxied())
            return this;
        EqNaming ne = this;
        while (true) {
            ne = ne.getParent();
            if (ne == null)
                return null;
            if (ne.isNe()&&!ne.isProxied())
                return ne;
        } 
    }
    
    public int getNeLevel(){
        int neLevel=0;
        EqNaming parent=this;
        while(parent!=null){
            if(parent.isNe())
                neLevel=neLevel+1;
            parent=parent.getParent();
        }
        return neLevel;
    }
    
    public boolean isProxied(){
        return this.getEqObjectType().equals(EqObjectType.psne);
    }
    
    
    public boolean isSubordinate(){
        return this.getEqObjectType().equals(EqObjectType.psne)||this.getEqObjectType().equals(EqObjectType.isne);
    }
    


    /**
     * 是否为other子节点
     * 
     * @param other
     * @return
     */
    public boolean isChild(EqNaming other) {
        return this.getName().startsWith(other.getName()) && !this.equals(other);
    }

    /**
     * 是否为other节点父节点
     * 
     * @param other
     * @return
     */
    public boolean isParent(EqNaming other) {
        return other.getName().startsWith(this.getName()) && !this.equals(other);
    }

    /**
     * 
     * @param other
     * @return
     */
    public boolean contain(EqNaming other) {
        return this.getName().startsWith(other.getName());
    }

    /**
     * 判断该DN是否为NE
     * 
     * @return
     */
    public boolean isNe() {
    	EqObjectType eqObjType = getEqObjectType();
        return eqObjType == null ? false : eqObjType.isNe();
    }

    /**
     * 获取当前name名称
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 获取当前EqNaming的Simple Namem比如:<br>
     * 1)slot的simple Name:01, 01为板卡号 <br>
     * 2)port的simple Name:01/02, 01为板卡号 02为端口号 <br>
     * 3)pvc 的simple Name:01/02-03, 01为板卡号 02为端口号 03为pvcIndex <br>
     * 4)其他的类型直接返回name <br>
     * 
     * @return
     */
    public String getSimpleName() {
        StringBuilder sb = new StringBuilder();

        EqObjectType objectType = getEqObjectType();
        if (EqObjectType.slot.equals(objectType)) {
            sb.append(EqmStringUtil.toString(getInstance(), 2));
            return sb.toString();
        } else if (EqObjectType.port.equals(objectType)) {
            sb.append(EqmStringUtil.toString(getParent().getInstance(), 2));
            sb.append(EqNaming.PRIMARY_DELIMETER);
            sb.append(EqmStringUtil.toString(getInstance(), 2));
            return sb.toString();
        } else if (EqObjectType.pvc.equals(objectType)) {
            sb.append(EqmStringUtil.toString(getParent().getParent().getInstance(), 2));
            sb.append(EqNaming.PRIMARY_DELIMETER);
            sb.append(EqmStringUtil.toString(getParent().getInstance(), 2));
            sb.append('-');
            sb.append(EqmStringUtil.toString(getInstance(), 2));
            return sb.toString();
        }

        return name;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof EqNaming))
            return false;
        EqNaming name = (EqNaming) obj;
        return this.getName().equals(name.getName());
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(EqNaming o) {
        int compareTo = 0;
        if (!isNe() && !o.isNe()) {//都不是NE
            compareTo = getParent().compareTo(o.getParent());
            if (compareTo != 0) {
                return compareTo;
            } else {
                compareTo = Long.valueOf(getInstance() - o.getInstance()).intValue();
            }
        }else if(o.getParent()!=null&&getParent()!=null){
        	compareTo = getParent().compareTo(o.getParent());
        	if (compareTo != 0) {
                return compareTo;
            } else {
                compareTo = Long.valueOf(getInstance() - o.getInstance()).intValue();
            }
        } else {
            compareTo = toString().compareTo(o.toString());
        }

        return compareTo;
    }
    
    public boolean isDomain(){
        if(name.indexOf("domain") != -1)
            return true;
        return false;
    }
}
