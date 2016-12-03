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
package net.spookygames.gdx.gfx;

import org.adrianwalker.multilinestring.Multiline;

public class CommonShaders {
	
	/**
#ifdef GL_ES
	#define PRECISION mediump
	precision PRECISION float;
#else
	#define PRECISION
#endif

attribute vec4 a_position;
attribute vec2 a_texCoord0;
varying vec2 v_texCoords;

void main()
{
	v_texCoords = a_texCoord0;
	gl_Position = a_position;
}
	*/
	@Multiline public static String Screenspace;

	/**
#ifdef GL_ES
	#define PRECISION mediump
	precision PRECISION float;
#else
	#define PRECISION
#endif

uniform sampler2D u_texture0;
varying vec2 v_texCoords;

void main(void)
{
	gl_FragColor = texture2D(u_texture0, v_texCoords);
}
	*/
	@Multiline public static String Copy;
}
