uniform mat4 uWorldMatrix;
uniform mat4 uViewMatrix;
uniform mat4 uProjectionMatrix;
uniform vec3 uLightDir;
uniform vec3 uEyePos;
uniform vec4 uAmbient;
uniform vec4 uDiffuse;
uniform vec4 uSpecular;
uniform float uShininess;
uniform float uTime;
attribute vec3 aPosition;
attribute vec3 aNormal;
varying vec4 vColor;
varying vec3 vPos;
void main() {
	float t = uTime;
	vec3 dpos = aPosition;
	mat4 wvp = (uProjectionMatrix*uViewMatrix)*uWorldMatrix;
	vPos = vec3(uWorldMatrix * vec4(dpos,1.0));  
	vec3 N = normalize(mat3(uWorldMatrix) * aNormal);
	vec3 L = (uLightDir);
	vec3 R = normalize(reflect(L,N));
	vec3 D = normalize(uEyePos - vPos);
	vColor = uAmbient + abs(dot(N,-L)) * uDiffuse + pow(clamp(dot(R,D),0.0,1.0),uShininess) * uSpecular;
	vColor.a = 1.0;
	gl_Position = wvp * vec4(dpos,1.0);
}