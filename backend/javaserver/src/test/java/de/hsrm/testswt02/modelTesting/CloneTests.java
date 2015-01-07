package de.hsrm.testswt02.modelTesting;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.hsrm.swt02.model.Action;
import de.hsrm.swt02.model.FinalStep;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.MetaEntry;
import de.hsrm.swt02.model.Role;
import de.hsrm.swt02.model.RootElement;
import de.hsrm.swt02.model.StartStep;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.User;
import de.hsrm.swt02.model.Workflow;

/**
 * Unittest class for testing the cloning process of all the datamodels.
 */
public class CloneTests {

    /**
     * method for testing the cloning process of Action. There is no need to
     * check if after the cloning process the nextSteps list is identical to the
     * new nextSteps list because the conversion of references to ids and
     * vice-versa is only important if you clone workflows
     * 
     * @exception CloneNotSupportedException
     *                convention
     * @throws CloneNotSupportedException
     */
    @Test
    public void actionCloneTest() throws CloneNotSupportedException {
        final Action a = new Action();
        Action clone = new Action();
        Role role = new Role();
        role.setRolename("testrolle");

        a.setId("1");
        a.setDescription("desc");
        a.getRoles().add(role.getRolename());
        a.getNextStepIds().add("2");
        a.getNextStepIds().add("3");
        a.setOpener("user4711");

        clone = (Action) a.clone();

        assertTrue(clone.getId().equals(a.getId()));
        assertTrue(clone.getDescription().equals(a.getDescription()));
        assertTrue(clone.getRoles().equals(a.getRoles()));
        assertTrue(clone.getOpener().equals(a.getOpener()));
        assertTrue(clone.getNextStepIds().get(0) == a.getNextStepIds().get(0));
        assertTrue(clone.getNextStepIds().get(1) == a.getNextStepIds().get(1));

    }

    /**
     * method for testing the cloning process of StartStep. There is no need to
     * check if after the cloning process the nextSteps list is identical to the
     * new nextSteps list because the conversion of references to ids and
     * vice-versa is only important if you clone workflows
     * 
     * @exception CloneNotSupportedException
     *                convention
     * @throws CloneNotSupportedException
     */
    @Test
    public void startStepCloneTest() throws CloneNotSupportedException {
        final StartStep ss = new StartStep();
        StartStep clone = new StartStep();
        Role role = new Role();
        role.setRolename("testrolle");

        ss.setId("1");
        ss.getRoles().add(role.getRolename());
        ss.getNextStepIds().add("2");
        ss.getNextStepIds().add("3");
        ss.setOpener("user4711");
        clone = (StartStep) ss.clone();

        assertTrue(clone.getId().equals(ss.getId()));
        assertTrue(clone.getRoles().equals(ss.getRoles()));
        assertTrue(clone.getOpener().equals(ss.getOpener()));
        assertTrue(clone.getNextStepIds().get(0) == ss.getNextStepIds().get(0));
        assertTrue(clone.getNextStepIds().get(1) == ss.getNextStepIds().get(1));

    }

    /**
     * method for testing the cloning process of FinalStep. There is no need to
     * check if after the cloning process the nextSteps list is identical to the
     * new nextSteps list because the conversion of references to ids and
     * vice-versa is only important if you clone workflows
     * 
     * @exception CloneNotSupportedException
     *                convention
     * @throws CloneNotSupportedException
     */
    @Test
    public void finalStepCloneTest() throws CloneNotSupportedException {
        final FinalStep fs = new FinalStep();
        FinalStep clone = new FinalStep();
        Role role = new Role();
        role.setRolename("testrolle");

        fs.setId("1");
        fs.getRoles().add(role.getRolename());
        fs.setOpener("user4711");
        fs.getNextStepIds().add("2");
        fs.getNextStepIds().add("3");

        clone = (FinalStep) fs.clone();

        assertTrue(clone.getId().equals(fs.getId()));
        assertTrue(clone.getOpener().equals("user4711"));
        assertTrue(clone.getRoles().equals(fs.getRoles()));
        assertTrue(clone.getNextStepIds().get(0).equals("2"));
        assertTrue(clone.getNextStepIds().get(1).equals("3"));

    }

