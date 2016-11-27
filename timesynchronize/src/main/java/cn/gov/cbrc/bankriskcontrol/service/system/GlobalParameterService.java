package cn.gov.cbrc.bankriskcontrol.service.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.system.GlobalParameterDao;
import cn.gov.cbrc.bankriskcontrol.entity.GlobalParameter;

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
