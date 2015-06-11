#define NUM_WAVES 5

precision mediump float;

uniform mat4 uWorldMatrix;
uniform mat4 uViewMatrix;
uniform mat4 uProjectionMatrix;

uniform float uTime;

uniform float uWaveAmplitude[NUM_WAVES];
uniform float uWavePhase[NUM_WAVES];
uniform float uWaveLength[NUM_WAVES];
uniform float uWaveSharpness[NUM_WAVES];
uniform vec2 uWaveVector[NUM_WAVES];
uniform vec4 uWaveConst[NUM_WAVES]; // sqrt(2PIg/L), 2PI/L, Dirx*A, Diry*A

uniform vec2 uWaterSize;
uniform vec2 uWaterPos;

uniform float detailDist2;

attribute vec3 aPosition;
attribute vec2 aTextureCoord;
attribute vec3 aNormal;

varying vec2 vTextureCoord;
varying vec2 vHeightmapCoord;
varying vec3 vPos;

const float PI = 3.14159265358979323846264;

vec3 applywavepoint(in vec3 P0, in float t) {
	vec3 P = P0;
	int i;
	float KdotP;
	float a;
	for(i=0;i<NUM_WAVES;i++) {
		KdotP = dot(uWaveVector[i]*uWaveConst[i].y,P0.xz);
		float a = KdotP - uWaveConst[i].x * t + uWavePhase[i];
		P.x += uWaveConst[i].z * uWaveSharpness[i] * cos(a);
		P.z += uWaveConst[i].w * uWaveSharpness[i] * cos(a);
		P.y += uWaveAmplitude[i] * sin(a);
		//P.y = P0.y;
	}
	return P;
}

void main() {
	vPos = vec3(uWorldMatrix * vec4(aPosition,1.0));
	
	vec3 pos = applywavepoint(vPos,uTime);
	vTextureCoord = aTextureCoord;
	vHeightmapCoord = vec2((vPos.x-uWaterPos.x)/uWaterSize.x,1.0-(vPos.z-uWaterPos.y)/uWaterSize.y);
	
	gl_Position = (uProjectionMatrix*uViewMatrix) * vec4(pos,1.0);
}