package cn.gov.cbrc.bankriskcontrol.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.ui.Model;

import cn.gov.cbrc.bankriskcontrol.dto.AnalyseQueryParam;
import cn.gov.cbrc.bankriskcontrol.dto.MonitorRate;
import cn.gov.cbrc.bankriskcontrol.dto.ReportQueryParam;
import cn.gov.cbrc.bankriskcontrol.entity.ElectronicActiveUserChangeRate;
import cn.gov.cbrc.bankriskcontrol.entity.ElectronicTransactionChangeRate;
import cn.gov.cbrc.bankriskcontrol.entity.FakeSiteAttachmentRate;
import cn.gov.cbrc.bankriskcontrol.entity.GlobalParameter;
import cn.gov.cbrc.bankriskcontrol.entity.InfoTechnologyRiskEventCount;
import cn.gov.cbrc.bankriskcontrol.entity.OperationChangeSuccessRate;
import cn.gov.cbrc.bankriskcontrol.entity.Organization;
import cn.gov.cbrc.bankriskcontrol.entity.OutsideAttackChangeRate;
import cn.gov.cbrc.bankriskcontrol.entity.RiskCategory;
import cn.gov.cbrc.bankriskcontrol.entity.RiskTca;
import cn.gov.cbrc.bankriskcontrol.entity.SystemAvailableRate;
import cn.gov.cbrc.bankriskcontrol.entity.SystemTransactionSuccessRate;
import cn.gov.cbrc.bankriskcontrol.service.system.OrganizationService;
import cn.gov.cbrc.bankriskcontrol.service.system.RiskCategoryService;

@SuppressWarnings({ "rawtypes", "deprecation" })
public class ExportUtil {
	
	private XSSFWorkbook wb = null;

	private XSSFSheet sheet = null;
	
	public static GlobalParameter parameter;

	/**
	 * @param wb
	 * @param sheet
	 */
	public ExportUtil(XSSFWorkbook wb, XSSFSheet sheet) {
		this.wb = wb;
		this.sheet = sheet;
	}
	
	public static XSSFRow getRow(XSSFSheet sheet,int num){
		XSSFRow row=sheet.getRow(num);
		return row!=null?row:sheet.createRow(num);
	}
	
	//http://zhaoxian705.blog.163.com/blog/static/54190157201171124242322/
	public static void exportExcel(String fileName,List<Integer> periodList,List<Date> dates,List<RiskCategory> categorys,List<MonitorRate> datas,HttpServletResponse response) {
		try {
			response.setContentType("application/binary;charset=ISO8859_1");
			ServletOutputStream outputStream = response.getOutputStream(); 
			response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes(), "ISO8859_1") + ".xlsx");// 组装附件名称和格式           
	        
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet("sheet");
						
			List<String> riskTypes=ConvertUtils.getPropertyList(categorys, "riskCode", String.class);
			Map<String,RiskCategory> codeRiskMap=ConvertUtils.getMapByList(categorys, "riskCode", String.class);
			riskTypes.remove(0);
			int periodNum=periodList.size();
			int riskTypeNum=riskTypes.size();
			
			//String topRiskType=categorys.get(0).getRiskName();
			
			Map<String,List<MonitorRate>> map=new HashMap<String,List<MonitorRate>>();
			for(MonitorRate data:datas){
				String riskType=data.getRiskCode();
				if(map.containsKey(riskType)){
					map.get(riskType).add(data);					
				}else{
					map.put(riskType, ConvertUtils.newArrayList(data));
				}
			}
			
			ExportUtil exportUtil = new ExportUtil(workBook, sheet);
			XSSFCellStyle headStyle = exportUtil.getHeadStyle();
			XSSFCellStyle smallHeadStyle = exportUtil.getSmallHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			bodyStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
			
