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

import cn.gov.cbrc.bankriskcontrol.dao.asset.ComputerRoomInfoDao;
import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.ComputerRoomInfo;
import cn.gov.cbrc.bankriskcontrol.entity.PcServer;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.ComputerRoomInfoService;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Service("computerRoomInfoService")
@Transactional
public class ComputerRoomInfoServiceImpl implements ComputerRoomInfoService {
	@Autowired
	private ComputerRoomInfoDao computerRoomInfoDao;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void reportComputerRoomInfo(List<ComputerRoomInfo> list) {
		for (ComputerRoomInfo info : list) {
			addComputerRoomInfo(info);
		}
	}

	@Override
	public void addComputerRoomInfo(ComputerRoomInfo additional) {
		computerRoomInfoDao.save(additional);
	}

	@Override
	public void deleteComputerRoomInfoById(long id) {
		computerRoomInfoDao.delete(id);
	}

	@Override
	public void updateComputerRoomInfo(ComputerRoomInfo additional) {
		computerRoomInfoDao.update(additional);
	}

	@Override
	public ComputerRoomInfo getComputerRoomInfoById(long id) {
		return computerRoomInfoDao.get(id);
	}
	
	@Override
	public ComputerRoomInfo getComputerRoomInfoByUniqueVal(String uniqueVal){
		return computerRoomInfoDao.findUniqueBy("uniqueVal", uniqueVal);
	}

	@Override
	public Page<ComputerRoomInfo> getComputerRoomInfos(AssetQueryParam param, Page<ComputerRoomInfo> page) {
		return computerRoomInfoDao.findPage(page, RiskUtils.getHqlByQueryParam(param, userService, ComputerRoomInfo.class));
	}
	
	@Override
	public List<ComputerRoomInfo> getComputerRoomInfosByOrganization(long orgId) {
		return computerRoomInfoDao.findBy("organization.orgId", orgId);
	}
}
