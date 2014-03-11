package com.hatstick.behavior;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.hatstick.interfaces.MovementBehavior;

public class Wander implements MovementBehavior {

	private float angle = 0f;

	@Override
	// Note, we use speed in Wander to determine radius of random circle
	public float move(Vector2 position, Vector2 destination, float speed) {

		// Find random point along circumference of circle of radius=speed.
		// double a represents an angle found at random.
		double a = Math.PI*2*Math.random();
		destination.set(new Vector2((float)(position.x+(1.5f*speed*Math.cos(a))),(float)(position.y+(1.5f*speed*Math.sin(a)))));
		angle = (float) ((Math.atan2 (destination.y - position.y, -(destination.x - position.x))*180.0d/Math.PI));
		angle -= 90;
		if(angle < 0) angle = 360-(-angle);
		angle = 360-angle;
		
		return angle;
	}
}
