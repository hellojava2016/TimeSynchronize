package cn.gov.cbrc.bankriskcontrol.service.asset.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.asset.OperateSystemInfoDao;
import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.OperateSystemInfo;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.OperateSystemInfoService;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Service("operateSystemInfoService")
@Transactional
public class OperateSystemInfoServiceImpl implements OperateSystemInfoService {
	@Autowired
	private OperateSystemInfoDao operateSystemInfoDao;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void reportOperateSystemInfo(List<OperateSystemInfo> list) {
		for (OperateSystemInfo info : list) {
			addOperateSystemInfo(info);
		}
	}

	@Override
	public void addOperateSystemInfo(OperateSystemInfo additional) {
		operateSystemInfoDao.save(additional);
	}

	@Override
	public void deleteOperateSystemInfoById(long id) {
		operateSystemInfoDao.delete(id);
	}

	@Override
	public void updateOperateSystemInfo(OperateSystemInfo additional) {
		operateSystemInfoDao.update(additional);
	}

	@Override
	public OperateSystemInfo getOperateSystemInfoById(long id) {
		return operateSystemInfoDao.get(id);
	}
	
	@Override
	public OperateSystemInfo getOperateSystemInfoByUniqueVal(String uniqueVal){
		return operateSystemInfoDao.findUniqueBy("uniqueVal", uniqueVal);
	}

	@Override
	public Page<OperateSystemInfo> getOperateSystemInfos(AssetQueryParam param,
			Page<OperateSystemInfo> page) {
		return operateSystemInfoDao.findPage(page, RiskUtils.getHqlByQueryParam(param, userService, OperateSystemInfo.class));
	}
	
	@Override
	public List<OperateSystemInfo> getOperateSystemInfosByOrganization(long orgId) {
		return operateSystemInfoDao.find("select distinct op from OperateSystemInfo op where op.organization.orgId='"+orgId+"'");
	}
}
