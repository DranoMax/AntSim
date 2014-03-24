package com.hatstick.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.InputProcessor;
import com.hatstick.antgame.WorldRenderer;
import com.hatstick.entity.Level;


public class GameScreen implements Screen, InputProcessor, GestureListener {

	private WorldRenderer 	renderer;
	
	private InputMultiplexer multi = new InputMultiplexer();
	
	private Vector2 initialTouch = new Vector2();
	
	/**
	 * touchCircle used for determining touches.
	 */
	private Circle touchCircle = new Circle(0,0,20);

	@Override
	public void show() {
		multi.addProcessor(new GestureDetector(this));
		multi.addProcessor(new InputMultiplexer(this));
		Gdx.input.setInputProcessor(multi);
		renderer = new WorldRenderer(new Level());
	}

	@Override
	public void render(float delta) {
		// clear the screen with white.
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderer.render();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
	}

	// * InputProcessor methods ***************************//

	@Override
	public boolean keyDown(int keycode) {

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean touchMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		renderer.setZoom(amount);
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		initialTouch.set(screenX,screenY);
		//	renderer.setTouch(screenX, screenY);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//	renderer.setTouch(screenX, screenY);
		//renderer.setTranslation(initialTouch.sub(screenX,screenY));
	//	initialTouch.set(screenX,screenY);
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// Stop following ant
		renderer.setFollowingAnt(null);
		
		// Unproject our values from Screen to world values
		Vector3 temp = new Vector3(x,y,0);
		renderer.getCam().unproject(temp);
		touchCircle.setPosition(temp.x,temp.y);
		// See if we've selected an ant
		renderer.selectAnt(touchCircle);
		
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		renderer.setTranslation(new Vector2 (-deltaX,deltaY));
		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		renderer.setZoom((initialDistance-distance)*.01f);
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}
}
