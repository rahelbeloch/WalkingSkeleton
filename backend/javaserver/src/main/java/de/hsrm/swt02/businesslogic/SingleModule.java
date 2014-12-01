package de.hsrm.swt02.businesslogic;

import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.PersistenceImp;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.messaging.ServerPublisherImp;

import com.google.inject.AbstractModule;

public class SingleModule extends AbstractModule {

    protected void configure() {
        bind(ServerPublisher.class).to(ServerPublisherImp.class);
        bind(ProcessManager.class).to(ProcessManagerImp.class);
        bind(Persistence.class).to(PersistenceImp.class);
    }
}
