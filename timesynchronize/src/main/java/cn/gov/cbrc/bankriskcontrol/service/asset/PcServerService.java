package cn.gov.cbrc.bankriskcontrol.service.asset;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.PcServer;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface PcServerService {
    public void addPcServer(PcServer additional);
	
	public void deletePcServerById(long id);
	
	public void updatePcServer(PcServer additional);
		
	public PcServer getPcServerById(long id);
	
	public PcServer getPcServerByUniqueVal(String uniqueVal);
	
	public Page<PcServer> getPcServrs(AssetQueryParam param,Page<PcServer> page);
	
	public List<PcServer> getPcServersByOrganization(long orgId);
	
	public void reportPcServer(List<PcServer> list);
}
