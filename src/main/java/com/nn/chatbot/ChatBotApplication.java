package com.nn.chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@AutoConfigurationPackage
public class ChatBotApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ChatBotApplication.class, args);
	}

}
