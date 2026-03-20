precision mediump float;

uniform mat4 projection;
uniform mat4 modelview;

uniform vec3 u_chunkPos;

attribute vec4 position;
attribute vec4 color;

varying vec4 vColor;
varying float vDist;
varying vec2 vWaveUV;
varying float vHeight;
varying vec3 vWorldPos;

void main() {

    vec4 viewPos = modelview * position;

    vDist = length(viewPos.xyz);
    vColor = color;

    // 🌍 Weltkoordinaten für Waves
    vec2 worldXZ = position.xz + u_chunkPos.xz;
    vWaveUV = worldXZ * 0.08;

    // Höhe
    vHeight = position.y;

    // 👉 wichtig für Height Fog
    vWorldPos = viewPos.xyz;

    gl_Position = projection * viewPos;
}