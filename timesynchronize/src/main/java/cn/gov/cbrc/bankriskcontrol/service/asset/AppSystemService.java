package cn.gov.cbrc.bankriskcontrol.service.asset;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AssetQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.AppSystem;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface AppSystemService {
	public void addAppSystem(AppSystem additional);

	public void deleteAppSystemById(long id);

	public void updateAppSystem(AppSystem additional);

	public AppSystem getAppSystemById(long id);

	public AppSystem getAppSystemByName(String appName);

	public List<AppSystem> getAllAppSystems();

	public Page<AppSystem> getAppSystems(AssetQueryParam param, Page<AppSystem> page);
}
