package com.nn.chatbot.utils;

import com.nn.chatbot.model.CashFlow;
import com.nn.chatbot.model.TypeCashFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {
    public static void parse(String text) {
//        List<Integer> additions = new ArrayList<>();
//        List<Integer> subtractions = new ArrayList<>();
//        int balance = 0;
//
//        List<CashFlow> cashFlowList = new ArrayList<>();
//
//        String[] lines = message.split("\n");
//        for (String line : lines) {
//            if (line.startsWith("+")) {
//                String updateLine = line.replace("+", "");
//                String[] parts = updateLine.split("\\s+");
//                int number;
//                if(parts[parts.length-1].matches("[-+]?\\d+"))
//                    number= Integer.parseInt(parts[parts.length-1]);
//                StringBuilder stringBuilder = new StringBuilder();
//                for()
//                CashFlow cashFlow = CashFlow.builder()
//                        .typeCashFlow(TypeCashFlow.ARRIVAL)
//                        .price(Double.parseDouble(parts[0]))
//                        .description(parts[1])
//                        .order_number(Integer.parseInt(parts[2]))
//                        .build();
//                cashFlowList.add(cashFlow);
//            } else if (line.startsWith("-")) {
//                String updateLine = line.replace("-", "");
//                String[] parts = updateLine.split("\\s+");
//                CashFlow cashFlow = CashFlow.builder()
//                        .typeCashFlow(TypeCashFlow.EXPENDITURE)
//                        .price(Double.parseDouble(parts[0]))
//                        .description(parts[1])
//                        .order_number(Integer.parseInt(parts[2]))
//                        .build();
//                cashFlowList.add(cashFlow);
//            } else if (line.startsWith("Остаток")) {
//                String[] parts = line.split("\\s+");
//                balance = Integer.parseInt(parts[1]);
//            }
//        }
//
//        for(CashFlow cashFlow: cashFlowList){
//            System.out.println(cashFlow);
//        }
//        System.out.println("Balance: " + balance);

        List<CashFlow> cashFlows = new ArrayList<>();
        int balance = 0;

        Pattern pattern = Pattern.compile("([-+])\\s*(\\d+\\.?\\d*)\\s*([^\\d]+)(?:\\s*(\\d+))?");

        String[] lines = text.split("\\n");
        for (String line : lines) {
            line = line.trim(); // Удаляем лишние пробелы в начале и конце строки
            if (line.startsWith("Остаток")) {
                String[] parts = line.split("\\s+");
                balance = Integer.parseInt(parts[1]);
            } else {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    TypeCashFlow type = matcher.group(1).equals("+") ? TypeCashFlow.ARRIVAL : TypeCashFlow.EXPENDITURE;
                    double price = Double.parseDouble(matcher.group(2));
                    String description = matcher.group(3).trim(); // Удаляем лишние пробелы в начале и конце описания
                    int orderNumber = matcher.group(4) != null ? Integer.parseInt(matcher.group(4)) : 0;

                    CashFlow cashFlow = new CashFlow(type, price, description, orderNumber);
                    cashFlows.add(cashFlow);
                }
            }
        }

        System.out.println("Cash Flows:");
        for (CashFlow cashFlow : cashFlows) {
            System.out.println(cashFlow);
        }

        System.out.println("Balance: " + balance);
    }
}