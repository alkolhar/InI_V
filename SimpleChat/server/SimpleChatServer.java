package server;

import java.net.*;
import java.util.HashMap;
import java.util.Map;
import message.*;

public class SimpleChatServer {

    private static final int PORT = 5555;

    private  Message                         message;
    private  InetSocketAddress               socketAddress;
    private  Map<String, InetSocketAddress>  clients;

    private  Communication  communication;  // this object enables client/server communication

    private SimpleChatServer() throws Exception {

        // add shutdown hook
        Thread shutdown = new Thread() {
                public void run () {
                    System.out.println ("Closing communication ...");
                    if (communication != null) {
                        communication.close ();
                    }
                    System.out.println ("Communication closed.");
                }
            };            // shutdown thread
        Runtime.getRuntime().addShutdownHook(shutdown);

        System.out.println ("SimpleChatServer starting...");
       
        InetAddress localhost = InetAddress.getLocalHost ();
	System.out.println ("IP Address: " + localhost + ", Port: " + PORT);
        
        communication = new Communication ();  // this object enables client/server communication
        clients = new HashMap<String, InetSocketAddress> ();

        try {
            // open the communication channel
            communication.open(PORT);
            while (true) {

                // Wait to receive a datagram, blocking call
                System.out.println ("Waiting for message...");
                System.out.println ();
                communication.waitForMessage ();
                System.out.println ("Received message");
                // datagram was received, read message and socket address
                message = communication.getMessage ();
                socketAddress = (InetSocketAddress) communication.getSocketAddress ();

                // check type of message
                if (message instanceof PostingMessage) {

                    // send posting to all clients
                    PostingMessage p = (PostingMessage) message;
                    System.out.println ("Number of clients: " + clients.values().size ());
                    System.out.println("Message: " + p.getText());
                    for (InetSocketAddress address : clients.values()) {
                        communication.sendMessage  (address, p); 
                        System.out.println ("Posted message to " + address.getHostName() + "/" + address.getPort());
                    } 

                } else if (message instanceof RegisterMessage) {

                    // register client
                    clients.put (message.getUser(), socketAddress);
                    System.out.println ("Registered client: " + message.getUser());

                } else if (message instanceof UnregisterMessage) {

                    // unregister client
                    clients.remove (message.getUser());
                    System.out.println ("Unregistered client: " + message.getUser());

                } 
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 

    } 

    public static void main (String[] args) throws Exception {
        new SimpleChatServer ();
    } 

} 