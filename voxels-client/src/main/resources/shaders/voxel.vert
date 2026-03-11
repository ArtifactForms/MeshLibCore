uniform mat4 projection;
uniform mat4 modelview;
attribute vec4 position;
attribute vec2 texCoord;
attribute vec4 color;
attribute vec3 normal;
varying vec2 vTexCoord;
varying float vDist;
varying vec4 vColor;
varying vec3 vNormal;

void main() {
vec4 viewPos = modelview * position;
vDist = length(viewPos.xyz);
vTexCoord = texCoord;
vColor = color;
vNormal = normal;
gl_Position = projection * viewPos;
}