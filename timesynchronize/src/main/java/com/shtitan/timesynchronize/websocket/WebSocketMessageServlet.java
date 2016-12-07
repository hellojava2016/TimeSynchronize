package com.shtitan.timesynchronize.websocket;

import java.io.EOFException;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shtitan.timesynchronize.websocket.message.CloseWebsocketMessage;



@ServerEndpoint(value="/sendMessage") 
public class WebSocketMessageServlet  {
	private Logger log = LoggerFactory.getLogger(WebSocketMessageServlet.class); 
	
	private static volatile WebSocketMessageCenter msgCenter = null;
    private static final Object msgLock = new Object();
	
	public static WebSocketMessageCenter getWebSocketMessageCenter(boolean create) {
        if (create) {
            if (msgCenter == null) {
                synchronized (msgLock) {
                    if (msgCenter == null) {
                    	msgCenter = new WebSocketMessageCenter();
                    }
                }
            }
            return msgCenter;
        } else {
            return msgCenter;
        }
    }
	
	@OnOpen  
    public void start(Session session){  
		session.setMaxTextMessageBufferSize(10000);
		final Client client = new Client(session);
	   
         
        Map<String,List<String>> params = session.getRequestParameterMap();
        
        String deviceName="";
        if(params.containsKey("deviceName")){
        	deviceName = params.get("deviceName").get(0);
        }
        
        final String name = deviceName;
        System.out.println(params);
        
        final WebSocketMessageCenter msgCenter = getWebSocketMessageCenter(true);
        msgCenter.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {

                    try {
                        
                        msgCenter.addClientToMsgCenter(client, name);
                        
                    } catch (IllegalStateException ex) {
                        // Probably the max. number of players has been
                        // reached.
                        client.sendMessage(new CloseWebsocketMessage());
                        // Close the connection.
                        client.close();
                    }

                } catch (RuntimeException ex) {
                    log.error("Unexpected exception: " + ex.toString(), ex);
                }
            }
        });
        
        System.out.println("session "+session.getId()+" open."); 
        
    }  
  
    @OnMessage  
    public void process(Session session, String message){  
        System.out.println("rece:" + message);  
        RemoteEndpoint.Basic remote = session.getBasicRemote();  
        int c = Integer.valueOf(message);  
        for (int i=1; i<=c; i++){  
            try {  
                remote.sendText("response "+i);  
                Thread.sleep(500);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    @OnClose  
    public void end(Session session){  
        
        final Client client = new Client(session);
        final WebSocketMessageCenter msgCenter = getWebSocketMessageCenter(false);
        if (msgCenter != null) {
        	msgCenter.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    try {
                    	msgCenter.removeFromMsgCenter(client.getSession().getId());
                    } catch (RuntimeException ex) {
                        log.error("Unexpected exception: " + ex.toString(), ex);
                    }
                }
            });
        }
        System.out.println("session "+session.getId()+" close.");
        
    }  
  
    @OnError  
    public void error(Session session, java.lang.Throwable throwable){  
        int count = 0;
        Throwable root = throwable;
        while (root.getCause() != null && count < 20) {
            root = root.getCause();
            count ++;
        }
        if (root instanceof EOFException) {
        } else {
        	log.error("onError: " + throwable.toString(), throwable);
        }
    }
    

}