    /**
     * method for testing the cloning process of Steps. There is no need to
     * check if after the cloning process the nextSteps list is identical to the
     * new nextSteps list because the conversion of references to ids and
     * vice-versa is only important if you clone workflows
     * 
     * @exception CloneNotSupportedException
     *                convention
     * @throws CloneNotSupportedException
     */
    @Test
    public void stepCloneTest() throws CloneNotSupportedException {
        final Step s = new Step();
        Step clone = new Step();
        Role role = new Role();
        role.setRolename("testrolle");

        s.setId("1");
        s.getRoles().add(role.getRolename());
        s.setOpener("user4711");
        s.getNextStepIds().add("2");
        s.getNextStepIds().add("3");

        clone = (Step) s.clone();

        assertTrue(clone.getId().equals(s.getId()));
        assertTrue(clone.getOpener().equals("user4711"));
        assertTrue(clone.getRoles().equals(s.getRoles()));
        assertTrue(clone.getNextStepIds().get(0).equals("2"));
        assertTrue(clone.getNextStepIds().get(1).equals("3"));

    }

    /**
     * method for testing the cloning process of an Item.
     * 
     * @exception CloneNotSupportedException
     *                convention
     * @throws CloneNotSupportedException
     */
    @Test
    public void itemCloneTest() throws CloneNotSupportedException {
        final Item i = new Item();
        Item clone = new Item();
        final MetaEntry m1 = new MetaEntry();
        final MetaEntry m2 = new MetaEntry();

        i.setId("1");
        i.setWorkflowId("1");
        i.setFinished(false);

        m1.setGroup("group1");
        m1.setKey("key1");
        m1.setValue("value1");
        m2.setGroup("group1");
        m2.setKey("key2");
        m2.setValue("value2");
        i.getMetadata().add(m1);
        i.getMetadata().add(m2);

        clone = (Item) i.clone();

        assertTrue(clone.getId() == i.getId());
        assertTrue(clone.getWorkflowId().equals(i.getWorkflowId()));
        assertTrue(clone.isFinished() == i.isFinished());
        assertTrue(clone.getMetadata().get(0).getGroup()
                .equals(i.getMetadata().get(0).getGroup()));
        assertTrue(clone.getMetadata().get(0).getKey()
                .equals(i.getMetadata().get(0).getKey()));
        assertTrue(clone.getMetadata().get(0).getValue()
                .equals(i.getMetadata().get(0).getValue()));
        assertTrue(clone.getMetadata().get(1).getGroup()
                .equals(i.getMetadata().get(1).getGroup()));
        assertTrue(clone.getMetadata().get(1).getKey()
                .equals(i.getMetadata().get(1).getKey()));
        assertTrue(clone.getMetadata().get(1).getValue()
                .equals(i.getMetadata().get(1).getValue()));

    }

    /**
     * method for testing the cloning process of a MetaEntry.
     * 
     * @exception CloneNotSupportedException
     *                convention
     * @throws CloneNotSupportedException
     */
    @Test
    public void metaEntryCloneTest() throws CloneNotSupportedException {
        final MetaEntry me = new MetaEntry();
        MetaEntry clone = new MetaEntry();

        me.setGroup("group1");
        me.setKey("key1");
        me.setValue("value1");

        clone = (MetaEntry) me.clone();

        assertTrue(clone.getGroup().equals(me.getGroup()));
        assertTrue(clone.getKey().equals(me.getKey()));
        assertTrue(clone.getValue().equals(me.getValue()));
    }

    /**
     * method for testing the cloning process of a Role.
     * 
     * @exception CloneNotSupportedException
     *                convention
     * @throws CloneNotSupportedException
     */
    @Test
    public void roleCloneTest() throws CloneNotSupportedException {
        final Role r = new Role();
        Role clone = new Role();

        r.setId("1");
        r.setRolename("rolename");

        clone = (Role) r.clone();

        assertTrue(clone.getId().equals(r.getId()));
        assertTrue(clone.getRolename().equals(r.getRolename()));
    }

    /**
     * method for testing the cloning process of a RootElement.
     * 
     * @exception CloneNotSupportedException
     *                convention
     * @throws CloneNotSupportedException
     */
    @Test
    public void rootElementCloneTest() throws CloneNotSupportedException {
        final RootElement re = new RootElement();
        RootElement clone = new RootElement();

        re.setId("1");

        clone = (RootElement) re.clone();

        assertTrue(clone.getId().equals(re.getId()));
    }

