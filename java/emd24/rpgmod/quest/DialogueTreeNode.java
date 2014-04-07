/**
 * A class that is used to represent a dialogue between a player and an NPC
 * 
 * @author Wesley Reardan
 */
package emd24.rpgmod.quest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;

public class DialogueTreeNode {
	public List<DialogueTreeNode> children;	//List of children dialogue nodes
	public String dialogueText;	//Text display to user, or choices if isReply
	public String itemNeeded;		//The item ID needed to access this dialogue item
	public int itemQuantity;		//The quantity of items needed to access this dialogue item
	public boolean isReply;		//if the node is a reply to a statement made by the character
	public String action;	//use in a factory to create action stuff
	
	public DialogueTreeNode() {
		children = new ArrayList<DialogueTreeNode>();
		dialogueText = "";
		itemNeeded = "";
		itemQuantity = 0;
		isReply = false;
		action = "";
	}
	
	/*
	 * Function to add a new child
	 */
	public DialogueTreeNode addChild() {
		DialogueTreeNode child = new DialogueTreeNode();
		child.isReply = !isReply;
		children.add(child);
		return child;
	}
	
	/*
	 * Function to store the node to a string
	 */
	public String store() {
		String result = "";
		
		result += dialogueText + "`";
		result += itemNeeded + "`";
		result += itemQuantity + "`";
		result += isReply + "`";
		result += action + "`";
		
		result += children.size() + "`";
		
		result += "\n";
		
		for(DialogueTreeNode child : children) {
			result += child.store();
		}
		
		return result;
	}
	
	/*
	 * Function to retrieve a tree from a string
	 */
	public void load(String value) {
		String[] lines = value.split("\n");
		load(lines, 0, "");
	}
	
	/**
	 * Recursive load method for dialogue tree
	 * 
	 * @return resuling line number
	 */
	protected int load(String[] lines, int lineNumber, String spaces) {
		String line = lines[lineNumber].trim();
		String[] values = line.split("`");
		
		int i = 0;
		dialogueText = values[i++];
		itemNeeded = values[i++];
		itemQuantity = Integer.parseInt(values[i++]);
		isReply = Boolean.parseBoolean(values[i++]);
		action = values[i++];
		int childrenSize = Integer.parseInt(values[i++]);
		
		lineNumber++;
		
		spaces += " ";
		for(int j = 0; j < childrenSize; j++) {
			DialogueTreeNode child = new DialogueTreeNode();
			lineNumber = child.load(lines, lineNumber, spaces);
			children.add(child);
		}
		
		return lineNumber;
	}
	
	/*
	 * Get a list of Nodes in tree for displaying in list
	 */
	public void getList(List<String> strings, String spaces) {
		String isReplyText = isReply ? "-" : "+";
		strings.add(spaces + isReplyText + dialogueText);
		for(DialogueTreeNode child : children) {
			child.getList(strings, spaces + " ");
		}
	}
	
	/*
	 * Get a list of Nodes in tree for displaying in list
	 */
	public void getList(List<DialogueTreeNode> nodes) {
		nodes.add(this);
		for(DialogueTreeNode child : children) {
			//nodes.add(child);
			child.getList(nodes);
		}
	}
}
