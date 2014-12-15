package de.hsrm.swt02.constructionfactory;

import com.google.inject.AbstractModule;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.businesslogic.LogicImp;
import de.hsrm.swt02.businesslogic.ProcessManager;
import de.hsrm.swt02.businesslogic.ProcessManagerImp;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.messaging.ServerPublisherImp;
import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.PersistenceImp;

/**
 * This class binds interfaces to default implementations. 
 *
 */
public class SingleModule extends AbstractModule {

    /**
     * This methods binds interfaces to default implementations.
     */
    protected void configure() {
        bind(ServerPublisher.class).to(ServerPublisherImp.class);
        bind(ProcessManager.class).to(ProcessManagerImp.class);
        bind(Persistence.class).to(PersistenceImp.class);
        bind(Logic.class).to(LogicImp.class);
    }
}
