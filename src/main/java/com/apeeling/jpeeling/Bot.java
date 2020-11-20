/*
 * The MIT License
 *
 * Copyright 2020 A-Peeling.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.apeeling.jpeeling;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is a pretty epic bot ngl
 *
 * @author reitraced
 * @author stophman1
 */

public class Bot {

    public static void main(String[] args) throws IOException {
        Properties prop = PropertiesInterface.readPropertiesFile("./res/config.properties");
        if (prop.getProperty("token").equals("INSERT_TOKEN"))
        {
            System.out.print("You need to setup the bot in order to use it. /res/config.properties");
            System.exit(0);
        }
        GatewayDiscordClient client = DiscordClientBuilder.create(prop.getProperty("token")).build().login().block();

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
        
        // calendar command and this time im wide awake yee haw
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("%calendar"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(messageSpec -> { try { 
                    messageSpec.addFile("gamer.png", CalCommand.gamer());
            } catch (IOException ex) {
                Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
            }
                                                                           messageSpec.setContent("here is a bonafide gamer:"); }))
                .subscribe();

        client.onDisconnect().block();
    }

}
