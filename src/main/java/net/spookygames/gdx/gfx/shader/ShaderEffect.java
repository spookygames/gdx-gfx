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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Mesh.VertexDataType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import net.spookygames.gdx.gfx.VisualEffect;
import net.spookygames.gdx.gfx.shader.ShaderParameter.ShaderParameterValueChangedListener;

public abstract class ShaderEffect implements VisualEffect, Disposable {

	protected static final int u_texture0 = 0;
	protected static final int u_texture1 = 1;
	protected static final int u_texture2 = 2;
	protected static final int u_texture3 = 3;

	// Mesh with *very* basic reference counting
	protected static int meshRefCount = 0;
	protected static Mesh mesh = null;

	protected final ShaderProgram program;
	private final Array<ShaderParameter> parameters = new Array<ShaderParameter>();
	private final ShaderParameterValueChangedListener listener = new ShaderParameterValueChangedListener() {
		@Override
		public void onValueChanged(ShaderParameter parameter) {
			rebindParameter(parameter);
		}
	};

	/** Does NOT own shader, so won't dispose it! */
	public ShaderEffect(ShaderProgram program) {
		this.program = program;
		
		if(meshRefCount++ <= 0) {
			mesh = new Mesh(VertexDataType.VertexArray, true, 4, 0,
					new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE),
//					new VertexAttribute(Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE),
					new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
				float[] verts = {
						//	vertex		texture
							-1, -1,		0f, 0f,
							 1, -1,		1f, 0f,
							 1,  1,		1f, 1f,
							-1,  1,		0f, 1f,
				};
				mesh.setVertices(verts);
		}
	}
	
	@Override
	public void dispose() {
		if(--meshRefCount <= 0) {
			mesh.dispose();
			mesh = null;
		}
	}
	
	public void render(FrameBuffer source, FrameBuffer destination) {
		render(source.getColorBufferTexture(), destination);
	}

	@Override
	public void render(Texture source, FrameBuffer destination) {
		if (destination == null) {
			actualRender(source);
		} else {
			destination.begin();
			actualRender(source);
			destination.end();
		}
	}

	protected void actualRender(Texture source) {
		program.begin();
		mesh.render(program, GL20.GL_TRIANGLE_FAN, 0, 4);
		program.end();
	}
	
	@Override
	public void rebind() {
		program.begin();
		for(ShaderParameter parameter : parameters)
			parameter.apply(program);
		program.end();
	}
	
	protected void rebindParameter(ShaderParameter parameter) {
		program.begin();
		parameter.apply(program);
		program.end();
	}

	protected IntShaderParameter registerParameter(String name, int initialValue) {
		return registerParameter(new IntShaderParameter(name).setValue(initialValue));
	}

	protected FloatShaderParameter registerParameter(String name, float initialValue) {
		return registerParameter(new FloatShaderParameter(name).setValue(initialValue));
	}

	protected FloatArrayShaderParameter registerParameter(String name, float[] initialValue) {
		return registerParameter(new FloatArrayShaderParameter(name).setValue(initialValue));
	}

	protected Vector2ShaderParameter registerParameter(String name, Vector2 initialValue) {
		return registerParameter(new Vector2ShaderParameter(name).setValue(initialValue));
	}

	protected Vector2ShaderParameter registerParameter(String name, float initialX, float initialY) {
		return registerParameter(new Vector2ShaderParameter(name).setValue(initialX, initialY));
	}

	protected Vector2ArrayShaderParameter registerParameter(String name, Vector2[] initialValue) {
		return registerParameter(new Vector2ArrayShaderParameter(name).setValue(initialValue));
	}

	protected Vector3ShaderParameter registerParameter(String name, Vector3 initialValue) {
		return registerParameter(new Vector3ShaderParameter(name).setValue(initialValue));
	}

	protected Vector3ArrayShaderParameter registerParameter(String name, Vector3[] initialValue) {
		return registerParameter(new Vector3ArrayShaderParameter(name).setValue(initialValue));
	}

	protected ColorShaderParameter registerParameter(String name, Color initialValue) {
		return registerParameter(new ColorShaderParameter(name).setValue(initialValue));
	}

	protected Matrix3ShaderParameter registerParameter(String name, Matrix3 initialValue) {
		return registerParameter(new Matrix3ShaderParameter(name).setValue(initialValue));
	}

	protected Matrix4ShaderParameter registerParameter(String name, Matrix4 initialValue) {
		return registerParameter(new Matrix4ShaderParameter(name).setValue(initialValue));
	}
	
	protected <T extends ShaderParameter> T registerParameter(T parameter) {
		parameters.add(parameter);
		rebindParameter(parameter);
		parameter.listener = this.listener;
		return parameter;
	}
}
