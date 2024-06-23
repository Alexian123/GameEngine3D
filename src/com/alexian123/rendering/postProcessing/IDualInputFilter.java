package com.alexian123.rendering.postProcessing;

import com.alexian123.util.gl.TextureSampler;

public interface IDualInputFilter {

	TextureSampler run(TextureSampler sampler1, TextureSampler sampler2);
	
	void cleanup();
}
