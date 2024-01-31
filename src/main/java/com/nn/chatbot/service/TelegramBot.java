package com.nn.chatbot.service;

import com.nn.chatbot.config.BotConfig;
import com.nn.chatbot.googleConfig.SheetsQuickstart;
import com.nn.chatbot.model.CashFlow;
import com.nn.chatbot.utils.MessageParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private  final SheetsQuickstart sheetsQuickstart;
    private  final BotConfig botConfig;

    public TelegramBot(SheetsQuickstart sheetsQuickstart, BotConfig botConfig){
        this.sheetsQuickstart = sheetsQuickstart;
        this.botConfig = botConfig;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Добрый вечер"));
        listOfCommands.add(new BotCommand("/help", "Просмотр формата ввода данных"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }

    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {

                case "/start" -> {
                    sendMessage("Данный бот предназначен для учета прихода/расхода", chatId);

                }
                case "/help" -> {
                    sendMessage("Формат ввода данных <+/->  <цена>  <примечание>  <номер заказа>", chatId);
                }
                default -> {
                    List<CashFlow> cashFlowList = MessageParser.parse(messageText);
                    String message = "Данные успешно записаны";
                    if(cashFlowList.isEmpty()){
                        message = "Введите сообщение в заданном формате, просмотрите команду /help";
                    }
                    else {
                        try {
                            sheetsQuickstart.addArrival(cashFlowList);
                        } catch (Exception e) {
                            log.error(Arrays.toString(e.getStackTrace()));
                            message = "Внутренняя ошибка сервера";
                        }
                    }
                    sendMessage(message, chatId);
                }
            }
        }
    }

    private void sendMessage(String textToSend, long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        send(message);
    }
    private void send(SendMessage msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }
    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }
}
