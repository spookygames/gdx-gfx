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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class FrameBufferFactory {

	private int width;
	private int height;
	private boolean use32Bits;
	private boolean alphaChannel;
	private boolean depth;
	private Pixmap.Format format;

	public FrameBufferFactory() {
		super();
		Graphics gfx = Gdx.graphics;
		width = gfx.getWidth();
		height = gfx.getHeight();
		use32Bits = true;
		alphaChannel = true;
		depth = true;
		format = null;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean uses32Bits() {
		return use32Bits;
	}

	public void set32Bits(boolean use32Bits) {
		this.format = null;
		this.use32Bits = use32Bits;
	}

	public boolean hasAlphaChannel() {
		return alphaChannel;
	}

	public void setAlphaChannel(boolean alphaChannel) {
		this.format = null;
		this.alphaChannel = alphaChannel;
	}

	public boolean hasDepth() {
		return depth;
	}

	public void setDepth(boolean depth) {
		this.depth = depth;
	}

	/**
	 * Get the Pixmap.Format from this factory. If not yet defined, it will be
	 * computed from the use32bits and alphaChannel properties.
	 * 
	 * @return the Pixmap.Format from this factory
	 */
	public Pixmap.Format getFormat() {
		if (format == null)
			format = defineFormat(use32Bits, alphaChannel);
		return format;
	}

	public FrameBuffer createFrameBuffer() {
		return new FrameBuffer(getFormat(), width, height, depth);
	}

	public BouncingBuffer createBouncingBuffer() {
		return new BouncingBuffer(getFormat(), width, height, depth);
	}

	private static Format defineFormat(boolean use32Bits, boolean alphaChannel) {
		return use32Bits ? alphaChannel ? Format.RGBA8888 : Format.RGB888
				: alphaChannel ? Format.RGBA4444 : Format.RGB565;
	}

}
