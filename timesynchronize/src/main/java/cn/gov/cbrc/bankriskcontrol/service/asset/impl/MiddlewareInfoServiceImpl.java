package cn.gov.cbrc.bankriskcontrol.service.asset.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.asset.MiddlewareInfoDao;
import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.DatabaseInfo;
import cn.gov.cbrc.bankriskcontrol.entity.MiddlewareInfo;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.MiddlewareInfoService;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Service("middlewareInfoService")
@Transactional
public class MiddlewareInfoServiceImpl implements MiddlewareInfoService {
	@Autowired
	private MiddlewareInfoDao middlewareInfoDao;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void reportMiddlewareInfo(List<MiddlewareInfo> list) {
		for (MiddlewareInfo info : list) {
			addMiddlewareInfo(info);
		}
	}

	@Override
	public void addMiddlewareInfo(MiddlewareInfo additional) {
		middlewareInfoDao.save(additional);
	}

	@Override
	public void deleteMiddlewareInfoById(long id) {
		middlewareInfoDao.delete(id);
	}

	@Override
	public void updateMiddlewareInfo(MiddlewareInfo additional) {
		middlewareInfoDao.update(additional);
	}

	@Override
	public MiddlewareInfo getMiddlewareInfoById(long id) {
		return middlewareInfoDao.get(id);
	}

	@Override
	public MiddlewareInfo getMiddlewareInfoByUniqueVal(String uniqueVal) {
		return middlewareInfoDao.findUniqueBy("uniqueVal", uniqueVal);
	}

	@Override
	public Page<MiddlewareInfo> getMiddlewareinfos(AssetQueryParam param,
			Page<MiddlewareInfo> page) {
		return middlewareInfoDao.findPage(page, RiskUtils.getHqlByQueryParam(param, userService, MiddlewareInfo.class));
	}
	
	@Override
	public List<MiddlewareInfo> getMiddlewareInfoByOrg(long orgid) {
		return middlewareInfoDao.find("select distinct a from MiddlewareInfo a where a.organization.orgId="+orgid);
	}
}
