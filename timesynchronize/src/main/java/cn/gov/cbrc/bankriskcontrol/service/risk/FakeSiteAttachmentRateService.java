package cn.gov.cbrc.bankriskcontrol.service.risk;

import java.util.List;

import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.FakeSiteAttachmentRate;
import cn.gov.cbrc.bankriskcontrol.util.Page;

public interface FakeSiteAttachmentRateService {
public void addFakeSiteAttachmentRates(FakeSiteAttachmentRate rate);
	
	public void deleteFakeSiteAttachmentRate(long id);
	
	public Page<FakeSiteAttachmentRate> getFakeSiteAttachmentRates(ReportQueryParam param,Page<FakeSiteAttachmentRate> page,boolean riskCodeLike,boolean checkControl);
	
	public void reportFakeSiteAttachmentRates(List<FakeSiteAttachmentRate> rates);
	
	public Page<FakeSiteAttachmentRate> getFakeSiteAttachmentRates(AnalyseQueryParam param,Page<FakeSiteAttachmentRate> page);
}
