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
package net.spookygames.gdx.gfx.demo.fx;

import org.adrianwalker.multilinestring.Multiline;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import net.spookygames.gdx.gfx.CommonShaders;
import net.spookygames.gdx.gfx.TemporalEffect;
import net.spookygames.gdx.gfx.shader.FloatShaderParameter;
import net.spookygames.gdx.gfx.shader.OwnedSinglePassShaderEffect;
import net.spookygames.gdx.gfx.shader.Vector2ShaderParameter;

public class Shockwave extends OwnedSinglePassShaderEffect implements TemporalEffect  {

	/**
// https://github.com/mattdesl/kami-demos/tree/master/src/shockwave

#ifdef GL_ES
    #define PRECISION mediump
    #define LOWP lowp
    precision PRECISION float;
#else
    #define PRECISION
    #define LOWP 
#endif

uniform sampler2D u_texture0;

uniform vec2 u_center;      // Shock position, normalized 0.0 to 1.0
uniform float u_time;       // Effect elapsed time, normalized 0.0 to 1.0
uniform float u_diffusion;
uniform float u_diffusionp;
uniform float u_thickness;

varying vec2 v_texCoords;

void main() {
    vec2 uv = v_texCoords.xy;
    vec2 texCoord = uv;
    float dist = distance(uv, u_center);
    float diff = dist - u_time; 
    if ((diff <= u_thickness) && (diff >= -u_thickness)) 
    {
        float powDiff = 1.0 - pow(abs(diff * u_diffusion), u_diffusionp); 
        float diffTime = diff * powDiff;
        vec2 diffUV = normalize(uv - u_center); 
        texCoord = uv + (diffUV * diffTime);
    }
    gl_FragColor = texture2D(u_texture0, texCoord);
}
	*/
	@Multiline static String Shockwave;

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
