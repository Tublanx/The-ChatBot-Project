package com.chatbot.chatbot.event;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public abstract class MessageListener {

    public Mono<Void> processCommand(Message message) {
        return Mono.just(message)
                .filter(x -> x.getAuthor().map(y -> !y.isBot()).orElse(false))
                .filter(x -> x.getContent().equalsIgnoreCase("!todo"))
                .flatMap(Message::getChannel)
                .flatMap(z -> z.createMessage("Things to do today:\n - write a bot\n - eat lunch\n - play a game"))
                .then();
    }

}
