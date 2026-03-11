uniform mat4 projection;
uniform mat4 modelview;

uniform vec3 u_chunkPos;

attribute vec4 position;
attribute vec4 color;

varying vec4 vColor;
varying float vDist;
varying vec2 vWaveUV;
varying float vHeight;

void main() {

    vec4 viewPos = modelview * position;

    vDist = length(viewPos.xyz);
    vColor = color;

    // Weltkoordinaten
    vec2 worldXZ = position.xz + u_chunkPos.xz;

    vWaveUV = worldXZ * 0.08;

    // Höhe merken (für Tiefenfarbe)
    vHeight = position.y;

    gl_Position = projection * viewPos;
}