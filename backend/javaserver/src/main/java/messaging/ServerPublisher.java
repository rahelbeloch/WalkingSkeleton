package messaging;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

/** Class for message publishing on server-side.
 *  Uses ActiveMQ as embedded message broker.
 *  String messages can be published under specific topic names.
 */
public class ServerPublisher {
	
	private Connection connection;
	private Session session;
	private MessageProducer publisher;
	private BrokerService broker;
	
	//broker will run in the same VM (embeded)
	final private String BROKER_URL = "vm://localhost";
	final private String CONNECTOR_URL = "tcp://0.0.0.0:61616";
	
	public ServerPublisher() {
		
	}
	
	/**Publishes a String-content on a specified topic.
	 * @param content - content to publish
	 * @param topicName - topic where the content will be published
	 * @throws Exception
	 */
	public void publish(String content, String topicName) throws Exception {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
		connection = factory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//create a message-producer for the topic
		Topic topic = session.createTopic(topicName);
		publisher = session.createProducer(topic);
		publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		//define messaging-content (TextMessage or MapMessage)
		TextMessage msg = session.createTextMessage(content);
		
		//send and close
		System.out.println("INHALT: " + msg.getText());
		publisher.send(msg);
		session.close();
		connection.close();
	}
	
	/**
	 * Starts the embedded messaging broker
	 * Uses the CONNECTOR_URL specified by <class>ServerPublisher</class>
	 */
	public void startBroker() {
		broker = new BrokerService();
		if (!broker.isStarted()){
			try {
				broker.addConnector(CONNECTOR_URL);
				broker.start();
			} catch (Exception ex) {
				//Error: No Broker available
				ex.printStackTrace();
			}	
		}
		else {
			System.err.println("Broker allready running! (" + CONNECTOR_URL + ")");
		}
	}
	
	/**
	 * Stops the embedded messaging broker
	 */
	public void stopBroker(){
		try {
			broker.stop();
			System.out.println("Broker stopped...");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}