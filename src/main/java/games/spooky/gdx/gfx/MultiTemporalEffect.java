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

import com.badlogic.gdx.utils.Array;

/**
 * A convenience class to manage multiple TemporalEffects. The TemporalEffects
 * added are applied simultaneously.
 */
public class MultiTemporalEffect implements TemporalEffect {

	private final Array<TemporalEffect> effects = new Array<TemporalEffect>();

	public MultiTemporalEffect() {
		super();
	}

	public void addEffect(TemporalEffect effect) {
		effects.add(effect);
	}

	public void removeEffect(TemporalEffect effect) {
		effects.removeValue(effect, true);
	}

	public void clearEffects() {
		effects.clear();
	}

	public boolean hasEffects() {
		return effects.size > 0;
	}

	@Override
	public boolean update(float deltaTime) {
		boolean allEnded = true;
		for (int i = effects.size - 1; i >= 0; i--) {
			TemporalEffect effect = effects.get(i);
			boolean ended = effect.update(deltaTime);
			if (ended) {
				removeEffect(effect);
				effect.reset();
			}
			allEnded &= ended;
		}
		return allEnded;
	}

	@Override
	public void reset() {
		for (TemporalEffect effect : effects)
			effect.reset();
	}

}