			//文件头
			XSSFRichTextString str=new XSSFRichTextString(fileName+"\n"+StringUtils.repeat(" ", 40)+"制表日期"+new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
			XSSFFont font = workBook.createFont();
			// 设置字体加粗
			font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
			str.applyFont(fileName.length(), str.length(), font);
			XSSFRow row0 = sheet.createRow(0); 	
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,4));        
	        Cell cell_1 = row0.createCell(0); 
	        cell_1.setCellValue(str);	
	        batchSetCellStyle(row0, 0, 4, headStyle);
	        row0.setHeight((short)800);
			// 构建表头
			XSSFRow row1 = sheet.createRow(1);
			Cell title0=row1.createCell(0);
			Cell title1=row1.createCell(1);
			Cell title2=row1.createCell(2);
			Cell title3=row1.createCell(3);
			Cell title4=row1.createCell(4);
			title0.setCellValue("期数");
			title1.setCellValue("指标明细");
			title2.setCellValue("指标值");
			title3.setCellValue("同比增长率");
			title4.setCellValue("环比增长率");
			batchSetCellStyle(sheet, 1, 2, 0,0, smallHeadStyle);
			title1.setCellStyle(smallHeadStyle);
			title2.setCellStyle(smallHeadStyle);
			title3.setCellStyle(smallHeadStyle);
			title4.setCellStyle(smallHeadStyle);			
			int index=2;
			for(int i=0;i<periodNum;i++){
				int period=periodList.get(i);
				sheet.addMergedRegion(new CellRangeAddress(index, index+riskTypeNum-1, 0,0));
				XSSFRow indexRow = getRow(sheet, index);
				Cell temp=CellUtil.getCell(indexRow, 0);
				temp.setCellValue("第"+period+"期("+DateUtil.getShortDate(dates.get(i))+")");
				batchSetCellStyle(sheet, index, index+riskTypeNum-1, 0,0, smallHeadStyle);
				//小指标
				for(int k=0;k<riskTypeNum;k++){
					String riskType=riskTypes.get(k);
					String riskName=codeRiskMap.get(riskType).getRiskName();
					String riskStr = riskName;
					//对下级指标的级联展示处理
					if (riskType.startsWith("6") && riskType.length() == 10 || !riskType.startsWith("6")&&riskType.length() == 7) {
						if (riskType.endsWith("001"))
							riskStr = "其中：" + riskName;
						else
							riskStr = "      " + riskName;
					}
					XSSFRow row = getRow(sheet, index+k);
					XSSFCell createCell = row.createCell(1);
					createCell.setCellStyle(bodyStyle);
					createCell.setCellValue(riskStr);
					List<MonitorRate> temps = map.get(riskType);
					if (CollectionUtils.isNotEmpty(temps)) {
						MonitorRate data = ConvertUtils.getObjectFromList(temps, "period", period);
						if (data != null) {
							batchCreateCell(row, new int[] { 1, 2, 3, 4 }, new String[] { riskStr, data.getValue(),
									data.getTongRate(), data.getHuanRate() }, bodyStyle);
						} else {
							batchCreateCell(row, new int[] { 1, 2, 3, 4 }, new String[] { riskStr, "", "", "" },
									bodyStyle);
						}
					} else {
						batchCreateCell(row, new int[] { 1, 2, 3, 4 }, new String[] { riskStr, "", "", "" }, bodyStyle);
					}					
				}
				index+=riskTypeNum;
			}
			
			for(int i=0;i<5;i++)
			{
				sheet.autoSizeColumn(i);
			}	
			sheet.setColumnWidth(0, 10000);
			workBook.write(outputStream);
			outputStream.flush();
			outputStream.close();
			outputStream=null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void exportExce3(String fileName,int periodNum,List<Organization> orgList,List<List<MonitorRate>> datas,List<RiskCategory> categorys,HttpServletResponse response) {
		try {
			response.setContentType("application/binary;charset=ISO8859_1");
			ServletOutputStream outputStream = response.getOutputStream(); 
			response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes(), "ISO8859_1") + ".xlsx");// 组装附件名称和格式           
	        
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet("sheet");
			
			Map<String,List<RiskCategory>> parentCodeListMap=new HashMap<String,List<RiskCategory>>();
			List<String> list=ConvertUtils.newArrayList("1","2","3","4","5","6001","7","8");
			Map<String,RiskCategory> maps=ConvertUtils.getMapByList(categorys, "riskCode", String.class);
			for(RiskCategory rc:categorys){
				String code = rc.getRiskCode();
				if(rc.isHasLeaf()){
					parentCodeListMap.put(code, new ArrayList<RiskCategory>());
					if(list.contains(RiskUtils.getParentCode(code))){
						parentCodeListMap.get(RiskUtils.getParentCode(code)).add(rc);
					}
				}else{
					parentCodeListMap.get(RiskUtils.getParentCode(code)).add(rc);
				}
				if(code.startsWith("1")){//对可用率特殊处理，因为可用率需要分别展示名义和实际可用率两个不存在的子指标
					rc.setAllowReport(false);
				}
			}
			
			//将指标按照期数次序排序(每种大指标的期数不同，但是次序还是相同的)
    		List<List<MonitorRate>> rr=new ArrayList<List<MonitorRate>>();
    		for(int i=0;i<periodNum;i++){
    			rr.add(new ArrayList<MonitorRate>());
    		}
    		List<List<String>> allDates=new ArrayList<List<String>>();
            for(int i=0;i<datas.size();i++){
            	List<MonitorRate> rates=datas.get(i);
            	Map<String,List<MonitorRate>> dateRateMap=new HashMap<String,List<MonitorRate>>();
            	for(MonitorRate rate:rates){
            		String date=rate.getDate();
            		if(dateRateMap.containsKey(date)){
            			dateRateMap.get(date).add(rate);
            		}else{
            			dateRateMap.put(date, ConvertUtils.newArrayList(rate));
            		}
            	}
            	List<String> dateList=new ArrayList<String>();
            	dateList.addAll(dateRateMap.keySet());
            	Collections.sort(dateList);
            	Collections.reverse(dateList);
            	allDates.add(dateList);
            	for(int j=0;j<dateList.size();j++){
            		rr.get(j).addAll(dateRateMap.get(dateList.get(j)));
            	}
			}
			
			ExportUtil exportUtil = new ExportUtil(workBook, sheet);
			XSSFCellStyle headStyle = exportUtil.getHeadStyle();
			XSSFCellStyle smallHeadStyle = exportUtil.getSmallHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();			
			
			// 构建指标表头
	        XSSFRow row1 = sheet.createRow(1);
	        XSSFRow row2 = sheet.createRow(2);
	        XSSFRow row3 = sheet.createRow(3);
	        row2.setHeight((short)600);
	        row3.setHeight((short)600);
	        XSSFRow row4 = sheet.createRow(4);
	        int index=2;
	        int tempIndex=index; 
	        List<Integer> dateIndexList=new ArrayList<Integer>();
	        Map<String,Integer> codeColumnMap=new HashMap<String,Integer>();
	        for(String topCode:list){
	        	tempIndex++;
	        	dateIndexList.add(index);
	        	//二级子指标
	        	List<RiskCategory> rcList=parentCodeListMap.get(topCode);
	        	for(int i=0;i<rcList.size();i++){
	        		RiskCategory rc=rcList.get(i);
	        		String code=rc.getRiskCode();
	        		if(parentCodeListMap.containsKey(code)){//有三级指标
	        			List<RiskCategory> childs=parentCodeListMap.get(code);
	        			for(int j=0;j<childs.size();j++){
	        				RiskCategory temp=childs.get(j);
	        				XSSFCell cell=row4.createCell(tempIndex+j);
	    	        		cell.setCellValue(temp.getRiskName());
	    	        		cell.setCellStyle(bodyStyle);
	    	        		codeColumnMap.put(temp.getRiskCode(),tempIndex+j);
	        			}
	        			sheet.addMergedRegion(new CellRangeAddress(3, 3, tempIndex,tempIndex+childs.size()-1)); 
	        			XSSFCell cell=row3.createCell(tempIndex);
    	        		cell.setCellValue(rc.getRiskName());
    	        		batchSetCellStyle(row3, tempIndex, tempIndex+childs.size()-1, bodyStyle);
    	        		codeColumnMap.put(code,tempIndex);
    	        		tempIndex+=childs.size();
	        		}else{//没有三级指标
	        			sheet.addMergedRegion(new CellRangeAddress(3, 4, tempIndex,tempIndex)); 
	        			XSSFCell cell=row3.createCell(tempIndex);
    	        		cell.setCellValue(rc.getRiskName());
    	        		batchSetCellStyle(sheet, 3, 4, tempIndex, tempIndex, bodyStyle);
    	        		codeColumnMap.put(code,tempIndex);
    	        		tempIndex++;
	        		}
	        	}
	        	//一级指标          		
	        	sheet.addMergedRegion(new CellRangeAddress(2, 2, index+1,tempIndex-1)); 
	        	XSSFCell cell=row2.createCell(index+1);
	        	cell.setCellValue(maps.get(topCode).getRiskName());
        		batchSetCellStyle(row2, index+1, tempIndex-1, smallHeadStyle); 
        		
        		sheet.addMergedRegion(new CellRangeAddress(2, 4, index,index)); 
            	XSSFCell datecell=row2.createCell(index);
            	datecell.setCellValue("指标日期");
        		batchSetCellStyle(row2, index, index, smallHeadStyle);
        		
	        	index=tempIndex;
	        }
	        
	        sheet.addMergedRegion(new CellRangeAddress(1, 1, 2,index-1)); 
        	XSSFCell cell=row1.createCell(2);
        	cell.setCellValue("指标");
    		cell.setCellStyle(smallHeadStyle);    		
    		
	        //文件头
			XSSFRichTextString str=new XSSFRichTextString(fileName+"\n"+StringUtils.repeat(" ", 40)+"制表日期"+new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
			XSSFFont font = workBook.createFont();
			// 设置字体加粗
			font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
			str.applyFont(fileName.length(), str.length(), font);
			XSSFRow row0 = sheet.createRow(0); 	
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,index-1));        
	        Cell cell_1 = row0.createCell(0); 
	        cell_1.setCellValue(str);	        
	        row0.setHeight((short)800);
	        batchSetCellStyle(row0, 0, index-1, headStyle);
	        //构建期数和金融机构表头
	        sheet.addMergedRegion(new CellRangeAddress(1, 4, 0,0)); 
	        sheet.addMergedRegion(new CellRangeAddress(1, 4, 1,1)); 
	        XSSFCell cell_qishu=row1.createCell(0);
	        cell_qishu.setCellValue("期数");
	        batchSetCellStyle(sheet, 1, 4, 0, 0, smallHeadStyle);
    		XSSFCell cell_org=row1.createCell(1);
    		cell_org.setCellValue("金融机构");
    		batchSetCellStyle(sheet, 1, 4, 1, 1, smallHeadStyle);
    		
    		int orgIndex=5;
    		tempIndex=orgIndex;
    		for(int i=1;i<=periodNum;i++){
    			List<MonitorRate> rates=rr.get(i-1);
            	Map<String,List<MonitorRate>> map=new HashMap<String,List<MonitorRate>>();
            	for(MonitorRate rate:rates){
            		String org=rate.getOrg();
            		if(map.containsKey(org)){
            			map.get(org).add(rate);
            		}else{
            			map.put(org, ConvertUtils.newArrayList(rate));
            		}
            	}
    			for(int j=0;j<orgList.size();j++){
    				XSSFRow row=getRow(sheet, tempIndex);
    				XSSFRow row_2=getRow(sheet, tempIndex+1);
    				XSSFRow tempRow=row;
    				sheet.addMergedRegion(new CellRangeAddress(tempIndex, tempIndex+1, 1,1)); 
    				XSSFCell temp=row.createCell(1);
    				String orgName = orgList.get(j).getName();
					temp.setCellValue(orgName);
					batchSetCellStyle(sheet,tempIndex,tempIndex+1, 1,1,bodyStyle);
    				
    				List<MonitorRate> temps=map.get(orgName);
    				if(temps==null)
    					temps=new ArrayList<MonitorRate>();
    				Map<String,MonitorRate> tempMap=ConvertUtils.getMapByList(temps, "riskCode", String.class);
    				List<Integer> allColumns=new ArrayList<Integer>();
    				allColumns.addAll(codeColumnMap.values());
    				for(String code:codeColumnMap.keySet()){
    					Integer column=codeColumnMap.get(code);
						// 合并单元格
						if (parentCodeListMap.containsKey(code)) {// 包含三级指标的二级指标
							if (maps.get(code).getAllowReport()) {// 允许上报的二级指标
								sheet.addMergedRegion(new CellRangeAddress(tempIndex, tempIndex, column, column
										+ parentCodeListMap.get(code).size() - 1));
								batchSetCellStyle(row, column, column + parentCodeListMap.get(code).size() - 1,
										bodyStyle);
								tempRow = row;
							} else {// 不允许上报则什么都不做
							}
						} else if (list.contains(RiskUtils.getParentCode(code))) {// 不包含三级指标的二级指标
							sheet.addMergedRegion(new CellRangeAddress(tempIndex, tempIndex + 1, column, column));
							batchSetCellStyle(sheet, tempIndex, tempIndex + 1, column, column, bodyStyle);
							tempRow = row;
						} else {// 三级指标
							String parentCode = RiskUtils.getParentCode(code);
							if (maps.get(parentCode).getAllowReport()) {// 所属二级指标允许上报
								tempRow = row_2;
								XSSFCell ce = tempRow.createCell(column);
								ce.setCellStyle(bodyStyle);
							} else {
								sheet.addMergedRegion(new CellRangeAddress(tempIndex, tempIndex + 1, column, column));
								batchSetCellStyle(sheet, tempIndex, tempIndex + 1, column, column, bodyStyle);
								tempRow = row;
							}
						}
    					//写值
    					if(tempMap.containsKey(code)){
        					MonitorRate rate=tempMap.get(code);
        					CellUtil.getCell(tempRow, column).setCellValue(rate.getValue());
        				}
    				}
    				tempIndex+=2;
    			}
    			sheet.addMergedRegion(new CellRangeAddress(orgIndex, tempIndex-1, 0,0)); 
    			XSSFRow row=getRow(sheet, orgIndex);
				XSSFCell temp=row.createCell(0);
				temp.setCellValue("最近"+i+"期");
				batchSetCellStyle(sheet, orgIndex, tempIndex-1, 0, 0, bodyStyle);
				
				for (int k = 0; k < dateIndexList.size(); k++) {
					int d = dateIndexList.get(k);
					sheet.addMergedRegion(new CellRangeAddress(orgIndex, tempIndex - 1, d, d));
					XSSFCell tempcell = row.createCell(d);
					tempcell.setCellValue(allDates.get(k).get(i - 1));
					batchSetCellStyle(sheet, orgIndex, tempIndex - 1, d, d, bodyStyle);
				}

				orgIndex=tempIndex;
    		}
			
			for(int i=2;i<2+index;i++)
			{
				sheet.setColumnWidth(i, 2000);
			}		
			sheet.setColumnWidth(1, 4000);
			workBook.write(outputStream);
			outputStream.flush();
			outputStream.close();
			outputStream=null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportExce3_org(String fileName,int periodNum,List<Organization> orgList,List<List<MonitorRate>> datas,List<RiskCategory> categorys,HttpServletResponse response) {
		try {
			response.setContentType("application/binary;charset=ISO8859_1");
			ServletOutputStream outputStream = response.getOutputStream(); 
			response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes(), "ISO8859_1") + ".xlsx");// 组装附件名称和格式           
	        
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet("sheet");
			
			Map<String,List<RiskCategory>> parentCodeListMap=new HashMap<String,List<RiskCategory>>();
			List<String> list=ConvertUtils.newArrayList("1","2","3","4","5","6001","7","8");
			Map<String,RiskCategory> maps=ConvertUtils.getMapByList(categorys, "riskCode", String.class);
			for(RiskCategory rc:categorys){
				String code = rc.getRiskCode();
				if(rc.isHasLeaf()){
					parentCodeListMap.put(code, new ArrayList<RiskCategory>());
					if(list.contains(RiskUtils.getParentCode(code))){
						parentCodeListMap.get(RiskUtils.getParentCode(code)).add(rc);
					}
				}else{
					parentCodeListMap.get(RiskUtils.getParentCode(code)).add(rc);
				}
				if(code.startsWith("1")){//对可用率特殊处理，因为可用率需要分别展示名义和实际可用率两个不存在的子指标
					rc.setAllowReport(false);
				}
			}
			
			//将指标按照期数次序排序(每种大指标的期数不同，但是次序还是相同的)
    		List<List<MonitorRate>> rr=new ArrayList<List<MonitorRate>>();
    		for(int i=0;i<periodNum;i++){
    			rr.add(new ArrayList<MonitorRate>());
    		}
    		List<List<String>> allDates=new ArrayList<List<String>>();
            for(int i=0;i<datas.size();i++){
            	List<MonitorRate> rates=datas.get(i);
            	Map<String,List<MonitorRate>> dateRateMap=new HashMap<String,List<MonitorRate>>();
            	for(MonitorRate rate:rates){
            		String date=rate.getDate();
            		if(dateRateMap.containsKey(date)){
            			dateRateMap.get(date).add(rate);
            		}else{
            			dateRateMap.put(date, ConvertUtils.newArrayList(rate));
            		}
            	}
            	List<String> dateList=new ArrayList<String>();
            	dateList.addAll(dateRateMap.keySet());
            	Collections.sort(dateList);
            	Collections.reverse(dateList);
            	allDates.add(dateList);
            	for(int j=0;j<dateList.size();j++){
            		rr.get(j).addAll(dateRateMap.get(dateList.get(j)));
            	}
			}
			
			ExportUtil exportUtil = new ExportUtil(workBook, sheet);
			XSSFCellStyle headStyle = exportUtil.getHeadStyle();
			XSSFCellStyle smallHeadStyle = exportUtil.getSmallHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();			
			
			// 构建指标表头
	        XSSFRow row1 = sheet.createRow(1);
	        XSSFRow row2 = sheet.createRow(2);
	        XSSFRow row3 = sheet.createRow(3);
	        row3.setHeight((short)600);
	        XSSFRow row4 = sheet.createRow(4);
	        int index=2;
	        int tempIndex=index;
	        List<Integer> dateIndexList=new ArrayList<Integer>();
	        Map<String,Integer> codeColumnMap=new HashMap<String,Integer>();
	        for(String topCode:list){
	        	tempIndex++;
	        	dateIndexList.add(index);
	        	//二级子指标
	        	List<RiskCategory> rcList=parentCodeListMap.get(topCode);
	        	for(int i=0;i<rcList.size();i++){
	        		RiskCategory rc=rcList.get(i);
	        		String code=rc.getRiskCode();
	        		if(parentCodeListMap.containsKey(code)){//有三级指标
	        			List<RiskCategory> childs=parentCodeListMap.get(code);
	        			for(int j=0;j<childs.size();j++){
	        				RiskCategory temp=childs.get(j);
	        				XSSFCell cell=row4.createCell(tempIndex+j);
	    	        		cell.setCellValue(temp.getRiskName());
	    	        		cell.setCellStyle(bodyStyle);
	    	        		codeColumnMap.put(temp.getRiskCode(),tempIndex+j);
	        			}
	        			sheet.addMergedRegion(new CellRangeAddress(3, 3, tempIndex,tempIndex+childs.size()-1)); 
	        			XSSFCell cell=row3.createCell(tempIndex);
    	        		cell.setCellValue(rc.getRiskName());
    	        		batchSetCellStyle(row3, tempIndex, tempIndex+childs.size()-1, bodyStyle);
    	        		codeColumnMap.put(code,tempIndex);
    	        		tempIndex+=childs.size();
	        		}else{//没有三级指标
	        			sheet.addMergedRegion(new CellRangeAddress(3, 4, tempIndex,tempIndex)); 
	        			XSSFCell cell=row3.createCell(tempIndex);
    	        		cell.setCellValue(rc.getRiskName());
    	        		batchSetCellStyle(sheet, 3, 4, tempIndex, tempIndex, bodyStyle);
    	        		codeColumnMap.put(code,tempIndex);
    	        		tempIndex++;
	        		}
	        	}
	        	//一级指标          		
	        	sheet.addMergedRegion(new CellRangeAddress(2, 2, index+1,tempIndex-1)); 
	        	XSSFCell cell=row2.createCell(index+1);
	        	cell.setCellValue(maps.get(topCode).getRiskName());
        		batchSetCellStyle(row2, index+1, tempIndex-1, smallHeadStyle); 
        		
        		sheet.addMergedRegion(new CellRangeAddress(2, 4, index,index)); 
            	XSSFCell datecell=row2.createCell(index);
            	datecell.setCellValue("指标日期");
        		batchSetCellStyle(row2, index, index, smallHeadStyle);
        		
	        	index=tempIndex;
	        }
	        sheet.addMergedRegion(new CellRangeAddress(1, 1, 2,index-1));  
        	XSSFCell cell=row1.createCell(2);
        	cell.setCellValue("指标");
    		cell.setCellStyle(smallHeadStyle);    		
    		
	        //文件头
			XSSFRichTextString str=new XSSFRichTextString(fileName+"\n"+StringUtils.repeat(" ", 40)+"制表日期"+new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
			XSSFFont font = workBook.createFont();
			// 设置字体加粗
			font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
			str.applyFont(fileName.length(), str.length(), font);
			XSSFRow row0 = sheet.createRow(0); 	
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,index-1));        
	        Cell cell_1 = row0.createCell(0); 
	        cell_1.setCellValue(str);	        
	        row0.setHeight((short)800);
	        batchSetCellStyle(row0, 0, index-1, headStyle);
	        //构建期数和金融机构表头
	        sheet.addMergedRegion(new CellRangeAddress(1, 4, 0,0)); 
	        sheet.addMergedRegion(new CellRangeAddress(1, 4, 1,1)); 
	        XSSFCell cell_qishu=row1.createCell(0);
	        cell_qishu.setCellValue("金融机构");
	        batchSetCellStyle(sheet, 1, 4, 0, 0, smallHeadStyle);
			XSSFCell cell_org = row1.createCell(1);
			cell_org.setCellValue("期数");
			batchSetCellStyle(sheet, 1, 4, 1, 1, smallHeadStyle);
    		
    		int periodIndex=5;
    		tempIndex=periodIndex;
    		for(int i=1;i<=orgList.size();i++){
    			String orgName = orgList.get(i - 1).getName();    			
    			for(int j=1;j<=periodNum;j++){
    				XSSFRow row=getRow(sheet, tempIndex);
    				XSSFRow row_2=getRow(sheet, tempIndex+1);
    				XSSFRow tempRow=row;
    				sheet.addMergedRegion(new CellRangeAddress(tempIndex, tempIndex+1, 1,1)); 
    				XSSFCell temp=row.createCell(1);
					temp.setCellValue("最近" + j + "期");
					batchSetCellStyle(sheet,tempIndex,tempIndex+1, 1,1,bodyStyle);
					
					for (int k = 0; k < dateIndexList.size(); k++) {
    					int d = dateIndexList.get(k);
    					sheet.addMergedRegion(new CellRangeAddress(tempIndex, tempIndex+1, d, d));
    					XSSFCell tempcell = row.createCell(d);
    					tempcell.setCellValue(allDates.get(k).get(j - 1));
    					batchSetCellStyle(sheet,tempIndex, tempIndex+1, d, d, bodyStyle);
    				}
    				
					List<MonitorRate> lists = rr.get(j - 1);
					List<MonitorRate> temps = new ArrayList<MonitorRate>();
					for (MonitorRate rate : lists) {
						if (rate.getOrg().equals(orgName))
							temps.add(rate);
					}
					
    				Map<String,MonitorRate> tempMap=ConvertUtils.getMapByList(temps, "riskCode", String.class);
    				List<Integer> allColumns=new ArrayList<Integer>();
    				allColumns.addAll(codeColumnMap.values());
    				for(String code:codeColumnMap.keySet()){
    					Integer column=codeColumnMap.get(code);
    					// 合并单元格
						if (parentCodeListMap.containsKey(code)) {// 包含三级指标的二级指标
							if (maps.get(code).getAllowReport()) {// 允许上报的二级指标
								sheet.addMergedRegion(new CellRangeAddress(tempIndex, tempIndex, column, column
										+ parentCodeListMap.get(code).size() - 1));
								batchSetCellStyle(row, column, column + parentCodeListMap.get(code).size() - 1,
										bodyStyle);
								tempRow = row;
							} else {// 不允许上报则什么都不做
							}
						} else if (list.contains(RiskUtils.getParentCode(code))) {// 不包含三级指标的二级指标
							sheet.addMergedRegion(new CellRangeAddress(tempIndex, tempIndex + 1, column, column));
							batchSetCellStyle(sheet, tempIndex, tempIndex + 1, column, column, bodyStyle);
							tempRow = row;
						} else {// 三级指标
							String parentCode = RiskUtils.getParentCode(code);
							if (maps.get(parentCode).getAllowReport()) {// 所属二级指标允许上报
								tempRow = row_2;
								XSSFCell ce = tempRow.createCell(column);
								ce.setCellStyle(bodyStyle);
							} else {
								sheet.addMergedRegion(new CellRangeAddress(tempIndex, tempIndex + 1, column, column));
								batchSetCellStyle(sheet, tempIndex, tempIndex + 1, column, column, bodyStyle);
								tempRow = row;
							}
						}
    					//写值
    					if(tempMap.containsKey(code)){
        					MonitorRate rate=tempMap.get(code);
        					CellUtil.getCell(tempRow, column).setCellValue(rate.getValue());
        				}
    				}
    				tempIndex+=2;    				
    			}
    			sheet.addMergedRegion(new CellRangeAddress(periodIndex, tempIndex - 1, 0, 0));
    			XSSFRow row=getRow(sheet, periodIndex);
				XSSFCell temp=row.createCell(0);
				temp.setCellValue(orgName);
				batchSetCellStyle(sheet, periodIndex, tempIndex-1, 0, 0, bodyStyle);
				periodIndex=tempIndex;
    		}
			
			for(int i=2;i<2+index;i++)
			{
				sheet.setColumnWidth(i, 2000);
			}		
			sheet.setColumnWidth(1, 4000);
			workBook.write(outputStream);
			outputStream.flush();
			outputStream.close();
			outputStream=null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportExcel_tongbi(String fileName,int period,List<RiskCategory> categorys,List<MonitorRate> datas,HttpServletResponse response) {
		try {
			response.setContentType("application/binary;charset=ISO8859_1");
			ServletOutputStream outputStream = response.getOutputStream(); 
			response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes(), "ISO8859_1") + ".xlsx");// 组装附件名称和格式           
	        
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet("sheet");
						
			List<String> riskTypes=ConvertUtils.getPropertyList(categorys, "riskCode", String.class);
			Map<String,RiskCategory> codeRiskMap=ConvertUtils.getMapByList(categorys, "riskCode", String.class);
			String topRiskType=riskTypes.get(0);
			String topRiskName=codeRiskMap.get(topRiskType).getRiskName();
			riskTypes.remove(0);
			int riskTypeNum=riskTypes.size();
			
			ExportUtil exportUtil = new ExportUtil(workBook, sheet);
			XSSFCellStyle headStyle = exportUtil.getHeadStyle();
			XSSFCellStyle smallHeadStyle = exportUtil.getSmallHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			bodyStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
			
			//文件头
			XSSFRichTextString str=new XSSFRichTextString(fileName+"\n"+StringUtils.repeat(" ", 40)+"制表日期"+new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
			XSSFFont font = workBook.createFont();
			// 设置字体加粗
			font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
			str.applyFont(fileName.length(), str.length(), font);
			XSSFRow row0 = sheet.createRow(0); 	
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,4));        
	        Cell cell_1 = row0.createCell(0); 
	        cell_1.setCellValue(str);
	        cell_1.setCellStyle(headStyle);
	        row0.setHeight((short)800);
			// 构建表头
			XSSFRow row1 = sheet.createRow(1);
			XSSFRow row2 = sheet.createRow(2);
			batchCreateCell(row1, new int[]{0,1,2,3,4}, new String[]{"指标","指标明细","指标值","同比增长率","环比增长率"}, smallHeadStyle);
			
			//指标合并处理
			sheet.addMergedRegion(new CellRangeAddress(2, 1+riskTypeNum, 0,0));
			Cell temp=row2.createCell(0);
			temp.setCellValue(topRiskName);
			batchSetCellStyle(sheet, 2, 1+riskTypeNum, 0, 0, smallHeadStyle);
			//小指标
			for(int i=2;i<2+riskTypeNum;i++){
				String riskType=riskTypes.get(i-2);
				String riskName=codeRiskMap.get(riskType).getRiskName();
				String riskStr = riskName;
				//对下级指标的级联展示处理
				if (riskType.startsWith("6") && riskType.length() == 10 || riskType.length() == 7) {
					if (riskType.endsWith("001"))
						riskStr = "其中：" + riskName;
					else
						riskStr = "      " + riskName;
				}
				XSSFRow row = getRow(sheet, i);
				XSSFCell createCell = row.createCell(1);
				createCell.setCellStyle(bodyStyle);
				createCell.setCellValue(riskStr);
				MonitorRate data = ConvertUtils.getObjectFromList(datas, "riskCode", riskType);
				if (data!=null) {
					XSSFCell cell2 = row.createCell(2);
					cell2.setCellStyle(bodyStyle);
					cell2.setCellValue(data.getValue());
					XSSFCell cell3 = row.createCell(3);
					cell3.setCellStyle(bodyStyle);
					cell3.setCellValue(data.getTongRate());
					XSSFCell cell4 = row.createCell(4);
					cell4.setCellStyle(bodyStyle);
					cell4.setCellValue(data.getHuanRate());
				}else{
					//没有值也需要设置style否则表格没有边框
					for (int j = 2; j < 5; j++){
						XSSFCell valueCell = row.createCell(j);
						valueCell.setCellStyle(bodyStyle);
					}
				}
			}
			for(int i=0;i<5;i++)
			{
				sheet.autoSizeColumn(i,true);
			}
			workBook.write(outputStream);
			outputStream.flush();
			outputStream.close();
			outputStream=null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void exportExcel_tca(String fileName,List<RiskTca> datas,HttpServletResponse response) {
		try {
			response.setContentType("application/binary;charset=ISO8859_1");
			ServletOutputStream outputStream = response.getOutputStream(); 
			response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes(), "ISO8859_1") + ".xlsx");// 组装附件名称和格式           
	        
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet("sheet");
			
			Map<String,List<String>> orgTypeMap=new HashMap<String,List<String>>();
			Map<String,List<RiskTca>> orgTcaMap=new HashMap<String,List<RiskTca>>();
			for(RiskTca data:datas){
				Organization org = data.getOrg();
				String orgName=org.getName();
				if(orgTcaMap.containsKey(orgName)){
					orgTcaMap.get(orgName).add(data);					
				}else{
					orgTcaMap.put(orgName, ConvertUtils.newArrayList(data));
				}
				String orgType=org.getCategoryStr();
				if(!orgTypeMap.containsKey(orgType)){
					orgTypeMap.put(orgType, ConvertUtils.newArrayList(orgName));
				}else if(!orgTypeMap.get(orgType).contains(orgName)){
					orgTypeMap.get(orgType).add(orgName);
				}
			}
			
			ExportUtil exportUtil = new ExportUtil(workBook, sheet);
			XSSFCellStyle headStyle = exportUtil.getHeadStyle();
			XSSFCellStyle smallHeadStyle = exportUtil.getSmallHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			
			//文件头
			XSSFRichTextString str=new XSSFRichTextString(fileName+"\n"+StringUtils.repeat(" ", 40)+"制表日期"+new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
			XSSFFont font = workBook.createFont();
			// 设置字体加粗
			font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
			str.applyFont(fileName.length(), str.length(), font);
			XSSFRow row0 = sheet.createRow(0); 	
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,5));        
	        Cell cell_1 = row0.createCell(0); 
	        cell_1.setCellValue(str);
	        cell_1.setCellStyle(headStyle);
	        row0.setHeight((short)800);
			// 构建表头	
			
			XSSFRow row1 = sheet.createRow(1);
			batchCreateCell(row1, new int[]{0,1,2,3,4,5}, new String[]{"机构类型","机构名称","周期","指标项","指标值","阈值"}, smallHeadStyle);
			int typeIndex=2;
			int orgIndex=typeIndex;
			for(String orgType:orgTypeMap.keySet()){
				List<String> orgNames=orgTypeMap.get(orgType);
				for(int j=0;j<orgNames.size();j++){
					String orgName=orgNames.get(j);
					List<RiskTca> tcas=orgTcaMap.get(orgName);
					int tcaSize=tcas.size();
					for(int index=0;index<tcaSize;index++){
						RiskTca tca=tcas.get(index);
						RiskCategory rc=tca.getRiskCategory();
						XSSFRow row=getRow(sheet, orgIndex+index);
						batchCreateCell(row, new int[]{2,3,4,5}, new String[]{tca.getShowDate(),rc.getRiskName(),tca.getCurrentValue()+"",rc.getMinValue()+"~"+rc.getMaxValue()+""}, bodyStyle);
					}
					//合并机构指标
					for(int i=orgIndex;i<=orgIndex+tcaSize-1;i++){
						getRow(sheet, i).createCell(1).setCellStyle(bodyStyle);
					}
					sheet.addMergedRegion(new CellRangeAddress(orgIndex, orgIndex+tcaSize-1, 1,1)); 
					XSSFRow row=getRow(sheet, orgIndex);
					XSSFCell cell=row.getCell(1);
					cell.setCellValue(orgName);
					cell.setCellStyle(bodyStyle);
					
					orgIndex+=tcaSize;
				}
				
				sheet.addMergedRegion(new CellRangeAddress(typeIndex, orgIndex-1, 0,0)); 
				XSSFRow row=getRow(sheet, typeIndex);
				XSSFCell cell=row.createCell(0);
				cell.setCellValue(orgType);
				batchSetCellStyle(sheet, typeIndex, orgIndex-1, 0,0, bodyStyle);
				
				typeIndex=orgIndex;
			}
			for(int i=0;i<6;i++)
			{
				sheet.autoSizeColumn(i);
			}			
			workBook.write(outputStream);
			outputStream.flush();
			outputStream.close();
			outputStream=null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void batchCreateCell(XSSFRow row, int[] columns, String[] values, XSSFCellStyle style) {
		for (int i = 0; i < columns.length; i++) {
			XSSFCell cell = row.createCell(columns[i]);
			cell.setCellValue(values[i]);
			if (style != null)
				cell.setCellStyle(style);
		}
	}
	
	private static void batchSetCellStyle(XSSFRow row, int columnStart,int columnEnd, XSSFCellStyle style) {
		for (int i = columnStart; i <=columnEnd; i++) {
			CellUtil.getCell(row, i).setCellStyle(style);
		}
	}
	
	private static void batchSetCellStyle(XSSFSheet sheet, int rowStart, int rowEnd, int columnStart, int columnEnd,
			XSSFCellStyle style) {
		for (int i = rowStart; i <= rowEnd; i++) {
			XSSFRow row = getRow(sheet, i);
			for (int j = columnStart; j <= columnEnd; j++) {
				CellUtil.getCell(row, j).setCellStyle(style);
			}
		}
	}
	
	
	
	public static void exportExcel(List<String[]> list, String fileName, HttpServletResponse response) {
		try {
			response.setContentType("application/binary;charset=ISO8859_1");
			ServletOutputStream outputStream = response.getOutputStream();
			fileName = new String(fileName.getBytes(), "ISO8859_1");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");// 组装附件名称和格式
			String[] titles = list.get(0);
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheet = workBook.createSheet("sheet");
			ExportUtil exportUtil = new ExportUtil(workBook, sheet);
			XSSFCellStyle headStyle = exportUtil.getSmallHeadStyle();
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
			// 构建表头
			XSSFRow headRow = sheet.createRow(0);
			XSSFCell cell = null;
			for (int i = 0; i < titles.length; i++) {
				cell = headRow.createCell(i);
				cell.setCellStyle(headStyle);
				cell.setCellValue(titles[i]);
			}
			// 构建表体数据
			for (int j = 1; j < list.size(); j++) {
				XSSFRow bodyRow = sheet.createRow(j);
				String[] values = list.get(j);
				for (int i = 0; i < values.length; i++) {
					cell = bodyRow.createCell(i);
					cell.setCellStyle(bodyStyle);
					cell.setCellValue(values[i]);
				}
			}
			for(int i=0;i < titles.length;i++)
			{
				sheet.autoSizeColumn(i);
			}
			workBook.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 合并单元格后给合并后的单元格加边框
	 * 
	 * @param region
	 * @param cs
	 */
	public void setRegionStyle(CellRangeAddress region, XSSFCellStyle cs) {

		int toprowNum = region.getFirstRow();
		for (int i = toprowNum; i <= region.getLastRow(); i++) {
			XSSFRow row = sheet.getRow(i);
			for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
				XSSFCell cell = row.getCell(j);// XSSFCellUtil.getCell(row,
												// (short) j);
				cell.setCellStyle(cs);
			}
		}
	}

	/**
	 * 设置表头的单元格样式
	 * 
	 * @return
	 */
	public XSSFCellStyle getHeadStyle() {
		// 创建单元格样式
		XSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格的背景颜色为淡蓝色
		cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		// 设置单元格居中对齐
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		// 设置单元格垂直居中对齐
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 创建单元格内容显示不下时自动换行
		cellStyle.setWrapText(true);
		// 设置单元格字体样式
		XSSFFont font = wb.createFont();
		// 设置字体加粗
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 400);
		cellStyle.setFont(font);
		// 设置单元格边框为细线条
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		return cellStyle;
	}
	
	/**
	 * 设置表头的单元格样式
	 * 
	 * @return
	 */
	public XSSFCellStyle getSmallHeadStyle() {
		// 创建单元格样式
		XSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格居中对齐
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		// 设置单元格垂直居中对齐
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 创建单元格内容显示不下时自动换行
		cellStyle.setWrapText(true);
		// 设置单元格字体样式
		XSSFFont font = wb.createFont();
		// 设置字体加粗
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 200);
		cellStyle.setFont(font);
		// 设置单元格边框为细线条
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		return cellStyle;
	}

	/**
	 * 设置表体的单元格样式
	 * 
	 * @return
	 */
	public XSSFCellStyle getBodyStyle() {
		// 创建单元格样式
		XSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格居中对齐
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		// 设置单元格垂直居中对齐
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// 创建单元格内容显示不下时自动换行
		cellStyle.setWrapText(true);
		// 设置单元格字体样式
		XSSFFont font = wb.createFont();		
		font.setFontName("宋体");
		font.setFontHeight((short) 200);
		cellStyle.setFont(font);
		// 设置单元格边框为细线条
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		return cellStyle;
	}
	
	/**
	 * 获取查询Page信息
	 * @param pageNo
	 * @param t
	 * @return
	 */
	public  static <T> Page<T> getQueryPage(int pageNo,T t){		
		Page<T> page = new Page<T>();
		page.setPageNo(pageNo);
		page.setPageSize(Integer.MAX_VALUE);
		return page;
	}
	
	/**
	 * 获取查询参数
	 * @param request
	 * @param model
	 * @return
	 */
	public  static ReportQueryParam getQueryParam(HttpServletRequest request,Model model){
		long orgId=ServletUtils.getRequestParamValue_int(request, "Organization");
		Date startDate=null;
		String startdatestr =request.getParameter("startdate");
		if(StringUtils.isNotEmpty(startdatestr)){
			startDate=(Date)ConvertUtils.convertStringToObject(startdatestr, Date.class);
		}else{
			startdatestr="";
		}
		Date endDate=null;
		String endtdatestr =request.getParameter("endtdate");
		if(StringUtils.isNotEmpty(endtdatestr)){
			endDate=(Date)ConvertUtils.convertStringToObject(endtdatestr, Date.class);
		}else{
			endtdatestr="";
		}
		
		model.addAttribute("startdate", startdatestr);
		model.addAttribute("enddate", endtdatestr);
		model.addAttribute("selectedorgid", orgId);
		String dist = request.getParameter("dist");
		if (StringUtils.isEmpty(dist)  || "null".equals(dist))
			 dist="";
		String city = request.getParameter("city");
		if (StringUtils.isEmpty(city)  ||  "null".equals(city))
			 city="";
		String prov = request.getParameter("prov");
		if (StringUtils.isEmpty(prov)  ||  "null".equals(prov))
			prov="";
		model.addAttribute("prov", prov);
		model.addAttribute("city", city);
		model.addAttribute("dist", dist);
		String reportType = RiskUtils.getSelectedRiskCodeFromView(request);		
		ReportQueryParam param = new ReportQueryParam();
		param.setDepartmentId(orgId);
		param.setStartDate(startDate);
		param.setEndDate(endDate);
		param.setReportType(reportType);
		return param;
	}	
	
	/**
	 * 获取查询参数
	 * @param request
	 * @param model
	 * @return
	 */
	public  static ReportQueryParam getExcelQueryParam(HttpServletRequest request,Model model){
		long orgId=ServletUtils.getRequestParamValue_int(request, "Organization");
		Date startDate=null;
		String startdatestr =request.getParameter("startdate");
		if(StringUtils.isNotEmpty(startdatestr)){
			startDate=(Date)ConvertUtils.convertStringToObject(startdatestr, Date.class);
		}else{
			startdatestr="";
		}
		Date endDate=null;
		String endtdatestr =request.getParameter("endtdate");
		if(StringUtils.isNotEmpty(endtdatestr)){
			endDate=(Date)ConvertUtils.convertStringToObject(endtdatestr, Date.class);
		}else{
			endtdatestr="";
		}
		Date exactDate=null;
		String exacttdatestr =request.getParameter("exactDate");
		if(StringUtils.isNotEmpty(exacttdatestr)){
			exactDate=(Date)ConvertUtils.convertStringToObject(exacttdatestr, Date.class);
		}else{
			exacttdatestr="";
		}
		String riskcode=request.getParameter("risktarget");
		model.addAttribute("startdate", startdatestr);
		model.addAttribute("enddate", endtdatestr);
		model.addAttribute("exactDate", exacttdatestr);
		model.addAttribute("selectedorgid", orgId);
		model.addAttribute("selectedrisk",riskcode);
			
		ReportQueryParam param = new ReportQueryParam();
		param.setDepartmentId(orgId);
		param.setStartDate(startDate);
		param.setEndDate(endDate);
		param.setExactDate(exactDate);
		param.setReportType(riskcode);
		return param;
	}
	
	public static List<Integer> getPeriods(ReportQueryParam param,
			RiskCategoryService riskCategoryService){
		List<Integer> list=new ArrayList<Integer>();
		Date startDate = param.getStartDate();
		Date endDate = param.getEndDate();
		int startPeriod=0;
		int endPeriod=0;
		RiskCategory rc = riskCategoryService.getRiskCategoryByCode(param.getReportType());
		if (startDate == null) {
			startDate=new Date(new Date().getYear(),0,1);
		}
		startPeriod = RiskUtils.getPeroidByReportDate(rc, startDate);
		if (endDate == null) {
			endDate=new Date(new Date().getYear(),11,31);
		}
		endDate=RiskUtils.getNewDate_last(rc, endDate);
		if(endDate.before(startDate))
			return list;
		endPeriod = RiskUtils.getPeroidByReportDate(rc, endDate);
		for(int i=startPeriod;i<=endPeriod;i++){
			list.add(i);
		}
		return list;
	}
	
	public static List<Date> getPeriodDates(ReportQueryParam param,
			RiskCategoryService riskCategoryService){
		List<Date> list=new ArrayList<Date>();
		Date startDate = param.getStartDate();
		Date endDate = param.getEndDate();
		int startPeriod=0;
		int endPeriod=0;
		RiskCategory rc = riskCategoryService.getRiskCategoryByCode(param.getReportType());
		if (startDate == null) {
			startDate=new Date(new Date().getYear(),0,1);
		}
		startPeriod = RiskUtils.getPeroidByReportDate(rc, startDate);
		if (endDate == null) {
			endDate=new Date(new Date().getYear(),11,31);
		}
		endDate=RiskUtils.getNewDate_last(rc, endDate);
		if(endDate.before(startDate))
			return list;
		endPeriod = RiskUtils.getPeroidByReportDate(rc, endDate);
		for(int i=startPeriod;i<=endPeriod;i++){
			list.add(getDateByPeriod(rc, startDate.getYear(), i));
		}
		return list;
	}
	
	public static List<String> getPeriods(AnalyseQueryParam param,Class clazz,
			RiskCategoryService riskCategoryService){
		List<String> list=new ArrayList<String>();
		Date startDate = param.getStartDate();
		Date endDate = param.getEndDate();
		int startPeriod=0;
		int endPeriod=0;
		RiskCategory rc = riskCategoryService.getRiskCategoryByRiskClass(clazz);		
		if (endDate == null) {
			endDate=new Date();
		}
		endDate=RiskUtils.getNewDate_last(rc, endDate);
		if (startDate == null) {
			startDate=new Date(endDate.getYear(),0,1);
		}
		startPeriod = RiskUtils.getPeroidByReportDate(rc, startDate);
		if(endDate.before(startDate))
			return list;
		endPeriod = RiskUtils.getPeroidByReportDate(rc, endDate);
		for(int i=startPeriod;i<=endPeriod;i++){
			list.add(i+"");
		}
		return list;
	}

	public static List<String> getPeriods_echarts(AnalyseQueryParam param,Class clazz,
			RiskCategoryService riskCategoryService){
		List<String> list=new ArrayList<String>();
		Date startDate = param.getStartDate();
		Date endDate = param.getEndDate();
		int startPeriod=0;
		int endPeriod=0;
		RiskCategory rc = riskCategoryService.getRiskCategoryByRiskClass(clazz);		
		if (endDate == null) {
			endDate=new Date();
		}
		endDate=RiskUtils.getNewDate_last(rc, endDate);
		if (startDate == null) {
			startDate=new Date(endDate.getYear(),0,1);
		}
		startPeriod = RiskUtils.getPeroidByReportDate(rc, startDate);
		if(endDate.before(startDate))
			return list;
		endPeriod = RiskUtils.getPeroidByReportDate(rc, endDate);
		for(int i=startPeriod;i<=endPeriod;i++){
			if(rc.getCycle()==1){
				list.add("D"+i+"("+new SimpleDateFormat("MM-dd").format(DateUtils.addDays(startDate, i-startPeriod))+")");
			}else if(rc.getCycle()==2){
				Date date=DateUtils.addDays(startDate, startDate.getDay()==0?(i-startPeriod)*7:(i-startPeriod+1)*7-startDate.getDay());
				list.add("W"+i+"("+new SimpleDateFormat("MM-dd").format(date)+")");
			}else if(rc.getCycle()==3){
				list.add("M"+i);
			}
		}
		return list;
	}
	
	public static Date getDateByPeriod(RiskCategory rc,int year,int period){
		if(rc.getCycle()==1){//日
			Date firstDate=new Date(year,0,1);
			Calendar cal=Calendar.getInstance();
			cal.setTime(firstDate);
			int temp=cal.get(Calendar.DAY_OF_YEAR);
			return DateUtils.addDays(firstDate,period-temp);
		}else if(rc.getCycle()==2){//周
			Date firstDate=new Date(year,0,1);
			Calendar cal=Calendar.getInstance();
			cal.setTime(firstDate);
			int temp=cal.get(Calendar.WEEK_OF_YEAR);
			int day=cal.get(Calendar.DAY_OF_WEEK);
			return DateUtils.addDays(firstDate, (period-temp)*7+8-day);
		} else if (rc.getCycle() == 3) {// 月
			if (period == 12)
				return DateUtils.addDays(new Date(year + 1, 0, 1), -1);
			else
				return DateUtils.addDays(new Date(year, period, 1), -1);
		}
		return null;
	}
	
	public static String getEchartString(RiskCategoryService riskCategoryService,OrganizationService organizationService,AnalyseQueryParam param,List<FakeSiteAttachmentRate> rates){
		List<Organization> orgs=getOrgs(organizationService, param);
		List<String> periods=getPeriods(param, FakeSiteAttachmentRate.class, riskCategoryService);
		List<String> periods__echarts=getPeriods_echarts(param, FakeSiteAttachmentRate.class, riskCategoryService);
		List<String> orgNames=new ArrayList<String>();
		
		Map<String,Map<String,String>> maps=new HashMap<String,Map<String,String>>();		
		for(Organization org:orgs){
			orgNames.add(org.getName());			
			Map<String,String> map=new HashMap<String,String>();
			for(String period:periods){
				map.put(period, 0+"");
			}
			maps.put(org.getName(),map);
		}
        for(FakeSiteAttachmentRate rate:rates){
			String orgName=rate.getOrganization().getName();
			int period=rate.getPeriod();
			maps.get(orgName).put(period+"", ConvertUtils.get2pointDouble(rate.getPercent()));
		}
		return getEchartString("假冒网站查封率","%",orgNames,periods,periods__echarts,maps);
	}	
	
	private static List<Organization> getOrgs(OrganizationService organizationService,AnalyseQueryParam param){
		Page<Organization> page=new Page<Organization>();
		page.setPageSize(Integer.MAX_VALUE);
		List<Organization> orgs=organizationService.getPage(param.getCategory(), param.getAreaCode(), null, page,true).getResult();
		List<Long> orgIdList=param.getDepartmentIdList();
		for (Iterator<Organization> iterator = orgs.iterator(); iterator.hasNext();) {
			Organization org =iterator.next();
			if(!org.isCanControl()||CollectionUtils.isNotEmpty(orgIdList)&&!orgIdList.contains(org.getOrgId())){
				iterator.remove();
			}
		}
		return orgs;
	}
	
	public static String getEchartStringAvaliable(RiskCategoryService riskCategoryService,OrganizationService organizationService,AnalyseQueryParam param,List<SystemAvailableRate> rates){
		List<Organization> orgs=getOrgs(organizationService, param);
		List<String> periods=getPeriods(param, SystemAvailableRate.class, riskCategoryService);
		List<String> periods__echarts=getPeriods_echarts(param, SystemAvailableRate.class, riskCategoryService);
		List<String> orgNames=new ArrayList<String>();
		
		Map<String,Map<String,String>> maps=new HashMap<String,Map<String,String>>();		
		for(Organization org:orgs){
			orgNames.add(org.getName()+"名义可用率");
			orgNames.add(org.getName()+"实际可用率");			
			Map<String,String> map1=new HashMap<String,String>();
			Map<String,String> map2=new HashMap<String,String>();
			for(String period:periods){
				map1.put(period, 0+"");
				map2.put(period, 0+"");
			}
			maps.put(org.getName()+"名义可用率",map1);
			maps.put(org.getName()+"实际可用率",map2);
		}
        for(SystemAvailableRate rate:rates){
			String orgName=rate.getOrganization().getName();
			int period=rate.getPeriod();
			maps.get(orgName+"名义可用率").put(period+"", ConvertUtils.get2pointDouble(rate.getPercentExpected()));
			maps.get(orgName+"实际可用率").put(period+"", ConvertUtils.get2pointDouble(rate.getPercentActual()));
		}
		return getEchartString("系统可用率","%",orgNames,periods,periods__echarts,maps);
	}
	
	public static String getEchartStringEleuser(RiskCategoryService riskCategoryService,OrganizationService organizationService,AnalyseQueryParam param,List<ElectronicActiveUserChangeRate> rates){
		List<Organization> orgs=getOrgs(organizationService, param);
		List<String> periods=getPeriods(param, ElectronicActiveUserChangeRate.class, riskCategoryService);
		List<String> periods__echarts=getPeriods_echarts(param, ElectronicActiveUserChangeRate.class, riskCategoryService);
		List<String> orgNames=new ArrayList<String>();
		
		Map<String,Map<String,String>> maps=new HashMap<String,Map<String,String>>();		
		for(Organization org:orgs){
			orgNames.add(org.getName());			
			Map<String,String> map=new HashMap<String,String>();
			for(String period:periods){
				map.put(period, 0+"");
			}
			maps.put(org.getName(),map);
		}
        for(ElectronicActiveUserChangeRate rate:rates){
			String orgName=rate.getOrganization().getName();
			int period=rate.getPeriod();
			maps.get(orgName).put(period+"", ConvertUtils.get2pointDouble((rate.getPercent())));
		}
		return getEchartString("主要电子渠道活跃用户账户变化率","%",orgNames,periods,periods__echarts,maps);
	}
	
	public static String getEchartStringInfo(RiskCategoryService riskCategoryService,OrganizationService organizationService,AnalyseQueryParam param,List<InfoTechnologyRiskEventCount> rates){
		List<Organization> orgs=getOrgs(organizationService, param);
		List<String> periods=getPeriods(param, InfoTechnologyRiskEventCount.class, riskCategoryService);
		List<String> periods__echarts=getPeriods_echarts(param, InfoTechnologyRiskEventCount.class, riskCategoryService);
		List<String> orgNames=new ArrayList<String>();
		
		Map<String,Map<String,String>> maps=new HashMap<String,Map<String,String>>();		
		for(Organization org:orgs){
			orgNames.add(org.getName());			
			Map<String,String> map=new HashMap<String,String>();
			for(String period:periods){
				map.put(period, 0+"");
			}
			maps.put(org.getName(),map);
		}
        for(InfoTechnologyRiskEventCount rate:rates){
			String orgName=rate.getOrganization().getName();
			int period=rate.getPeriod();
			maps.get(orgName).put(period+"", rate.getCount()+"");
		}
		return getEchartString("信息科技风险",null,orgNames,periods,periods__echarts,maps);
	}
	
	public static String getEchartStringOperate(RiskCategoryService riskCategoryService,OrganizationService organizationService,AnalyseQueryParam param,List<OperationChangeSuccessRate> rates){
		List<Organization> orgs=getOrgs(organizationService, param);
		List<String> periods=getPeriods(param, OperationChangeSuccessRate.class, riskCategoryService);
		List<String> periods__echarts=getPeriods_echarts(param, OperationChangeSuccessRate.class, riskCategoryService);
		List<String> orgNames=new ArrayList<String>();
		
		Map<String,Map<String,String>> maps=new HashMap<String,Map<String,String>>();		
		for(Organization org:orgs){
			orgNames.add(org.getName());			
			Map<String,String> map=new HashMap<String,String>();
			for(String period:periods){
				map.put(period, 0+"");
			}
			maps.put(org.getName(),map);
		}
        for(OperationChangeSuccessRate rate:rates){
			String orgName=rate.getOrganization().getName();
			int period=rate.getPeriod();
			maps.get(orgName).put(period+"", ConvertUtils.get2pointDouble(rate.getPercent()));
		}
		return getEchartString("投产变更成功率","%",orgNames,periods,periods__echarts,maps);
	}
	
	public static String getEchartStringOutside(RiskCategoryService riskCategoryService,OrganizationService organizationService,AnalyseQueryParam param,List<OutsideAttackChangeRate> rates){
		List<Organization> orgs=getOrgs(organizationService, param);
		List<String> periods=getPeriods(param, OutsideAttackChangeRate.class, riskCategoryService);
		List<String> periods__echarts=getPeriods_echarts(param, OutsideAttackChangeRate.class, riskCategoryService);
		List<String> orgNames=new ArrayList<String>();
		
		Map<String,Map<String,String>> maps=new HashMap<String,Map<String,String>>();		
		for(Organization org:orgs){
			orgNames.add(org.getName());			
			Map<String,String> map=new HashMap<String,String>();
			for(String period:periods){
				map.put(period, 0+"");
			}
			maps.put(org.getName(),map);
		}
        for(OutsideAttackChangeRate rate:rates){
			String orgName=rate.getOrganization().getName();
			int period=rate.getPeriod();
			maps.get(orgName).put(period+"", ConvertUtils.get2pointDouble((rate.getPercent())));
		}        
		return getEchartString("外部攻击变化率","%",orgNames,periods,periods__echarts,maps);
	}
	
	public static String getEchartStringEleTra(RiskCategoryService riskCategoryService,OrganizationService organizationService,AnalyseQueryParam param,List<ElectronicTransactionChangeRate> rates){
		List<Organization> orgs=getOrgs(organizationService, param);
		List<String> periods=getPeriods(param, ElectronicTransactionChangeRate.class, riskCategoryService);
		List<String> periods__echarts=getPeriods_echarts(param, ElectronicTransactionChangeRate.class, riskCategoryService);
		List<String> orgNames=new ArrayList<String>();
		
		Map<String,Map<String,String>> maps=new HashMap<String,Map<String,String>>();		
		for(Organization org:orgs){
			orgNames.add(org.getName());			
			Map<String,String> map=new HashMap<String,String>();
			for(String period:periods){
				map.put(period, 0+"");
			}
			maps.put(org.getName(),map);
		}
        for(ElectronicTransactionChangeRate rate:rates){
			String orgName=rate.getOrganization().getName();
			int period=rate.getPeriod();
			maps.get(orgName).put(period+"",  ConvertUtils.get2pointDouble((rate.getPercent())));
		}
		return getEchartString("主要电子渠道交易变化率","%",orgNames,periods,periods__echarts,maps);
	}
	
	public static String getEchartStringTransaction(RiskCategoryService riskCategoryService,OrganizationService organizationService,AnalyseQueryParam param,List<SystemTransactionSuccessRate> rates){
		List<Organization> orgs=getOrgs(organizationService, param);
		List<String> periods=getPeriods(param, SystemTransactionSuccessRate.class, riskCategoryService);
		List<String> periods__echarts=getPeriods_echarts(param, SystemTransactionSuccessRate.class, riskCategoryService);
		List<String> orgNames=new ArrayList<String>();
		
		Map<String,Map<String,String>> maps=new HashMap<String,Map<String,String>>();		
		for(Organization org:orgs){
			orgNames.add(org.getName());			
			Map<String,String> map=new HashMap<String,String>();
			for(String period:periods){
				map.put(period, 0+"");
			}
			maps.put(org.getName(),map);
		}
        for(SystemTransactionSuccessRate rate:rates){
			String orgName=rate.getOrganization().getName();
			int period=rate.getPeriod();
			maps.get(orgName).put(period+"", ConvertUtils.get2pointDouble(rate.getPercent()));
		}
		return getEchartString("系统交易成功率","%",orgNames,periods,periods__echarts,maps);
	}
	
	public static String getEchartString(String title,String yunit,List<String> legendDatas,List<String> peroids,List<String> periods__echarts,Map<String,Map<String,String>> maps){
		//boolean twoMuchLegend=legendDatas.size()>5;//对于太多Legend的情况，将其放在最右侧
		boolean twoMuchLegend=true;
		String yMinMax="";//scale为true已经能根据数值的最小最大值自动调整，无须手动设置min和max
		if(title.equals("系统交易成功率")){
			yMinMax=",min:"+parameter.getSystemtransaction()+",max:100";
		}else if(title.equals("系统可用率")){
			yMinMax=",min:"+parameter.getSystemavailable()+",max:100";
		}else if(title.equals("假冒网站查封率")){
			yMinMax=",min:"+parameter.getFakesiteattachment()+",max:100";
		}else if(title.equals("投产变更成功率")){
			yMinMax=",min:"+parameter.getOperationchanges()+",max:100";
		}
		StringBuilder sb = new StringBuilder();
		String xTitle=twoMuchLegend?",x:'center'":"";
		sb.append("{title:{text:'"+title+"'"+xTitle+"},");
		sb.append("tooltip:{trigger:'axis'},");
		//legend
		sb.append("legend:{data:[");
		for(String data:legendDatas){
			sb.append("'"+data+"',");
		}
		String left=twoMuchLegend?"orient:'vertical',x:'right',y:40,":"";
		sb.append("],"+left+"show:false},");
		//toolbox
		sb.append("toolbox:{show:true,feature:{mark:{show:true},dataView : {show: true, readOnly: false}, magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']}, restore : {show: true},saveAsImage:{show: true}}},");
		sb.append(twoMuchLegend?"grid:{x:60,x2:200},":"");
		//calculable
		sb.append("calculable:true,");
		//xAxis
		sb.append("xAxis:[{ type : 'category', boundaryGap : false,data:[");
		for(String data:periods__echarts){
			sb.append("'"+data+"',");
		}
		sb.append("]}],");
		//yAxis 处理单位		
		if(StringUtils.isNotBlank(yunit)){
			sb.append("yAxis : [{type : 'value'"+yMinMax+",axisLabel : {formatter: '{value} "+yunit+"'}}],");
		}else{
			sb.append("yAxis:[{type : 'value'"+yMinMax+"}],");
		}
		//series
		sb.append("series:[");
		for(String org:legendDatas){
			Map<String,String> map=maps.get(org);
			String str="{name:'"+org+"',type:'line', smooth:true, itemStyle: {normal: {areaStyle: {type: 'default'}}},data:[";
			for(String period:peroids){
				str+=map.get(period)+",";
			}
			str+="]}";
			sb.append(str+",");
		}
		sb.append("]};");
		return sb.toString();
	}
	
	/**
	 * 获取查询参数
	 * @param request
	 * @param model
	 * @return
	 */
	public  static AnalyseQueryParam getAnalyseQueryParam(HttpServletRequest request,Model model){
		AnalyseQueryParam param = new AnalyseQueryParam();
		
		//起始结束时间
		Date startDate=null;
		String startdatestr =request.getParameter("startdate");
		if(StringUtils.isNotEmpty(startdatestr)){
			startDate=(Date)ConvertUtils.convertStringToObject(startdatestr, Date.class);
		}else{
			startdatestr="";
		}
		Date endDate=null;
		String endtdatestr =request.getParameter("endtdate");
		if(StringUtils.isNotEmpty(endtdatestr)){
			endDate=(Date)ConvertUtils.convertStringToObject(endtdatestr, Date.class);
		}else{
			endtdatestr="";
		}		
		//区域	
		String area =request.getParameter("area");
		//排序类型
		String orderstr = request.getParameter("reportorder");
		  if(StringUtils.isEmpty(orderstr))
			  orderstr="1";
		int order=Integer.parseInt(orderstr);
		//类别
		String category =request.getParameter("category");
		if(StringUtils.isEmpty(category)){
			category="";
		} 
		model.addAttribute("startdate", startdatestr);
		model.addAttribute("enddate", endtdatestr);
		model.addAttribute("area", area);
		model.addAttribute("category", category);
		model.addAttribute("reportorder", order);		
		
		String reportType = RiskUtils.getSelectedRiskCodeFromView(request);	
		if(reportType!=null){
			String dist = request.getParameter("dist");
			if (StringUtils.isEmpty(dist)  || "null".equals(dist))
				 dist="";
			String city = request.getParameter("city");
			if (StringUtils.isEmpty(city)  ||  "null".equals(city))
				 city="";
			String prov = request.getParameter("prov");
			if (StringUtils.isEmpty(prov)  ||  "null".equals(prov))
				prov="";
			model.addAttribute("prov", prov);
			model.addAttribute("city", city);
			model.addAttribute("dist", dist);
			param.setReportTypeList(ConvertUtils.newArrayList(reportType));
		}		
		
		param.setStartDate(startDate);
		param.setEndDate(endDate);
		param.setAreaCode(area);
		param.setCategory(0);
		param.setSortType(order);
		if(StringUtils.isNotEmpty(category)){
			String[] departlist = category.split(",");
			List<Long> llist = new ArrayList<Long>();
			for(String str:departlist)
				llist.add(Long.parseLong(str));
			param.setDepartmentIdList(llist);
		} 
		return param;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<MonitorRate> getAnalyseResult(T t, Object service, String serviceMethodName,List<RiskCategory> categorys, String valuePropertyName,boolean ispercent,ReportQueryParam currentparam,Page<MonitorRate> page3,RiskCategory riskCategory,boolean systemAvailableExport) {
		List<MonitorRate> result = new ArrayList<MonitorRate>();
		Page<T> page = new Page<T>();
		page.setPageNo(page3.getPageNo());
		page.setPageSize(page3.getPageSize());
		try {
			Page<T> cuPage = (Page<T>) MethodUtils.invokeMethod(service, serviceMethodName, new Object[] {
					currentparam, page, true, true });
			List<T> list = cuPage.getResult();
			if(list.size()==0)
				return result;
			Date startDate = (Date) MethodUtils.invokeMethod(list.get(list.size() - 1), "getReportDate", new Object[0]);//降序
			Date endDate = (Date) MethodUtils.invokeMethod(list.get(0), "getReportDate", new Object[0]);

			ReportQueryParam lastYearParam = currentparam.getCopyObject();
			Page<T> lastYearPage = new Page<T>();
			lastYearPage.setPageNo(0);
			lastYearPage.setPageSize(Integer.MAX_VALUE);
			lastYearParam.setStartDate(ExportUtil.getDateByPeriod(riskCategory, startDate.getYear() - 1,
					RiskUtils.getPeroidByReportDate(riskCategory, startDate)));
			lastYearParam.setEndDate(ExportUtil.getDateByPeriod(riskCategory, endDate.getYear() - 1,
					RiskUtils.getPeroidByReportDate(riskCategory, endDate)));
			

			ReportQueryParam lastPeriodParam = currentparam.getCopyObject();
			Page<T> lastPeriodPage = new Page<T>();
			lastPeriodPage.setPageNo(0);
			lastPeriodPage.setPageSize(Integer.MAX_VALUE);
			lastPeriodParam.setStartDate(ExportUtil.getDateByPeriod(riskCategory, startDate.getYear(),
					RiskUtils.getPeroidByReportDate(riskCategory, startDate) - 1));
			lastPeriodParam.setEndDate(ExportUtil.getDateByPeriod(riskCategory, endDate.getYear(),
					RiskUtils.getPeroidByReportDate(riskCategory, endDate) - 1));

			Page<T> lastyearPage = (Page<T>) MethodUtils.invokeMethod(service, serviceMethodName, new Object[] {
					lastYearParam, lastYearPage, true, true });
			Page<T> lastperiodPage = (Page<T>) MethodUtils.invokeMethod(service, serviceMethodName, new Object[] {
					lastPeriodParam, lastPeriodPage, true, true });
			if (CollectionUtils.isNotEmpty(cuPage.getResult())) {
				for (T current : cuPage.getResult()) {
					T lastyear = null;
					T lastPeriod = null;
					Date lastperioddate = ExportUtil.getDateByPeriod(riskCategory,
							(int) PropertyUtils.getProperty(current, "dataYear"),
							(int) PropertyUtils.getProperty(current, "period") - 1);
					Date lastyeardate = ExportUtil.getDateByPeriod(riskCategory,
							((int) PropertyUtils.getProperty(current, "dataYear")) - 1,
							(int) PropertyUtils.getProperty(current, "period"));
					for (T year : lastyearPage.getResult()) {
						if (PropertyUtils.getProperty(year, "riskCode").equals(
								PropertyUtils.getProperty(current, "riskCode"))) {
							if (lastyeardate.equals(PropertyUtils.getProperty(year, "reportDate"))) {
								lastyear = year;
								break;
							}
						}
					}
					for (T period : lastperiodPage.getResult()) {
						if (PropertyUtils.getProperty(period, "riskCode").equals(
								PropertyUtils.getProperty(current, "riskCode"))) {
							if (lastperioddate.equals(PropertyUtils.getProperty(period, "reportDate"))) {
								lastPeriod = period;
								break;
							}
						}
					}
					if (t != SystemAvailableRate.class) {
						MonitorRate mrate = ExportUtil.getMonitorRate(current, lastPeriod, lastyear, categorys,
								valuePropertyName, ispercent);
						result.add(mrate);
					} else {
						if (systemAvailableExport) {
							MonitorRate expect = ExportUtil.getMonitorRate(current, lastPeriod, lastyear, categorys,
									"percentExpected", ispercent);
							expect.setRiskCode(expect.getRiskCode()+"001");
							expect.setRiskmearment("名义可用率");
							expect.setMearmentdetail("名义可用率");
							result.add(expect);
							MonitorRate actual = ExportUtil.getMonitorRate(current, lastPeriod, lastyear, categorys,
									"percentActual", ispercent);
							actual.setRiskCode(actual.getRiskCode()+"002");
							actual.setRiskmearment("实际可用率");
							actual.setMearmentdetail("实际可用率");
							result.add(actual);
						} else {
							MonitorRate mrate = ExportUtil.getMonitorRate(current, lastPeriod, lastyear, categorys,
									valuePropertyName, ispercent);
							mrate.setValue("名义可用率"
									+ ConvertUtils.get2pointDouble(((SystemAvailableRate) current).getPercentExpected())
									+ "%" + " 实际可用率"
									+ ConvertUtils.get2pointDouble(((SystemAvailableRate) current).getPercentActual())
									+ "%");
							result.add(mrate);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		page3.setTotalCount(page.getTotalCount());
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<MonitorRate> getAnalyseResult(T t, Object service, String serviceMethodName,
			List<RiskCategory> categorys, String valuePropertyName, boolean ispercent, ReportQueryParam currentparam,Page<MonitorRate> page3) {
		List<MonitorRate> result = new ArrayList<MonitorRate>();
		Page<T> page = new Page<T>();
		page.setPageNo(page3.getPageNo());
		page.setPageSize(page3.getPageSize());
		try {
			Page<T> cuPage = (Page<T>) MethodUtils.invokeMethod(service, serviceMethodName, new Object[] {
					currentparam, page, true,true });		
			if(t!=SystemAvailableRate.class){
				for (T cu : cuPage.getResult()) {
					MonitorRate mrate = ExportUtil.getMonitorRate(cu, categorys.get(0), valuePropertyName, ispercent);
					result.add(mrate);
				}
			} else {
				List<SystemAvailableRate> list = (List<SystemAvailableRate>) cuPage.getResult();
				result.addAll(getMonitorRates_Available(list));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		page3.setTotalCount(page.getTotalCount());
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<MonitorRate> getAnalyseResult(T t, Object service, String serviceMethodName,List<RiskCategory> categorys, String valuePropertyName,boolean ispercent,ReportQueryParam currentparam,
			ReportQueryParam lastyearparam, ReportQueryParam lastperiodparam,Page<MonitorRate> page3) {
		List<MonitorRate> result = new ArrayList<MonitorRate>();
		Page<T> page = new Page<T>();
		page.setPageNo(page3.getPageNo());
		page.setPageSize(page3.getPageSize());
		try {
			Page<T> cuPage = (Page<T>) MethodUtils.invokeMethod(service, serviceMethodName, new Object[] {
					currentparam, page, true,true });
			Page<T> lastyearPage = (Page<T>) MethodUtils.invokeMethod(service, serviceMethodName, new Object[] {
					lastyearparam, page.simpleCopy(), true,true });
			Page<T> lastperiodPage = (Page<T>) MethodUtils.invokeMethod(service, serviceMethodName, new Object[] {
					lastperiodparam, page.simpleCopy(), true,true });
			if (CollectionUtils.isNotEmpty(cuPage.getResult())) {
				Map<String, T> codeMap = ConvertUtils.getMapByList(cuPage.getResult(), "riskCode", String.class);
				Map<String, T> lastyearcodeMap = ConvertUtils.getMapByList(lastyearPage.getResult(), "riskCode",
						String.class);
				Map<String, T> lastperiodcodeMap = ConvertUtils.getMapByList(lastperiodPage.getResult(), "riskCode",
						String.class);
				for (String code : codeMap.keySet()) {
					MonitorRate mrate = ExportUtil.getMonitorRate(codeMap.get(code), lastperiodcodeMap.get(code),
							lastyearcodeMap.get(code), categorys, valuePropertyName,ispercent);
					result.add(mrate);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<MonitorRate> getAnalyseResult(T t, Object service, String serviceMethodName,List<RiskCategory> categorys, String valuePropertyName,boolean ispercent,ReportQueryParam currentparam,
			ReportQueryParam lastyearparam, ReportQueryParam lastperiodparam) {
		List<MonitorRate> result = new ArrayList<MonitorRate>();
		Page<T> page = new Page<T>();
		page.setPageNo(0);
		page.setPageSize(Integer.MAX_VALUE);
		try {
			Page<T> cuPage = (Page<T>) MethodUtils.invokeMethod(service, serviceMethodName, new Object[] {
					currentparam, page, true,true });
			Page<T> lastyearPage = (Page<T>) MethodUtils.invokeMethod(service, serviceMethodName, new Object[] {
					lastyearparam, page.simpleCopy(), true,true });
			Page<T> lastperiodPage = (Page<T>) MethodUtils.invokeMethod(service, serviceMethodName, new Object[] {
					lastperiodparam, page.simpleCopy(), true,true });
			if (CollectionUtils.isNotEmpty(cuPage.getResult())) {
				Map<String, T> codeMap = ConvertUtils.getMapByList(cuPage.getResult(), "riskCode", String.class);
				Map<String, T> lastyearcodeMap = ConvertUtils.getMapByList(lastyearPage.getResult(), "riskCode",
						String.class);
				Map<String, T> lastperiodcodeMap = ConvertUtils.getMapByList(lastperiodPage.getResult(), "riskCode",
						String.class);
				for (String code : codeMap.keySet()) {
					MonitorRate mrate = ExportUtil.getMonitorRate(codeMap.get(code), lastperiodcodeMap.get(code),
							lastyearcodeMap.get(code), categorys, valuePropertyName,ispercent);
					result.add(mrate);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static MonitorRate getMonitorRate(Object current, Object lastPeriod, Object lastYear, List<RiskCategory> rc,String valuePropertyName,boolean ispercent) {
		MonitorRate mrate = new MonitorRate();
		try {
			String riskCode = (String) PropertyUtils.getProperty(current, "riskCode");
			mrate.setRiskCode(riskCode);
			RiskCategory category = ConvertUtils.getObjectFromList(rc, "riskCode", riskCode);
			mrate.setRiskmearment(rc.get(0).getRiskName());
			mrate.setMearmentdetail(category.getRiskName());
			mrate.setRiskCategory(category);
			mrate.setDate(DateUtil.getShortDate((Date) PropertyUtils.getProperty(current, "reportDate")));
			mrate.setOrg((String) PropertyUtils.getProperty(PropertyUtils.getProperty(current, "organization"), "name"));
			mrate.setPeriod((int) PropertyUtils.getProperty(current, "period"));
			if (ispercent) {
				double cuValue = (double) PropertyUtils.getProperty(current, valuePropertyName);
				if (lastYear != null) {
					double lastYearValue = (double) PropertyUtils.getProperty(lastYear, valuePropertyName);
					double tong = (cuValue + 0.0) / lastYearValue - 1;
					mrate.setTongRate(ConvertUtils.get2pointDouble(tong * 100) + "%");
				}
				if (lastPeriod != null) {
					double lastPeriodValue = (double) PropertyUtils.getProperty(lastPeriod, valuePropertyName);
					double huan = (cuValue + 0.0) / lastPeriodValue - 1;
					mrate.setHuanRate(ConvertUtils.get2pointDouble(huan * 100) + "%");
				}
				if (ispercent)
					mrate.setValue(ConvertUtils.get2pointDouble(cuValue) + "%");
			} else {
				long cuValue = Long.parseLong(PropertyUtils.getProperty(current, valuePropertyName)+"");
				if (lastYear != null) {
					long lastYearValue = Long.parseLong(PropertyUtils.getProperty(lastYear, valuePropertyName)+"");
					double tong = (cuValue + 0.0) / lastYearValue - 1;
					mrate.setTongRate(ConvertUtils.get2pointDouble(tong * 100) + "%");
				}
				if (lastPeriod != null) {
					long lastPeriodValue =Long.parseLong(PropertyUtils.getProperty(lastPeriod, valuePropertyName)+"");
					double huan = (cuValue + 0.0) / lastPeriodValue - 1;
					mrate.setHuanRate(ConvertUtils.get2pointDouble(huan * 100) + "%");
				}
				mrate.setValue(cuValue + "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return mrate;
	}
	
	public static MonitorRate getMonitorRate(Object current,RiskCategory parentRC,String valuePropertyName,boolean ispercent) {
		MonitorRate mrate = new MonitorRate();
		try {
			String riskCode = (String) PropertyUtils.getProperty(current, "riskCode");
			mrate.setRiskCode(riskCode);
			RiskCategory category = (RiskCategory)PropertyUtils.getProperty(current, "riskCategory");
			if (parentRC != null) {
				mrate.setRiskmearment(parentRC.getRiskName());
			} else {
				mrate.setRiskmearment(category.getRiskName());
			}
			mrate.setMearmentdetail(category.getRiskName());
			mrate.setRiskCategory(category);
			mrate.setDate(DateUtil.getShortDate((Date) PropertyUtils.getProperty(current, "reportDate")));
			mrate.setOrg((String) PropertyUtils.getProperty(PropertyUtils.getProperty(current, "organization"), "name"));
			mrate.setPeriod((int) PropertyUtils.getProperty(current, "period"));
			if (ispercent) {
				double cuValue = (double) PropertyUtils.getProperty(current, valuePropertyName);
				mrate.setValue(ConvertUtils.get2pointDouble(cuValue) + "%");					
			} else {
				long cuValue = Long.parseLong(PropertyUtils.getProperty(current, valuePropertyName)+"");
				mrate.setValue(cuValue + "");				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return mrate;
	}
	
	public static List<MonitorRate> getMonitorRates(List currents, String valuePropertyName, boolean ispercent) {
		List<MonitorRate> rates = new ArrayList<MonitorRate>();
		try {
			for (Object current : currents) {
				String riskCode = (String) PropertyUtils.getProperty(current, "riskCode");
				MonitorRate mrate = new MonitorRate();
				mrate.setRiskCode(riskCode);
				RiskCategory category = (RiskCategory) PropertyUtils.getProperty(current, "riskCategory");
				mrate.setRiskmearment(category.getRiskName());
				mrate.setMearmentdetail(category.getRiskName());
				mrate.setRiskCategory(category);
				mrate.setDate(DateUtil.getShortDate((Date) PropertyUtils.getProperty(current, "reportDate")));
				mrate.setOrg((String) PropertyUtils.getProperty(PropertyUtils.getProperty(current, "organization"),
						"name"));
				mrate.setPeriod((int) PropertyUtils.getProperty(current, "period"));
				if (ispercent) {
					double cuValue = (double) PropertyUtils.getProperty(current, valuePropertyName);
					mrate.setValue(ConvertUtils.get2pointDouble(cuValue) + "%");
				} else {
					long cuValue = Long.parseLong(PropertyUtils.getProperty(current, valuePropertyName) + "");
					mrate.setValue(cuValue + "");
				}
				rates.add(mrate);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return rates;
	}
	
	public static List<MonitorRate> getMonitorRates_Available(List<SystemAvailableRate> currents) {
		List<MonitorRate> rates = new ArrayList<MonitorRate>();
		try {
			for (SystemAvailableRate current : currents) {
				rates.addAll(to2SystemAvailabel(current));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return rates;
	}
	
	private static List<MonitorRate> to2SystemAvailabel(SystemAvailableRate current){
		List<MonitorRate> rates = new ArrayList<MonitorRate>();
		String riskCode =current.getRiskCode();				
		RiskCategory category = current.getRiskCategory();
		MonitorRate mrate1 = new MonitorRate();
		mrate1.setRiskCode(riskCode+"001");
		mrate1.setRiskmearment("名义可用率");
		mrate1.setMearmentdetail("名义可用率");
		mrate1.setRiskCategory(category);
		mrate1.setDate(DateUtil.getShortDate(current.getReportDate()));
		mrate1.setOrg(current.getOrganization().getName());
		mrate1.setPeriod(current.getPeriod());
		mrate1.setValue(ConvertUtils.get2pointDouble(current.getPercentExpected()) + "%");
		MonitorRate mrate2 = new MonitorRate();
		mrate2.setRiskCode(riskCode+"002");
		mrate2.setRiskmearment("实际可用率");
		mrate2.setMearmentdetail("实际可用率");
		mrate2.setRiskCategory(category);
		mrate2.setDate(DateUtil.getShortDate(current.getReportDate()));
		mrate2.setOrg(current.getOrganization().getName());
		mrate2.setPeriod(current.getPeriod());
		mrate2.setValue(ConvertUtils.get2pointDouble(current.getPercentActual()) + "%");
		rates.add(mrate1);
		rates.add(mrate2);
		return rates;
	}
	
	public static List<RiskCategory> dealwithSystemAvailabelCategorys(List<RiskCategory> categorys){
		List<RiskCategory> list=new ArrayList<RiskCategory>();
		int size=categorys.size();
		for(int i=0;i<size;i++){
			RiskCategory rc=categorys.get(i);
			list.add(rc);
			String code=rc.getRiskCode();
			if(code.startsWith("11")){
				rc.setHasLeaf(true);
				RiskCategory temp1=new RiskCategory();
				temp1.setRiskCode(code+"001");
				temp1.setRiskName("名义可用率");
				temp1.setHasLeaf(false);
				RiskCategory temp2=new RiskCategory();
				temp2.setRiskCode(code+"002");
				temp2.setRiskName("实际可用率");
				temp2.setHasLeaf(false);
				list.add(temp1);
				list.add(temp2);
			}
		}
		return list;
	}
	
	public static String getShowDate(RiskCategory riskCategory,int period,Date reportDate) {
		return getShowDate(riskCategory.getCycle(), period, reportDate);
	}
	
	public static String getShowDate(int cycle,int period,Date reportDate) {
		String periodStr = period + "";
		if (cycle == 1) {
			periodStr = "D" + period;
		} else if (cycle == 2) {
			periodStr = "W" + period;
		} else if (cycle == 3) {
			periodStr = "M" + period;
		}
		return DateUtil.getShortDate(reportDate) + "(" + periodStr + ")";
	}
}