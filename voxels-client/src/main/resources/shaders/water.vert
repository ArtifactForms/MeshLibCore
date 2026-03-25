precision mediump float;

uniform mat4 projection;
uniform mat4 modelview;

uniform vec3 u_chunkPos;
uniform vec3 u_worldChunkPos;

attribute vec4 position;
attribute vec4 color;

varying vec4 vColor;
varying float vDist;
varying vec2 vWaveUV;
varying float vHeight;
varying vec3 vWorldPos;

void main() {
    // Für die Positionierung nutzen wir weiterhin die relative Position
    vec4 viewPos = modelview * position;

    vDist = length(viewPos.xyz);
    vColor = color;

    // 🌊 Absolute Weltkoordinaten für statische Wellen
    // Jetzt bewegen sich die UVs nicht mehr, wenn die Kamera wandert
    vec2 absoluteWorldXZ = position.xz + u_worldChunkPos.xz;
    vWaveUV = absoluteWorldXZ * 0.08;

    vHeight = position.y;
    vWorldPos = viewPos.xyz;

    gl_Position = projection * viewPos;
}