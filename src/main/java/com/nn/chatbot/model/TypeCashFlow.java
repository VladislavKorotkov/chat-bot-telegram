package com.nn.chatbot.model;

public enum TypeCashFlow {
    ARRIVAL("Приход"),
    EXPENDITURE("Расход");

    private final String type_name;

    TypeCashFlow(String type_name){
        this.type_name = type_name;
    }

    public String getTypeName(){
        return type_name;
    }
}
