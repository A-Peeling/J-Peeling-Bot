/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apeeling.jpeeling;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.Embed;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bot {

    public static void main(String[] args) {
        GatewayDiscordClient client = DiscordClientBuilder.create("BOT_TOKEN").build().login().block();

        assert client != null;
        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    User self = event.getSelf();
                    System.out.printf("Logged in as %s#%s%n", self.getUsername(), self.getDiscriminator());
                });

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("%ping"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Pong!"))
                .subscribe();

        // date and time command
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime = now.format(format);
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("%datetime"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(formatDateTime))
                .subscribe();

        // garfield command fuck im tired
        int year = now.getYear();
        LocalDate rhrn = LocalDate.now();
        DateTimeFormatter yymmdd = DateTimeFormatter.ofPattern("yyMMdd");
        String formatDate = rhrn.format(yymmdd);
        String formatDashes = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("%garfield"))
                .flatMap(Message::getChannel)
                .flatMap(
                        channel -> channel.createMessage(
                                messageSpec -> messageSpec.setEmbed(embedSpec -> {
                                    embedSpec.setImage("http://www.professorgarfield.org/ipi1200/" + year + "/ga" + formatDate + ".gif");
                                    embedSpec.setDescription("Today on Garfield - " + formatDashes);
                                })
                        )
                )
                .subscribe();

        client.onDisconnect().block();
    }

}
