precision mediump float;

uniform float u_time;

uniform vec3 u_fogColor;
uniform float u_fogDensity;

uniform float u_ambient;
uniform vec3 u_cameraPos;

varying vec4 vColor;
varying float vDist;
varying vec2 vWaveUV;
varying float vHeight;
varying vec3 vWorldPos;

void main() {

    // =========================
    // 🌊 WAVES
    // =========================
    float wave1 = sin(vWaveUV.x * 4.0 + u_time * 1.5);
    float wave2 = cos(vWaveUV.y * 5.0 + u_time * 1.2);
    float wave = (wave1 + wave2) * 0.03;

    // =========================
    // 🎨 WATER COLOR
    // =========================
    vec3 shallow = vec3(0.0, 0.55, 0.85);
    vec3 deep    = vec3(0.0, 0.25, 0.55);

    float depthFactor = clamp((vHeight - 8.0) * -0.15, 0.0, 1.0);
    vec3 waterColor = mix(shallow, deep, depthFactor);

    waterColor += wave;
    waterColor *= vColor.rgb;

    // =========================
    // ✨ FRESNEL
    // =========================
    float fresnel = pow(1.0 - gl_FragCoord.z, 3.0);
    waterColor = mix(waterColor, vec3(1.0), fresnel * 0.25);

    // =========================
    // 🌫 BASE FOG (gleich wie voxel!)
    // =========================
    float fog = 1.0 - exp(-vDist * u_fogDensity * 0.5);
    fog *= smoothstep(50.0, 240.0, vDist);

    // Horizon boost
    float horizon = 1.0 - abs(vWorldPos.y * 0.01);
    fog += horizon * 0.15;

    // =========================
    // 🌫 HEIGHT FOG
    // =========================
    float heightDiff = u_cameraPos.y - vWorldPos.y;

    float heightFog = clamp(heightDiff * 0.04, 0.0, 1.0);
    heightFog = smoothstep(0.0, 1.0, heightFog);

    float nightBoost = 1.0 - u_ambient;

    fog += heightFog * nightBoost * 0.2;

    // =========================
    // 🌫 FINAL FOG
    // =========================
    fog = pow(fog, 1.3);
    fog = clamp(fog, 0.0, 0.65);

    vec3 finalColor = mix(waterColor, u_fogColor, fog);

    gl_FragColor = vec4(finalColor, 0.65);
}