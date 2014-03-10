package com.hatstick.entity;

import com.badlogic.gdx.math.Vector2;

public class PathNode {
	private Vector2 pos;
	private PathNode prev;
	private PathNode next;
	
	public PathNode(Vector2 pos) {
		this.setPos(pos);
		setPrev(null);
		setNext(null);
	}
	
	public PathNode(Vector2 pos, PathNode prev) {
		this.setPos(pos);
		this.setPrev(prev);
		setNext(null);
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
