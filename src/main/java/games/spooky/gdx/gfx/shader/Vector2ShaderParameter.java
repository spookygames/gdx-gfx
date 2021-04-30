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
import com.badlogic.gdx.math.Vector2;

public class Vector2ShaderParameter extends ShaderParameter {

	private final Vector2 value = new Vector2();

	Vector2ShaderParameter(String name) {
		super(name);
	}

	public Vector2 getValue() {
		return this.value;
	}

	/**
	 * Sets the value of this Vector2 parameter. Event is triggered only if new
	 * coordinates are different from former coordinates.
	 *
	 * @param x
	 *            the x-coordinates of the new Vector2 value
	 * @param y
	 *            the y-coordinates of the new Vector2 value
	 * @return this shader parameter, for chaining
	 */
	public Vector2ShaderParameter setValue(float x, float y) {
		if (this.value.x != x || this.value.y != y) {
			this.value.set(x, y);
			valueChanged();
		}
		return this;
	}

	/**
	 * Sets the value of this Vector2 parameter. Event is triggered only if new
	 * Vector2 is different from former Vector2, tested with .equals().
	 *
	 * @param value
	 *            the new Vector2 value
	 * @return this shader parameter, for chaining
	 */
	public Vector2ShaderParameter setValue(Vector2 value) {
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
