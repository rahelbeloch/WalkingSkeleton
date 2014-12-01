package de.hsrm.swt02;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import de.hsrm.swt02.persistence.Persistence;
import de.hsrm.swt02.persistence.PersistenceImp;
import de.hsrm.swt02.restserver.resource.UserResource;

public class JerseyDIBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(UserResource.class);
        bind(PersistenceImp.class).to(Persistence.class);
    }

}