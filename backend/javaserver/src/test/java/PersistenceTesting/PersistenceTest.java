package PersistenceTesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import persistence.Persistence;
import beans.Item;
import beans.User;
import beans.Workflow;

public class PersistenceTest {

	Persistence db = new Persistence();
	
	@Test
	public void testWorkflowStorage() {
		Workflow workflow007 = new Workflow();
		Workflow workflow006 = new Workflow();
		Workflow workflow005 = new Workflow();
		workflow005.setId(005);
		workflow006.setId(006);
		workflow007.setId(007);
		
		db.storeWorkflow(workflow005);
		db.storeWorkflow(workflow006);
		db.storeWorkflow(workflow007);
		
		assertEquals(db.loadWorkflow(005), workflow005);
		assertEquals(db.loadWorkflow(006), workflow006);
		assertEquals(db.loadWorkflow(007), workflow007);
	}
	
	@Test
	public void testDuplicateWorkflowStorage() {
		Workflow workflow001 = new Workflow();
		Workflow workflow002 = new Workflow();
		workflow001.setId(17);
		workflow002.setId(17);
		
		db.storeWorkflow(workflow001);
		db.storeWorkflow(workflow002);
		
		assertNotEquals(workflow001,db.loadWorkflow(17));
		assertEquals(workflow002,db.loadWorkflow(17));
		//assertSame(workflow002,db.loadWorkflow(17));
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
		User user001 = new User();
		User user002 = new User();
		User user003 = new User();
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
		User user001 = new User();
		User user002 = new User();
		user001.setId(17);
		user002.setId(17);
		
		db.storeUser(user001);
		db.storeUser(user002);
		
		assertNotEquals(user001,db.loadUser(17));
		assertEquals(user002,db.loadUser(17));
	}


}
