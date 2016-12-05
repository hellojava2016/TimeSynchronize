package com.shtitan.timesynchronize.entity.eqm.bo;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;

import com.shtitan.timesynchronize.entity.eqm.naming.EqNaming;
import com.shtitan.timesynchronize.enums.ConnectionState;
import com.shtitan.timesynchronize.enums.DeviceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "NetworkElement", length = 255)
@Proxy(lazy = false)
public class NetworkElement extends EqBo{
	 private static final long serialVersionUID = 2389783780792882533L;
	    
	    /**连接该网元的端口的EqNaming,如ONU的upLinkPort为Olt的PonPort*/
	    @Type(type = "com.shtitan.timesynchronize.entity.usertype.EqNamingUserType")
	    protected EqNaming upLinkPort;
	    /** 网元设备类型 */	    
	    @Type(type = "com.shtitan.timesynchronize.entity.usertype.EnumUserType", parameters = { @Parameter(name = "enumClassName", value = "com.shtitan.timesynchronize.enums.DeviceType") })
	    protected DeviceType deviceType = DeviceType.STANDARD;
	    
	    @Type(type = "com.shtitan.timesynchronize.entity.usertype.EnumUserType", parameters = { @Parameter(name = "enumClassName", value = "com.shtitan.timesynchronize.enums.ConnectionState") })	    
	    protected ConnectionState connectionState = ConnectionState.LINKUP;
	    
	    /**
	     * 网元管理IP
	     * <p>
	     * 如果是OLT/MDU登录参数中IP有，此处一定有IP；如果是SFU则登录参数为NULL，但此处可能也有IP
	     **/
	    protected String ipAddress="0.0.0.0";
	    
	    protected String macAddress;
	    
	    protected Integer mibVersion;
	    
	    protected String hwSn;
	    
	    protected String profileVendor;
	    
	    protected String generalFwVersion;

		public EqNaming getUpLinkPort() {
			return upLinkPort;
		}

		public void setUpLinkPort(EqNaming upLinkPort) {
			this.upLinkPort = upLinkPort;
		}

		public DeviceType getDeviceType() {
			return deviceType;
		}

		public void setDeviceType(DeviceType deviceType) {
			this.deviceType = deviceType;
		}

		public ConnectionState getConnectionState() {
			return connectionState;
		}

		public void setConnectionState(ConnectionState connectionState) {
			this.connectionState = connectionState;
		}

		public String getIpAddress() {
			return ipAddress;
		}

		public void setIpAddress(String ipAddress) {
			this.ipAddress = ipAddress;
		}

		public String getMacAddress() {
			return macAddress;
		}

		public void setMacAddress(String macAddress) {
			this.macAddress = macAddress;
		}

		public Integer getMibVersion() {
			return mibVersion;
		}

		public void setMibVersion(Integer mibVersion) {
			this.mibVersion = mibVersion;
		}

		public String getHwSn() {
			return hwSn;
		}

		public void setHwSn(String hwSn) {
			this.hwSn = hwSn;
		}

		public String getProfileVendor() {
			return profileVendor;
		}

		public void setProfileVendor(String profileVendor) {
			this.profileVendor = profileVendor;
		}

		public String getGeneralFwVersion() {
			return generalFwVersion;
		}

		public void setGeneralFwVersion(String generalFwVersion) {
			this.generalFwVersion = generalFwVersion;
		}
	    
	    
}
