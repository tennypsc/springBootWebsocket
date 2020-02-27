package com.cnksi.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
 
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
 
/**
 * WebSocket 服务
 *
 **/
@Slf4j
@ServerEndpoint(value = "/websocket/{userno}")
@Component
public class WebSocketServer {
 
	
	// 用来记录当前连接数的变量
	private static volatile int onlineCount = 0;
 
	// 线程安全Set，用来存放每个客户端对应的 WebSocket 对象
	//private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();
	
	//concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	private static ConcurrentHashMap<String, WebSocketServer> webSocketSet = new ConcurrentHashMap<String, WebSocketServer>();
	   
 
	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
 
	private String userno = "";

	
	/**
	 * 连接建立成功调用的方法
	 * @param session 连接会话
	 */
	@OnOpen
	public void onOpen(@PathParam(value = "userno") String param,Session session) {
		System.out.println(param);
        userno = param;//接收到发送消息的人员编号
        this.session = session;
        webSocketSet.put(param, this);//加入map中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
	}
 
	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		if (!userno.equals("")) {
            webSocketSet.remove(userno);  //从set中删除
            subOnlineCount();           //在线数减1
            log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
        }
	}
 
	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message 消息
	 * @param session 连接会话
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("收到来自客户端的消息：" + message);
		
		if (1 > 2) {
            sendAll(message);
        } else {
            //给指定的人发消息
            sendToUser(message);
        }
 
		
	}
 
	private void sendToUser(String message) {
		 String sendUserno = message.split("[|]")[1];
	        String sendMessage = message.split("[|]")[0];
	        String now = getNowTime();
	        try {
	            if (webSocketSet.get(sendUserno) != null) {
	                webSocketSet.get(sendUserno).sendMessage(now + "用户" + userno + "发来消息：" + " <br/> " + sendMessage);
	            } else {
	                System.out.println("当前用户不在线");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
	}

	private void sendAll(String message) {
		 String now = getNowTime();
	        String sendMessage = message.split("[|]")[0];
	        //遍历HashMap
	        for (String key : webSocketSet.keySet()) {
	            try {
	                //判断接收用户是否是当前发消息的用户
	                if (!userno.equals(key)) {
	                    webSocketSet.get(key).sendMessage(now + "用户" + userno + "发来消息：" + " <br/> " + sendMessage);
	                    System.out.println("key = " + key);
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }

	}

	private String getNowTime() {
			Date date = new Date();
	        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String time = format.format(date);
	        return time;
	}

	/**
	 * 发生错误时调用的方法
	 *
	 * @param session 连接会话
	 * @param error 错误信息
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("发送错误");
		error.printStackTrace();
	}
 
	/**
	 * 实现服务器主动推送消息
	 *
	 * @param message 消息
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}
 
	
 
	/**
	 * 获取连接数
	 * @return 连接数
	 */
	private static synchronized int getOnlineCount() {
		return onlineCount;
	}
 
	/**
	 * 连接数加1
	 */
	private static synchronized void addOnlineCount() {
		WebSocketServer.onlineCount++;
	}
 
	/**
	 * 连接数减1
	 */
	private static synchronized void subOnlineCount() {
		WebSocketServer.onlineCount--;
	}
 
}
