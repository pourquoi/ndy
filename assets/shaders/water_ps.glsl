#define NUM_WAVES 3

precision mediump float;
uniform sampler2D sTexture0;
uniform sampler2D sTexture1; // envmap
uniform sampler2D sTexture2; // heightmap

uniform vec3 uLightDir;
uniform vec3 uEyePos;

uniform vec4 uAmbient;
uniform vec4 uDiffuse;
uniform vec4 uSpecular;
uniform float uShininess;

uniform float uTime;

uniform float uWaveAmplitude[NUM_WAVES];
uniform float uWavePhase[NUM_WAVES];
uniform float uWaveLength[NUM_WAVES];
uniform float uWaveSharpness[NUM_WAVES];
uniform vec2 uWaveVector[NUM_WAVES];
uniform vec4 uWaveConst[NUM_WAVES]; // sqrt(2PIg/L), 2PI/L, Dirx*A, Diry*A

varying vec2 vHeightmapCoord;
varying vec2 vTextureCoord;
varying vec3 vPos;

const float PI = 3.14159265358979323846264;

vec3 applywavenormal(in vec3 P0, in float t) {
	vec3 N = vec3(0.0,1.0,0.0);
	int i;
	for(i=0;i<NUM_WAVES;i++) {
		float KdotP = dot(uWaveVector[i]*uWaveConst[i].y,P0.xz);
		float a = KdotP - uWaveConst[i].x * t + uWavePhase[i];
		float c = cos(a);
		N.x -= uWaveConst[i].z*uWaveConst[i].y*c;
		N.z -= uWaveConst[i].w*uWaveConst[i].y*c;
		N.y -= uWaveSharpness[i]*uWaveAmplitude[i]*sin(a);
	}
	
	return normalize(N);
}

void main() {
	float h = texture2D(sTexture2,vHeightmapCoord).b;
	
	if(h<0.1) {
		vec3 D = uEyePos - vPos;
		float l = length(D);
		D /= l;
		float a = l/100.0;
		vec3 N = vec3(0.0,1.0,0.0);
		if( a > 1.0 ) {
			N = vec3(0.0,1.0,0.0);
		} else {
			vec3 N1 = applywavenormal(vPos, uTime);
			vec3 N2 = vec3(0.0,1.0,0.0);
			N = a*N2 + (1.0-a)*N1;
		}
		
		vec3 L = (uLightDir);
		vec3 R = (reflect(L,N));		
		vec4 color = uAmbient + dot(N,-L) * uDiffuse ;
		
		vec3 RV = (reflect(D,N));
	
		float m = 2.0*sqrt( RV.x*RV.x + RV.y*RV.y + (RV.z+1.0)*(RV.z+1.0) );
		vec2 uv_envmap = vec2(RV.x/m+0.5, RV.y/m+0.5);
		
		gl_FragColor = texture2D(sTexture1,uv_envmap)*color;
	} else {
		gl_FragColor = vec4(0.918,0.929,0.592,1.0);
	}	

}