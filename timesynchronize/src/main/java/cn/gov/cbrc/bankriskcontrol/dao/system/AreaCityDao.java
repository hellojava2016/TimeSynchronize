package cn.gov.cbrc.bankriskcontrol.dao.system;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import cn.gov.cbrc.bankriskcontrol.dao.HibernateDao;
import cn.gov.cbrc.bankriskcontrol.entity.AreaCity;
import cn.gov.cbrc.bankriskcontrol.util.Page;

@Repository
public class AreaCityDao extends HibernateDao<AreaCity, Long> {

	/**
	 * 获取最新的一个子区域
	 * 
	 * @param parentId
	 *            父区域ID
	 * @return AreaCity 或者 null
	 */
	public AreaCity getLastChildByParentId(long parentId) {
		String hql = "from AreaCity ac where ac.areaCity.areaId = ? order by ac.areeCode desc ";
		List<AreaCity> list = this.find(hql, parentId);
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		} else {
			return null;
		}

	}

	/**
	 * 获取最新的顶级区域（父为null）
	 * 
	 * @return
	 */
	public AreaCity getLastFistLevelArea() {
		String hql = "from AreaCity ac where ac.areaCity is null order by ac.areeCode desc ";
		List<AreaCity> list = this.find(hql);
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 根据名称和父区域ID 查找区域
	 * 
	 * @param name
	 * @param id
	 * @return
	 */
	public AreaCity getAreaByNameAndParent(String name, long id) {
		String hql = "from AreaCity ac where ac.name =? and ac.areaCity.areaId=? ";
		List<AreaCity> list = this.find(hql,name,id);
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		} else {
			return null;
		}
	}


	/**
	 * 使用hql进行分页查询
	 * 
	 * */
	public Page<AreaCity> getPage() {
		return null;

	}

}
