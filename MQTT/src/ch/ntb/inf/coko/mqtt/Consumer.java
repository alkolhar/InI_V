/* -----------------
     Consumer.java
   ----------------- */

/*  Computerkommunikation & Verteilte Systeme 2015, Rene Pawlitzek, NTB  */

/*  Note: This example requires the mosquitto MQTT broker  */

package ch.ntb.inf.coko.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Consumer implements MqttCallback {

	// private static String broker = "tcp://broker.mqttdashboard.com:1883";
	// private static String broker = "tcp://test.mosquitto.org:1883";
	private static String broker = "tcp://localhost:1883";
	private static String clientID = "NTB-Consumer";
	private static String topic = "home/livingroom/temp";
	private static int QoS = 2;

	private MqttClient client;

	public Consumer() throws Exception {
		client = new MqttClient(broker, clientID, new MemoryPersistence());
		client.connect();
	} // Consumer

	public void run() throws Exception {
		client.setCallback(this);
		client.subscribe(topic, QoS);
	} // run

	/* ----- Implementation of MqttCallback ----- */

	@Override
	public void connectionLost(Throwable arg0) {
		System.out.println("Connection lost");
	} // connectionLost

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		System.out.println("Delivery complete");
	} // deliveryComplete

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		String msg = new String(arg1.getPayload());
		System.out.println(msg);
	} // messageArrived

	public static void main(String[] args) {
		try {
			Consumer consumer = new Consumer();
			consumer.run();
		} catch (Exception e) {
			e.printStackTrace();
		} // try
	} // main

} // Consumer

/* ----- End of File ----- */
