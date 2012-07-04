precision mediump float;
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

uniform vec4 uWaveParams1; // amplitude, phase, wavelength, sharpness
uniform vec2 uWaveVector1;
uniform vec4 uWaveParams2;
uniform vec2 uWaveVector2;
uniform vec4 uWaveParams3;
uniform vec2 uWaveVector3;

attribute vec3 aPosition;
attribute vec2 aTextureCoord;
attribute vec3 aNormal;

varying vec2 vTextureCoord;
varying vec3 vPos;

const float PI = 3.14159265358979323846264;
const float g = 9.8;

vec3 applywavepoint(in vec3 P0, in float t) {
	vec2 K1 = normalize(uWaveVector1);
	vec2 K2 = normalize(uWaveVector2);
	vec2 K3 = normalize(uWaveVector3);
	
	float w1 = sqrt(2.0*PI*g/uWaveParams1.z);
	float w2 = sqrt(2.0*PI*g/uWaveParams2.z);
	float w3 = sqrt(2.0*PI*g/uWaveParams3.z);
	
	float K1dotP = dot(uWaveVector1,P0.xz);
	float K2dotP = dot(uWaveVector2,P0.xz);
	float K3dotP = dot(uWaveVector3,P0.xz);
	
	vec3 P;
	
	P.xz = P0.xz + ( 
	uWaveParams1.w*K1*uWaveParams1.x*cos(K1dotP - w1*t + uWaveParams1.y) +
	uWaveParams2.w*K2*uWaveParams2.x*cos(K2dotP - w2*t + uWaveParams2.y) +
	uWaveParams3.w*K3*uWaveParams3.x*cos(K3dotP - w3*t + uWaveParams3.y) );
	
	P.y = P0.y + (
	uWaveParams1.x*sin(K1dotP-w1*t+uWaveParams1.y) +
	uWaveParams2.x*sin(K2dotP-w2*t+uWaveParams2.y) +
	uWaveParams3.x*sin(K3dotP-w3*t+uWaveParams3.y) );
	
	return P;
}

void main() {
	float t = uTime/1000.0;
	vPos = vec3(uWorldMatrix * vec4(aPosition,1.0));
	vec3 pos = applywavepoint(vPos,t);
	
	vTextureCoord = aTextureCoord;
	gl_Position = (uProjectionMatrix*uViewMatrix) * vec4(pos,1.0);
}