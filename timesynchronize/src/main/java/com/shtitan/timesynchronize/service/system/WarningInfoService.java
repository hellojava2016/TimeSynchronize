package com.shtitan.timesynchronize.service.system;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shtitan.timesynchronize.dao.risk.RiskCategoryDao;
import com.shtitan.timesynchronize.dao.system.GlobalParameterDao;
import com.shtitan.timesynchronize.dao.system.OrganizationDao;
import com.shtitan.timesynchronize.entity.GlobalParameter;
import com.shtitan.timesynchronize.entity.Organization;
import com.shtitan.timesynchronize.entity.RiskCategory;
import com.shtitan.timesynchronize.entity.SystemAvailableRate;
import com.shtitan.timesynchronize.entity.WarningInfo;
import com.shtitan.timesynchronize.service.risk.SystemAvailableRateService;
import com.shtitan.timesynchronize.util.ConvertUtils;
import com.shtitan.timesynchronize.util.Page;


@Service("warningInfoService")
@Transactional
public class WarningInfoService {
	@Autowired
	private SystemAvailableRateService systemAvailableRateService;

	@Autowired
	private GlobalParameterDao globalParameterDao;

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private RiskCategoryDao riskCategoryDao;

	public Page<WarningInfo> getWarningInfos(String type, List<Long> orgIds, Page<WarningInfo> page) {
		if (type.equals("1"))
			return getWarningInfos_ContinueDecline(orgIds, page);
		else
			return getWarningInfos_DeviateAverage(orgIds, page);
	}

