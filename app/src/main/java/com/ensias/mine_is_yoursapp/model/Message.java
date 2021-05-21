package com.ensias.mine_is_yoursapp.model;

public class Message {

    private String idFrom;
    private String idTo;
    private String text;

    public Message(String idFrom, String idTo, String text) {
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.text = text;
    }
    public Message(){

    }
    public String getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(String idFrom) {
        this.idFrom = idFrom;
    }

    public String getIdTo() {
        return idTo;
    }

    public void setIdTo(String idTo) {
        this.idTo = idTo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
