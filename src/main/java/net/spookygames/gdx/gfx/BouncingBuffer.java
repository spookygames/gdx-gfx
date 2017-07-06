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
package net.spookygames.gdx.gfx;

import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public final class BouncingBuffer implements Disposable {

	public final int width;
	public final int height;

	private final FrameBuffer buffer1;
	private final FrameBuffer buffer2;

	private Texture texture1;
	private Texture texture2;

	private FrameBuffer current;
	
	private TextureWrap uWrap;
	private TextureWrap vWrap;
	
	private Rectangle viewport = null;

	public BouncingBuffer(Format format, int width, int height, boolean hasDepth) {
		buffer1 = new BouncyBuffer(format, width, height, hasDepth);
		buffer2 = new BouncyBuffer(format, width, height, hasDepth);
		current = buffer1;

		this.width = this.buffer1.getWidth();
		this.height = this.buffer1.getHeight();
		
		this.uWrap = TextureWrap.ClampToEdge;
		this.vWrap = TextureWrap.ClampToEdge;

		rebind();
	}

	public Rectangle getViewport() {
		return viewport;
	}

	public void setViewport(Rectangle viewport) {
		this.viewport = viewport;
	}

	public void rebind() {
		texture1 = buffer1.getColorBufferTexture();
		texture2 = buffer2.getColorBufferTexture();
		
		texture1.setWrap(uWrap, vWrap);
		texture2.setWrap(uWrap, vWrap);
	}

	public void setTextureWrap(TextureWrap u, TextureWrap v) {
		this.uWrap = u;
		this.vWrap = v;
		
		texture1.setWrap(u, v);
		texture2.setWrap(u, v);
	}

	public void begin() {
		current.begin();
	}

	public void end() {
		current.end();
	}

	public FrameBuffer getCurrentBuffer() {
		return current;
	}

	public Texture getCurrentTexture() {
		return current == buffer1 ? texture1 : texture2;
	}

	public FrameBuffer getResultBuffer() {
		return (current == buffer1) ? buffer2 : buffer1;
	}

	public Texture getResultTexture() {
		return current == buffer1 ? texture2 : texture1;
	}

	public FrameBuffer getOtherBuffer() {
		return (current == buffer1) ? buffer2 : buffer1;
	}

	public Texture getOtherTexture() {
		return current == buffer1 ? texture2 : texture1;
	}

	@Override
	public void dispose() {
		buffer1.dispose();
		buffer2.dispose();
	}

	private void bounce() {
		current = (current == buffer1) ? buffer2 : buffer1;
	}
	
	private class BouncyBuffer extends FrameBuffer {

		public BouncyBuffer(Format format, int width, int height, boolean hasDepth) {
			super(format, width, height, hasDepth);
		}

		@Override
		public void end() {
			if (viewport == null)
				super.end();
			else {
				super.end((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);
			}
			bounce();
		}
	}
}
