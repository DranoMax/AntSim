package com.hatstick.entity;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

/** Ants will create pheromone trails leading to food/war/bathroom/ect 
 *  If another ant crosses within range of one of these nodes and if it's
 *  something that ant is looking for, it will add the path and begin following
 *  until it wants something else. */
public class PathNodes {

	private HashMap<Node, Integer> map;
	
	public PathNodes() {
		setMap(new HashMap<Node, Integer>());
	}
	
	public HashMap<Node, Integer> getMap() {
		return map;
	}

	public void setMap(HashMap<Node, Integer> map) {
		this.map = map;
	}

	class Node {
		private Vector2 pos;
		private Node prev;
		private Node next;
		
		public Node(Vector2 pos) {
			this.setPos(pos);
			setPrev(null);
			setNext(null);
		}
		
		public Node(Vector2 pos, Node prev) {
			this.setPos(pos);
			this.setPrev(prev);
			setNext(null);
		}

		public Node getNext() {
			return next;
		}

		public void setNext(Node next) {
			this.next = next;
		}

		public Node getPrev() {
			return prev;
		}

		public void setPrev(Node prev) {
			this.prev = prev;
		}

		public Vector2 getPos() {
			return pos;
		}

		public void setPos(Vector2 pos) {
			this.pos = pos;
		}
	}
}
