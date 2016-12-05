package com.shtitan.timesynchronize.entity.eqm.bo;
import java.util.Comparator;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;

import com.shtitan.timesynchronize.entity.eqm.naming.EqNaming;
import com.shtitan.timesynchronize.enums.EqObjectType;
import com.shtitan.timesynchronize.util.EqmStringUtil;



@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "EqBo", length = 255)
@Proxy(lazy = false)
public class EqBo  extends Bo implements Comparator<EqBo> {

    private static final long serialVersionUID = -4988794896088703959L;

    @Id
    @Type(type = "com.shtitan.timesynchronize.entity.usertype.EqNamingUserType")
    private EqNaming deviceName;
    /** 当前eq的用户标签 */
    protected String emsName;
    
    /** 当前Eq版本信息 */
    protected String installedVersion;


    
    public EqNaming getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(EqNaming deviceName) {
        this.deviceName = deviceName;
    }
    /**
     * @return the userLabel
     */
    public String getEmsName() {
        if (emsName == null)
            return this.getClass().getSimpleName() + " "
                    + this.getDeviceName().getInstance();
        else
            return emsName;
    }



    /**
     * @return the installedVersion
     */
    public String getInstalledVersion() {
        return installedVersion;
    }

    /**
     * @param installedVersion
     *            the installedVersion to set
     */
    public void setInstalledVersion(String installedVersion) {
        this.installedVersion = installedVersion;
    }

   
    
    public boolean canLinkSubordinateNe(){
        return false;
    }
    /**
     * 获取EqBo所属的网元
     * 
     * @return
     */
    public EqNaming getNe() {
        return this.getDeviceName().getNe();
    }

    /**
     * 获取当前Eq所属的EqHolder EqNaming； 当前为值为deviceName;
     * 
     * @return
     */
    public EqNaming getEqHolder() {
        return this.getDeviceName();
    }

    /**
     * 获取当前Eq所属的holder EqObjectType 类型
     * 
     * @return
     */
    public EqObjectType getEqObjectType() {
        return this.getDeviceName().getEqObjectType();
    }

    /**
     * 当前EqBo的simpleName： 比如：Port的端口号为1，那么simpleName为port01
     * 
     * @return
     */
    public String getSimpleName() {
        StringBuilder sb = new StringBuilder();
        sb.append(EqmStringUtil.capitalize(this.getDeviceName()
                .getEqObjectType().name()));
        sb.append(":");
        sb.append(EqmStringUtil.toString(this.getDeviceName().getInstance(),
                2));
        return sb.toString();
    }

    /**
     * 
     * (non-Javadoc)
     * 
     * @see com.yotc.opview.framework.bo.DetectedBo#getSource()
     */
    public EqNaming getSource() {
        return this.getDeviceName();
    }

    /**
     * 
     * (non-Javadoc)
     * 
     * @see com.yotc.opview.framework.bo.DetectedBo#getSourceType()
     */
    public String getSourceType() {
        return "UNKNOWN";
    }

    /**
     * 
     * (non-Javadoc)
     * 
     * @see com.yotc.opview.framework.bo.DetectedBo#getCoType()
     */
    public String getCoType() {
        return "UNKNOWN";
    }

    
 

    /**
     * 
     * (non-Javadoc)
     * 
     * @see com.yotc.opview.framework.bo.BusinessObject#equals(java.lang.Object)
     */
    public boolean equals(Object o) {// 根据实际需要修改
        if (this == o)
            return true;

        if (o == null || !(o instanceof EqBo))
            return false;

        if (getClass() != o.getClass()) {
            return false;
        }

        EqBo eqBo = (EqBo) o;
        return ObjectUtils.equals(this.getDeviceName(), eqBo.getDeviceName());
    }

    /**
     * 
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ObjectUtils.hashCode(this.getDeviceName());
        return result;
    }

    /**
     * 
     * (non-Javadoc)
     * 
     * @see com.yotc.opview.framework.bo.BusinessObject#toString()
     */
    public String toString() {
        return super.toString();
    }

 

    @Override
    public int compare(EqBo o1, EqBo o2) {
        // 升序排列
        if (o1 != null && o1.getDeviceName() != null) {
            if (o2 != null && o2.getDeviceName() != null)
                return o1.getDeviceName().compareTo(o2.getDeviceName());
            else
                return 1;
        } else if (o2 != null && o2.getDeviceName() != null) {
            return -1;
        }

        return 0;
    }

}
