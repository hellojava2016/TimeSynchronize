package cn.gov.cbrc.bankriskcontrol.service.asset;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.MiddlewareInfo;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface MiddlewareInfoService {
    public void addMiddlewareInfo(MiddlewareInfo additional);
	
	public void deleteMiddlewareInfoById(long id);
	
	public void updateMiddlewareInfo(MiddlewareInfo additional);
		
	public MiddlewareInfo getMiddlewareInfoById(long id);
	
	public MiddlewareInfo getMiddlewareInfoByUniqueVal(String uniqueVal);
	
	public Page<MiddlewareInfo> getMiddlewareinfos(AssetQueryParam param,Page<MiddlewareInfo> page);
	
	public void reportMiddlewareInfo(List<MiddlewareInfo> list);
	
	public List<MiddlewareInfo> getMiddlewareInfoByOrg(long orgid);
}
