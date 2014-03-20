package com.hatstick.entity;

import com.badlogic.gdx.math.Vector2;

/**
 * PathNode defines a piece of a followable path similar to a bread crumb
 */
public class PathNode {
	
	private int id;
	private Vector2 pos;
	private PathNode prev;
	private PathNode next;
	
	public PathNode(int id, Vector2 pos) {
		this.id = id;
		this.pos = pos;
		setPrev(null);
		setNext(null);
	}
	
	public PathNode(Vector2 pos, PathNode prev) {
		this.setPos(pos);
		this.setPrev(prev);
		setNext(null);
	}
	
	public int getId() {
		return id;
	}

	public PathNode getNext() {
		return next;
	}

	public void setNext(PathNode next) {
		this.next = next;
	}

	public PathNode getPrev() {
		return prev;
	}

	public void setPrev(PathNode prev) {
		this.prev = prev;
	}

	public Vector2 getPos() {
		return pos;
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}
}
