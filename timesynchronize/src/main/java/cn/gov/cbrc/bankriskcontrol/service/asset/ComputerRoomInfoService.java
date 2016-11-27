package cn.gov.cbrc.bankriskcontrol.service.asset;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.ComputerRoomInfo;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface ComputerRoomInfoService {
    public void addComputerRoomInfo(ComputerRoomInfo additional);
	
	public void deleteComputerRoomInfoById(long id);
	
	public void updateComputerRoomInfo(ComputerRoomInfo additional);
		
	public ComputerRoomInfo getComputerRoomInfoById(long id);
	
	public ComputerRoomInfo getComputerRoomInfoByUniqueVal(String uniqueVal);
	
	public Page<ComputerRoomInfo> getComputerRoomInfos(AssetQueryParam param,Page<ComputerRoomInfo> page);
	
	public List<ComputerRoomInfo> getComputerRoomInfosByOrganization(long orgId);
	
	public void reportComputerRoomInfo(List<ComputerRoomInfo> list);
}
