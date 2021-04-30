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
package games.spooky.gdx.gfx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * The NameBufferPool manages a set of FrameBuffers indexed by name. If missing,
 * FrameBuffers are created with a given FrameBufferFactory. FrameBuffers can
 * also be released afterwards.
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

	public FrameBufferFactory getFactory() {
		return factory;
	}

	/**
	 * Gets the FrameBuffer indexed by given name. If it does not exist it will
	 * be created by this pool's FrameBufferFactory.
	 * 
	 * @param name
	 *            the FrameBuffer's name
	 * @return the FrameBuffer of given name
	 */
	public FrameBuffer get(String name) {
		FrameBuffer buffer = frameBuffers.get(name);
		if (buffer == null) {
			buffer = factory.createFrameBuffer();
			frameBuffers.put(name, buffer);
		}
		return buffer;
	}

	/**
	 * Gets the Texture from FrameBuffer indexed by given name. If it does not
	 * exist the FrameBuffer will be created by this pool's FrameBufferFactory.
	 * Texture returned is the color buffer texture from this FrameBuffer.
	 * 
	 * @param name
	 *            the FrameBuffer's name
	 * @return the color buffer Texture from the FrameBuffer of given name
	 */
	public Texture getTexture(String name) {
		return get(name).getColorBufferTexture();
	}

	/**
	 * Gets the FrameBuffer indexed by given name and immediately draws to it.
	 * 
	 * @param name
	 *            the FrameBuffer's name
	 * @param drawer
	 *            the drawer to write into the FrameBuffer.
	 * @return the FrameBuffer of given name
	 */
	public FrameBuffer draw(String name, Drawer drawer) {
		FrameBuffer buffer = get(name);
		drawer.draw(buffer);
		return buffer;
	}

	/**
	 * Gets the FrameBuffer indexed by given name and immediately draws to it.
	 * Provided Runnable will be run between calls to FrameBuffer's begin() and
	 * end() methods.
	 * 
	 * @param name
	 *            the FrameBuffer's name
	 * @param drawer
	 *            the drawer to write into the FrameBuffer.
	 * @return the FrameBuffer of given name
	 */
	public FrameBuffer draw(String name, Runnable drawer) {
		FrameBuffer buffer = get(name);
		buffer.begin();
		drawer.run();
		buffer.end();
		return buffer;
	}

	/**
	 * Releases the FrameBuffer indexed by given name. It will be removed from
	 * this pool then disposed.
	 * 
	 * @param name
	 *            the FrameBuffer's name
	 */
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
