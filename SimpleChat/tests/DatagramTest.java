package tests;

import java.io.*;
import java.net.*;

public class DatagramTest
{
    DatagramSocket socket;

    public DatagramTest(int meinePortNummer) {
        try {
            socket = new DatagramSocket(meinePortNummer);
        } catch (SocketException e) {
            System.out.println("Problem beim Erzeugen des Sockets");
        }
    }

    public void sendeDatagramm (String destinationsHostName, int destinationsPortNummer){
        String text = "Hoi Du.";
        byte[] inhalt = text.getBytes();
        try {
            InetAddress ipAdresse = InetAddress.getByName(destinationsHostName);
            InetSocketAddress destinationsAdresse = new InetSocketAddress(ipAdresse, destinationsPortNummer);
            DatagramPacket packet = new DatagramPacket(inhalt, inhalt.length, destinationsAdresse);
            socket.send(packet);
        } catch (UnknownHostException e) {
            // thrown by InetAddress.getByName()
            System.out.println("Destinations host kann nicht gefunden werden.");
        } catch (IOException e) {
            // thrown by DatagrammSocket.send()
            System.out.println("Datagramm kann nicht gesendet werden.");
        }
    }

    public void empfangeDatagramm (){
        byte[] empfangsPuffer = new byte[2048];
        DatagramPacket receivePacket = null;
        try {
            receivePacket = new DatagramPacket(empfangsPuffer, empfangsPuffer.length);
            socket.receive(receivePacket);  // blocking call
        } catch (IOException e) {
            System.out.println("Problem beim Senden der Message.");
        }
        String text = new String(empfangsPuffer, 0, receivePacket.getLength());
        System.out.println("Datagram von " + 
            receivePacket.getAddress().getHostName() + "/" +
            receivePacket.getPort() + " mit Inhalt " + text + " erhalten");
    }

    public void beenden() {
        if (socket != null){
            socket.close();
        }
    }

}
