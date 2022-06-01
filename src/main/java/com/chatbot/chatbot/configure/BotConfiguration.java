package com.chatbot.chatbot.configure;

import com.chatbot.chatbot.event.EventListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BotConfiguration {

    @Value("${token}")
    private String token;

    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners) {
        GatewayDiscordClient client = DiscordClientBuilder.create(token).build().login().block();

        for (var listener : eventListeners) {
            client.on(listener.getEventType()).flatMap(listener::execute).onErrorResume(listener::handleError).subscribe();
        }

        return client;
    }

}