    /**
     * Method for testing of the cloning process of a workflow. IMPORTANT! We
     * need to check if the cloning process of the references between steps (a
     * step has a nextsteps list) works this is done via the
     * convertIdsToReference method in workflows
     * 
     * @exception CloneNotSupportedException
     *                convention
     * @throws CloneNotSupportedException
     */
    @Test
    public void workflowCloneTest() throws CloneNotSupportedException {
        final Workflow w = new Workflow();
        Workflow clone = new Workflow();
        final Step s1 = new Step();
        final Step s2 = new Step();
        final Item i1 = new Item();
        final Item i2 = new Item();
        final MetaEntry m1 = new MetaEntry();
        final MetaEntry m2 = new MetaEntry();

        Role role1 = new Role();
        Role role2 = new Role();
        role1.setRolename("testrolle_1");
        role2.setRolename("testrolle_2");

        
        w.setId("1");
        w.setActive(true);
        s1.setId("1");
        s1.getRoles().add(role1.getRolename());
        s1.setOpener("user4711");
        s1.getNextStepIds().add("2");
        s2.setId("2");
        s2.getRoles().add(role2.getRolename());
        s2.setOpener("user4711");
        w.getSteps().add(s1);
        w.getSteps().add(s2);

        m1.setGroup("group1");
        m1.setKey("key1");
        m1.setValue("value1");
        m2.setGroup("group2");
        m2.setKey("key2");
        m2.setValue("value2");
        i1.getMetadata().add(m1);
        i2.getMetadata().add(m2);
        w.getItems().add(i1);
        w.getItems().add(i2);

        clone = (Workflow) w.clone();

        assertTrue(clone.getId().equals(w.getId()));
        assertTrue(clone.isActive() == w.isActive());

        assertTrue(clone.getSteps().get(0).getId().equals(w.getSteps().get(0)
                .getId()));
        assertTrue(clone.getSteps().get(0).getOpener()
                .equals(w.getSteps().get(0).getOpener()));
        assertTrue(clone.getSteps().get(0).getRoles()
                .equals(w.getSteps().get(0).getRoles()));
        assertTrue(clone.getSteps().get(1).getId() == w.getSteps().get(1)
                .getId());
        assertTrue(clone.getSteps().get(1).getOpener()
                .equals(w.getSteps().get(1).getOpener()));
        assertTrue(clone.getSteps().get(1).getRoles()
                .equals(w.getSteps().get(1).getRoles()));

        // Testing if convertIdsToReferences in cloning process works
        assertTrue(clone.getSteps().get(0).getNextSteps().get(0).getId().equals(w
                .getSteps().get(1).getId()));
        assertTrue(clone.getSteps().get(0).getNextSteps().get(0).getRoles()
                .equals(w.getSteps().get(1).getRoles()));

        assertTrue(clone.getItems().get(0).getMetadata().get(0).getGroup()
                .equals(w.getItems().get(0).getMetadata().get(0).getGroup()));
        assertTrue(clone.getItems().get(0).getMetadata().get(0).getKey()
                .equals(w.getItems().get(0).getMetadata().get(0).getKey()));
        assertTrue(clone.getItems().get(0).getMetadata().get(0).getValue()
                .equals(w.getItems().get(0).getMetadata().get(0).getValue()));
        assertTrue(clone.getItems().get(1).getMetadata().get(0).getGroup()
                .equals(w.getItems().get(1).getMetadata().get(0).getGroup()));
        assertTrue(clone.getItems().get(1).getMetadata().get(0).getKey()
                .equals(w.getItems().get(1).getMetadata().get(0).getKey()));
        assertTrue(clone.getItems().get(1).getMetadata().get(0).getValue()
                .equals(w.getItems().get(1).getMetadata().get(0).getValue()));

    }

    /**
     * method for testing the cloning process of a User.
     * 
     * @exception CloneNotSupportedException
     *                convention
     * @throws CloneNotSupportedException
     */
    @Test
    public void userCloneTest() throws CloneNotSupportedException {
        final User u = new User();
        User clone = new User();
        final Role r1 = new Role();
        final Role r2 = new Role();

        u.setId("1");
        u.setUsername("user1");
        r1.setId("1");
        r1.setRolename("role1");
        r2.setRolename("role2");
        u.getRoles().add(r1);
        u.getRoles().add(r2);

        clone = (User) u.clone();

        assertTrue(clone.getId() == u.getId());
        assertTrue(clone.getUsername().equals(u.getUsername()));
        assertTrue(clone.getRoles().get(0).getId().equals(u.getRoles().get(0)
                .getId()));
        assertTrue(clone.getRoles().get(0).getRolename()
                .equals(u.getRoles().get(0).getRolename()));
        assertTrue(clone.getRoles().get(1).getId().equals(u.getRoles().get(1)
                .getId()));
        assertTrue(clone.getRoles().get(1).getRolename()
                .equals(u.getRoles().get(1).getRolename()));
    }

}
