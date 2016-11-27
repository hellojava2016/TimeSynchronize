package cn.gov.cbrc.bankriskcontrol.service.asset.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.asset.PrecisionAcInfoDao;
import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.PrecisionAcInfo;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.PrecisionAcInfoService;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Service("precisionAcInfoService")
@Transactional
public class PrecisionAcInfoServiceImpl implements PrecisionAcInfoService {
	@Autowired
	private PrecisionAcInfoDao precisionAcInfoDao;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void reportPrecisionAcInfo(List<PrecisionAcInfo> list) {
		for (PrecisionAcInfo info : list) {
			addPrecisionAcInfo(info);
		}
	}

	@Override
	public void addPrecisionAcInfo(PrecisionAcInfo additional) {
		precisionAcInfoDao.save(additional);
	}

	@Override
	public void deletePrecisionAcInfoById(long id) {
		precisionAcInfoDao.delete(id);
	}

	@Override
	public void updatePrecisionAcInfo(PrecisionAcInfo additional) {
		precisionAcInfoDao.update(additional);
	}

	@Override
	public PrecisionAcInfo getPrecisionAcInfoById(long id) {
		return precisionAcInfoDao.get(id);
	}
	
	@Override
	public PrecisionAcInfo getPrecisionAcInfoByUniqueVal(String uniqueVal){
		return precisionAcInfoDao.findUniqueBy("uniqueVal", uniqueVal);
	}

	@Override
	public Page<PrecisionAcInfo> getPrecisionAcInfos(AssetQueryParam param, Page<PrecisionAcInfo> page) {
		return precisionAcInfoDao.findPage(page, RiskUtils.getHqlByQueryParam(param, userService, PrecisionAcInfo.class));
	}
}
