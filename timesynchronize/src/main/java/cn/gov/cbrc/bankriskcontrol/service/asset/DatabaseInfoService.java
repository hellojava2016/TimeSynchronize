package cn.gov.cbrc.bankriskcontrol.service.asset;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.DatabaseInfo;
import cn.gov.cbrc.bankriskcontrol.entity.NetworkEquipmentInfo;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface DatabaseInfoService {
    public void addDatabaseInfo(DatabaseInfo additional);
	
	public void deleteDatabaseInfoById(long id);
	
	public void updateDatabaseInfo(DatabaseInfo additional);
		
	public DatabaseInfo getDatabaseInfoById(long id);
	
	public DatabaseInfo getDatabaseInfoByUniqueVal(String uniqueVal);
	
	public Page<DatabaseInfo> getDatabaseInfos(AssetQueryParam param,Page<DatabaseInfo> page);
	
	public List<DatabaseInfo> getDatabaseInfosByOrganization(long orgId);
	
	public void reportDatabaseInfo(List<DatabaseInfo> list);
	
	public List<DatabaseInfo> getDatabaseInfoByOrg(long orgid);
}
