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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * An effect which performs a single rendering pass with the ShaderProgram
 * provided. It is NOT responsible for the creation and destruction of the
 * ShaderProgram.
 */
public class SinglePassShaderEffect extends ShaderEffect {

	/**
	 * Instantiates a new SinglePassShaderEffect. The effect does NOT own
	 * shader, so it will not dispose it!
	 *
	 * @param program
	 *            the ShaderProgram to apply
	 */
	public SinglePassShaderEffect(ShaderProgram program) {
		super(program);
		if (!program.isCompiled())
			System.err.println(program.getLog());
		registerParameter("u_texture0", u_texture0);
	}

	@Override
	protected void actualRender(Texture source) {
		source.bind(u_texture0);
		super.actualRender(source);
	}
}
