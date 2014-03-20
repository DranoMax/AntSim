package com.hatstick.behavior;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.hatstick.entity.PathList;
import com.hatstick.entity.PathNode;
import com.hatstick.interfaces.MovementBehavior;

public class Wander implements MovementBehavior {

	private static float WAIT_TIME = 1f;
	private float time = 6f;
	private GoToLocation goToLocation = new GoToLocation();

	@Override
	// Note, we use speed in Wander to determine radius of random circle
	public float move(Vector2 position, Vector2 destination, PathList path, float speed) {
		
		// Find new wander target after wait
		time += Gdx.graphics.getDeltaTime();
		if(time >= WAIT_TIME) {
			// Create search node
			path.insert(new PathNode(path.size(),position.cpy()), PathList.Type.SEARCH);
			// Find random point along circumference of circle of radius=speed.
			// double a represents an angle found at random.
			double a = Math.PI*2*Math.random();
			destination.set(new Vector2((float)(position.x+(1.5f*speed*Math.cos(a))),(float)(position.y+(1.5f*speed*Math.sin(a)))));
			time -= WAIT_TIME;
			WAIT_TIME = (float) Math.random()*3;
		}
		goToLocation.move(position, destination, path, speed);
		
		return goToLocation.move(position, destination, path, speed);
	}
}
