package cn.gov.cbrc.bankriskcontrol.service.asset;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.UpsInfo;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface UpsInfoService {
    public void addUpsInfo(UpsInfo additional);
	
	public void deleteUpsInfoById(long id);
	
	public void updateUpsInfo(UpsInfo additional);
		
	public UpsInfo getUpsInfoById(long id);
	
	public UpsInfo getUpsInfoByUniqueVal(String uniqueVal);
	
	public Page<UpsInfo> getUpsInfos(AssetQueryParam param,Page<UpsInfo> page);
	
	public void reportUpsInfo(List<UpsInfo> list);
}
