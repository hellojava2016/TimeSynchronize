package com.shtitan.timesynchronize.service.system.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shtitan.timesynchronize.dao.system.AreaCityDao;
import com.shtitan.timesynchronize.entity.AreaCity;
import com.shtitan.timesynchronize.entity.Organization;
import com.shtitan.timesynchronize.entity.User;
import com.shtitan.timesynchronize.service.account.UserService;
import com.shtitan.timesynchronize.service.system.AreaCityService;
import com.shtitan.timesynchronize.util.ConvertUtils;
import com.shtitan.timesynchronize.util.PropertyFilter.MatchType;


@Service("areaCityService")
@Transactional
public class AreaCityServiceImpl implements AreaCityService {

	@Autowired
	private AreaCityDao areaCityDao;
	
	@Autowired
	private UserService userService;

	@Override
	public void addAreaCity(AreaCity areaCity) {
		AreaCity pac=getAreaCityById(areaCity.getAreaCity().getAreaId());
		// 设置区域码
		areaCity.setAreeCode(generateAreaCode(pac));
		areaCityDao.save(areaCity);
	}

	@Override
	public void deleteAreaCity(long areaId) {
		areaCityDao.delete(areaId);

	}

	@Override
	public void updateAreaCity(AreaCity areaCity) {
		areaCityDao.update(areaCity);

	}

	@Override
	public AreaCity getAreaCityById(long areaId) {
		return areaCityDao.get(areaId);
	}
	
	@Override
	public AreaCity getAreaCityByCode(String areaCode) {
		return areaCityDao.getUniqueEntityByPropNames(new String[]{"areeCode"}, new Object[]{areaCode});
	}

	/**
	 * 查找 父下指定名称的区域
	 * 
	 * @param parentId
	 * @param name
	 * @return
	 */
	public AreaCity getChildAreaByName(long parentId, String name) {
		return areaCityDao.getAreaByNameAndParent(name, parentId);
	}

	/**
	 * 根据父区域 产生子区域的区域码
	 * 
	 * @param parentArea
	 * @return
	 */
	public String generateAreaCode(AreaCity parentArea) {
		String parentCode = parentArea != null ? parentArea.getAreeCode() : "";
		AreaCity lastChild = null;
		if (parentArea == null) {
			lastChild = areaCityDao.getLastFistLevelArea();
		} else {
			lastChild = areaCityDao.getLastChildByParentId(parentArea
					.getAreaId());
		}

		if (lastChild == null) {
			return parentCode + "01";
		} else {
			// 获取最新的子区域的区域编码后两位
			int num = Integer.valueOf(StringUtils.substring(lastChild
					.getAreeCode(), lastChild.getAreeCode().length() - 2,
					lastChild.getAreeCode().length()));
			return parentCode
					+ StringUtils.leftPad(String.valueOf(++num), 2, "0");
		}
	}
	
	@Override
	public Map<String, List<AreaCity>> getChildren(String code) {
		List<AreaCity> list;
		if (StringUtils.isEmpty(code)) {
			list = areaCityDao.getAll();
		} else {
			list = areaCityDao.findBy("areeCode", code,
					MatchType.LIKE_START);
			//去掉自身
			for (Iterator<AreaCity> iterator = list.iterator(); iterator
					.hasNext();) {
				AreaCity rc = iterator.next();
				if (rc.getAreeCode().equals(code)) {
					iterator.remove();
					break;
				}
			}
		}
		Map<String, List<AreaCity>> codeChildMap = new HashMap<String, List<AreaCity>>();
		for (AreaCity rc : list) {
			String rcode=rc.getAreeCode();
			String pcode=getParentCode(rcode);
			if(codeChildMap.containsKey(pcode)){
				codeChildMap.get(pcode).add(rc);
			}else{
				codeChildMap.put(pcode, ConvertUtils.newArrayList(rc));
			}
		}
		return codeChildMap;
	}
	
	private String getParentCode(String code){
		return code.substring(0,code.length()-2);
	}

	@Override
	public List<AreaCity> getSubAreaCity(Long areaId) {
		if (areaId == null)
			return areaCityDao.find("select distinct a from AreaCity a where a.areaCity.areaId is null");
		else{
			Subject user = SecurityUtils.getSubject();
			String username=(String)user.getPrincipal();  
			User u = userService.getUserByUserName(username);
			Organization organization=u.getOrganization();
			return areaCityDao.find("select distinct a from AreaCity a where a.areaCity.areaId='"+areaId+"' and a.areeCode like '"+organization.getAreaCity().getAreeCode()+"%'");}
	}
	
	@Override
	public List<AreaCity> getSubAreaCityByUser(User user) {
		Organization organization=user.getOrganization();
		return areaCityDao.find("select distinct a from AreaCity a where a.areeCode like '"+organization.getAreaCity().getAreeCode()+"%'");
	}
	
	@Override
	public List<AreaCity> getTopAreaCityByCurrentUser() {
		Subject user = SecurityUtils.getSubject();
		String username=(String)user.getPrincipal();  
		User u = userService.getUserByUserName(username);
		AreaCity city=u.getOrganization().getAreaCity();
		if(city.getAreaCity()==null){
			return areaCityDao.find("select distinct a from AreaCity a where a.areaCity.areaId='"+city.getAreaId()+"'");
		}else{
			return ConvertUtils.newArrayList(city);
		}
	}
}
