precision mediump float;
uniform sampler2D sTexture0;

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

uniform vec4 uWaveParams1;
uniform vec2 uWaveVector1;
uniform vec4 uWaveParams2;
uniform vec2 uWaveVector2;
uniform vec4 uWaveParams3;
uniform vec2 uWaveVector3;

uniform float uDetailsDistance;

varying vec2 vTextureCoord;
varying vec3 vPos;
varying float vCamDist;

const float PI = 3.14159265358979323846264;
const float g = 9.8;

vec3 applywavenormal(in vec3 P0, in float t) {
	vec2 K1 = normalize(uWaveVector1);
	vec2 K2 = normalize(uWaveVector2);
	vec2 K3 = normalize(uWaveVector3);
	
	float w1 = sqrt(2.0*PI*g/uWaveParams1.z);
	float w2 = sqrt(2.0*PI*g/uWaveParams2.z);
	float w3 = sqrt(2.0*PI*g/uWaveParams3.z);
	
	float K1dotP = dot(uWaveVector1,P0.xz);
	float K2dotP = dot(uWaveVector2,P0.xz);
	float K3dotP = dot(uWaveVector3,P0.xz);
	
	vec3 N;
	
	N.xz = -1.0 * (
	K1*uWaveParams1.x*cos(K1dotP-w1*t+uWaveParams1.y) +
	K2*uWaveParams2.x*cos(K2dotP-w2*t+uWaveParams2.y) +
	K3*uWaveParams3.x*cos(K3dotP-w3*t+uWaveParams3.y) );
	
	N.y = 1.0 - (
	uWaveParams1.w*uWaveParams1.x*sin(K1dotP-w1*t+uWaveParams1.y) +
	uWaveParams2.w*uWaveParams2.x*sin(K2dotP-w2*t+uWaveParams2.y) +
	uWaveParams3.w*uWaveParams3.x*sin(K3dotP-w3*t+uWaveParams3.y) );
	
	return normalize(N);
}

void main() {
	vec3 N;
	if(vCamDist<uDetailsDistance) {
		N = -applywavenormal(vPos,uTime);
	} else {
		N = vec3(0.0, -1.0, 0.0);
	}
	vec3 L = normalize(uLightDir);
	vec3 R = normalize(reflect(L,N));
	vec3 D = normalize(uEyePos - vPos);
	vec4 color = uAmbient + dot(N,L) * uDiffuse + pow(clamp(dot(R,D),0.0,1.0),uShininess) * uSpecular;
	color.a = 1.0;
	
	vec3 RV = normalize(reflect(-D,N));
	
	float m = 2.0*sqrt( RV.x*RV.x + RV.y*RV.y + (RV.z+1.0)*(RV.z+1.0) );
	vec2 uv;
	uv.x=RV.x/m+0.5;
	uv.y=RV.y/m+0.5;
	
	//float h = texture2D(sTexture1, vec2((vPos.x+500.0)/1000.0,(vPos.z+500.0)/1000.0)).r;

	gl_FragColor = texture2D(sTexture0, uv) * color;
}