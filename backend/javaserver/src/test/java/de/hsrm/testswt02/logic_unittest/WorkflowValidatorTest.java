package de.hsrm.testswt02.logic_unittest;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

import de.hsrm.swt02.businesslogic.WorkflowValidator;
import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Form;
import de.hsrm.swt02.model.FormEntry;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.PersistenceImp;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;


/**
 * This Testclass tests the logic interface.
 *
 */
public class WorkflowValidatorTest {

    private WorkflowValidator validator;
    private PersistenceImp db;
    
    @BeforeClass
    public void setupTestData() throws PersistenceException {
        final double standardDistance = 100;
        int elementCounter = 1;
        
        Workflow workflow1;
        User user1, user2, user3, user4;
        StartStep startStep1;
        Action action1, action2;
        FinalStep finalStep;
        Role role1, role2, role3;
        Form form1, form2;

        user1 = new User();
        user1.setUsername("Alex");
        user2 = new User();
        user2.setUsername("Dominik");
        user3 = new User();
        user3.setUsername("Tilman");
        user4 = new User();
        user4.setUsername("TestAdmin");
        user4.setPassword("");

        role1 = new Role();
        // role1.setId("1");
        role1.setRolename("Manager");
        db.storeRole(role1);
        role2 = new Role();
        role2.setRolename("Sachbearbeiter");
        db.storeRole(role2);
        role3 = new Role();
        role3.setRolename("admin");
        db.storeRole(role3);

        user1.addRole(role1);
        user2.addRole(role2);
        user4.addRole(role3);

        db.storeUser(user1);
        db.storeUser(user2);
        db.storeUser(user3);
        db.storeUser(user4);

//        final Set<Role> user1Roles;        
//        user1Roles = user1.getRoles();
//        
//        startStep1 = new StartStep();
//        for (Role r: user1Roles) {
//            startStep1.addRole(r.getRolename());           
//        }
        
        startStep1 = new StartStep();
        
        // display formatting
        startStep1.setLeft(elementCounter * standardDistance);
        startStep1.setTop(elementCounter * standardDistance);
        elementCounter++;
        
        startStep1.getRoleIds().add(role1.getRolename());

        action1 = new Action(new ArrayList<String>(), "Action von "
                + user1.getUsername());
        
        // display formatting
        action1.setLeft(elementCounter * standardDistance);
        action1.setTop(elementCounter * standardDistance);
        elementCounter++;
        
        action1.addRole(role2.getRolename());
        action2 = new Action(new ArrayList<String>(), "Action von "
                + user2.getUsername());

        // display formatting
        action2.setLeft(elementCounter * standardDistance);
        action2.setTop(elementCounter * standardDistance);
        elementCounter++;
        
        action2.addRole(role1.getRolename());

        finalStep = new FinalStep();
        
        // display formatting
        finalStep.setLeft(elementCounter * standardDistance);
        finalStep.setTop(elementCounter * standardDistance);
        elementCounter++;
        
        finalStep.getRoleIds().add(role2.getRolename());

        workflow1 = new Workflow();
        workflow1.addStep(startStep1);
        workflow1.addStep(action1);
        workflow1.addStep(action2);
        workflow1.addStep(finalStep);

        db.storeWorkflow(workflow1);
        
        form1 = new Form("das ist ein Formular");
        form2 = new Form("FORM");
        
        final FormEntry fe1 = new FormEntry();
        fe1.setId("nameEntry");
        fe1.setKey("name");
        fe1.setValue("String");
        final FormEntry fe2 = new FormEntry();
        fe2.setId("ageEntry");
        fe2.setKey("age");
        fe2.setValue("int");
        final FormEntry fe3 = new FormEntry();
        fe3.setId("deliveredBookCount");
        fe3.setKey("bookCount");
        fe3.setValue("int");
        final FormEntry fe4 = new FormEntry();
        fe4.setId("deliveryOnTime");
        fe4.setKey("onTime");
        fe4.setValue("boolean");
        final FormEntry fe5 = new FormEntry();
        fe5.setId("chargedCosts");
        fe5.setKey("cost");
        fe5.setValue("int");
        
        final List<FormEntry> fd1 = form1.getFormDef();
        fd1.add(fe1);
        fd1.add(fe2);
        form1.setId("form1");
        
        final List<FormEntry> fd2 = form2.getFormDef();
        fd2.add(fe3);
        fd2.add(fe4);
        fd2.add(fe5);
        form2.setId("form2");
        
        db.storeForm(form1);
        db.storeForm(form2);
    }
    
    public void testWorkflowValidation() {        
    }
}
