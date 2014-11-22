package PersistenceTesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import persistence.Persistence;
import abstractbeans.AbstractUser;
import backingbeans.Item;
import backingbeans.Workflow;

public class PersistenceTest {

	Persistence db = new Persistence();
	
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
		
		assertNotEquals(workflow001,db.loadWorkflow(17));
		assertEquals(workflow002,db.loadWorkflow(17));
		//assertSame(workflow002,db.loadWorkflow(17));
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
		
		assertNotEquals(item001,db.loadItem(17));
		assertEquals(item002,db.loadItem(17));
	}
	
	@Test
	public void testUserStorage() {
		AbstractUser user001 = new AbstractUser();
		AbstractUser user002 = new AbstractUser();
		AbstractUser user003 = new AbstractUser();
		user001.setId(1);
		user002.setId(2);
		user003.setId(3);
		
		db.storeUser(user001);
		db.storeUser(user002);
		db.storeUser(user003);
		
		assertEquals(user001, db.loadUser(1));
		assertEquals(user002, db.loadUser(2));
		assertEquals(user003, db.loadUser(3));
	}
	
	@Test
	public void testDuplicateUserStorage() {
		AbstractUser user001 = new AbstractUser();
		AbstractUser user002 = new AbstractUser();
		user001.setId(17);
		user002.setId(17);
		
		db.storeUser(user001);
		db.storeUser(user002);
		
		assertNotEquals(user001,db.loadUser(17));
		assertEquals(user002,db.loadUser(17));
	}
	
	@Test
	public void testUserDeletion() {
		AbstractUser user001 = new AbstractUser();
		AbstractUser user002 = new AbstractUser();
		user001.setId(1);
		user002.setId(2);
		db.storeUser(user001);
		db.storeUser(user002);
		
		db.deleteUser(1);
		
		assertEquals(db.loadUser(1), null);
		assertEquals(db.loadUser(2), user002);
	}
}
