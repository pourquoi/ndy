precision mediump float;
varying vec2 vTextureCoord;
varying vec4 vColor;
varying vec3 vPos;
uniform sampler2D sTexture;
void main() {
  gl_FragColor = vColor;
}