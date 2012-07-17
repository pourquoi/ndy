package ndy.game.shader;

import ndy.game.NDYGLSurfaceView;
import ndy.game.actor.NDYActor;
import ndy.game.actor.NDYCamera;
import ndy.game.actor.NDYGame;
import ndy.game.component.NDYComponentMaterial;
import ndy.game.component.NDYRessource;
import ndy.game.material.NDYMaterial;
import ndy.game.math.NDYVector3;
import ndy.game.mesh.NDYMesh;
import ndy.game.mesh.NDYSubMesh;
import android.opengl.GLES20;

public class NDYProgramBasic extends NDYProgram {
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

	public NDYProgramBasic(String name) {
		super(name);
	}

	@Override
	protected void initShaderSource() {
		mVertexSource = NDYRessource.readTextFile(NDYGame.instance.mContext, mName + "_vs.glsl");
		mFragmentSource = NDYRessource.readTextFile(NDYGame.instance.mContext, mName + "_ps.glsl");
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
		NDYGame g = NDYGame.instance;

		if (mTimeHandle != -1) {
			float t = g.mTime / 1000.f;
			GLES20.glUniform1f(mTimeHandle, t);
			NDYGLSurfaceView.checkGLError("glUniform1f time");
		}

		NDYCamera c = g.mCamera;

		GLES20.glUniformMatrix4fv(mViewMatrixHandle, 1, false, c.mViewMatrix, 0);
		NDYGLSurfaceView.checkGLError("glUniformMatrix4fv view matrix");

		GLES20.glUniformMatrix4fv(mProjectionHandle, 1, false, c.mProjectionMatrix, 0);
		NDYGLSurfaceView.checkGLError("glUniformMatrix4fv projection matrix");

		if (mEyePosHandle != -1) {
			GLES20.glUniform3f(mEyePosHandle, c.mPos.x, c.mPos.y, c.mPos.z);
			NDYGLSurfaceView.checkGLError("glUniform3f eyepos");
		}

		if (mLightDirHandle != -1) {
			NDYVector3 ldir = g.mWeather.mLightDir;
			GLES20.glUniform3f(mLightDirHandle, ldir.x, ldir.y, ldir.z);
			NDYGLSurfaceView.checkGLError("glUniform3f lightdir");
		}
	}

	public void sendActorMaterials(NDYActor actor) {
		NDYMaterial material = null;
		for (int i = 0; i < NDYProgramBasic.MAX_TEXTURES; i++) {
			NDYComponentMaterial m = ((NDYComponentMaterial) actor.findComponent("material" + i));
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
						GLES20.glUniform1f(mShininessHandle, (float)material.shininess);
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
	
	public void sendSubmesh(NDYSubMesh submesh) {
		submesh.vbuffer.position(submesh.posOffset);
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, submesh.vsize, submesh.vbuffer);
		NDYGLSurfaceView.checkGLError("glVertexAttribPointer maPosition");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		NDYGLSurfaceView.checkGLError("glEnableVertexAttribArray maPositionHandle");

		if (submesh.getDesc() == NDYSubMesh.VERTEX_DESC_POSITION_TEXCOORDS || submesh.getDesc() == NDYSubMesh.VERTEX_DESC_POSITION_NORMAL_TEXCOORDS) {
			if (mTextureHandle != -1) {
				submesh.vbuffer.position(submesh.texcoordsOffset);
				GLES20.glVertexAttribPointer(mTextureHandle, 2, GLES20.GL_FLOAT, false, submesh.vsize, submesh.vbuffer);
				NDYGLSurfaceView.checkGLError("glVertexAttribPointer maTextureHandle");
				GLES20.glEnableVertexAttribArray(mTextureHandle);
				NDYGLSurfaceView.checkGLError("glEnableVertexAttribArray maTextureHandle");
			}
		}

		if (submesh.getDesc() == NDYSubMesh.VERTEX_DESC_POSITION_NORMAL || submesh.getDesc() == NDYSubMesh.VERTEX_DESC_POSITION_NORMAL_TEXCOORDS) {
			if (mNormalHandle != -1) {
				submesh.vbuffer.position(submesh.normalOffset);
				GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, false, submesh.vsize, submesh.vbuffer);
				GLES20.glEnableVertexAttribArray(mNormalHandle);
				NDYGLSurfaceView.checkGLError("glEnableVertexAttribArray mNormalHandle");
			}
		}

		if (submesh.getDesc() == NDYSubMesh.VERTEX_DESC_POSITION_COLOR) {
			if (mColorHandle != -1) {
				submesh.vbuffer.position(submesh.colorOffset);
				GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, submesh.vsize, submesh.vbuffer);
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
				GLES20.glUniform1f(mShininessHandle, (float)submesh.material.shininess);
				NDYGLSurfaceView.checkGLError("glUniform1f shininess");
			}
		}

		if (submesh.ibuffer != null) {
			GLES20.glDrawElements(submesh.drawMode, submesh.ibuffer.capacity(), GLES20.GL_UNSIGNED_SHORT, submesh.ibuffer);
			NDYGLSurfaceView.checkGLError("glDrawElements");
		} else {
			GLES20.glDrawArrays(submesh.drawMode, 0, submesh.vbuffer.capacity() * NDYMesh.FLOAT_SIZE_BYTES / submesh.vsize);
			NDYGLSurfaceView.checkGLError("glDrawArrays");
		}
	}
}
