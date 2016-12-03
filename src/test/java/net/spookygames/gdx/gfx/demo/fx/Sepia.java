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
package net.spookygames.gdx.gfx.demo.fx;

import org.adrianwalker.multilinestring.Multiline;

import net.spookygames.gdx.gfx.CommonShaders;
import net.spookygames.gdx.gfx.shader.OwnedSinglePassShaderEffect;

public class Sepia extends OwnedSinglePassShaderEffect {

	/**
#ifdef GL_ES
precision lowp float;
#define MED mediump
#else
#define MED
#endif

varying MED vec2 v_texCoords;
uniform sampler2D u_texture0;

void main()
{
vec4 c = texture2D(u_texture0, v_texCoords);
gl_FragColor.r = (c.r * 0.393) + (c.g * 0.769) + (c.b * 0.189);
gl_FragColor.g = (c.r * 0.349) + (c.g * 0.686) + (c.b * 0.168);
gl_FragColor.b = (c.r * 0.272) + (c.g * 0.534) + (c.b * 0.131);
gl_FragColor.a = c.a;
}
	*/
	@Multiline static String Sepia;
	
	public Sepia() {
		super(CommonShaders.Screenspace, Sepia);
	}
}