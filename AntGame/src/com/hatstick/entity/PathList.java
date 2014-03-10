package com.hatstick.entity;

import java.util.HashMap;
import com.hatstick.entity.PathNode;

/** Ants will create pheromone trails leading to food/war/bathroom/ect 
 *  If another ant crosses within range of one of these nodes and if it's
 *  something that ant is looking for, it will add the path and begin following
 *  until it wants something else. */
public class PathList {
	
	/**
	 * Type of paths
	 */
	public enum Type {
		SEARCH, FOOD, WAR
	}
	
	private PathNode head;
	private int size;

	private HashMap<PathNode,Type> map;
	
	public PathList() {
		head = null;
		size = 0;
		setMap(new HashMap<PathNode,Type>());
	}
	
	public void insert(PathNode node, Type type) {
		if (head == null) {
			head = node;
			map.put(head, type);
			size++;
			return;
		}

		PathNode temp = getLast();
		temp.setNext(node);
		node.setPrev(temp);
		map.put(node, type);
		size++;
	}
	
	public PathNode getHead() {
		return head;
	}
	
	public PathNode getLast() {
		PathNode temp = head;
		while(temp.getNext() != null) {
			temp = temp.getNext();
		}
		return temp;
	}
	
	public int size() {
		return size;
	}
	
	public HashMap<PathNode,Type> getMap() {
		return map;
	}

	public void setMap(HashMap<PathNode,Type> map) {
		this.map = map;
	}
}
