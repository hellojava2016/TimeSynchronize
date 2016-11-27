package cn.gov.cbrc.bankriskcontrol.service.asset;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.PrecisionAcInfo;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface PrecisionAcInfoService {
    public void addPrecisionAcInfo(PrecisionAcInfo additional);
	
	public void deletePrecisionAcInfoById(long id);
	
	public void updatePrecisionAcInfo(PrecisionAcInfo additional);
		
	public PrecisionAcInfo getPrecisionAcInfoById(long id);
	
	public PrecisionAcInfo getPrecisionAcInfoByUniqueVal(String uniqueVal);
	
	public Page<PrecisionAcInfo> getPrecisionAcInfos(AssetQueryParam param,Page<PrecisionAcInfo> page);
	
	public void reportPrecisionAcInfo(List<PrecisionAcInfo> list);
}
