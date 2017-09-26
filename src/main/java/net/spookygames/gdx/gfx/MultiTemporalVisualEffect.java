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
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;

/**
 * The MultiTemporalVisualEffect combines the aspects of both
 * MultiTemporalEffect and MultiVisualEffect. All in one! Bargain!
 */
public class MultiTemporalVisualEffect extends MultiTemporalEffect implements VisualEffect, Disposable {

	private final MultiVisualEffect visual;

	public MultiTemporalVisualEffect(Format format, boolean useDepth) {
		super();
		visual = new MultiVisualEffect(format, useDepth);
	}

	public MultiTemporalVisualEffect(Format format, int bufferWidth, int bufferHeight, boolean useDepth) {
		super();
		visual = new MultiVisualEffect(format, bufferWidth, bufferHeight, useDepth);
	}

	@Override
	public void addEffect(TemporalEffect effect) {
		addEffect((Effect) effect);
	}

	public void addEffect(VisualEffect effect) {
		addEffect((Effect) effect);
	}

	public void addEffect(Effect effect) {
		if (effect instanceof TemporalEffect)
			super.addEffect((TemporalEffect) effect);
		if (effect instanceof VisualEffect)
			visual.addEffect((VisualEffect) effect);
	}

	@Override
	public void removeEffect(TemporalEffect effect) {
		removeEffect((Effect) effect);
	}

	public void removeEffect(VisualEffect effect) {
		removeEffect((Effect) effect);
	}

	public void removeEffect(Effect effect) {
		if (effect instanceof TemporalEffect)
			super.removeEffect((TemporalEffect) effect);
		if (effect instanceof VisualEffect)
			visual.removeEffect((VisualEffect) effect);
	}

	@Override
	public void clearEffects() {
		super.clearEffects();
		visual.clearEffects();
	}

	@Override
	public boolean hasEffects() {
		return super.hasEffects() || visual.hasEffects();
	}

	@Override
	public void rebind() {
		visual.rebind();
	}

	public void render(FrameBuffer source, FrameBuffer destination) {
		visual.render(source, destination);
	}

	@Override
	public void render(Texture source, FrameBuffer resultBuffer) {
		visual.render(source, resultBuffer);
	}

	@Override
	public void dispose() {
		visual.dispose();
	}

	public BouncingBuffer getCombinedBuffer() {
		return visual.getBuffer();
	}

	public void capture() {
		visual.capture();
	}

	public Texture endCapture() {
		return visual.endCapture();
	}

}
