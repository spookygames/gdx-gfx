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
package games.spooky.gdx.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * A convenience class to chain multiple VisualEffects. Uses a BouncingBuffer
 * for this purpose.
 */
public final class MultiVisualEffect implements VisualEffect, Disposable {

	private final BouncingBuffer buffer;
	private final Array<VisualEffect> effects = new Array<VisualEffect>();

	private Copy copy = null;

	public MultiVisualEffect(Format format, boolean useDepth) {
		this(format, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), useDepth);
	}

	public MultiVisualEffect(Format format, int bufferWidth, int bufferHeight, boolean useDepth) {
		buffer = new BouncingBuffer(format, bufferWidth, bufferHeight, useDepth);
	}

	public BouncingBuffer getBuffer() {
		return buffer;
	}

	public void addEffect(VisualEffect effect) {
		effects.add(effect);
	}

	public void removeEffect(VisualEffect effect) {
		effects.removeValue(effect, true);
	}

	public void clearEffects() {
		effects.clear();
	}

	public boolean hasEffects() {
		return effects.size > 0;
	}

	@Override
	public void rebind() {
		buffer.rebind();

		for (VisualEffect e : effects)
			e.rebind();
	}

	/**
	 * Begins capture of inner BoundingBuffer.
	 */
	public void capture() {
		buffer.begin();
	}

	/**
	 * Ends capture of inner BoundingBuffer.
	 *
	 * @return the texture generated from the capture
	 */
	public Texture endCapture() {
		buffer.end();
		return buffer.getResultTexture();
	}

	public void render(FrameBuffer source, FrameBuffer destination) {
		render(source.getColorBufferTexture(), destination);
	}

	@Override
	public void render(Texture source, FrameBuffer destination) {

		switch (effects.size) {
		case 0:
			// No effect: make some simple copy
			if (copy == null)
				copy = new Copy();
			copy.render(source, destination);
			break;
		case 1:
			effects.first().render(source, destination);
			break;
		default:
			effects.first().render(source, buffer.getCurrentBuffer());
			for (int i = 1; i < effects.size - 1; i++)
				effects.get(i).render(buffer.getResultTexture(), buffer.getCurrentBuffer());
			effects.peek().render(buffer.getResultTexture(), destination);
			break;
		}
	}

	@Override
	public void dispose() {
		buffer.dispose();
		if (copy != null)
			copy.dispose();
	}

}
