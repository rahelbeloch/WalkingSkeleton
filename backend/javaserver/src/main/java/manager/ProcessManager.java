package manager;

import java.util.Observer;

import model.Item;
import model.Step;
import model.User;

/**
 * Interface for ProcessManager. (Due to Dependency Injection)
 *
 */
public interface ProcessManager extends Observer {
	
	public boolean checkUser(User user, Step step);
	
	public void selectProcessor(Step step, Item item, User user);
	
	public void startBroker();
	
	public void stopBroker();
}
