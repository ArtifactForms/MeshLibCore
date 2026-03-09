// voxel.vert
uniform mat4 projection;
uniform mat4 modelview;
attribute vec4 position;
attribute vec2 texCoord;
attribute vec4 color; // Das ist für das AO

varying vec2 vTexCoord;
varying float vDist;
varying vec4 vColor; // Weitergabe an Fragment Shader
void main() {
vec4 viewPos = modelview * position;
vDist = length(viewPos.xyz);
vTexCoord = texCoord;
vColor = color; // AO Farbe übernehmen
gl_Position = projection * viewPos;
}