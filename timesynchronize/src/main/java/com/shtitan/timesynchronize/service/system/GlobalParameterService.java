package com.shtitan.timesynchronize.service.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shtitan.timesynchronize.dao.system.GlobalParameterDao;
import com.shtitan.timesynchronize.entity.GlobalParameter;


@Service("globalParameterService")
@Transactional
public class GlobalParameterService {
	@Autowired
	private GlobalParameterDao globalParameterDao;

	public GlobalParameter getGlobalParameter() {
		return globalParameterDao.getAll().get(0);
	}

	public void update(GlobalParameter parameter) {
		globalParameterDao.update(parameter);
	}
}
