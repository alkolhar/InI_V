/* -----------------
     Producer.java
   ----------------- */

/*  Computerkommunikation & Verteilte Systeme 2015, Rene Pawlitzek, NTB  */

/*  Note: This example requires the mosquitto MQTT broker  */

package ch.ntb.inf.coko.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Producer {

	// private static String broker = "tcp://broker.mqttdashboard.com:1883";
	// private static String broker = "tcp://test.mosquitto.org:1883";
	//private static String broker = "tcp://localhost:1883";
	private static String broker = "tcp://146.136.36.40:1883";
	private static String clientID = "NTB-Producer";
	private static String topic = "home/livingroom/temp";
	private static int QoS = 2;

	private MqttClient client;

	public Producer() throws Exception {
		client = new MqttClient(broker, clientID, new MemoryPersistence());
		client.connect();
		System.out.println("Client is connected");
	} // Producer

	public void run() throws Exception {
		int i = 0;
		while (true) {
			int temp = 20 + i;
			i = (i + 1) % 10;
			String data = "Temperature: " + temp + " [C]";
			client.publish(topic, // topic
					data.getBytes("UTF-8"), // message
					QoS, // QoS level
					true); // retained message
			System.out.println(data);
			Thread.sleep(5000);
		} // while
	} // run

	public static void main(String[] args) {
		try {
			Producer producer = new Producer();
			producer.run();
		} catch (Exception e) {
			e.printStackTrace();
		} // try
	} // main

} // Producer

/* ----- End of File ------ */
