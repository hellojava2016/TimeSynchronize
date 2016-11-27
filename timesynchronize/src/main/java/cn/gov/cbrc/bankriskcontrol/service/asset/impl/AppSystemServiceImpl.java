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

import cn.gov.cbrc.bankriskcontrol.dao.asset.AppSystemDao;
import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.AppSystem;
import cn.gov.cbrc.bankriskcontrol.service.asset.AppSystemService;
import cn.gov.cbrc.bankriskcontrol.util.Page;

@Service("appSystemService")
@Transactional
public class AppSystemServiceImpl implements AppSystemService {

	@Autowired
	private AppSystemDao appSystemDao;

	@Override
	public void addAppSystem(AppSystem additional) {
		appSystemDao.save(additional);
	}

	@Override
	public void deleteAppSystemById(long id) {
		appSystemDao.delete(id);
	}

	@Override
	public void updateAppSystem(AppSystem additional) {
		appSystemDao.update(additional);
	}

	@Override
	public AppSystem getAppSystemById(long id) {
		return appSystemDao.get(id);
	}

	@Override
	public AppSystem getAppSystemByName(String appName) {
		return appSystemDao.getUniqueEntityByPropNames(new String[] { "appName" }, new Object[] { appName });
	}

	@Override
	public List<AppSystem> getAllAppSystems() {
		return appSystemDao.getAll();
	}

	@Override
	public Page<AppSystem> getAppSystems(AssetQueryParam param, Page<AppSystem> page) {
		List<Criterion> list = new ArrayList<Criterion>();		
		if (StringUtils.isNotEmpty(param.getName()))
			list.add(Restrictions.like("appName", param.getName(),
					MatchMode.ANYWHERE));		
		return appSystemDao.findPage(page, list.toArray(new Criterion[0]));
	}

}
