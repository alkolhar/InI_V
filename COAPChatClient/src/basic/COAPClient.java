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

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

public class COAPClient extends JFrame implements ActionListener, WindowListener, KeyListener, CoapHandler {

	private static final String CRLF = "\r\n";

	private static final long serialVersionUID = -2937100503312197315L;

	private boolean firstRun;
	private CoapClient client;
	private CoapObserveRelation relation;
	private String COAPSERVER = "coap://146.136.51.159/chat";
	
	private JLabel nameLabel = new JLabel(" Name: ");
	private JButton sendButton = new JButton("Send");
	private JButton registerButton = new JButton("Register");
	private JButton unregisterButton = new JButton("Unregister");
	private JTextArea messagesText = new JTextArea();
	private JTextField nameField = new JTextField();
	private JTextField messageField = new JTextField();
	private JScrollPane scrollPane = new JScrollPane(messagesText);

	public COAPClient() {
		super("CoAP SimpleChatClient");

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

	private void quit() {
		System.out.println("Quit Chat Client");
		System.exit(0);
	} // quit

	private void send() {
		try {
			String user = nameField.getText();
			String text = messageField.getText();
			if (text.isEmpty()) {
				return;
			}
			String msg = user + ": " + text;
			client.post(msg, MediaTypeRegistry.TEXT_PLAIN);
			messageField.setText("");
			System.out.println("Message: '" + text + "' sent!");
		} catch (Exception e) {
			System.out.println("Unable to send message: " + e.toString());
		} // try
	} // send

	private void register() {
		try {
			firstRun = true;
			client = new CoapClient(COAPSERVER);
			client.useCONs();
			System.out.println("Client is registered");

			relation = client.observe(this);
			System.out.println("Observing CoAP resource");

		} catch (Exception e) {
			System.out.println("Unable to register: " + e.toString());
		} // try
	} // register

	private void unregister() {
		try {
			relation.proactiveCancel();
			client.shutdown();
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

	// ----- implementation of CoAP -----
	@Override
	public void onError() {
		System.out.println("CoAP Error");
	}

	@Override
	public void onLoad(CoapResponse arg0) {
		if (client != null) {
			if (firstRun) {
				firstRun = false;
			} else {
				String msg = arg0.getResponseText();
				System.out.println("Server: '" + msg + "'");
				if (msg != null && !msg.isEmpty()) {
					messagesText.insert(msg + CRLF, 0);
				}
			}
		}
	}

	public static void main(String argv[]) {
		new COAPClient();
	} // main
}