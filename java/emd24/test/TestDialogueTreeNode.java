package emd24.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import emd24.rpgmod.quest.DialogueTreeNode;

public class TestDialogueTreeNode {
	DialogueTreeNode tree_node;
	
	@Before
	public void setUp() throws Exception {
		tree_node = new DialogueTreeNode();
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void test_add_children() {
		tree_node.addChild();
		assertEquals(tree_node.children.size(), 1);
		
		tree_node.addChild();
		assertEquals(tree_node.children.size(), 2);
	}

	@Test
	public void test_add_childrens_children() {
		tree_node.addChild();
		assertEquals(tree_node.children.size(), 1);
		
		DialogueTreeNode child = tree_node.children.get(0);
		assertEquals(child.children.size(), 1);
	}

	@Test
	public void test_add_response_to_reply() {
		tree_node.addChild();
		assertEquals(tree_node.children.size(), 1);
		
		DialogueTreeNode child = tree_node.children.get(0);
		child.addChild();
		assertEquals(child.children.size(), 1);
	}

	@Test
	public void test_delete_child() {
		tree_node.addChild();
		assertEquals(tree_node.children.size(), 1);
		
		DialogueTreeNode child = tree_node.children.get(0);
		
		child.remove();
		assertEquals(tree_node.children.size(), 0);	//can delete replies
	}

	@Test
	public void test_delete_grand_child() {
		tree_node.addChild();
		assertEquals(tree_node.children.size(), 1);
		
		DialogueTreeNode child = tree_node.children.get(0);
		DialogueTreeNode grand_child = child.children.get(0);
		
		grand_child.remove();
		assertEquals(child.children.size(), 1);	//cannot remove a dialogue leaf
	}


	@Test
	public void test_store() {
		tree_node.dialogueText = "Hi I'm Nick the blacksmith.";
		tree_node.itemNeeded = "None";
		tree_node.itemQuantity = 0;
		tree_node.action = "zilch";

		String stored = tree_node.store();
		
		String expected = "Hi I'm Nick the blacksmith.`None`0`false`zilch`0`\n";
		
		assertEquals(expected, stored);
	}


	@Test
	public void test_load() {
		String loaded = "Hi I'm Nick the blacksmith.`None`0`false`zilch`0`\n";
		
		tree_node.load(loaded);
		
		assertEquals("Hi I'm Nick the blacksmith.", tree_node.dialogueText);
		assertEquals("None", tree_node.itemNeeded);
		assertEquals(0, tree_node.itemQuantity);
		assertEquals("zilch", tree_node.action);
		
	}


	@Test
	public void test_load_store_complex() {
		assert(false);
		
	}


	@Test
	public void test_getList() {
		tree_node.addChild();
		
		List<DialogueTreeNode> nodes = new ArrayList<DialogueTreeNode>();
		tree_node.getList(nodes);
		
		assertEquals(nodes.size(), 3);
	}


	@Test
	public void test_getList_strings() {
		tree_node.addChild();
		
		List<String> nodes = new ArrayList<String>();
		tree_node.getList(nodes, "");
		
		assertEquals(nodes.size(), 3);
		
	}
	

}