	private Page<WarningInfo> getWarningInfos_DeviateAverage(List<Long> orgIds, Page<WarningInfo> page) {
		long time1=System.currentTimeMillis();
		List<WarningInfo> infos = new ArrayList<WarningInfo>();
		GlobalParameter parameter = globalParameterDao.getAll().get(0);
		if (!parameter.isDeviateAverageEnable())
			return new Page<WarningInfo>();
		int percent = parameter.getDeviateAverage();
		String percentSql ="abs("+ (1 + percent / 100.0) + "*o2.avs)";
		String orgSql = "";
		if (CollectionUtils.isNotEmpty(orgIds)) {
			orgSql += " and ORG_ID in (" + ConvertUtils.convertList2String(orgIds, ",") + ")";
		}

		List<Organization> orgList = organizationDao.getCategory();
		Map<String, Organization> orgMap = ConvertUtils.getMapByList(orgList, "orgNo", String.class);
		List<RiskCategory> riskList = riskCategoryDao.getAll();
		Map<String, RiskCategory> riskMap = ConvertUtils.getMapByList(riskList, "riskCode", String.class);

		Map<Class, List<String>> map = new HashMap<Class, List<String>>();
		map.put(SystemAvailableRate.class,
				ConvertUtils.newArrayList("system_available_rate", "1-(o1.ud+o1.pd)/o1.ltsp", "1-(ud+pd)/ltsp"));

		ConnectionProvider cp = ((SessionFactoryImplementor) globalParameterDao.getSessionFactory())
				.getConnectionProvider();
		Connection connection = null;
		Statement st;
		ResultSet rs;
		try {
			connection = cp.getConnection();
			for (Class temp : map.keySet()) {
				List<String> list = map.get(temp);
				StringBuffer sb = new StringBuffer();
				sb.append("select o1.RISK_CODE,o1.ORG_CODE,o1.period,o1.REPORT_DATE,");
				sb.append(list.get(1));
				sb.append(" as myavs,o2.avs from ");
				sb.append(list.get(0));// tableName
				sb.append(" o1 ,(select AVG(");
				sb.append(list.get(2));
				sb.append(") as avs,ORG_CODE,RISK_CODE,DATA_YEAR from ");
				sb.append(list.get(0));// tableName
				sb.append(" where DATA_YEAR=");
				sb.append(new Date().getYear());
				sb.append(orgSql);
				sb.append(" GROUP BY ORG_CODE,RISK_CODE) as o2 where o2.ORG_CODE=o1.ORG_CODE AND o2.RISK_CODE=o1.RISK_CODE AND o2.DATA_YEAR=o1.DATA_YEAR and ");
				sb.append("abs("+list.get(1)+")");
				sb.append(">");
				sb.append(percentSql);

				st = connection.createStatement();
				rs = st.executeQuery(sb.toString());
				while (rs.next()) {
					String riskCode = rs.getString(1);
					String orgCode = rs.getString(2);
					int period = rs.getInt(3);
					Date reportDate = rs.getDate(4);

					WarningInfo info = new WarningInfo();
					info.setOrg(orgMap.get(orgCode));
					info.setRiskName(riskMap.get(riskCode).getRiskName());
					info.setPeriod(period);
					info.setReportDate(reportDate);
					info.setWarningType("2");					
						double value = rs.getDouble(5);
						double average = rs.getDouble(6);
						info.setValue(ConvertUtils.get2pointDouble(value * 100) + "%");
						String deviate=ConvertUtils.get2pointDouble(Math.abs(value/average-1) * 100) + "%";
						info.setMemos("平均值:" + ConvertUtils.get2pointDouble(average * 100) + "%"+",偏离"+deviate);					
					infos.add(info);
				}
				rs.close();
				st.close();
				rs = null;
				st = null;
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// 本地分页
		int size = infos.size();
		int pageSize = page.getPageSize();
		int pageNo = page.getPageNo();
		int firstIndex = (pageNo - 1) * pageSize;
		int lastIndex = pageNo * pageSize;
		List<WarningInfo> result = new ArrayList<WarningInfo>();
		if (lastIndex < size) {
			for (int i = firstIndex; i < lastIndex; i++) {
				result.add(infos.get(i));
			}
		} else {
			for (int i = firstIndex; i < size; i++) {
				result.add(infos.get(i));
			}
		}
		Page<WarningInfo> infoPage = new Page<WarningInfo>();
		infoPage.setPageNo(pageNo);
		infoPage.setPageSize(pageSize);
		infoPage.setResult(result);
		infoPage.setTotalCount(size);
		System.out.println(System.currentTimeMillis()-time1);
		return infoPage;
	}

	private Page<WarningInfo> getWarningInfos_ContinueDecline(List<Long> orgIds, Page<WarningInfo> page) {
		List<WarningInfo> infos = new ArrayList<WarningInfo>();
		GlobalParameter parameter = globalParameterDao.getAll().get(0);
		if (!parameter.isContinueDeclineEnable())
			return new Page<WarningInfo>();
		int period = parameter.getContinueDecline();
		String hql = "";
		if (CollectionUtils.isNotEmpty(orgIds)) {
			hql += " and a.organization.orgId in (" + ConvertUtils.convertList2String(orgIds, ",") + ")";
		}
		hql += " order by a.organization desc,a.riskCode desc,a.reportDate desc";
		List<SystemAvailableRate> list1 = systemAvailableRateService.findLastPeriod(SystemAvailableRate.class, period,
				hql);
		setWarningInfos(infos, period, list1, "percentActual", true, true);
		// 本地分页
		int size = infos.size();
		int pageSize = page.getPageSize();
		int pageNo = page.getPageNo();
		int firstIndex = (pageNo - 1) * pageSize;
		int lastIndex = pageNo * pageSize;
		List<WarningInfo> result = new ArrayList<WarningInfo>();
		if (lastIndex < size) {
			for (int i = firstIndex; i < lastIndex; i++) {
				result.add(infos.get(i));
			}
		} else {
			for (int i = firstIndex; i < size; i++) {
				result.add(infos.get(i));
			}
		}
		Page<WarningInfo> infoPage = new Page<WarningInfo>();
		infoPage.setPageNo(pageNo);
		infoPage.setPageSize(pageSize);
		infoPage.setResult(result);
		infoPage.setTotalCount(size);
		return infoPage;
	}

	private void setWarningInfos(List<WarningInfo> infos, int period, List list1, String propertyName,
			boolean isPercent, boolean isBigger) {
		Map<String, Map<String, List<Object>>> orgMaps = new HashMap<String, Map<String, List<Object>>>();
		try {
			for (Object rate : list1) {
				String orgName = (String) PropertyUtils.getProperty(PropertyUtils.getProperty(rate, "organization"),
						"name");
				String riskCode = (String) PropertyUtils.getProperty(rate, "riskCode");
				Map<String, List<Object>> map = orgMaps.get(orgName);
				if (map != null) {
					if (map.containsKey(riskCode)) {
						map.get(riskCode).add(rate);
					} else {
						map.put(riskCode, ConvertUtils.newArrayList(rate));
					}
				} else {
					Map<String, List<Object>> temp = new HashMap<String, List<Object>>();
					temp.put(riskCode, ConvertUtils.newArrayList(rate));
					orgMaps.put(orgName, temp);
				}
			}
			for (String orgName : orgMaps.keySet()) {
				Map<String, List<Object>> tempMap = orgMaps.get(orgName);
				for (String riskCode : tempMap.keySet()) {
					List<Object> rates = tempMap.get(riskCode);
					if (rates.size() != period)
						continue;
					boolean hasWarning = true;
					for (int i = 0; i < period - 1; i++) {
						Object rate1 = rates.get(i);
						Object rate2 = rates.get(i + 1);
						if (isPercent && isBigger) {
							if ((double) PropertyUtils.getProperty(rate1, propertyName) >= (double) PropertyUtils
									.getProperty(rate2, propertyName))
								hasWarning = false;
						} else if (isPercent && !isBigger) {
							if ((double) PropertyUtils.getProperty(rate1, propertyName) <= (double) PropertyUtils
									.getProperty(rate2, propertyName))
								hasWarning = false;
						} else if (!isPercent && isBigger) {
							if ((long) PropertyUtils.getProperty(rate1, propertyName) >= (long) PropertyUtils
									.getProperty(rate2, propertyName))
								hasWarning = false;
						} else if (!isPercent && !isBigger) {
							if ((long) PropertyUtils.getProperty(rate1, propertyName) <= (long) PropertyUtils
									.getProperty(rate2, propertyName))
								hasWarning = false;
						}
					}
					if (!hasWarning)
						continue;
					Object rate1 = rates.get(0);
					WarningInfo info = new WarningInfo();
					info.setOrg((Organization) PropertyUtils.getProperty(rate1, "organization"));
					info.setRiskName((String) PropertyUtils.getProperty(
							PropertyUtils.getProperty(rate1, "riskCategory"), "riskName"));
					info.setPeriod((int) PropertyUtils.getProperty(rate1, "period"));
					info.setReportDate((Date) PropertyUtils.getProperty(rate1, "reportDate"));
					String value = getValue(rate1, propertyName, isPercent);
					info.setValue(value);
					info.setWarningType("1");
					List<String> memoList = new ArrayList<String>();
					for (int j = rates.size() - 1; j >= 0; j--) {
						memoList.add(getValue(rates.get(j), propertyName, isPercent));
					}
					info.setMemos("最近"+period+"期指标值:"+ConvertUtils.convertList2String(memoList, ","));
					infos.add(info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getValue(Object rate1, String propertyName, boolean isPercent) throws Exception {
		if (isPercent) {
			return ConvertUtils.get2pointDouble((double) PropertyUtils.getProperty(rate1, propertyName)) + "%";
		} else {
			return (long) PropertyUtils.getProperty(rate1, propertyName) + "";
		}
	}
}
