attribute vec4 position;

uniform mat4 projection;
uniform mat4 modelview;

varying vec3 vDir;

void main() {
    vDir = normalize(position.xyz);
    gl_Position = projection * modelview * position;
}