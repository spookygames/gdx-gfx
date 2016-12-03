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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * 
 */
public class NamedBufferPool implements Disposable {

	private final FrameBufferFactory factory;
	private final ObjectMap<String, FrameBuffer> frameBuffers = new ObjectMap<String, FrameBuffer>();

	public NamedBufferPool() {
		this(new FrameBufferFactory());
	}

	public NamedBufferPool(FrameBufferFactory factory) {
		super();
		this.factory = factory;
	}

	public FrameBuffer get(String name) {
		FrameBuffer buffer = frameBuffers.get(name);
		if (buffer == null) {
			buffer = factory.createFrameBuffer();
			frameBuffers.put(name, buffer);
		}
		return buffer;
	}

	public Texture getTexture(String name) {
		return get(name).getColorBufferTexture();
	}

	public FrameBuffer draw(String name, Drawer drawer) {
		FrameBuffer buffer = get(name);
		drawer.draw(buffer);
		return buffer;
	}

	public FrameBuffer draw(String name, Runnable drawer) {
		FrameBuffer buffer = get(name);
		buffer.begin();
		drawer.run();
		buffer.end();
		return buffer;
	}

	public void free(String name) {
		FrameBuffer fb = frameBuffers.remove(name);
		if (fb != null)
			fb.dispose();
	}

	@Override
	public void dispose() {
		for (FrameBuffer fb : frameBuffers.values())
			fb.dispose();
		frameBuffers.clear();
	}
	
	public interface Drawer {
		void draw(FrameBuffer buffer);
	}

}
