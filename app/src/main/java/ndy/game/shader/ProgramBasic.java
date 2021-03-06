package ndy.game.shader;

import ndy.game.Game;
import ndy.game.NDYGLSurfaceView;
import ndy.game.Renderer;
import ndy.game.Ressource;
import ndy.game.actor.Actor;
import ndy.game.actor.GameWorld;
import ndy.game.component.CameraComponent;
import ndy.game.component.MaterialComponent;
import ndy.game.material.Material;
import ndy.game.math.Vec3;
import ndy.game.mesh.Mesh;
import ndy.game.mesh.SubMesh;
import android.opengl.GLES20;

public class ProgramBasic extends Program {
	private static String TAG = "NDYProgramBasic";

	public static int MAX_TEXTURES = 8;

	public int mTimeHandle;

	public int mTextureHandle;
	public int mNormalHandle;
	public int mColorHandle;

	public int[] mSamplerHandles;

	public int mWorldMatrixHandle;
	public int mViewMatrixHandle;
	public int mProjectionHandle;

	public int mLightDirHandle;
	public int mEyePosHandle;

	public int mAmbientHandle;
	public int mDiffuseHandle;
	public int mSpecularHandle;
	public int mShininessHandle;

	public ProgramBasic(String name) {
		super(name);
	}

	@Override
	protected void initShaderSource() {
		mVertexSource = Ressource.readTextFile(Game.instance.mContext, mName + "_vs.glsl");
		mFragmentSource = Ressource.readTextFile(Game.instance.mContext, mName + "_ps.glsl");
	}

	@Override
	protected void bindAttribs() {
		super.bindAttribs();

		mWorldMatrixHandle = GLES20.glGetUniformLocation(mId, "uWorldMatrix");
		NDYGLSurfaceView.checkGLError("glGetUniformLocation uWorldMatrix");
		if (mWorldMatrixHandle == -1) {
			throw new RuntimeException("Could not get attrib location for uWorldMatrix");
		}

		mViewMatrixHandle = GLES20.glGetUniformLocation(mId, "uViewMatrix");
		NDYGLSurfaceView.checkGLError("glGetUniformLocation uViewMatrix");
		if (mViewMatrixHandle == -1) {
			throw new RuntimeException("Could not get attrib location for uViewMatrix");
		}

		mProjectionHandle = GLES20.glGetUniformLocation(mId, "uProjectionMatrix");
		NDYGLSurfaceView.checkGLError("glGetUniformLocation uProjectionMatrix");
		if (mProjectionHandle == -1) {
			throw new RuntimeException("Could not get attrib location for uProjectionMatrix");
		}

		mSamplerHandles = new int[MAX_TEXTURES];

		for (int i = 0; i < MAX_TEXTURES; i++) {
			mSamplerHandles[i] = GLES20.glGetUniformLocation(mId, "sTexture" + i);
		}

		mColorHandle = GLES20.glGetAttribLocation(mId, "aColor");

		mTextureHandle = GLES20.glGetAttribLocation(mId, "aTextureCoord");

		mNormalHandle = GLES20.glGetAttribLocation(mId, "aNormal");

		mTimeHandle = GLES20.glGetUniformLocation(mId, "uTime");

		mLightDirHandle = GLES20.glGetUniformLocation(mId, "uLightDir");

		mEyePosHandle = GLES20.glGetUniformLocation(mId, "uEyePos");

		mShininessHandle = GLES20.glGetUniformLocation(mId, "uShininess");

		mDiffuseHandle = GLES20.glGetUniformLocation(mId, "uDiffuse");

		mAmbientHandle = GLES20.glGetUniformLocation(mId, "uAmbient");

		mSpecularHandle = GLES20.glGetUniformLocation(mId, "uSpecular");
	}

	public void sendGameParams() {
		Game g = Game.instance;
		GameWorld w = Game.instance.world;

		if (mTimeHandle != -1) {
			float t = g.time / 1000.f;
			GLES20.glUniform1f(mTimeHandle, t);
			NDYGLSurfaceView.checkGLError("glUniform1f time");
		}

		CameraComponent c = w.camera;

		GLES20.glUniformMatrix4fv(mViewMatrixHandle, 1, false, c.mViewMatrix, 0);
		NDYGLSurfaceView.checkGLError("glUniformMatrix4fv view matrix");

		GLES20.glUniformMatrix4fv(mProjectionHandle, 1, false, c.mProjectionMatrix, 0);
		NDYGLSurfaceView.checkGLError("glUniformMatrix4fv projection matrix");

		if (mEyePosHandle != -1) {
			GLES20.glUniform3f(mEyePosHandle, c.pos.x, c.pos.y, c.pos.z);
			NDYGLSurfaceView.checkGLError("glUniform3f eyepos");
		}

		if (mLightDirHandle != -1) {
			Vec3 ldir = g.world.weather.mLightDir;
			GLES20.glUniform3f(mLightDirHandle, ldir.x, ldir.y, ldir.z);
			NDYGLSurfaceView.checkGLError("glUniform3f lightdir");
		}
	}

