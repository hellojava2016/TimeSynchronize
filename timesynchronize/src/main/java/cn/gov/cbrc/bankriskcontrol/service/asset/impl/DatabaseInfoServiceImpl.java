package cn.gov.cbrc.bankriskcontrol.service.asset.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.asset.DatabaseInfoDao;
import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.DatabaseInfo;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.DatabaseInfoService;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Service("databaseInfoService")
@Transactional
public class DatabaseInfoServiceImpl implements DatabaseInfoService {
	@Autowired
	private DatabaseInfoDao databaseInfoDao;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void reportDatabaseInfo(List<DatabaseInfo> list) {
		for (DatabaseInfo info : list) {
			addDatabaseInfo(info);
		}
	}

	@Override
	public void addDatabaseInfo(DatabaseInfo additional) {
		databaseInfoDao.save(additional);
	}

	@Override
	public void deleteDatabaseInfoById(long id) {
		databaseInfoDao.delete(id);
	}

	@Override
	public void updateDatabaseInfo(DatabaseInfo additional) {
		databaseInfoDao.update(additional);
	}

	@Override
	public DatabaseInfo getDatabaseInfoById(long id) {
		return databaseInfoDao.get(id);
	}
	
	@Override
	public DatabaseInfo getDatabaseInfoByUniqueVal(String uniqueVal){
		return databaseInfoDao.findUniqueBy("uniqueVal", uniqueVal);
	}

	@Override
	public Page<DatabaseInfo> getDatabaseInfos(AssetQueryParam param,
			Page<DatabaseInfo> page) {		
		return databaseInfoDao.findPage(page, RiskUtils.getHqlByQueryParam(param, userService, DatabaseInfo.class));
	}
	
	@Override
	public List<DatabaseInfo> getDatabaseInfosByOrganization(long orgId) {
		return databaseInfoDao.find("from DatabaseInfo db where db.organization.orgId='"+orgId+"'");
	}
	
	@Override
	public List<DatabaseInfo> getDatabaseInfoByOrg(long orgid) {
		return databaseInfoDao.find("select distinct a from DatabaseInfo a where a.organization.orgId="+orgid);
	}
}
