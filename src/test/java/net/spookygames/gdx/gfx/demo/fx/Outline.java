/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Spooky Games
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
package net.spookygames.gdx.gfx.demo.fx;

import org.adrianwalker.multilinestring.Multiline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;

import net.spookygames.gdx.gfx.BouncingBuffer;
import net.spookygames.gdx.gfx.CommonShaders;
import net.spookygames.gdx.gfx.VisualEffect;
import net.spookygames.gdx.gfx.shader.ColorShaderParameter;
import net.spookygames.gdx.gfx.shader.FloatShaderParameter;
import net.spookygames.gdx.gfx.shader.OwnedSinglePassShaderEffect;
import net.spookygames.gdx.gfx.shader.Vector2ShaderParameter;

public abstract class Outline implements VisualEffect, Disposable {

	private BouncingBuffer buffer;

	private OutlineFilter filter;
	private Superimpose superimpose;
	
	public Outline (int width, int height, float viewportWidth, float viewportHeight) {
		buffer = new BouncingBuffer(Format.RGBA8888, width, height, false);

		superimpose = new Superimpose();

		filter = new OutlineFilter();
		filter.setViewportInverse(1f / viewportWidth, 1f / viewportHeight);
	}

	@Override
	public void dispose () {
		superimpose.dispose();
		filter.dispose();
		buffer.dispose();
	}

	public float getThickness() {
		return filter.getThickness();
	}

	public void setThickness(float thickness) {
		filter.setThickness(thickness);
	}

	public Color getColor() {
		return filter.getColor();
	}

	public void setColor(Color color) {
		filter.setColor(color);
	}

	@Override
	public void render(Texture source, FrameBuffer destination) {

		// Draw outlined to buffer
		buffer.getCurrentTexture().bind(0);
		buffer.begin();
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderOutlined();
		
		buffer.end();

		filter.render(buffer.getResultTexture(), buffer.getCurrentBuffer());

		// mix original scene and outlines
		superimpose.setAdditionalInput(buffer.getResultTexture());
		superimpose.render(source, destination);
	}

	@Override
	public void rebind () {
		buffer.rebind();
		superimpose.rebind();
		filter.rebind();
	}

	public abstract void renderOutlined();
	

	private static class OutlineFilter extends OwnedSinglePassShaderEffect {

		/**
// http://www.allpiper.com/2d-selection-outline-shader-in-libgdx/
// http://blogs.love2d.org/content/let-it-glow-dynamically-adding-outlines-characters

#ifdef GL_ES
    #define PRECISION mediump
    #define LOWP lowp
    precision PRECISION float;
#else
    #define PRECISION
    #define LOWP 
#endif

uniform sampler2D u_texture0;

// The inverse of the viewport dimensions along X and Y
uniform vec2 u_viewportInverse;

// Color of the outline
uniform vec4 u_color;

// Thickness of the outline
uniform float u_thickness;

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

void main() {
   vec2 orig = v_texCoords.xy;
   
   float a = - 6.0 * texture2D(u_texture0, orig).a;
   a += texture2D(u_texture0, orig + vec2(0, u_thickness) * u_viewportInverse).a;
   a += 0.5 * texture2D(u_texture0, orig + vec2(-u_thickness, u_thickness) * u_viewportInverse).a;
   a += texture2D(u_texture0, orig + vec2(-u_thickness, 0) * u_viewportInverse).a;
   a += 0.5 * texture2D(u_texture0, orig + vec2(-u_thickness, -u_thickness) * u_viewportInverse).a;
   a += texture2D(u_texture0, orig + vec2(0, -u_thickness) * u_viewportInverse).a;
   a += 0.5 * texture2D(u_texture0, orig + vec2(u_thickness, -u_thickness) * u_viewportInverse).a;
   a += texture2D(u_texture0, orig + vec2(u_thickness, 0) * u_viewportInverse).a;
   a += 0.5 * texture2D(u_texture0, orig + vec2(u_thickness, u_thickness) * u_viewportInverse).a;
   
   gl_FragColor = vec4(u_color.r * a, u_color.g * a, u_color.b * a, a);
}

		*/
		@Multiline static String Outline;
	
		private final Vector2ShaderParameter viewportInverse;
		private final FloatShaderParameter thickness;
		private final ColorShaderParameter color;

		public OutlineFilter() {
			super(CommonShaders.Screenspace, Outline);

			viewportInverse = registerParameter("u_viewportInverse", 0f, 0f);
			thickness = registerParameter("u_thickness", 0f);
			color = registerParameter("u_color", Color.WHITE);
		}
		
		void setViewportInverse(float viewportInverseX, float viewportInverseY) {
			this.viewportInverse.setValue(viewportInverseX, viewportInverseY);
		}

		public float getThickness() {
			return thickness.getValue();
		}

		public void setThickness(float thickness) {
			this.thickness.setValue(thickness);
		}

		public Color getColor() {
			return color.getValue();
		}

		public void setColor(Color color) {
			this.color.setValue(color);
		}
	}
	

	private static class Superimpose extends OwnedSinglePassShaderEffect {

		/**
	#ifdef GL_ES
		#define PRECISION mediump
		precision PRECISION float;
	#else
		#define PRECISION
	#endif

	uniform PRECISION sampler2D u_texture0;
	uniform PRECISION sampler2D u_texture1;

	varying vec2 v_texCoords;

	void main()
	{
		vec4 src1 = texture2D(u_texture0, v_texCoords);
		vec4 src2 = texture2D(u_texture1, v_texCoords);

		gl_FragColor = src1 * (1.0 - src2) + src2;
	}
		*/
		@Multiline static String Superimpose;

		private Texture inputTexture2 = null;

		public Superimpose() {
			super(CommonShaders.Screenspace, Superimpose);

			registerParameter("u_texture1", u_texture1);
		}

		public void setAdditionalInput(Texture texture) {
			this.inputTexture2 = texture;
		}

		@Override
		protected void actualRender(Texture source) {
			inputTexture2.bind(u_texture1);
			super.actualRender(source);
		}
	}
}
