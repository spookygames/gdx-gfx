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

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import net.spookygames.gdx.gfx.CommonShaders;
import net.spookygames.gdx.gfx.shader.SinglePassShaderEffect;

public class Greyscale extends SinglePassShaderEffect {

	/**
#ifdef GL_ES
#define PRECISION mediump
precision PRECISION float;
#else
#define PRECISION
#endif

varying PRECISION vec2 v_texCoords;
uniform sampler2D u_texture0;

void main()
{
// Greyscale coeffs: 0.2989, 0.5870, 0.1140
// http://stackoverflow.com/questions/687261/converting-rgb-to-grayscale-intensity

vec4 c = texture2D(u_texture0, v_texCoords);
float value = c.r * 0.2989 + c.g * 0.5870 + c.b * 0.1140;
gl_FragColor.r = value;
gl_FragColor.g = value;
gl_FragColor.b = value;
gl_FragColor.a = c.a;
}
	*/
	@Multiline static String Greyscale;
	
	public Greyscale() {
		super(new ShaderProgram(CommonShaders.Screenspace, Greyscale));
	}
	
	@Override
	public void dispose() {
		super.dispose();
		program.dispose();
	}
}
