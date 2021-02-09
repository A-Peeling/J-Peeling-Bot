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
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.Properties;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
        if (prop.getProperty("playingorstreaming").equals("streaming")) {
            if (!Activity.isValidStreamingUrl(prop.getProperty("streamingurl"))) {
                System.out.print("streamingurl is invalid!\n");
                JDABuilder.createLight(prop.getProperty("token"), GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                        .addEventListeners(new Bot())
                        .setActivity(Activity.playing(prop.getProperty("doin")))
                        .build();
                return;
            }
            JDABuilder.createLight(prop.getProperty("token"), GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                    .addEventListeners(new Bot())
                    .setActivity(Activity.streaming(prop.getProperty("doin"), prop.getProperty("streamingurl")))
                    .build();
        } else if (prop.get("playingorstreaming").equals("playing")) {
            JDABuilder.createLight(prop.getProperty("token"), GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                    .addEventListeners(new Bot())
                    .setActivity(Activity.playing(prop.getProperty("doin")))
                    .build();
        } else {
            System.out.print("ERROR IN THE CONFIG!!! >:( /res/config.properties \nThe field \"playingorstreaming\" must be either \"playing\" or \"streaming\"");
        }
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
        assert prop != null;
        String prefix = prop.getProperty("prefix");
        
        Message msg = event.getMessage();
        
        if (msg.getContentRaw().equals(prefix + "ping")) {
            if (event.getAuthor().isBot()) return;
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!") /* => RestAction<Message> */
                    .queue(response /* => Message */ -> response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue());
        }
        
        if (msg.getContentRaw().equals(prefix + "garfield")) {
            if (event.getAuthor().isBot()) return;
            MessageChannel channel = event.getChannel();
            DateTimeFormatter yymmdd = DateTimeFormatter.ofPattern("yyMMdd");
            DateTimeFormatter formatDashes = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            EmbedBuilder embed = new EmbedBuilder();
            embed.setImage("http://images.ucomics.com/comics/ga/" + LocalDateTime.now().getYear() + "/ga" + LocalDateTime.now().format(yymmdd) + ".gif")
                    .setDescription("Today on Garfield ‣ " + LocalDateTime.now().format(formatDashes));
            channel.sendMessage(embed.build()).queue();
        }
        
        if (msg.getContentRaw().startsWith(prefix + "garfield")) {
        	MessageChannel channel = event.getChannel();
        	DateTimeFormatter yymmdd = DateTimeFormatter.ofPattern("yyMMdd");
        	
        	try {
                DateTimeFormatter formatDashes = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        		LocalDate garfie = LocalDate.parse(msg.getContentRaw().substring(10));
        		EmbedBuilder embed = new EmbedBuilder();
        		embed.setImage("http://images.ucomics.com/comics/ga/" + garfie.getYear() + "/ga" + garfie.format(yymmdd) + ".gif")
        		.setDescription("Garfield comic for " + garfie.format(formatDashes));
        		channel.sendMessage(embed.build()).queue();
        	}
        	catch (StringIndexOutOfBoundsException e) {
        		return;
        	}
        	catch (DateTimeParseException e) {
        		channel.sendMessage("ayo what the fuck kinda shit date format is this i only do YYYY-MM-DD capice?").queue();
        	}
        }
        
        if (msg.getContentRaw().equals(prefix + "calendar")) {
            if (event.getAuthor().isBot()) return;
            MessageChannel channel = event.getChannel();
            EmbedBuilder embed = new EmbedBuilder();
            File garry = CalCommand.imageFile();
            String filename = garry.toString().substring(12);
            filename = filename.replace('[', ']');
            filename = filename.replace("]", "");
            embed.setImage("attachment://" + filename)
                    .setFooter("Cool footer!")
                    .setDescription("here is a bonafide gamer");
            try {
                channel.sendFile(new FileInputStream(garry), filename).embed(embed.build()).queue();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (msg.getContentRaw().equals(prefix + "datetime")) {
            if (event.getAuthor().isBot()) return;
            MessageChannel channel = event.getChannel();
            channel.sendMessage(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss"))).queue();
        }
        /*
        mi dracykeia
        .i mi na se bangu le jbobau.
        .i .e'o ko livycru mi bu'u le bavla'i ke nunde'a tcana
        */
        if (msg.getContentRaw().startsWith(prefix + "game")) {
            MessageChannel channel = event.getChannel();
            String messageContentStr = msg.getContentRaw().substring((prefix + "game").length());
            if (messageContentStr.length() == 0 || messageContentStr.length() == 1) {
                channel.sendMessage("You need to say a game for me to play. " + prefix + "game {game}").queue();
                return;
            }
            if (messageContentStr.charAt(0) == ' ') messageContentStr = messageContentStr.replaceFirst("^ *", "");
            if (event.getAuthor().isBot()) return;
            if (!event.getAuthor().toString().substring((event.getAuthor().toString().length() - 19), event.getAuthor().toString().length() - 1).equals(prop.getProperty("userid"))) {
                channel.sendMessage("Your user id is not in the config mister.").queue();
                return;
            }
            try {
                PropertiesController.ChangePropertiesFile("./res/config.properties", "doin", messageContentStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (prop.getProperty("playingorstreaming").equals("streaming")) {
                if (!Activity.isValidStreamingUrl(prop.getProperty("streamingurl"))) {
                    channel.sendMessage("ERROR!!! Your streamingurl is not valid!").queue();
                    return;
                }
                event.getJDA().getPresence().setActivity(Activity.streaming(prop.getProperty("doin"), prop.getProperty("streamingurl")));
                channel.sendMessage("Success! Now streaming " + messageContentStr).queue();
                return;
            } else if (prop.getProperty("playingorstreaming").equals("playing")) {
                event.getJDA().getPresence().setActivity(Activity.playing(prop.getProperty("doin")));
                channel.sendMessage("Success! Now playing " + messageContentStr).queue();
                return;
            }
            channel.sendMessage("ERROR IN THE CONFIG!!! >:( The field \"playingorstreaming\" must be either \"playing\" or \"streaming\", silly goose!!!1!!!1!").queue();

        }
        if (msg.getContentRaw().equalsIgnoreCase("do bot")) {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("yes you can do bot heres the github: https://github.com/A-Peeling/J-Peeling-Bot also use the branch jda-rewrite not main k thx").queue();
        }
        if (msg.getContentRaw().startsWith(prefix + "penis")) {
        	MessageChannel channel = event.getChannel();
        	try {
        		int painis = Integer.parseInt(msg.getContentRaw().substring(7));
        		String penout = "3";
        		for (int i = 0; i < painis; i++) {
        			penout = penout + "=";
        		}
        		penout = penout + "D";
        		channel.sendMessage(penout).queue();;
        	}
        	catch (NumberFormatException | StringIndexOutOfBoundsException e){
        		channel.sendMessage("bruh wheres my integer :rage::rage::rage:").queue();
        	}
        }
    }
}
