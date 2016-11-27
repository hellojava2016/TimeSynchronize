package cn.gov.cbrc.bankriskcontrol.controller.system;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.gov.cbrc.bankriskcontrol.entity.WarningInfo;
import cn.gov.cbrc.bankriskcontrol.service.system.WarningInfoService;
import cn.gov.cbrc.bankriskcontrol.util.Page;

@Controller
@RequestMapping("/warninginfo")
public class WarningInfoController {
	@Autowired
	private WarningInfoService warningInfoService;

	@RequestMapping(value = "/list.do")
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model) {
		model.addAttribute("selectedwarningtype", 1);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("infos", new Page<WarningInfo>());
		return "system/warninginfo-list";
	}

	@RequestMapping(value = "/query.do")
	public String query(@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType, HttpServletRequest request,
			Model model) {
		String warningtype = request.getParameter("warningtype");
		String category = request.getParameter("category");
		model.addAttribute("selectedwarningtype", warningtype);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("category", category);

		List<Long> orgIds = new ArrayList<Long>();
		if (StringUtils.isNotEmpty(category)) {
			String[] departlist = category.split(",");
			for (String str : departlist)
				orgIds.add(Long.parseLong(str));
		}

		Page<WarningInfo> page = new Page<WarningInfo>();
		page.setPageNo(pageNo);
		String pageSize = request.getParameter("pageSize");
		if (StringUtils.isNotEmpty(pageSize))
			page.setPageSize(Integer.parseInt(pageSize));
		Page<WarningInfo> infoPage = warningInfoService.getWarningInfos(warningtype, orgIds, page);
		model.addAttribute("infos", infoPage);
		return "system/warninginfo-list";
	}
}
