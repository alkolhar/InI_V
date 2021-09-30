package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import com.sun.speech.freetts.*;

import message.*;

public class SimpleChatClient extends JFrame {

    private static final  String  CRLF        = "\r\n";
    private static final  int     SERVER_PORT = 5555;
    private static final  String  SERVER_HOST = "localhost";
    private static final  String  VOICE_NAME  = "kevin16";

    private static final long serialVersionUID = -2937100503312197315L;

    private  JLabel       nameLabel        = new JLabel (" Name: ");
    private  JLabel       serverLabel      = new JLabel (" Server: ");
    private  JButton      sendButton       = new JButton ("Send");
    private  JButton      registerButton   = new JButton ("Register");
    private  JButton      unregisterButton = new JButton ("Unregister");
    private  JTextArea    messagesText     = new JTextArea ();
    private  JTextField   nameField        = new JTextField ();
    private  JTextField   serverField      = new JTextField ();
    private  JTextField   messageField     = new JTextField ();
    private  JScrollPane  scrollPane       = new JScrollPane (messagesText);
    
    private Voice  voice;
    private Thread receiver;

    private  Communication  communication = new Communication ();    

    public SimpleChatClient () {
        super ("NTB SimpleChatClient");

        // implementation of WindowListener interface
        addWindowListener (new WindowAdapter() {
                public void windowClosing(WindowEvent w) {
                    quit();
                }
            });
        
        receiver = new Thread() {
            public void run () { 
                while (true) {
                    try {
                        // Wait to receive a datagram, blocking call
                        communication.waitForMessage ();
                        // datagram was received
                        Message message = communication.getMessage ();
                        // check message type
                        if (message instanceof PostingMessage) {
                            // it is a posting message
                            final PostingMessage p = (PostingMessage) message;
                            // add the message text to the messages textarea
                            Runnable appendText = new Runnable () {
                                    public void run () {
                                        String user = p.getUser ();
                                        String text = p.getText ();
                                        //messagesText.append (user + ": " + text);
                                        //messagesText.append (CRLF);
                                        messagesText.insert(user + ": " + text + CRLF, 0);
                                        messagesText.setCaretPosition(0);
                                        voice.speak (text);
                                    } 
                                };
                            SwingUtilities.invokeLater (appendText);
                        } 
                    } catch (Exception e) {
                        // swallow all exceptions
                    } 
                } 
            }   
        };
        
         // setup communication
        setup ();

        // setup ActionListeners for buttons
        sendButton.addActionListener(e -> send()); 
        registerButton.addActionListener(e -> register());
        unregisterButton.addActionListener(e -> unregister());

        // implementation of KeyListener interface 
        messageField.addKeyListener(new KeyAdapter() {
                public void keyPressed (KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        send ();
                    }
                }
            });

        // create server panel
        JPanel serverPanel = new JPanel (new BorderLayout ());
        serverLabel.setPreferredSize (new Dimension (50, 28));
        serverField.setPreferredSize (new Dimension (400, 28));
        serverField.setText (SERVER_HOST);
        serverPanel.add (serverLabel, BorderLayout.WEST);
        serverPanel.add (serverField, BorderLayout.CENTER);
        serverPanel.setBorder (new EmptyBorder (2, 2, 2, 2));
            
        // create button panel
        JPanel buttonPanel = new JPanel (new GridLayout (1, 2));
        buttonPanel.add (registerButton);
        buttonPanel.add (unregisterButton);

        // create name panel
        JPanel namePanel = new JPanel (new BorderLayout ());
        nameLabel.setPreferredSize (new Dimension (50, 28));
        nameField.setPreferredSize (new Dimension (400, 28));
        namePanel.add (nameLabel, BorderLayout.WEST);
        namePanel.add (nameField, BorderLayout.CENTER);
        namePanel.add (buttonPanel, BorderLayout.EAST);
        namePanel.setBorder (new EmptyBorder (2, 2, 2, 2));
        
        // create configuration panel
        JPanel configPanel = new JPanel (new GridLayout (2, 1));
        configPanel.add (serverPanel);
        configPanel.add (namePanel);
        configPanel.setBorder (new TitledBorder (" Configuration "));

        // create messages panel
        JPanel messagesPanel = new JPanel (new BorderLayout ());
        messagesPanel.add (scrollPane, BorderLayout.CENTER);
        messagesPanel.setBorder (new TitledBorder (" Received Messages "));

        // create message panel
        JPanel messagePanel = new JPanel (new BorderLayout ());
        messagePanel.add (messageField, BorderLayout.CENTER);
        messagePanel.add (sendButton, BorderLayout.EAST);
        messagePanel.setBorder (new TitledBorder (" Send Message "));

        // create panel with name, messages and message panel
        JPanel panel1 = new JPanel (new BorderLayout ());
        panel1.add (configPanel, BorderLayout.NORTH);
        panel1.add (messagesPanel, BorderLayout.CENTER);
        panel1.add (messagePanel, BorderLayout.SOUTH);

        // create panel to hold all over panels
        JPanel contentPanel = new JPanel (new BorderLayout ());
        contentPanel.setBorder (new EmptyBorder (10, 10, 10, 10));
        contentPanel.add (panel1, BorderLayout.CENTER);

        // add content panel to the window
        setContentPane (contentPanel);
        setMinimumSize (new Dimension (400, 300));
        setPreferredSize (new Dimension (600, 400));

        pack ();
        setVisible (true);
    } 

    private void setup () {
        System.out.println ("Setup Chat Client");
        // open the communication channel
        communication.open();
        // start a background thread to receive messages from the server
        receiver.start ();
        // create voice
        System.setProperty("mbrola.base", "C:\\libs\\libs\\freetts-1.2\\mbrola");
        // voice = VoiceManager.getInstance().getVoice(VOICE_NAME);
        // voice.speak ("Hello");
    } 

    private void quit () {
        // quit the application
        System.out.println ("Quit Chat Client");
        // communication.close ();
        System.out.println ("Done.");
        System.exit (0);
    } 

    private void send () {
        // send the user message
        String user = nameField.getText ();
        String text = messageField.getText ();
        String serverHost = serverField.getText ();
        System.out.println (user + ": " + text);
        communication.sendMessage (serverHost, SERVER_PORT, new PostingMessage (user, text));
        messageField.setText ("");
    } 

    private void register () {
        // register the user
        String user = nameField.getText ();
        String serverHost = serverField.getText ();
        System.out.println ("Register " + user);
        communication.sendMessage (serverHost, SERVER_PORT, new RegisterMessage (user));
    } 

    private void unregister () {
        // unregister the user
        String user = nameField.getText ();
        String serverHost = serverField.getText ();
        System.out.println ("Unregister " + user);
        communication.sendMessage (serverHost, SERVER_PORT, new UnregisterMessage (user));
    } 

    public static void main (String argv[]) {
        new SimpleChatClient ();
    } 

} 