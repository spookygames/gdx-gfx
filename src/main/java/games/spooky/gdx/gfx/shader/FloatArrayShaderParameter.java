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

import java.util.Arrays;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class FloatArrayShaderParameter extends ShaderParameter {

	private int length = -1;
	private float[] value;

	FloatArrayShaderParameter(String name) {
		super(name);
	}

	public float[] getValue() {
		return this.value;
	}

	/**
	 * Sets the value of this color parameter. Event is triggered only if new
	 * float array is different from former float array, tested with array
	 * length and java.util.Arrays.equals().
	 *
	 * @param value
	 *            the new float[] value
	 * @return this shader parameter, for chaining
	 */
	public FloatArrayShaderParameter setValue(float[] value) {
		if (this.length == -1) {
			this.value = value;
			this.length = value.length;
			valueChanged();
		} else if (value.length <= this.length && !Arrays.equals(this.value, value)) {
			for (int i = 0; i < value.length; i++)
				this.value[i] = value[i];
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
		shader.setUniform1fv(name, value, 0, length);
	}

}
