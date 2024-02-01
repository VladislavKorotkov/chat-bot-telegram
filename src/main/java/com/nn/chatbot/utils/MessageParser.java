package com.nn.chatbot.utils;

import com.nn.chatbot.model.CashFlow;
import com.nn.chatbot.model.TypeCashFlow;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class MessageParser {
    public static List<CashFlow> parse(String text) {
        List<CashFlow> cashFlows = new ArrayList<>();
        try{
            int balance = 0;

            Pattern pattern = Pattern.compile("([-+])\\s*(\\d+\\.?\\d*)\\s*([^\\d]+)(?:\\s*(\\d+))?");

            String[] lines = text.split("\\n");
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("Остаток")) {
                    String[] parts = line.split("\\s+");
                    balance = Integer.parseInt(parts[1]);
                } else {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        TypeCashFlow type = matcher.group(1).equals("+") ? TypeCashFlow.ARRIVAL : TypeCashFlow.EXPENDITURE;
                        double price = Double.parseDouble(matcher.group(2));
                        String description = matcher.group(3).trim();
                        int orderNumber = matcher.group(4) != null ? Integer.parseInt(matcher.group(4)) : 0;

                        CashFlow cashFlow = new CashFlow(type, price, description, orderNumber);
                        cashFlows.add(cashFlow);
                    }
                    else{
                        log.info(line);
                    }
                }
            }
        }
        catch (Exception e){
            log.error(Arrays.toString(e.getStackTrace()));
        }
        return cashFlows;
    }
}