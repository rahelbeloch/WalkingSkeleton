package messagingtest;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TestMessagingListener implements MessageListener{
	private Connection connection;
	private Session session;
	private MessageConsumer consumer;
	private String receivedMsg = "fail";
	
	private String BROKERURL = "tcp://localhost:61616";
	final private String TOPIC_NAME = "TEST_TOPIC";
	
	public TestMessagingListener() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKERURL);	
		try {
			connection = factory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(TOPIC_NAME);
			consumer = session.createConsumer(topic);
			consumer.setMessageListener(this);
			connection.start();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public void onMessage(Message message) {
		TextMessage tm = (TextMessage) message;
		try {
			receivedMsg = tm.getText();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public String getReceivedMsg() {
		return receivedMsg;
	}
}
