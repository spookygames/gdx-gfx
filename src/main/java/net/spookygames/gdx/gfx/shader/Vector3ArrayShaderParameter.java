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
package net.spookygames.gdx.gfx.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

public class Vector3ArrayShaderParameter extends ShaderParameter {

	private int length = -1;
	private float[] value;
	private Vector3[] tmp;

	Vector3ArrayShaderParameter(String name) {
		super(name);
	}

	public Vector3[] getValue() {
		if (tmp == null) {
			if (this.length == -1)
				return null;
			int count = length / 3;
			tmp = new Vector3[count];
			for (int i = 0; i < count; i++)
				tmp[i] = new Vector3(value[3 * i], value[3 * i + 1], value[3 * i + 2]);
		} else {
			int count = length / 3;
			for (int i = 0; i < count; i++)
				tmp[i].set(value[3 * i], value[3 * i + 1], value[3 * i + 2]);
		}
		return tmp;
	}

	public Vector3ArrayShaderParameter setValue(Vector3[] value) {
		if (this.length == -1) {
			this.length = value.length * 3;
			this.value = new float[this.length];
			for (int i = 0; i < value.length; i++) {
				Vector3 vector = value[i];
				if (vector != null) {
					this.value[3 * i] = vector.x;
					this.value[3 * i + 1] = vector.y;
					this.value[3 * i + 2] = vector.z;
				}
			}
			valueChanged();
		} else if (value.length * 3 <= this.length) {

			boolean changed = false; // Array equality check

			for (int i = 0; i < value.length; i++) {
				Vector3 vector = value[i];

				if (vector == null) {

					// If null, say we don't change a thing

				} else {

					float x = vector.x;
					float y = vector.y;
					float z = vector.z;

					int index = 3 * i;
					if (x != this.value[index]) {
						this.value[index] = x;
						changed = true;
					}

					index += 1;
					if (y != this.value[index]) {
						this.value[index] = y;
						changed = true;
					}

					index += 1;
					if (z != this.value[index]) {
						this.value[index] = z;
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
		shader.setUniform3fv(name, value, 0, length);
	}

}
