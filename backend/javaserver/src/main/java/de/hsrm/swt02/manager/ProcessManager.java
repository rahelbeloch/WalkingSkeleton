package de.hsrm.swt02.manager;

import java.util.Observer;

import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;

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
