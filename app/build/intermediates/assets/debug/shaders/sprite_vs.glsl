uniform mat4 uWorldMatrix;
uniform mat4 uViewMatrix;
uniform mat4 uProjectionMatrix;
attribute vec3 aPosition;
attribute vec2 aTextureCoord;
varying vec2 vTextureCoord;
varying vec3 vPos;
void main() {
	vec3 dpos = aPosition;
	mat4 wvp = (uProjectionMatrix*uViewMatrix)*uWorldMatrix;
	vPos = vec3(uWorldMatrix * vec4(dpos,1.0)); 
	vTextureCoord = aTextureCoord;
	gl_Position = wvp * vec4(dpos,1.0);
}