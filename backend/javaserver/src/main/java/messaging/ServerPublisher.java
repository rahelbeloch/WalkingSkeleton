package messaging;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

/** //TODO good Class description
 *  Class for Messaging on server-side.
 *  Uses an embeded message broker.
 * 
 * @author sehre001
 */
public class ServerPublisher {
	
	private Connection connection;
	private Session session;
	private MessageProducer publisher;
	private BrokerService broker;
	
	//broker will run in the same VM (embeded)
	final private String BROKER_URL = "vm://localhost";
	
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
		
		//define messaging-content (TextMessage/MapMessage)
		TextMessage msg = session.createTextMessage(content);
		
		//send and close
		System.out.println("INHALT: " + msg.getText());
		publisher.send(msg);
		session.close();
		connection.close();
	}
	
	/**
	 * Starts the embeded messaging broker
	 */
	public void startBroker() {
		broker = new BrokerService();
		try {
			broker.addConnector("tcp://0.0.0.0:61616");
			broker.start();
			System.out.println("Broker started...");
		} catch (Exception ex) {
			ex.printStackTrace();
			//Error: No Broker available
		}	
	}
	
	/**
	 * Stops the embeded messaging broker
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