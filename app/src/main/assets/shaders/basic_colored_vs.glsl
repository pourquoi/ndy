uniform mat4 uWorldMatrix;
uniform mat4 uViewMatrix;
uniform mat4 uProjectionMatrix;
attribute vec3 aPosition;
attribute vec4 aColor;
varying vec3 vPos;
varying vec4 vColor;
void main() {
	vec3 dpos = aPosition;
	mat4 wvp = (uProjectionMatrix*uViewMatrix)*uWorldMatrix;
	vPos = vec3(uWorldMatrix * vec4(dpos,1.0)); 
	vColor = aColor;
	gl_Position = wvp * vec4(dpos,1.0);
}