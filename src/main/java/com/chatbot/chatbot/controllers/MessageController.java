package com.chatbot.chatbot.controllers;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;

@LineMessageHandler
public class MessageController {

    @Autowired
    LineMessagingClient lineMessagingClient;

    @EventMapping
    public void handleTextEvent(MessageEvent<TextMessageContent> messageEvent) {
        try {
            Source source = messageEvent.getSource(); // 소스 메세지 캡처
            String userId = source.getUserId(); // 보낸 사람의 유저 ID
            String replyToken = messageEvent.getReplyToken(); // 응답을 보낼 때 메세지에 회신할 토큰
            String msg = messageEvent.getMessage().getText(); // 보낸 사람의 메세지를 검색하여 저장

            String displayName = lineMessagingClient.getProfile(userId).get().getDisplayName(); // 보낸 사람의 이름
            String answer = String.format("Hello, %s! Your message is %s", displayName, msg);
            TextMessage responseMsg = new TextMessage(answer); // 응답 변수 Message Object로만 회신

            lineMessagingClient.replyMessage(new ReplyMessage(replyToken, responseMsg)); // 응답을 보냄
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
