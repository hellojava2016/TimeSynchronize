package cn.gov.cbrc.bankriskcontrol.service.asset;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.OperateSystemInfo;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface OperateSystemInfoService {
    public void addOperateSystemInfo(OperateSystemInfo additional);
	
	public void deleteOperateSystemInfoById(long id);
	
	public void updateOperateSystemInfo(OperateSystemInfo additional);
		
	public OperateSystemInfo getOperateSystemInfoById(long id);
	
	public OperateSystemInfo getOperateSystemInfoByUniqueVal(String uniqueVal);
	
	public Page<OperateSystemInfo> getOperateSystemInfos(AssetQueryParam param,Page<OperateSystemInfo> page);
	
	public List<OperateSystemInfo> getOperateSystemInfosByOrganization(long orgId);
	
	public void reportOperateSystemInfo(List<OperateSystemInfo> list);
}
