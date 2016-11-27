package com.shtitan.timesynchronize.controller.system;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.shtitan.timesynchronize.entity.BackupDB;
import com.shtitan.timesynchronize.service.system.BackupDbService;


@Controller
@RequestMapping("/backupdb")
public class BackupDBController {
  @Autowired	
  private BackupDbService backupDbService;
  
  @RequestMapping(value = "/backup.do")
  public String backupdb(Model model){
	  model.addAttribute("backup", backupDbService.getBackupDB());
	  return "system/backup";
  }
  
  @RequestMapping(value = "/update.do")
  @ResponseBody
  public String update(HttpServletRequest request){
	  String ip = request.getParameter("ip");
	  String port = request.getParameter("port");
	  String username = request.getParameter("username");
	  String password = request.getParameter("password");
	  String daily = request.getParameter("daily");
	  BackupDB db=backupDbService.getBackupDB();
	  db.setIp(ip);
	  db.setUsername(username);
	  db.setPassword(password);
	  db.setPort(Integer.parseInt(port));
	  db.setDaily(Integer.parseInt(daily));
	  backupDbService.updateBackUpdb(db);
	  return "ok";
  }
  
  @RequestMapping(value = "/changeStatus.do")
  public ModelAndView changeStatus(){
	  backupDbService.changestatus();  
	  return new ModelAndView("redirect:/backupdb/backup.do");
  }  
}
