package tests;

import message.*;

public class MessageTest
{
    private Communication communication;

    public MessageTest(int port) {
        communication = new Communication();
        communication.open(port);
    }

    public void sendeMessage(String host, int port){
        Message message = new PostingMessage("Fritz","Hoi du!");
        communication.sendMessage(host, port, message);
    }

    public void empfangeMessage() {
        while (true) {
            communication.waitForMessage();
            Message message = communication.getMessage();
            if (message instanceof PostingMessage) {
                PostingMessage posting = (PostingMessage) message;
                System.out.println("Posting message von " + posting.getUser() + " mit Inhalt " + posting.getText() + " empfangen.");
            } else if (message instanceof RegisterMessage) {
                System.out.println("Register message von " + message.getUser() + " empfangen.");
            } else if (message instanceof UnregisterMessage) {
                System.out.println("Unregister message von " + message.getUser() + " empfangen.");
            } 
        } // while 
    }

    public void beenden() {
        communication.close();
    }
}
