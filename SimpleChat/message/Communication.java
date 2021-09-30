package message;

import java.io.*;
import java.net.*;

public class Communication {

    private  byte[]          receiveBuffer;
    private  DatagramSocket  socket;
    private  DatagramPacket  receivePacket;

    public Communication() {
        receiveBuffer = new byte[2048];
        receivePacket = new DatagramPacket (receiveBuffer, receiveBuffer.length);	
    } 

    public void open() {  // fuer Client
        try {
            socket = new DatagramSocket ();
        } catch (Exception e) {
            e.printStackTrace ();
        } 
    } 

    public void open(int port) {  // fuer Server
        try {
            socket = new DatagramSocket (port);
        } catch (Exception e) {
            e.printStackTrace ();
        } 
    } 

    public void waitForMessage () {
        try {
            // wait for message, blocking call
            receivePacket.setLength (receiveBuffer.length);
            socket.receive (receivePacket);
        } catch (Exception e) {
            e.printStackTrace ();
        } 
    } 

    public Message getMessage () {
        try {
            // read object from buffer
            ObjectInputStream objIn = new ObjectInputStream (
                    new ByteArrayInputStream (receivePacket.getData ()));
            Message message = (Message) objIn.readObject ();
            return message;
        } catch (NoClassDefFoundError e) {
            System.out.println ("Incompatible class received");
            return null;
        } catch (Exception e) {
            // e.printStackTrace ();
            return null;
        } 
    } 

    public SocketAddress getSocketAddress () {
        return receivePacket.getSocketAddress (); 
    } 

    public void sendMessage (String host, int port, Message message) {
        try {
            // Note: SocketAddress is a combination of address and port
            InetSocketAddress address = new InetSocketAddress(InetAddress.getByName (host), port);
            sendMessage (address, message);
        } catch (Exception e) {
            e.printStackTrace ();
        } 
    } 

    public void sendMessage (SocketAddress address, Message message) {
        try {
            // serialize message
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream ();
            ObjectOutputStream objOut = new ObjectOutputStream (byteStream);
            objOut.writeObject (message);

            // create datagram packet
            DatagramPacket packet = new DatagramPacket (byteStream.toByteArray(),
                    byteStream.toByteArray().length, address);

            // send packet
            socket.send (packet);

        } catch (Exception e) {
            e.printStackTrace ();
        } 
    } 

    public void close () {
        if (socket != null)
            socket.close ();
    } 

} 
