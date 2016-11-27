package cn.gov.cbrc.bankriskcontrol.service.asset.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.asset.PcServerDao;
import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.PcServer;
import cn.gov.cbrc.bankriskcontrol.service.account.UserService;
import cn.gov.cbrc.bankriskcontrol.service.asset.PcServerService;
import cn.gov.cbrc.bankriskcontrol.util.Page;
import cn.gov.cbrc.bankriskcontrol.util.RiskUtils;

@Service("pcServerService")
@Transactional
public class PcServerServiceImpl implements PcServerService {
	@Autowired
	private PcServerDao pcServerDao;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void reportPcServer(List<PcServer> list) {
		for (PcServer info : list) {
			addPcServer(info);
		}
	}

	@Override
	public void addPcServer(PcServer additional) {
		pcServerDao.save(additional);
	}

	@Override
	public void deletePcServerById(long id) {
		pcServerDao.delete(id);
	}

	@Override
	public void updatePcServer(PcServer additional) {
		pcServerDao.update(additional);
	}

	@Override
	public PcServer getPcServerById(long id) {
		return pcServerDao.get(id);
	}
	
	@Override
	public PcServer getPcServerByUniqueVal(String uniqueVal){
		return pcServerDao.findUniqueBy("uniqueVal", uniqueVal);
	}

	@Override
	public Page<PcServer> getPcServrs(AssetQueryParam param, Page<PcServer> page) {
		return pcServerDao.findPage(page, RiskUtils.getHqlByQueryParam(param, userService, PcServer.class));
	}
	
	@Override
	public List<PcServer> getPcServersByOrganization(long orgId) {
		return pcServerDao.findBy("organization.orgId", orgId);
	}
}
