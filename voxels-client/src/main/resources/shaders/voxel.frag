// voxel.frag
precision mediump float;
uniform sampler2D texture;
uniform vec3 u_fogColor;
uniform float u_fogDensity;

varying vec2 vTexCoord;
varying float vDist;
varying vec4 vColor;

void main() {
vec4 texCol = texture2D(texture, vTexCoord);

// AO anwenden: Texturfarbe mit Vertexfarbe multiplizieren
vec4 col = texCol * vColor;

float fog = exp(-pow(vDist * u_fogDensity, 2.0));
fog = clamp(fog, 0.0, 1.0);
gl_FragColor = vec4(mix(u_fogColor, col.rgb, fog), col.a);
}