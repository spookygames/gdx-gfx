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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ColorShaderParameter extends ShaderParameter {

	private final Color value = new Color();

	ColorShaderParameter(String name) {
		super(name);
	}

	public Color getValue() {
		return this.value;
	}

	/**
	 * Sets the value of this Color parameter. Event is triggered only if new
	 * Color is different from former Color, tested with .equals().
	 *
	 * @param value
	 *            the new Color value
	 * @return this shader parameter, for chaining
	 */
	public ColorShaderParameter setValue(Color value) {
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
