package com.pesem.com.pesaem.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

public class Transaction implements Serializable {

    String employeeName;

    String employeePhone;

    BigInteger amount;

    LocalDateTime transactionTime;

    String responseCode;

    String conversationId;

    String responseDescription;

    public Transaction() {
    }

    public Transaction(String employeeName, String employeePhone, BigInteger amount, LocalDateTime transactionTime, String responseCode, String conversationId, String responseDescription) {
        this.employeeName = employeeName;
        this.employeePhone = employeePhone;
        this.amount = amount;
        this.transactionTime = transactionTime;
        this.responseCode = responseCode;
        this.conversationId = conversationId;
        this.responseDescription = responseDescription;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "employeeName='" + employeeName + '\'' +
                ", responseCode=" + responseCode +
                ", responseDescription=" + responseDescription +
                '}';
    }
}
