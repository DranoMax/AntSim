package com.hatstick.behavior;

import com.alex.interfaces.MovementBehavior;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Wander implements MovementBehavior {

	private float angle = 0f;

	@Override
	public float move(Vector2 position, Vector2 destination) {

		// Find random point within circle of radius 10
		double a= Math.PI*2*Math.random();
		destination.set(new Vector2((float)(position.x+(2*Math.cos(a))),(float)(position.y+(2*Math.sin(a)))));
		angle = (float) ((Math.atan2 (destination.y - position.y, -(destination.x - position.x))*180.0d/Math.PI));
		angle -= 90;
		if(angle < 0) angle = 360-(-angle);
		angle = 360-angle;
		
		return angle;
	}
}
