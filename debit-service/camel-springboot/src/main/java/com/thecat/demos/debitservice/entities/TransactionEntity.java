package com.thecat.demos.debitservice.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;



@Entity
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq" )
    @SequenceGenerator(  name = "transaction_seq", allocationSize = 1)
    private int id;

    @Column (name="client_id")
    private String clientId;
    
    private String type;
    private String location;
    private double amount;

    @CreationTimestamp
    @Column( name="create_date" )
    public Date createDate;

    public TransactionEntity() {
    }

    public TransactionEntity(String clientId, String type, String location, double amount) {
        this.clientId = clientId;
        this.type = type;
        this.location = location;
        this.amount = amount;
    }

    public int getId() {
        return this.id;
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

    public Date getCreateDate() {
        return this.createDate;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer( "Transaction: \n" );
            
        sb.append("id [ " + this.id + "]\n");
        sb.append("clientId [ " + this.clientId + "]\n");
        sb.append("type [ " + this.type + "]\n");
        sb.append("location [ " + this.location + "]\n");
        sb.append("amount [ " + this.amount + "]\n");
        sb.append("createDate [ " + this.createDate + "]\n");

        return sb.toString();
    }
}