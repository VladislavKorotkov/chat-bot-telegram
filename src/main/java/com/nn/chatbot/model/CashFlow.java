package com.nn.chatbot.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CashFlow {
    private TypeCashFlow typeCashFlow;
    private Double price;
    private String description;
    private int order_number;

}
