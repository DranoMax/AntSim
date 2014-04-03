package com.hatstick.entity;

import java.util.HashMap;

import com.hatstick.entity.PathNode;
import com.hatstick.interfaces.State;

/** Ants will create pheromone trails leading to food/war/bathroom/ect 
 *  If another ant crosses within range of one of these nodes and if it's
 *  something that ant is looking for, it will add the path and begin following
 *  until it wants something else. */
public class PathList {
	
	private PathNode head;
	private PathNode tail;
	
	private int size;

	private HashMap<PathNode,State> map;
	
	public PathList() {
		head = null;
		tail = null;
		size = 0;
		setMap(new HashMap<PathNode,State>());
	}
	
	public void insert(PathNode node, State state) {
		if (head == null) {
			head = tail = node;
			head.setNext(tail);
			tail.setPrev(head);
			head.setPrev(null);
			map.put(head, state);
		} 
		else {
		tail.setNext(node);
		node.setPrev(tail);
		tail = tail.getNext();
		map.put(node, state);
		}
		size++;
	}
	
	public void clear() {
		head = tail = null;
		size = 0;
		map.clear();
	}
	
	/**
	 * Deletes everything and sets head to given Node
	 */
	public void reset(PathNode node, State state) {
		map.clear();
		head = tail = null;
		size = 0;
		insert(node, state);
	}
	
	public PathNode getHead() {
		return head;
	}
	
	public PathNode getTail() {
		return tail;
	}
	
	public int size() {
		return size;
	}
	
	public HashMap<PathNode,State> getMap() {
		return map;
	}

	public void setMap(HashMap<PathNode,State> map) {
		this.map = map;
	}
}
