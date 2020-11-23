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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Properties;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This is a pretty epic bot ngl
 *
 * @author reitraced
 * @author stophman1
 */

public class Bot extends ListenerAdapter {
    public static void main(String[] args) throws LoginException, IOException {
        Properties prop = PropertiesController.readPropertiesFile("./res/config.properties");
        if (prop.getProperty("token").equals("INSERT_TOKEN")) {
            System.out.print("You need to setup the bot in order to use it. /res/config.properties");
            System.exit(0);
        }
        // args[0] should be the token
        // We only need 2 intents in this bot. We only respond to messages in guilds and private channels.
        // All other events will be disabled.
        JDABuilder.createLight(prop.getProperty("token"), GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new Bot())
                .setActivity(Activity.playing(prop.getProperty("doin")))
                .build();
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Properties prop = null;
		try {
			prop = PropertiesController.readPropertiesFile("./res/config.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String prefix = prop.getProperty("prefix");
        
        Message msg = event.getMessage();
        
        if (msg.getContentRaw().equals(prefix + "ping")) {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!") /* => RestAction<Message> */
                    .queue(response /* => Message */ -> response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue());
        }
        
        if (msg.getContentRaw().equals(prefix + "garfield")) {
            MessageChannel channel = event.getChannel();
        	DateTimeFormatter yymmdd = DateTimeFormatter.ofPattern("yyMMdd");
            DateTimeFormatter formatDashes = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        	EmbedBuilder embed = new EmbedBuilder();
        	embed.setImage("http://www.professorgarfield.org/ipi1200/" + LocalDateTime.now().getYear() + "/ga" + LocalDateTime.now().format(yymmdd) + ".gif")
                 .setDescription("Today on Garfield ‣ " + LocalDateTime.now().format(formatDashes));
        	channel.sendMessage(embed.build());
        }
    }
}