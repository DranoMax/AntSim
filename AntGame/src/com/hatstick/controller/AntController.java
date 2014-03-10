package com.hatstick.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.hatstick.entity.Ant;

public class AntController {

	/** Returns velocity to target */
	public void goToLocation(Ant ant) {
		ant.getPosition().add((ant.getDestination().cpy().sub(ant.getPosition())).nor().scl(Gdx.graphics.getDeltaTime()));
	}
	
	/** Wander using random points in a radius around entity */
	public void wander(Ant ant) {
		// Find random point within circle of radius 10
		double a= Math.PI*2*Math.random();
		ant.setDestination(new Vector2((float)(ant.getPosition().x+(2*Math.cos(a))),(float)(ant.getPosition().y+(2*Math.sin(a)))));
		float angle = (float) ((Math.atan2 (ant.getDestination().y - ant.getPosition().y, -(ant.getDestination().x - ant.getPosition().x))*180.0d/Math.PI));
		angle -= 90;
		if(angle < 0) angle = 360-(-angle);
		angle = 360-angle;
		ant.setTarget(angle);
	}
}
