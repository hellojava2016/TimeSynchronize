package cn.gov.cbrc.bankriskcontrol.service.asset.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.asset.UpsInfoDao;
import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.PcServer;
import cn.gov.cbrc.bankriskcontrol.entity.UpsInfo;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.UpsInfoService;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Service("upsInfoService")
@Transactional
public class UpsInfoServiceImpl implements UpsInfoService {
	@Autowired
	private UpsInfoDao upsInfoDao;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void reportUpsInfo(List<UpsInfo> list) {
		for (UpsInfo info : list) {
			addUpsInfo(info);
		}
	}

	@Override
	public void addUpsInfo(UpsInfo additional) {
		upsInfoDao.save(additional);
	}

	@Override
	public void deleteUpsInfoById(long id) {
		upsInfoDao.delete(id);
	}

	@Override
	public void updateUpsInfo(UpsInfo additional) {
		upsInfoDao.update(additional);
	}

	@Override
	public UpsInfo getUpsInfoById(long id) {
		return upsInfoDao.get(id);
	}
	
	@Override
	public UpsInfo getUpsInfoByUniqueVal(String uniqueVal){
		return upsInfoDao.findUniqueBy("uniqueVal", uniqueVal);
	}

	@Override
	public Page<UpsInfo> getUpsInfos(AssetQueryParam param, Page<UpsInfo> page) {
		return upsInfoDao.findPage(page, RiskUtils.getHqlByQueryParam(param, userService, UpsInfo.class));
	}
}
