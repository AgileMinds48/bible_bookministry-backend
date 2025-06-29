package com.evbooksministry.bibleandbookministry.dtos;

public class EmailRequest {
    private String recipient;
    private String subject;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
