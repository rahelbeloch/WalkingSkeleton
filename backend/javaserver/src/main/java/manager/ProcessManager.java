package manager;

import backingbeans.Item;
import backingbeans.User;
import abstractbeans.AbstractStep;
import abstractbeans.AbstractUser;

/**
 * Interface for ProcessManager. (Due to Dependency Injection)
 *
 */
public interface ProcessManager {
	
	public boolean checkUser(AbstractUser user, AbstractStep step);
	public void selectProcessor(AbstractStep step, Item item, User user);
	public void stopBroker();

}
