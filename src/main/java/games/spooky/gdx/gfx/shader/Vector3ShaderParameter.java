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
package games.spooky.gdx.gfx.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

public class Vector3ShaderParameter extends ShaderParameter {

	private final Vector3 value = new Vector3();

	Vector3ShaderParameter(String name) {
		super(name);
	}

	public Vector3 getValue() {
		return this.value;
	}

	/**
	 * Sets the value of this Vector3 parameter. Event is triggered only if new
	 * Vector3 is different from former Vector3, tested with .equals().
	 *
	 * @param value
	 *            the new Vector3 value
	 * @return this shader parameter, for chaining
	 */
	public Vector3ShaderParameter setValue(Vector3 value) {
		if (!this.value.equals(value)) {
			this.value.set(value);
			valueChanged();
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * games.spooky.gdx.gfx.shader.ShaderParameter#apply(com.badlogic.gdx.
	 * graphics.glutils.ShaderProgram)
	 */
	@Override
	public void apply(ShaderProgram shader) {
		shader.setUniformf(name, value);
	}

}
