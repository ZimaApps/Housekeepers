package tz.co.nyotaapps.housekeeper.housekeeper;

public class Message {
    private String name;
    private String text;

    private Message() {}

    public Message(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getAuthor() {
        return name;
    }

    public String getText() {
        return text;
    }
}