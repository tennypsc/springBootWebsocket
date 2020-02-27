package com.cnksi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cnksi.service.WebSocketServer;
import com.cnksi.service.WebSocketServer3;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;
 
/**
 * WebSocket 控制类
 *
 **/
@Slf4j
@Controller
@Api(description="websocket主动推送信息")
@RequestMapping("/webSocketCtrl")
public class WebSocketController {
	
	private static Session session;
 
	@ApiOperation(value="websocket主动推送信息")
	@ResponseBody
	@RequestMapping(value = "/pushMessageToWeb", method = RequestMethod.POST, consumes = "application/json")
	public String pushMessageToWeb(@RequestBody String message) {
		try {
			for (int i = 0; i < 100; i++) {
				WebSocketServer3.sendInfo("有新内容："+ i +"====" + message);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}
 
	@RequestMapping("/hello")
	public String helloHtml(HashMap<String, Object> map) {
		map.put("hello", "这是一个thymeleaf页面");
		return "MyHtml";
	}
	
	@RequestMapping("/log")
	public String log(HashMap<String, Object> map) {
		map.put("hello", "这是一个thymeleaf页面");
		return "log";
	}
	
	@RequestMapping("/log2")
	public String log2(HashMap<String, Object> map) {
		map.put("hello", "这是一个thymeleaf页面");
		return "log2";
	}
	
	public static void sendMessage(String message) throws Exception {
        if (session!=null){
            if (WebSocketController.session.isOpen()) {
                WebSocketController.session.getBasicRemote().sendText(message);
            }
        }
    }
	
 
}