	public void sendActorMaterials(Actor actor) {
		Material material = null;
		for (int i = 0; i < ProgramBasic.MAX_TEXTURES; i++) {
			MaterialComponent m = ((MaterialComponent) actor.findComponent("material" + i));
			if (m != null) {
				if (material == null) {
					material = m.material;

					if (mAmbientHandle != -1) {
						GLES20.glUniform4fv(mAmbientHandle, 1, material.ambient, 0);
						NDYGLSurfaceView.checkGLError("glUniform4fv ambient");
					}

					if (mDiffuseHandle != -1) {
						GLES20.glUniform4fv(mDiffuseHandle, 1, material.diffuse, 0);
						NDYGLSurfaceView.checkGLError("glUniform4fv diffuse");
					}

					if (mSpecularHandle != -1) {
						GLES20.glUniform4fv(mSpecularHandle, 1, material.specular, 0);
						NDYGLSurfaceView.checkGLError("glUniform4fv specular");
					}

					if (mShininessHandle != -1) {
						GLES20.glUniform1f(mShininessHandle, (float) material.shininess);
						NDYGLSurfaceView.checkGLError("glUniform1f shininess");
					}
				}
				if (m.material.texture != null) {
					GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + i);
					GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m.material.texture.getId());
					if (mSamplerHandles[i] != -1)
						GLES20.glUniform1i(mSamplerHandles[i], i);
				}
			} else {
				break;
			}
		}
	}

	public void sendSubmesh(SubMesh submesh) {
		submesh.vbuffer.position(submesh.posOffset);

		if (submesh.vbo != -1)
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, submesh.vbo);

		if (submesh.vbo != -1)
			GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, submesh.vsize, 0);
		else
			GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, submesh.vsize, submesh.vbuffer);
		NDYGLSurfaceView.checkGLError("glVertexAttribPointer maPosition");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		NDYGLSurfaceView.checkGLError("glEnableVertexAttribArray maPositionHandle");

		if (submesh.getDesc() == SubMesh.VERTEX_DESC_POSITION_TEXCOORDS || submesh.getDesc() == SubMesh.VERTEX_DESC_POSITION_NORMAL_TEXCOORDS) {
			if (mTextureHandle != -1) {
				if (submesh.vbo != -1) {
					GLES20.glVertexAttribPointer(mTextureHandle, 2, GLES20.GL_FLOAT, false, submesh.vsize, submesh.texcoordsOffset);
				} else {
					submesh.vbuffer.position(submesh.texcoordsOffset);
					GLES20.glVertexAttribPointer(mTextureHandle, 2, GLES20.GL_FLOAT, false, submesh.vsize, submesh.vbuffer);
				}
				NDYGLSurfaceView.checkGLError("glVertexAttribPointer maTextureHandle");
				GLES20.glEnableVertexAttribArray(mTextureHandle);
				NDYGLSurfaceView.checkGLError("glEnableVertexAttribArray maTextureHandle");
			}
		}

		if (submesh.getDesc() == SubMesh.VERTEX_DESC_POSITION_NORMAL || submesh.getDesc() == SubMesh.VERTEX_DESC_POSITION_NORMAL_TEXCOORDS) {
			if (mNormalHandle != -1) {
				if (submesh.vbo != -1) {
					GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, false, submesh.vsize, submesh.normalOffset);
				} else {
					submesh.vbuffer.position(submesh.normalOffset);
					GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, false, submesh.vsize, submesh.vbuffer);
				}
				GLES20.glEnableVertexAttribArray(mNormalHandle);
				NDYGLSurfaceView.checkGLError("glEnableVertexAttribArray mNormalHandle");
			}
		}

		if (submesh.getDesc() == SubMesh.VERTEX_DESC_POSITION_COLOR) {
			if (mColorHandle != -1) {
				if (submesh.vbo != 1) {
					GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, submesh.vsize, submesh.colorOffset);
				} else {
					submesh.vbuffer.position(submesh.colorOffset);
					GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, submesh.vsize, submesh.vbuffer);
				}
				GLES20.glEnableVertexAttribArray(mColorHandle);
				NDYGLSurfaceView.checkGLError("glEnableVertexAttribArray mColorHandle");
			}
		}

		if (submesh.material != null) {
			if (mAmbientHandle != -1) {
				GLES20.glUniform4fv(mAmbientHandle, 1, submesh.material.ambient, 0);
				NDYGLSurfaceView.checkGLError("glUniform4fv ambient");
			}

			if (mDiffuseHandle != -1) {
				GLES20.glUniform4fv(mDiffuseHandle, 1, submesh.material.diffuse, 0);
				NDYGLSurfaceView.checkGLError("glUniform4fv diffuse");
			}

			if (mSpecularHandle != -1) {
				GLES20.glUniform4fv(mSpecularHandle, 1, submesh.material.specular, 0);
				NDYGLSurfaceView.checkGLError("glUniform4fv specular");
			}

			if (mShininessHandle != -1) {
				GLES20.glUniform1f(mShininessHandle, (float) submesh.material.shininess);
				NDYGLSurfaceView.checkGLError("glUniform1f shininess");
			}
		}

		if (submesh.ibuffer != null) {
			GLES20.glDrawElements(submesh.drawMode, submesh.ibuffer.capacity(), GLES20.GL_UNSIGNED_SHORT, submesh.ibuffer);
			NDYGLSurfaceView.checkGLError("glDrawElements");
		} else {
			GLES20.glDrawArrays(submesh.drawMode, 0, submesh.vbuffer.capacity() * Mesh.FLOAT_SIZE_BYTES / submesh.vsize);
			NDYGLSurfaceView.checkGLError("glDrawArrays");
		}
	}
}
