package com.shtitan.timesynchronize.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shtitan.timesynchronize.websocket.message.AbstractWebsocketMessage;

public class WebSocketMessageCenter {
	private static Logger log = LoggerFactory.getLogger(WebSocketMessageCenter.class); 
	/**
     * The lock used to synchronize access to this Room.
     */
    private final ReentrantLock roomLock = new ReentrantLock();

    /**
     * Indicates if this room has already been shutdown.
     */
    private volatile boolean closed = false;
    
    /**
     * The maximum number of consumers that can join this center.
     */
    private static final int MAX_CONSUMER_COUNT = 1000;
    
    //在线客户端
    private final static List<Client> clients = new ArrayList<Client>();
    //客户端分组，安装OLT deviceName分组
    private final static Map<String,List<Client>> sessionMap = new HashMap<String,List<Client>>();
    
    /**
     * A list of cached {@link Runnable}s to prevent recursive invocation of Runnables
     * by one thread. This variable is only used by one thread at a time and then
     * set to <code>null</code>.
     */
    private List<Runnable> cachedRunnables = null;

    /**
     * Submits the given Runnable to the Room Executor and waits until it
     * has been executed. Currently, this simply means that the Runnable
     * will be run directly inside of a synchronized() block.<br>
     * Note that if a runnable recursively calls invokeAndWait() with another
     * runnable on this Room, it will not be executed recursively, but instead
     * cached until the original runnable is finished, to keep the behavior of
     * using a Executor.
     * @param task
     */
    public void invokeAndWait(Runnable task)  {

        // Check if the current thread already holds a lock on this room.
        // If yes, then we must not directly execute the Runnable but instead
        // cache it until the original invokeAndWait() has finished.
        if (roomLock.isHeldByCurrentThread()) {

            if (cachedRunnables == null) {
                cachedRunnables = new ArrayList<>();
            }
            cachedRunnables.add(task);

        } else {

            roomLock.lock();
            try {
                // Explicitely overwrite value to ensure data consistency in
                // current thread
                cachedRunnables = null;

                if (!closed) {
                    task.run();
                }

                // Run the cached runnables.
                if (cachedRunnables != null) {
                    for (int i = 0; i < cachedRunnables.size(); i++) {
                        if (!closed) {
                            cachedRunnables.get(i).run();
                        }
                    }
                    cachedRunnables = null;
                }

            } finally {
                roomLock.unlock();
            }

        }

    }

    /**
     * Shuts down the roomExecutor and the drawmessageBroadcastTimer.
     */
    public void shutdown() {
        invokeAndWait(new Runnable() {
            @Override
            public void run() {
                closed = true;
               
            }
        });
    }
    
    
    //向连接池中添加连接
  	public static void addClientToMsgCenter(Client client,String deviceName){
  		//添加连接
  		clients.add(client);
  		if(StringUtils.isNotEmpty(deviceName)){
  			List<Client> clients = sessionMap.get(deviceName);
  			if(CollectionUtils.isEmpty(clients)){
  				sessionMap.put(deviceName, new ArrayList<Client>());
  			}
  			sessionMap.get(deviceName).add(client);
  		}
  	    log.info("add a client to msgcenter: " + client.getSession().toString());
  	}
  	
  	public static void removeFromMsgCenter(String sessionId){
  		Iterator<Client> it = clients.iterator();
  		while(it.hasNext()){
  			Client c = it.next();
  			if(c.getSession().getId().endsWith(sessionId)){
  				it.remove();
  				break;
  			}
  		}
  		
  		for(Entry<String,List<Client>> entry:sessionMap.entrySet()){
  			List<Client> cList = entry.getValue();
  			Iterator<Client> cit = cList.iterator();
  			while(cit.hasNext()){
  	  			Client c2 = cit.next();
  	  			if(c2.getSession().getId().endsWith(sessionId)){
  	  			   cit.remove();
  	  			   
  	  			}
  			}
  		}
  		
  		log.info("remove a client to msgcenter: " + sessionId);
  	}
  	

  	//获取所有的连线的设备
  	public static List<Client> getOnlineDevices(){ 		
  		return clients;
  	}

  	//获取在线的某一group的设备
  	public static List<Client> getGroupBDevices(String deviceName){ 		
  		return sessionMap.get(deviceName);
  	}
  	
  	//向一组客户端发送消息
  	public static void sendMessageToGroupClients(String deviceName,AbstractWebsocketMessage msg){
  		List<Client> cList = sessionMap.get(deviceName);
  		if(CollectionUtils.isEmpty(cList)){
  			return;
  		}
  		try {
  			for (Client client : cList) {
  				client.sendMessage(msg);
  			}
  		} catch (Exception ex) {
  			 log.error("send message to group clients , Unexpected exception: " + ex.toString(), ex);
  			 ex.printStackTrace();
  		}
  	}
  	
  	public static void sendMessageToOne(String sessionId,AbstractWebsocketMessage msg){
  		Client client=null;
  		for(Client cli : clients){
  			if(cli.getSession().getId().endsWith(sessionId)){
  				client = cli;
  				break;
  			}
  		}
  		
  		if(null!=client){
  			try {
				client.sendMessage(msg);
			} catch (Exception ex) {
				 log.error("send message to one client , Unexpected exception: " + ex.toString(), ex);
				 ex.printStackTrace();
			}
  		}
  	}
    //发送数据给所有在线设备
  	public static void sendMessageToAllClients(AbstractWebsocketMessage msg){
  		try {
  			for (Client client : clients) {
  				client.sendMessage(msg);
  			}
  		} catch (Exception ex) {
  			 log.error("send message to all client , Unexpected exception: " + ex.toString(), ex);
  			ex.printStackTrace();
  		}
  	}
    
    
}
