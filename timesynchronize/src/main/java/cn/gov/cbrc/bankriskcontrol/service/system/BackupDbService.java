package cn.gov.cbrc.bankriskcontrol.service.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.gov.cbrc.bankriskcontrol.dao.system.BackupDBDao;
import cn.gov.cbrc.bankriskcontrol.entity.BackupDB;
import cn.gov.cbrc.bankriskcontrol.util.DateUtil;

@Service("backupDbService")
@Transactional
public class BackupDbService {
	@Autowired
	private BackupDBDao backupDBDao;
	
	private static ScheduledExecutorService service=Executors.newScheduledThreadPool(2);
	
	private ScheduledFuture future;
	
	public synchronized void changestatus(){
		final BackupDB db=getBackupDB();
		if(null==db)
			return;
		db.setTaskstatus(!db.isTaskstatus());
		db.setChecknum(1+db.getChecknum());
		backupDBDao.update(db);
		if(db.isTaskstatus()){
			startTask(db);
		}else{
			shutdownTask();
		}
		 
	}
	
	public  void shutdownTask(){
		if(null!=future){
			try{
				future.cancel(true);
				future=null;
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}
	}
	
	public void startTask(final BackupDB db){

		 Runnable backtask=new Runnable()
        {
             public void run()
             {
           	  execute(db);
             }
        };
        Calendar nowcal = Calendar.getInstance();
        int nowhour = nowcal.get(Calendar.HOUR_OF_DAY);
        int daily = db.getDaily();
        long t =1L;
        if(nowhour<daily){
       	 nowcal.set(Calendar.HOUR_OF_DAY, daily);
       	 nowcal.set(Calendar.SECOND, 0);
       	 nowcal.set(Calendar.MINUTE, 0);
       	 t = nowcal.getTimeInMillis()-System.currentTimeMillis();
       	 t = t/1000;
       	 if(t<0)
       		 t=0;
        }else{
       	 long oneday = 24*3600;
       	 long chazhi = (nowhour-daily)*3600+60*nowcal.get(Calendar.MINUTE)+nowcal.get(Calendar.SECOND);
       	 t=oneday-chazhi+70*(new Random().nextInt(10));
        }
        
        System.out.println(t+"秒后开始执行数据库备份操作---");
        
        future=service.scheduleAtFixedRate(backtask,t,24*3600,TimeUnit.SECONDS);
        
        System.out.println("user.dir:"+System.getProperty("user.dir"));
	
	}
	
	//server端启动的时候调用
	public void startupOnServer(){
		final BackupDB db=getBackupDB();
		if(null==db)
			return;
		if(db.isTaskstatus()){
			startTask(db);
		}
	}
	
	public BackupDB getBackupDB(){
		List<BackupDB> list = backupDBDao.getAll();
		if(CollectionUtils.isEmpty(list))
			return null;
		return list.get(0);
	}
	
	public synchronized  void updateBackUpdb(BackupDB backupdb){
		backupdb.setChecknum(backupdb.getChecknum()+1); 
		backupDBDao.saveOrUpdate(backupdb);
		shutdownTask();
		if(backupdb.isTaskstatus()){
			startTask(backupdb);
		}
	}
	
	public void execute(BackupDB db) {
//		 BackupDB dbindb=null;
//		try {
//			dbindb = getBackupDB();
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		 if(dbindb.getChecknum()!=db.getChecknum()){
//			 shutdownTask();
//		 }
		String port = db.getPort()+"";
		String user = db.getUsername();
		String pwd = db.getPassword();
		String ip=db.getIp();
		String fileName="bankriskcontrol"+DateUtil.getShortCurrentDate()+".sql";
		String dir=BackupDbService.class.getResource("/").getPath();
		try {
			if(SystemUtils.IS_OS_WINDOWS){				
				dir=dir.startsWith("/")?dir.substring(1,dir.length()):dir;
				File exe = new File(dir+"mysqldump.exe");
				String cmdStr = " -u " + user +" --password=" + pwd + " -h "+ip;
				String exeStr = exe.getAbsolutePath() + cmdStr + " --default-character-set=utf8 --opt --hex-blob -x -P "
						+ port + " bank_risk_control > ";				
				File backupFile = new File(dir + fileName);
				exeStr = "cmd /c " + exeStr + backupFile.getAbsolutePath();
				System.out.println("exeStr:" + exeStr);

				long t1 = System.currentTimeMillis();
				Process process = Runtime.getRuntime().exec(exeStr);
				String line = null;
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				// 读取ErrorStream很关键，这个解决了挂起的问题。
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
				br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
				process.waitFor();
				long t2 = System.currentTimeMillis();	
				System.out.println("backupDB cost " + (t2 - t1) + " ms");
			}else{
				File exe = new File(dir+"mysqldump");
				String cmdStr = " -u " + user +" --password=" + pwd + " -h "+ip;
				String exeStr = exe.getAbsolutePath() + cmdStr + " --default-character-set=utf8 --opt --hex-blob -x -P "
						+ port + " bank_risk_control -r";				
				File backupFile = new File(dir + fileName);
				exeStr =exeStr + backupFile.getAbsolutePath();
				System.out.println("exeStr:" + exeStr);

				long t1 = System.currentTimeMillis();
				Process process = Runtime.getRuntime().exec(exeStr);
				String line = null;
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				// 读取ErrorStream很关键，这个解决了挂起的问题。
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
				br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
				process.waitFor();
				long t2 = System.currentTimeMillis();	
				System.out.println("backupDB cost " + (t2 - t1) + " ms");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
