package com.cnksi.config;

import com.cnksi.controller.WebSocketController;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyLogBackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
// 输出格式，用系统的，不然你输出的东西 不会解析
    Layout<ILoggingEvent> layout;
    @Override
    protected void append(ILoggingEvent eventObject) {
        if (eventObject != null && eventObject.getMessage() != null) {
            String message = layout.doLayout(eventObject);
            try {
            // 自定义的socket发送消息
                WebSocketController.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

