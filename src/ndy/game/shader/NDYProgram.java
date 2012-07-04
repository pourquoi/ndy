package ndy.game.shader;

import ndy.game.NDYGLSurfaceView;
import ndy.game.NDYRessource;
import android.opengl.GLES20;
import android.util.Log;

public class NDYProgram extends NDYRessource {
	private static String TAG = "NDYProgram";
	
	protected String mVertexSource;
	protected String mFragmentSource;
	
	public int mPositionHandle;
	
	public static NDYProgram factory(String programName) {
		if(programName.contains("water")) {
			return new NDYProgramWater(programName);
		} else {
			return new NDYProgramBasic(programName);
		}
	}
	
	public NDYProgram(String name) {
		super(name);
	}
	
	@Override
	public boolean load() {
		if( !super.load() ) return false;

		mId = createProgram();
		if( mId == 0 ) {
			mId = -1;
			return false;
		}
		
		bindAttribs();
		return true;
	}
	
	protected void bindAttribs() {
		mPositionHandle = GLES20.glGetAttribLocation(mId, "aPosition");
		NDYGLSurfaceView.checkGLError("glGetAttribLocation aPosition");
        if (mPositionHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }
	}

	protected int loadShader(int shaderType, String source) {
		int shader = GLES20.glCreateShader(shaderType);
		if( shader != 0 ) {
			GLES20.glShaderSource(shader, source);
			GLES20.glCompileShader(shader);
			int [] compiled = new int[1];
			GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
			if( compiled[0] == 0 ) {
				Log.e(TAG, "Could not compile shader " + shaderType + ":");
				Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
				GLES20.glDeleteShader(shader);
				shader = 0;
			}
		}
		return shader;
	}
	
	protected void initShaderSource() {
		
	}
	
	protected int createProgram() {
		initShaderSource();

		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, mVertexSource);
		if( vertexShader == 0 ) {
			return 0;
		}
		
		int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, mFragmentSource);
		if( pixelShader == 0 ) {
			return 0;
		}
		
		int program = GLES20.glCreateProgram();
		if( program != 0 ) {
			GLES20.glAttachShader(program, vertexShader);
			NDYGLSurfaceView.checkGLError("glAttachShader");
			GLES20.glAttachShader(program, pixelShader);
			NDYGLSurfaceView.checkGLError("glAttachShader");
			GLES20.glLinkProgram(program);
			int [] linkStatus = new int[1];
			GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
			if( linkStatus[0] != GLES20.GL_TRUE ) {
				Log.e(TAG, "Could not link program: ");
				Log.e(TAG, GLES20.glGetProgramInfoLog(program));
				GLES20.glDeleteProgram(program);
				program = 0;
			}
		}
		return program;
	}
}
