package messagingtest;

import static org.junit.Assert.*;
import messaging.ServerPublisher;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import consoleTest.SingleModule;

public class ServerPublisherTest {
	
	private ServerPublisher publisher;
	
	@Before
	public void initialize() {
		Injector i = Guice.createInjector(new SingleModule());
		publisher = i.getInstance(ServerPublisher.class);
	}
	
	@After
	public void tearDown() {
		if (publisher.brokerStarted()) {
			publisher.stopBroker();
		}
	}
	
	@Test
	public void testBrokerStart() {
		publisher.startBroker();
		assertTrue(publisher.brokerStarted());
	}
	
	@Test
	public void testBrokerStop() {
		publisher.startBroker();
		if (publisher.brokerStarted()) {
			publisher.stopBroker();
			assertTrue(!publisher.brokerStarted());
		}
	}
	
	@Test
	public void testPublishing() {
		publisher.startBroker();
		if (publisher.brokerStarted()) {
			TestMessagingListener listener = new TestMessagingListener();
			String testString = "test String";
			try {
				publisher.publish(testString, "TEST_TOPIC");
			} catch (Exception e) {
				e.printStackTrace();
			}
			assertEquals(testString, listener.getReceivedMsg());
		}
	}

}
