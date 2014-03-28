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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.InputProcessor;
import com.hatstick.antgame.WorldRenderer;
import com.hatstick.entity.Level;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*; 


public class GameScreen implements Screen, InputProcessor, GestureListener {

	private WorldRenderer renderer;

	private InputMultiplexer multi = new InputMultiplexer();

	private Vector2 initialTouch = new Vector2();
	private float prevZoom = 0f;

	// Following used for overlying menu
	private Stage menu;
	private Skin skin;
	
	private float menuFadeTimer = 0;
	private final int MENU_FADE_WAIT = 2;
	private boolean menuIsFading = true;
	// End overlying menu items
	
	/**
	 * touchCircle used for determining touches.
	 */
	private Circle touchCircle = new Circle(0,0,20);

	@Override
	public void show() {
		renderer = new WorldRenderer(new Level());

		createMenu();

		multi.addProcessor(menu);
		multi.addProcessor(new GestureDetector(this));
		multi.addProcessor(new InputMultiplexer(this));
		Gdx.input.setInputProcessor(multi);

	}
	
	public void createMenu() {
		menu = new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
		skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));

		final TextButton nodesButton = new TextButton("Nodes",skin,"default");
		final TextButton pathButton = new TextButton("Path",skin,"default");
		
		nodesButton.setWidth(80);
		pathButton.setWidth(80);
		
		nodesButton.setPosition(0, Gdx.graphics.getHeight()-nodesButton.getHeight());
		pathButton.setPosition(nodesButton.getWidth(), Gdx.graphics.getHeight()-pathButton.getHeight());

		nodesButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				renderer.drawNodes = !renderer.drawNodes;
			}
			
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				menuIsFading = false;
			}
		});
		menu.addActor(nodesButton);
		
		pathButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				renderer.drawPath = !renderer.drawPath;
			}
			
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				menuIsFading = false;
			}
		});
		menu.addActor(pathButton);
	}
	
	private void handleMenuFade() {
		if (menuIsFading) {
			menu.addAction(sequence(fadeOut(1),delay(.2f),visible(false)));
		} else {
			menu.getRoot().clearActions();
			menu.addAction(visible(true));
			menu.addAction(fadeIn(.5f));
			
			menuFadeTimer += Gdx.graphics.getDeltaTime();
			if (menuFadeTimer > MENU_FADE_WAIT) {
				menuFadeTimer = 0;
				menuIsFading = true;
			}
		}
	}

	@Override
	public void render(float delta) {
		// clear the screen with white.
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderer.render();
		
		handleMenuFade();
		menu.act(Gdx.graphics.getDeltaTime());
		menu.draw();
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
		menu.dispose();
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
		renderer.setZoom(amount*1.5f);
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
		// renderer.setTouch(screenX, screenY);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// renderer.setTouch(screenX, screenY);
		//renderer.setTranslation(initialTouch.sub(screenX,screenY));
		// initialTouch.set(screenX,screenY);
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
		
		// FadeIn our menu
		menuIsFading = false;

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
		if (Math.abs(deltaX) > 10 || Math.abs(deltaY) > 10) {
			// Stop following ant
			renderer.setFollowingAnt(null);
		}
		renderer.setTranslation(new Vector2 (-deltaX,deltaY), renderer.getCam());
		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		float zoom = initialDistance-distance;
		if (Math.abs(zoom-prevZoom) > 10f) {
			prevZoom = zoom;
			renderer.setZoom((zoom)*.01f);
		}
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}
}