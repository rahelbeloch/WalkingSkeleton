package manager;

import java.util.Observer;

import backingbeans.Item;
import backingbeans.User;
import abstractbeans.AbstractStep;
import abstractbeans.AbstractUser;

/**
 * Interface for ProcessManager. (Due to Dependency Injection)
 *
 */
public interface ProcessManager extends Observer {
	
	public boolean checkUser(AbstractUser user, AbstractStep step);
	
	public void selectProcessor(AbstractStep step, Item item, User user);
	
	public void startBroker();
	
	public void stopBroker();
}
