package com.nn.chatbot.model;

import lombok.Data;

@Data
public class CashFlow {
    private TypeCashFlow typeCashFlow;
    private Double price;
    private String description;
    private int order_number;
}
