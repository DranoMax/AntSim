package com.hatstick.behavior;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.hatstick.entity.PathList;
import com.hatstick.entity.PathNode;
import com.hatstick.interfaces.MovementBehavior;
import com.hatstick.interfaces.State;

public class Gather implements MovementBehavior {
	
	private GoToLocation goToLocation = new GoToLocation();
	private PathNode currentNode = null;
	private boolean goingHome = false;

	@Override
	public float move(Vector2 position, Vector2 destination, PathList path,
			float speed) {
		/**
		 * Found the end of path to food
		 */
		if (currentNode == null) {
			currentNode = path.getLast();
			currentNode.setPos(destination);
			path.getMap().put(currentNode, State.GATHERING);
			goingHome = true;
		}
		
	//	System.out.println("nodeId: " + currentNode.getId() + " out of " + path.size());

		Vector2 temp = position.cpy().sub(currentNode.getPos());
		float temp2 = speed*Gdx.graphics.getDeltaTime();

		// Determine if ant is within reasonable distance of node
		if (Math.abs(temp.x) < temp2 && Math.abs(temp.y) < temp2) {
			if (goingHome) {
				// Get next node
				if (currentNode.getPrev() != null) {
					currentNode = currentNode.getPrev();
					// We've found food! Update pheremone trail (path) to indicate food
					path.getMap().put(currentNode, State.GATHERING);
					// Else, turn around
				} else {
					goingHome = !goingHome;
				}
			} else {
				if (currentNode.getNext() != null) {
					currentNode = currentNode.getNext();
					path.getMap().put(currentNode, State.GATHERING);
				} else {
					goingHome = !goingHome;
				}
			}
		}
		destination = (currentNode.getPos());

		return goToLocation.move(position, destination, path, speed);
	}

}
