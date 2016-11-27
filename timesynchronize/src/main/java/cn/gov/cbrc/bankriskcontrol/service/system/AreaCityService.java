package cn.gov.cbrc.bankriskcontrol.service.system;

import java.util.List;
import java.util.Map;

import cn.gov.cbrc.bankriskcontrol.entity.AreaCity;
import cn.gov.cbrc.bankriskcontrol.entity.User;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface AreaCityService {
	/**
	 * 保存区域信息
	 * 
	 * @param AreaCity
	 * */
	public void addAreaCity(AreaCity areaCity);

	/**
	 * 删除区域信息
	 * 
	 * @param areaId
	 * */
	public void deleteAreaCity(long areaId);

	/**
	 * 更新区域信息
	 * 
	 * @param AreaCity
	 * 
	 * */
	public void updateAreaCity(AreaCity areaCity);

	/**
	 * 根据ID获取区域
	 * 
	 * @param id
	 * 
	 * */
	public AreaCity getAreaCityById(long areaId);
	
	/**
	 *  根据名字获取区域
	 * @param areaCode
	 * @return
	 */
	public AreaCity getAreaCityByCode(String areaCode);

	/**
	 * 获取子区域信息
	 * 
	 * @param areaId
	 * 
	 * */
	public List<AreaCity> getSubAreaCity(Long areaId);
		
	/**
	 * 获取区域级联关系
	 * @param code
	 * @return
	 */
	public Map<String, List<AreaCity>> getChildren(String code);
	
	/**
	 * 根据用户获取能查看的区域列表
	 * @param user
	 * @return
	 */
	public List<AreaCity> getSubAreaCityByUser(User user);
	
	public List<AreaCity> getTopAreaCityByCurrentUser();
}
