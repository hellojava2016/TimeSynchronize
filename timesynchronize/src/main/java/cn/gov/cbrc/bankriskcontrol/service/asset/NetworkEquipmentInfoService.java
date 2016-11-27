package cn.gov.cbrc.bankriskcontrol.service.asset;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.NetworkEquipmentInfo;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface NetworkEquipmentInfoService {
    public void addNetworkEquipmentInfo(NetworkEquipmentInfo additional);
	
	public void deleteNetworkEquipmentInfoById(long id);
	
	public void updateNetworkEquipmentInfo(NetworkEquipmentInfo additional);
		
	public NetworkEquipmentInfo getNetworkEquipmentInfoById(long id);
	
	public NetworkEquipmentInfo getNetworkEquipmentInfoByUniqueVal(String uniqueVal);
	
	public Page<NetworkEquipmentInfo> getNetworkEquipmentInfos(AssetQueryParam param,Page<NetworkEquipmentInfo> page);
	
	public void reportNetworkEquipmentInfo(List<NetworkEquipmentInfo> list);
	
	public List<NetworkEquipmentInfo> getNetworkEquipmentInfosByOrg(long orgid);
}
