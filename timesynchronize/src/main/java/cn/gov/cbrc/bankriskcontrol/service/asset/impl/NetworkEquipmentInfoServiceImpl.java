package cn.gov.cbrc.bankriskcontrol.service.asset.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.asset.NetworkEquipmentInfoDao;
import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.NetworkEquipmentInfo;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.NetworkEquipmentInfoService;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Service("networkEquipmentInfoService")
@Transactional
public class NetworkEquipmentInfoServiceImpl implements NetworkEquipmentInfoService {
	@Autowired
	private NetworkEquipmentInfoDao networkEquipmentInfoDao;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void reportNetworkEquipmentInfo(List<NetworkEquipmentInfo> list) {
		for (NetworkEquipmentInfo info : list) {
			addNetworkEquipmentInfo(info);
		}
	}

	@Override
	public void addNetworkEquipmentInfo(NetworkEquipmentInfo additional) {
		networkEquipmentInfoDao.save(additional);
	}

	@Override
	public void deleteNetworkEquipmentInfoById(long id) {
		networkEquipmentInfoDao.delete(id);
	}

	@Override
	public void updateNetworkEquipmentInfo(NetworkEquipmentInfo additional) {
		networkEquipmentInfoDao.update(additional);
	}

	@Override
	public NetworkEquipmentInfo getNetworkEquipmentInfoById(long id) {
		return networkEquipmentInfoDao.get(id);
	}
	
	@Override
	public NetworkEquipmentInfo getNetworkEquipmentInfoByUniqueVal(String uniqueVal){
		return networkEquipmentInfoDao.findUniqueBy("uniqueVal", uniqueVal);
	}

	@Override
	public Page<NetworkEquipmentInfo> getNetworkEquipmentInfos(AssetQueryParam param, Page<NetworkEquipmentInfo> page) {
		return networkEquipmentInfoDao.findPage(page, RiskUtils.getHqlByQueryParam(param, userService, NetworkEquipmentInfo.class));
	}
	
	@Override
	public List<NetworkEquipmentInfo> getNetworkEquipmentInfosByOrg(long orgid) {
		return networkEquipmentInfoDao.find("select distinct a from NetworkEquipmentInfo a where a.organization.orgId="+orgid);
	}
}
