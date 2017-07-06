/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Spooky Games
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.spookygames.gdx.gfx.demo;

import static com.badlogic.gdx.math.Interpolation.fade;
import static com.badlogic.gdx.math.Interpolation.pow2;
import static com.badlogic.gdx.math.Interpolation.sineIn;
import static com.badlogic.gdx.math.Interpolation.sineOut;
import static com.badlogic.gdx.math.Interpolation.swing;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveToAligned;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.utils.Align.bottomLeft;
import static com.badlogic.gdx.utils.Align.bottomRight;
import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.topLeft;
import static com.badlogic.gdx.utils.Align.topRight;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.spookygames.gdx.gfx.Effect;
import net.spookygames.gdx.gfx.MultiTemporalVisualEffect;
import net.spookygames.gdx.gfx.demo.fx.Greyscale;
import net.spookygames.gdx.gfx.demo.fx.Outline;
import net.spookygames.gdx.gfx.demo.fx.ScreenShake;
import net.spookygames.gdx.gfx.demo.fx.Sepia;
import net.spookygames.gdx.gfx.demo.fx.Shockwave;

public class GdxGfxDemo implements ApplicationListener {
	
	SpriteBatch batch;

	AssetManager assetManager;

	Stage stage;
	
	MultiTemporalVisualEffect effect;
	
	Rectangle viewportRectangle = new Rectangle();

	@Override
	public void create() {

		/******************/
		/* Initialization */
		/******************/

		final int virtualWidth = 700;
		final int virtualHeight = 500;

		Gdx.graphics.setTitle("gdx-gfx -- demo");
		
		batch = new SpriteBatch();

		Camera camera = new OrthographicCamera();

		FileHandleResolver resolver = new InternalFileHandleResolver();
		assetManager = new AssetManager(resolver);

		assetManager.load("uiskin.json", Skin.class);
		assetManager.load("lena.jpg", Texture.class);
		assetManager.load("knight.png", Texture.class);
		
		assetManager.finishLoading();

		Skin skin = assetManager.get("uiskin.json", Skin.class);

		effect = new MultiTemporalVisualEffect(Format.RGBA8888, false);
		
		/***************/
		/* Stage setup */
		/***************/
		
		Image lena = new Image(assetManager.get("lena.jpg", Texture.class));
		
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for(TextureRegion[] row : new TextureRegion(assetManager.get("knight.png", Texture.class)).split(128, 128))
			frames.addAll(row);
		final Image static1 = new AnimatedImage(new Animation(0.3f, frames));
		final Image static2 = new AnimatedImage(new Animation(0.3f, frames));
		final Image moving = new AnimatedImage(new Animation(0.3f, frames));
		final Image dynamic = new AnimatedImage(new Animation(0.3f, frames)) {
			@Override
			public void act(float delta) {
				int x;
				if(Gdx.input.isTouched() && (x = Gdx.input.getX()) < 500)
					setPosition(x, Gdx.graphics.getHeight() - Gdx.input.getY(), center);
				super.act(delta);
			}
		};
		
		final List<Effect> selector = new List<Effect>(skin);
		ArraySelection<Effect> policy = selector.getSelection();
		policy.setMultiple(true);
		policy.setRangeSelect(true);
		policy.setRequired(false);
		selector.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				effect.clearEffects();
				for(Effect fx : selector.getSelection())
					effect.addEffect(fx);
			}
		});

		Table rootTable = new Table(skin);
		rootTable.setFillParent(true);
		rootTable.row();
		rootTable.add(lena).size(500, 500);
		rootTable.add(selector).grow();
		rootTable.addActor(dynamic);
		rootTable.addActor(moving);
		rootTable.addActor(static1);
		rootTable.addActor(static2);
		
		FitViewport viewport = new FitViewport(virtualWidth, virtualHeight, camera);
		stage = new Stage(viewport, batch);
		stage.addActor(rootTable);

		effect.getCombinedBuffer().setViewport(viewportRectangle);
		
		/********************/
		/* Moving animation */
		/********************/

		static1.addAction(moveToAligned(400, 400, center));
		static2.addAction(moveToAligned(200, 200, center));
		moving.addAction(forever(sequence(
				moveToAligned(250, 250, center, 1, fade),
				moveToAligned(500, 500, topRight, 2, pow2),
				moveToAligned(500, 0, bottomRight, 3, sineIn),
				moveToAligned(0, 500, topLeft, 4, sineOut),
				moveToAligned(0, 0, bottomLeft, 5, swing)
				)));

		
		/*********/
		/* Input */
		/*********/

		Gdx.input.setInputProcessor(stage);

		
		/***********/
		/* Effects */
		/***********/
		
		final Effect[] effects = new Effect[] {
				new Greyscale() {
					@Override
					public String toString() {
						return "Black & White";
					};
				},
				new Sepia() {
					@Override
					public String toString() {
						return "Sepia";
					};
				},
				new ScreenShake(camera, 3f, 60f, 3f) {
					@Override
					public String toString() {
						return "Screen shake!";
					};
				},
				new Shockwave(camera, new Vector2(250, 250), 2f, false) {
					{
						setThickness(.02f);
						setDiffusionFactor(10.0f);
						setDiffusionPower(0.7f);
					}
					@Override
					public String toString() {
						return "Shock wave";
					};
				},
				new Outline(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera.viewportWidth, camera.viewportHeight) {
					{
						setThickness(2f);
						setColor(Color.BLUE);
					}
					@Override
					public void renderOutlined() {
						batch.begin();
						moving.draw(batch, 1f);
						batch.end();
					}
					@Override
					public String toString() {
						return "Outline";
					};
				},
		};
		selector.setItems(effects);
	}

	@Override
	public void render() {

		/* Update */
		float delta = Gdx.graphics.getDeltaTime();

		effect.update(delta);
		stage.act(delta);
		
		/* Draw */
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		effect.capture();
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.draw();
		
		effect.render(effect.endCapture(), null);
	}

	@Override
	public void resize(int width, int height) {
		Viewport viewport = stage.getViewport();
		viewport.update(width, height, true);
		viewportRectangle.set(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
		effect.rebind();
	}

	@Override
	public void dispose() {
		effect.dispose();
		batch.dispose();
		assetManager.dispose();
		stage.dispose();
	}
	
	public static void main(String[] args) {
		ApplicationListener listener = new GdxGfxDemo();

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 800;
		config.height = 600;

		new LwjglApplication(listener, config);
	}
	
}
