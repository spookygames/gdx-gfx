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
package net.spookygames.gdx.gfx.demo.fx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;

import net.spookygames.gdx.gfx.TemporalEffect;

/**
 * @see http://jonny.morrill.me/blog/view/14
 */
public class ScreenShake implements TemporalEffect {

	private final float duration;
	private final float amplitude;

	private final ShakeSamples samplesX;
	private final ShakeSamples samplesY;

	private final Camera camera;

	private float time;

	private float totalDeltaX;
	private float totalDeltaY;

	/**
	 * 
	 * @param camera
	 * @param duration
	 *            Length of the shake in seconds
	 * @param frequency
	 *            Frequency of the shake in hertz
	 * @param amplitude
	 */
	public ScreenShake(Camera camera, float duration, float frequency, float amplitude) {
		super();
		this.camera = camera;
		this.duration = duration;
		this.amplitude = amplitude;

		this.samplesX = new ShakeSamples(duration, frequency, amplitude);
		this.samplesY = new ShakeSamples(duration, frequency, amplitude);
	}

	@Override
	public void reset() {
		time = 0f;
		totalDeltaX = totalDeltaY = 0f;
	}

	@Override
	public boolean update(float deltaTime) {
		time += deltaTime;

		if (time > duration) {
			camera.translate(-totalDeltaX, -totalDeltaY, 0);
			camera.update();
			return true;
		}

		float deltaX = samplesX.amplitude(time);
		float deltaY = samplesY.amplitude(time);
		
		float newTotalDeltaX = totalDeltaX + deltaX;
		float amplitudeLeftX = amplitude * (duration - time);
		if(newTotalDeltaX > amplitudeLeftX) {
			deltaX = totalDeltaX - amplitudeLeftX;
			newTotalDeltaX = totalDeltaX + deltaX;
		}
		totalDeltaX = newTotalDeltaX;

		camera.translate(deltaX, deltaY, 0);
		camera.update();

		return false;
	}

	private static class ShakeSamples {

		// The duration in seconds
		private final float duration;

		// The frequency in Hz
		private final float frequency;

		private final float[] samples;

		public ShakeSamples(float duration, float frequency, float amplitude) {
			super();
			this.duration = duration;
			this.frequency = frequency;

			// sample count = number of peaks/valleys in the shake
			int sampleCount = (int) (duration * frequency);

			// Populate the samples array with randomized values between
			// -amplitude and +amplitude
			samples = new float[sampleCount];
			for (int i = 0; i < sampleCount; i++)
				samples[i] = MathUtils.random(-amplitude, amplitude);;
		}

		float amplitude(float time) {

			// Get the previous and next sample
			float s = time * frequency;
			int s0 = MathUtils.floor(s);
			int s1 = s0 + 1;

			// Get the current decay
			float k = decay(time);

			// Return the current amplitude
			return (noise(s0) + (s - s0) * (noise(s1) - noise(s0))) * k;

		};

		private float noise(int sample) {
			// Retrieve the randomized value from the samples
			if (sample >= samples.length)
				return 0;

			return samples[sample];
		};

		private float decay(float time) {
			// Linear decay
			if (time >= duration)
				return 0f;

			return (duration - time) / duration;
		};
	}
}
