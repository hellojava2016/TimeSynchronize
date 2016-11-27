package cn.gov.cbrc.bankriskcontrol.service.asset.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.asset.StorageSystemInfoDao;
import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.StorageSystemInfo;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.StorageSystemInfoService;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Service("storageSystemInfoService")
@Transactional
public class StorageSystemInfoServiceImpl implements StorageSystemInfoService {
	@Autowired
	private StorageSystemInfoDao storageSystemInfoDao;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void reportStorageSystemInfo(List<StorageSystemInfo> list) {
		for (StorageSystemInfo info : list) {
			addStorageSystemInfo(info);
		}
	}

	@Override
	public void addStorageSystemInfo(StorageSystemInfo additional) {
		storageSystemInfoDao.save(additional);
	}

	@Override
	public void deleteStorageSystemInfoById(long id) {
		storageSystemInfoDao.delete(id);
	}

	@Override
	public void updateStorageSystemInfo(StorageSystemInfo additional) {
		storageSystemInfoDao.update(additional);
	}

	@Override
	public StorageSystemInfo getStorageSystemInfoById(long id) {
		return storageSystemInfoDao.get(id);
	}
	
	@Override
	public StorageSystemInfo getStorageSystemInfoByUniqueVal(String uniqueVal){
		return storageSystemInfoDao.findUniqueBy("uniqueVal", uniqueVal);
	}

	@Override
	public Page<StorageSystemInfo> getStorageSystemInfos(AssetQueryParam param,
			Page<StorageSystemInfo> page) {
		return storageSystemInfoDao.findPage(page, RiskUtils.getHqlByQueryParam(param, userService, StorageSystemInfo.class));
	}
}
