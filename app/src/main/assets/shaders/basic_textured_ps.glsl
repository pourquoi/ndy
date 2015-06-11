precision mediump float;
varying vec2 vTextureCoord;
varying vec4 vColor;
varying vec3 vPos;
uniform sampler2D sTexture0;
void main() {
	gl_FragColor = texture2D(sTexture0, vTextureCoord);
}