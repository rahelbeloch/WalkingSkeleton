package PersistenceTesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import persistence.PersistenceImp;
import persistence.UserAlreadyExistsException;
import persistence.UserNotExistentException;
import abstractbeans.AbstractUser;
import backingbeans.Action;
import backingbeans.FinalStep;
import backingbeans.Item;
import backingbeans.StartStep;
import backingbeans.Workflow;

public class PersistenceTest {

	PersistenceImp db = new PersistenceImp();
	
	/*
	 * Worflow Testing
	 */
	@Test
	public void testWorkflowStorage() {
		Workflow workflow007 = new Workflow(7);
		Workflow workflow006 = new Workflow(6);
		Workflow workflow005 = new Workflow(5);
		
		db.storeWorkflow(workflow005);
		db.storeWorkflow(workflow006);
		db.storeWorkflow(workflow007);
		
		assertEquals(db.loadWorkflow(5), workflow005);
		assertEquals(db.loadWorkflow(6), workflow006);
		assertEquals(db.loadWorkflow(7), workflow007);
	}
	
	@Test
	public void testDuplicateWorkflowStorage() {
		Workflow workflow001 = new Workflow(17);
		Workflow workflow002 = new Workflow(17);
		
		db.storeWorkflow(workflow001);
		db.storeWorkflow(workflow002);
		
		// current assumption: second workflow entry should have overwritten the first one 
		assertNotEquals(workflow001,db.loadWorkflow(17));
		assertEquals(workflow002,db.loadWorkflow(17));
	}
	
	@Test
	public void testWorkflowDeletion() {
		Workflow wf001 = new Workflow(1);
		Workflow wf002 = new Workflow(2);
		db.storeWorkflow(wf001);
		db.storeWorkflow(wf002);
		
		db.deleteWorkflow(1);
		
		assertEquals(db.loadWorkflow(1), null);
		assertEquals(db.loadWorkflow(2), wf002);
	}
	
	@Test
	public void testWorkflowStorageIncludingSteps() {
		Workflow workflow007 = new Workflow(7);
		
		StartStep step1 = new StartStep();
		Action step2 = new Action(2, 001, "main action");
		FinalStep step3 = new FinalStep();
		
		workflow007.addStep(step1);
		workflow007.addStep(step2);
		workflow007.addStep(step3);

		db.storeWorkflow(workflow007);
		
		// step2, the action of workflow007, should be persistent
		// step1 and step2 cannot be tested because their ID is not yet implemented
		assertEquals(step2, db.loadStep(2));
		
		// a workflows steps should be the same before and after storage
		assertEquals(workflow007.getStep(), db.loadWorkflow(7).getStep());
		// no specific functions of a step can be tested, because db only returns an AbstractWorkflow
		//assertEquals(workflow007.getStepByPos(2), db.loadWorkflow(7).getStepByPos(2));
		
		// workflow instances should still be equal
		assertEquals(db.loadWorkflow(7), workflow007);
		
		
		db.deleteWorkflow(7);
		
		// workflow must not be existent on database anymore
		assertEquals(db.loadWorkflow(7), null);
		
		// a workflows steps must not be existent on databse anymore
		assertEquals(db.loadStep(2), null);
	}
	
	/*
	 * Item Testing
	 */
	@Test
	public void testItemStorage() {
		Item item001 = new Item();
		Item item002 = new Item();
		Item item003 = new Item();
		item001.setId(1);
		item002.setId(2);
		item003.setId(3);
		
		db.storeItem(item001);
		db.storeItem(item002);
		db.storeItem(item003);
		
		assertEquals(item001, db.loadItem(1));
		assertEquals(item002, db.loadItem(2));
		assertEquals(item003, db.loadItem(3));
	}
	
	@Test
	public void testDuplicateItemStorage() {
		Item item001 = new Item();
		Item item002 = new Item();
		item001.setId(17);
		item002.setId(17);
		
		db.storeItem(item001);
		db.storeItem(item002);
		
		// current assumption: second item entry should have overwritten the first one 
		assertNotEquals(item001,db.loadItem(17));
		assertEquals(item002,db.loadItem(17));
	}
	
	@Test
	public void testItemStorageIncludingMetaData() {
		Item item001 = new Item();
		item001.setId(1);
		item001.set("key1", "group1", "value1");
		item001.set("key2", "group2", "value2");
		
		db.storeItem(item001);
		
		// MetaEntries should be persistent
		assertEquals(item001.getMetadata().get(0), db.loadMetaEntry("key1"));
		assertEquals(item001.getMetadata().get(1), db.loadMetaEntry("key2"));
		
		// an items metaData should be the same before and after storage
		assertEquals(item001.getMetadata(), db.loadItem(1).getMetadata());
		assertEquals(item001.getMetadata().get(0).getValue(), db.loadMetaEntry("key1").getValue());
		
		// item instances should still be the same
		assertEquals(db.loadItem(1),item001);
		
		db.deleteItem(1);
		
		// item must not be existent anymore
		assertEquals(db.loadItem(1), null);
		
		// an items metaData must not be existent anymore
		assertEquals(db.loadMetaEntry("key1"), null);
	}
	
	/*
	 * User Testing
	 */
	@Test
	public void testUserStorage() throws UserAlreadyExistsException {
		AbstractUser user001 = new AbstractUser();
		AbstractUser user002 = new AbstractUser();
		AbstractUser user003 = new AbstractUser();
		user001.setName("1");
		user002.setName("2");
		user003.setName("3");
		
		db.addUser(user001);
		db.addUser(user002);
		db.addUser(user003);
		
		assertEquals(user001, db.loadUser("1"));
		assertEquals(user002, db.loadUser("2"));
		assertEquals(user003, db.loadUser("3"));
	}
	
	@Test(expected=UserAlreadyExistsException.class)
	public void testUserAlreadyExistsException() throws UserAlreadyExistsException {
		AbstractUser user001 = new AbstractUser();
		AbstractUser user002 = new AbstractUser();
		user001.setName("17");
		user002.setName("17");
		
		// first user is stored, second one should be rejected by UserAlreadyExistsException
		db.addUser(user001);
		db.addUser(user002);
	}
	
	@Test
	public void testDuplicateUserStorage() throws UserNotExistentException {
		AbstractUser user001 = new AbstractUser();
		AbstractUser user002 = new AbstractUser();
		user001.setName("17");
		user002.setName("17");
		
		// first user is stored, second one should be rejected
		try {
			db.addUser(user001);
			db.addUser(user002);
		} catch (UserAlreadyExistsException e) {
			// Exception ist catched manually in order to test if addUser(user002) was rejected
			// e.printStackTrace();
		}

		assertEquals(user001,db.loadUser("17"));		
		assertNotEquals(user002,db.loadUser("17"));
	}
	
	@Test
	public void testUserDeletion() throws UserAlreadyExistsException{
		AbstractUser user001 = new AbstractUser();
		AbstractUser user002 = new AbstractUser();
		user001.setName("1");
		user002.setName("2");
		
		db.addUser(user001);
		db.addUser(user002);
		
		db.deleteUser("1");
		
		assertEquals(db.loadUser("1"), null);
		assertEquals(db.loadUser("2"), user002);
	}
}
