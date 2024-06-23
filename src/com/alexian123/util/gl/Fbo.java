package com.alexian123.util.gl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.alexian123.util.Constants;
import com.alexian123.util.enums.DepthBufferType;

public class Fbo {
	
	private static final List<Fbo> FBOS = new ArrayList<>();
	
	private static final int MAX_COLOR_BUFFERS = 2;
	
	private final int width;
	private final int height;
	
	private final boolean textureTiling;

	private int frameBuffer;
	private boolean multisampled = false;
	private boolean multitarget = false;

	private TextureSampler colorTexture;
	private TextureSampler depthTexture;

	private int depthBuffer;
	private int[] colorBuffer = new int[MAX_COLOR_BUFFERS];

	/**
	 * Creates an FBO of a specified width and height, with the desired type of
	 * depth buffer attachment.
	 * 
	 * @param width
	 *            - the width of the FBO.
	 * @param height
	 *            - the height of the FBO.
	 * @param depthBufferType
	 *            - an integer indicating the type of depth buffer attachment that
	 *            this FBO should use.
	 * @param textureTiling
	 *            - if true, tile the texture if the coordinates exceed the dimension;
	 *            - if false, stretch the edge pixels to fill the extra space
	 */
	public Fbo(int width, int height, DepthBufferType type, boolean textureTiling) {
		this.width = width;
		this.height = height;
		this.textureTiling = textureTiling;
		initialiseFrameBuffer(type);
		FBOS.add(this);
	}
	
	public Fbo(int width, int height, boolean textureTiling, boolean multitarget) {
		this.width = width;
		this.height = height;
		this.textureTiling = textureTiling;
		this.multisampled = true;
		this.multitarget = multitarget;
		initialiseFrameBuffer(DepthBufferType.DEPTH_RENDER_BUFFER);
		FBOS.add(this);
	}

	/**
	 * Deletes the frame buffer and its attachments when the game closes.
	 */
	public void delete() {
		if (colorTexture != null) {
			colorTexture.delete();
		}
		if (depthTexture != null) {
			depthTexture.delete();
		}
		GL30.glDeleteFramebuffers(frameBuffer);
		GL30.glDeleteRenderbuffers(depthBuffer);
		GL30.glDeleteRenderbuffers(colorBuffer[0]);
		if (multitarget) {
			GL30.glDeleteRenderbuffers(colorBuffer[1]);
		}
	}

	/**
	 * Binds the frame buffer, setting it as the current render target. Anything
	 * rendered after this will be rendered to this FBO, and not to the screen.
	 */
	public void bindFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, width, height);
	}

	/**
	 * Unbinds the frame buffer, setting the default frame buffer as the current
	 * render target. Anything rendered after this will be rendered to the
	 * screen, and not this FBO.
	 */
	public void unbindFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	/**
	 * Binds the current FBO to be read from.
	 */
	public void bindToRead() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, frameBuffer);
		GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
	}

	/**
	 * @return The ID of the texture containing the colour buffer of the FBO.
	 */
	public TextureSampler getColorTexture() {
		return colorTexture;
	}

	/**
	 * @return The texture containing the FBOs depth buffer.
	 */
	public TextureSampler getDepthTexture() {
		return depthTexture;
	}
	
	public void resolveToFbo(int readBuffer, Fbo output) {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, output.frameBuffer);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.frameBuffer);
		GL11.glReadBuffer(readBuffer);
		GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, output.width, output.height,
				GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
		unbindFrameBuffer();
	}
	
	public void resolveAttachmentToFbo(int attachment, Fbo output) {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, output.frameBuffer);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.frameBuffer);
		GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0 + attachment);
		GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, output.width, output.height,
				GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
		unbindFrameBuffer();
	}
	
	public void resolveToScreen() {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.frameBuffer);
		GL11.glDrawBuffer(GL11.GL_BACK);
		GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, Display.getWidth(), Display.getHeight(),
				GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
		unbindFrameBuffer();
	}

	/**
	 * Creates the FBO along with a colour buffer texture attachment, and
	 * possibly a depth buffer.
	 * 
	 * @param type
	 *            - the type of depth buffer attachment to be attached to the
	 *            FBO.
	 */
	private void initialiseFrameBuffer(DepthBufferType type) {
		createFrameBuffer();
		if (multisampled) {
			colorBuffer[0] = createMultisampleColorAttachment(GL30.GL_COLOR_ATTACHMENT0);
			if (multitarget) {
				colorBuffer[1] = createMultisampleColorAttachment(GL30.GL_COLOR_ATTACHMENT1);
			}
		} else {
			createTextureAttachment();
		}
		switch (type) {
			case DEPTH_RENDER_BUFFER:
				createDepthBufferAttachment();
				break;
			case DEPTH_TEXTURE:
				createDepthTextureAttachment();
				break;
			case NONE:
			default:
				break;
		}
		unbindFrameBuffer();
	}

	/**
	 * Creates a new frame buffer object and sets the buffer to which drawing
	 * will occur - colour attachment 0. This is the attachment where the colour
	 * buffer texture is.
	 * 
	 */
	private void createFrameBuffer() {
		frameBuffer = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		determineDrawBuffers();
	}
	
	private void determineDrawBuffers() {
		IntBuffer drawBuffers = BufferUtils.createIntBuffer(MAX_COLOR_BUFFERS);
		drawBuffers.put(GL30.GL_COLOR_ATTACHMENT0);
		if (multitarget) {
			drawBuffers.put(GL30.GL_COLOR_ATTACHMENT1);
		}
		drawBuffers.flip();
		GL20.glDrawBuffers(drawBuffers);
	}

	/**
	 * Creates a texture and sets it as the colour buffer attachment for this
	 * FBO.
	 */
	private void createTextureAttachment() {
		colorTexture = new TextureSampler(GL11.GL_TEXTURE_2D, width, height)
				.withLinearMinMagFilter();
		if (!textureTiling) {
			colorTexture.withClampToEdgeWrapping();	
		}
		colorTexture.bind();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				(ByteBuffer) null);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colorTexture.getID(), 0);
	}

	/**
	 * Adds a depth buffer to the FBO in the form of a texture, which can later
	 * be sampled.
	 */
	private void createDepthTextureAttachment() {
		depthTexture = new TextureSampler(GL11.GL_TEXTURE_2D, width, height);
		depthTexture.withLinearMinMagFilter();
		if (!textureTiling) {
			depthTexture.withClampToEdgeWrapping();
		}
		depthTexture.bind();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, width, height, 0, GL11.GL_DEPTH_COMPONENT,
				GL11.GL_FLOAT, (ByteBuffer) null);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture.getID(), 0);
	}

	/**
	 * Adds a depth buffer to the FBO in the form of a render buffer. This can't
	 * be used for sampling in the shaders.
	 */
	private void createDepthBufferAttachment() {
		depthBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		if (multisampled) {
			GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, Constants.NUM_MULTISAMPLES, GL14.GL_DEPTH_COMPONENT24, width, height);	
		} else {
			GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width, height);	
		}
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER,
				depthBuffer);
	}
	
	private int createMultisampleColorAttachment(int attachment) {
		int colorBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, colorBuffer);
		GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, Constants.NUM_MULTISAMPLES, GL11.GL_RGBA8, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment, GL30.GL_RENDERBUFFER, colorBuffer);
		return colorBuffer;
	}
	
	public static void cleanup() {
		for (Fbo fbo : FBOS) {
			fbo.delete();
		}
		FBOS.clear();
	}

}