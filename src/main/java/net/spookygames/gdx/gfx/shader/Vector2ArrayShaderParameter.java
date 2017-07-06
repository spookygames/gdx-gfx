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
package net.spookygames.gdx.gfx.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;

public class Vector2ArrayShaderParameter extends ShaderParameter {

	private int length = -1;
	private float[] value;
	private Vector2[] tmp;

	Vector2ArrayShaderParameter(String name) {
		super(name);
	}

	public Vector2[] getValue() {
		if (tmp == null) {
			if (this.length == -1)
				return null;
			int count = length / 2;
			tmp = new Vector2[count];
			for (int i = 0; i < count; i++)
				tmp[i] = new Vector2(value[2 * i], value[2 * i + 1]);
		} else {
			int count = length / 2;
			for (int i = 0; i < count; i++)
				tmp[i].set(value[2 * i], value[2 * i + 1]);
		}
		return tmp;
	}

	public Vector2ArrayShaderParameter setValue(Vector2[] value) {
		if (this.length == -1) {

			this.length = value.length * 2;
			this.value = new float[this.length];
			for (int i = 0; i < value.length; i++) {
				Vector2 vector = value[i];
				if (vector != null) {
					this.value[2 * i] = vector.x;
					this.value[2 * i + 1] = vector.y;
				}
			}

			valueChanged();

		} else if (value.length * 2 <= this.length) {

			boolean changed = false; // Array equality check

			for (int i = 0; i < value.length; i++) {
				Vector2 vector = value[i];
				if (vector == null) {
					// If null, say we don't change a thing
				} else {
					float x = vector.x;
					float y = vector.y;

					int index = 2 * i;
					if (x != this.value[index]) {
						this.value[index] = x;
						changed = true;
					}

					index += 1;
					if (y != this.value[index]) {
						this.value[index] = y;
						changed = true;
					}
				}
			}

			if (changed)
				valueChanged();
		}
		return this;
	}

	@Override
	public void apply(ShaderProgram shader) {
		shader.setUniform2fv(name, value, 0, length);
	}

}
