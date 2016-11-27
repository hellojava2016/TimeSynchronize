package cn.gov.cbrc.bankriskcontrol.service.asset;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.StorageSystemInfo;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface StorageSystemInfoService {
    public void addStorageSystemInfo(StorageSystemInfo additional);
	
	public void deleteStorageSystemInfoById(long id);
	
	public void updateStorageSystemInfo(StorageSystemInfo additional);
		
	public StorageSystemInfo getStorageSystemInfoById(long id);
	
	public StorageSystemInfo getStorageSystemInfoByUniqueVal(String uniqueVal);
	
	public Page<StorageSystemInfo> getStorageSystemInfos(AssetQueryParam param,Page<StorageSystemInfo> page);
	
	public void reportStorageSystemInfo(List<StorageSystemInfo> list);
}
