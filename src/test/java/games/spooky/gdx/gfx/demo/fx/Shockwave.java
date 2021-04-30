/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2021 Spooky Games
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
package games.spooky.gdx.gfx.demo.fx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import games.spooky.gdx.gfx.CommonShaders;
import games.spooky.gdx.gfx.TemporalEffect;
import games.spooky.gdx.gfx.shader.FloatShaderParameter;
import games.spooky.gdx.gfx.shader.OwnedSinglePassShaderEffect;
import games.spooky.gdx.gfx.shader.Vector2ShaderParameter;

public class Shockwave extends OwnedSinglePassShaderEffect implements TemporalEffect  {

	// https://github.com/mattdesl/kami-demos/tree/master/src/shockwave
	static final String Shockwave = "#ifdef GL_ES\n" +
			"    #define PRECISION mediump\n" +
			"    #define LOWP lowp\n" +
			"    precision PRECISION float;\n" +
			"#else\n" +
			"    #define PRECISION\n" +
			"    #define LOWP \n" +
			"#endif\n" +
			"\n" +
			"uniform sampler2D u_texture0;\n" +
			"\n" +
			"uniform vec2 u_center;      // Shock position, normalized 0.0 to 1.0\n" +
			"uniform float u_time;       // Effect elapsed time, normalized 0.0 to 1.0\n" +
			"uniform float u_diffusion;\n" +
			"uniform float u_diffusionp;\n" +
			"uniform float u_thickness;\n" +
			"\n" +
			"varying vec2 v_texCoords;\n" +
			"\n" +
			"void main() {\n" +
			"    vec2 uv = v_texCoords.xy;\n" +
			"    vec2 texCoord = uv;\n" +
			"    float dist = distance(uv, u_center);\n" +
			"    float diff = dist - u_time; \n" +
			"    if ((diff <= u_thickness) && (diff >= -u_thickness)) \n" +
			"    {\n" +
			"        float powDiff = 1.0 - pow(abs(diff * u_diffusion), u_diffusionp); \n" +
			"        float diffTime = diff * powDiff;\n" +
			"        vec2 diffUV = normalize(uv - u_center); \n" +
			"        texCoord = uv + (diffUV * diffTime);\n" +
			"    }\n" +
			"    gl_FragColor = texture2D(u_texture0, texCoord);\n" +
			"}";

	private final FloatShaderParameter time;
	private final Vector2ShaderParameter center;
	private final FloatShaderParameter diffusionFactor;
	private final FloatShaderParameter diffusionPower;
	private final FloatShaderParameter thickness;

	private final Vector2 normalizedCenter = new Vector2();
	private final Vector3 tmp = new Vector3();
	
	private final Camera camera;
	private final float duration;
	private final boolean reverse;

	private float elapsed;
	private float lastX;
	private float lastY;

	public Shockwave(Camera camera, Vector2 center, float duration, boolean reverse) {
		super(CommonShaders.Screenspace, Shockwave);
		this.camera = camera;
		this.duration = duration;
		this.reverse = reverse;

		this.elapsed = 0f;

		this.time = registerParameter("u_time", 0f);
		this.center = registerParameter("u_center", normalizeCenter(center.x, center.y));
		this.diffusionFactor = registerParameter("u_diffusion", 10.0f);
		this.diffusionPower = registerParameter("u_diffusionp", 0.7f);
		this.thickness = registerParameter("u_thickness", 1f);
	}

	public float getTime() {
		return elapsed;
	}

	public void setTime (float time) {
		this.time.setValue(time);
	}

	public void setCenter (float x, float y) {
		this.center.setValue(normalizeCenter(x, y));
	}

	public void setCenter (Vector2 center) {
		this.center.setValue(normalizeCenter(center.x, center.y));
	}

	public void setThickness(float thickness) {
		this.thickness.setValue(thickness);
	}

	public float getDiffusionFactor() {
		return diffusionFactor.getValue();
	}

	public void setDiffusionFactor(float diffusionFactor) {
		this.diffusionFactor.setValue(diffusionFactor);
	}

	public float getDiffusionPower() {
		return diffusionPower.getValue();
	}

	public void setDiffusionPower(float diffusionPower) {
		this.diffusionPower.setValue(diffusionPower);
	}

	@Override
	public void reset() {
		elapsed = 0f;
	}
	
	@Override
	public boolean update(float delta) {
		elapsed += delta;
		if (elapsed < duration) {
			this.center.setValue(normalizeCenter(this.lastX, this.lastY));
			float elapsedNormalized = elapsed / duration;
			setTime(reverse ? 1 - elapsedNormalized : elapsedNormalized);
			return false;
		} else {
			return true;
		}
	}

	private Vector2 normalizeCenter(float x, float y) {
		this.lastX = x;
		this.lastY = y;
		camera.project(tmp.set(x, y, 0f));
		return normalizedCenter.set(tmp.x / camera.viewportWidth, tmp.y / camera.viewportHeight);
	}
	
}
