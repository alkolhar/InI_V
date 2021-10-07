/* -------------------------
     SimpleChatClient.java
   ------------------------- */

/*  Simple chat client with communication based on WebSockets (tyrus)  */

/*  Computerkommunikation & Verteilte Systeme 2016, Rene Pawlitzek, NTB  */
package basic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Client extends JFrame implements ActionListener, WindowListener, KeyListener {
	private static final long serialVersionUID = -909861873808064255L;
	private static String broker = "tcp://146.136.36.40:1883";
	private static String clientID = "OST-Chatter";
	private static String topic = "ost/SimpleChat/MQTT";
	private static int QoS = 2;
	
	private JLabel nameLabel = new JLabel(" Name: ");
	private JButton sendButton = new JButton("Send");
	private JButton registerButton = new JButton("Register");
	private JButton unregisterButton = new JButton("Unregister");
	private JTextArea messagesText = new JTextArea();
	private JTextField nameField = new JTextField();
	private JTextField messageField = new JTextField();
	private JScrollPane scrollPane = new JScrollPane(messagesText);

	private MqttClient client;

	public Client() {
		super("MQTT SimpleChatClient");

		// setup communication
		setup();

		// setup buttons
		registerButton.setActionCommand("Register");
		registerButton.addActionListener(this);
		unregisterButton.setActionCommand("Unregister");
		unregisterButton.addActionListener(this);
		sendButton.setActionCommand("Send");
		sendButton.addActionListener(this);

		// setup message field
		messageField.addKeyListener(this);

		// create button panel
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(registerButton, BorderLayout.WEST);
		buttonPanel.add(unregisterButton, BorderLayout.EAST);

		// create name panel
		JPanel namePanel = new JPanel(new BorderLayout());
		namePanel.add(nameLabel, BorderLayout.WEST);
		namePanel.add(nameField, BorderLayout.CENTER);
		namePanel.add(buttonPanel, BorderLayout.EAST);
		namePanel.setBorder(new TitledBorder(" Register/Unregister "));

		// create messages panel
		JPanel messagesPanel = new JPanel(new BorderLayout());
		messagesPanel.add(scrollPane, BorderLayout.CENTER);
		messagesPanel.setBorder(new TitledBorder(" Received Messages "));

		// create message panel
		JPanel messagePanel = new JPanel(new BorderLayout());
		messagePanel.add(messageField, BorderLayout.CENTER);
		messagePanel.add(sendButton, BorderLayout.EAST);
		messagePanel.setBorder(new TitledBorder(" Send Message "));

		// create panel with name, messages and message panel
		JPanel panel1 = new JPanel(new BorderLayout());
		panel1.add(namePanel, BorderLayout.NORTH);
		panel1.add(messagesPanel, BorderLayout.CENTER);
		panel1.add(messagePanel, BorderLayout.SOUTH);

		// create panel to hold all over panels
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPanel.add(panel1, BorderLayout.CENTER);

		// add content panel to the window
		setContentPane(contentPanel);
		setPreferredSize(new Dimension(600, 400));
		addWindowListener(this);
		pack();

		// show window
		setVisible(true);

	} // SimpleChatClient

	private void setup() {
		// setup the chat client
		try {
			System.out.println("Setup Chat Client");
		} catch (Exception e) {
			System.out.println("Unable to setup chat client: " + e.toString());
		} // try
	} // setup

	private void quit() {
		// quit the application
		System.out.println("Quit Chat Client");
		System.exit(0);
	} // quit

	private void send() {
		// send the user message
		try {
			String user = nameField.getText();
			String text = messageField.getText();
			String msg = user + ": " + text;
			sendMessage(msg);
			messageField.setText("");
		} catch (Exception e) {
			System.out.println("Unable to send message: " + e.toString());
		} // try
	} // send

	private void register() {
		// register
		try {
			client = new MqttClient(broker, clientID, new MemoryPersistence());
			client.connect();

			System.out.println("Client is registered");
		} catch (Exception e) {
			System.out.println("Unable to register: " + e.toString());
		} // try
	} // register

	private void unregister() {
		// unregister
		try {
			System.out.println("Client is unregistered");
		} catch (Exception e) {
			System.out.println("Unable to unregister: " + e.toString());
		} // try
	} // unregister

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("Send".equals(cmd)) {
			send();
		} else if ("Register".equals(cmd)) {
			register();
		} else if ("Unregister".equals(cmd)) {
			unregister();
		} // if
	} // actionPerformed

	// ----- implementation of WindowListener interface -----

	public void windowActivated(WindowEvent arg0) {
	} // windowActivated

	public void windowClosed(WindowEvent arg0) {
	} // windowClosed

	public void windowClosing(WindowEvent arg0) {
		quit();
	} // windowClosing

	public void windowDeactivated(WindowEvent arg0) {
	} // windowDeactived

	public void windowDeiconified(WindowEvent arg0) {
	} // windowDeiconified

	public void windowIconified(WindowEvent arg0) {
	} // windowIconified

	public void windowOpened(WindowEvent arg0) {
	} // windowOpened

	// ----- implementation of KeyListener interface -----

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			send();
	} // keyPressed

	public void keyReleased(KeyEvent e) {
	} // keyReleased

	public void keyTyped(KeyEvent e) {
	} // keyTyped


	private void sendMessage(String message) throws Exception {
		client.publish(topic, // topic
				message.getBytes("UTF-8"), // message
				QoS, // QoS level
				true); // retained message
		System.out.println("Nachricht gesendet: " + message);
	} // sendMessage

	public static void main(String args[]) {
		new Client();
	} // main

}
