package com.shtitan.timesynchronize.service.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shtitan.timesynchronize.scheduler.AbstactCustomizeSchedulerTask;
import com.shtitan.timesynchronize.websocket.WebSocketMessageCenter;
import com.shtitan.timesynchronize.websocket.WebSocketMessageServlet;
import com.shtitan.timesynchronize.websocket.message.AlarmWebSocketMessage;

public class WebSocketTimeTask extends AbstactCustomizeSchedulerTask {
	private Logger log = LoggerFactory.getLogger(WebSocketTimeTask.class); 
	private static WebSocketTimeTask instance = new WebSocketTimeTask();
   
	public static WebSocketTimeTask getInstance() {
		return instance;
	}

	@Override
	public void execute() {
		final WebSocketMessageCenter msgCenter = WebSocketMessageServlet.getWebSocketMessageCenter(true);
		msgCenter.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                	AlarmWebSocketMessage msg = new AlarmWebSocketMessage("ne=504103:/",5,"tee","test","sss");
                	msgCenter.sendMessageToAllClients(msg);
                	log.info("message aging for 5 min");

                } catch (RuntimeException ex) {
                    log.error("Unexpected exception: " + ex.toString(), ex);
                }
            }
        });
	}
	
	
}
