package com.thecat.demos.transactionservice.entities;

public class TransactionDTO {

    private String clientId;
    private String type;
    private String location;
    private double amount;

    public TransactionDTO() {
    }

    public TransactionDTO(String clientId, String type, String location, double amount) {
        this.clientId = clientId;
        this.type = type;
        this.location = location;
        this.amount = amount;
    }
    
    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer( "Transaction: \n" );
        
        sb.append("clientId [ " + this.clientId + "]\n");
        sb.append("type [ " + this.type + "]\n");
        sb.append("location [ " + this.location + "]\n");
        sb.append("amount [ " + this.amount + "]\n");

        return sb.toString();
    }
}