package com.alexian123.rendering.postProcessing;

import com.alexian123.util.gl.TextureSampler;

public interface ISingleInputFilter {

	TextureSampler run(TextureSampler sampler);
	
	void cleanup();
}
