precision mediump float;

uniform float u_time;
uniform vec3 u_fogColor;
uniform float u_fogDensity;

varying vec4 vColor;
varying float vDist;
varying vec2 vWaveUV;
varying float vHeight;

void main() {

    // 🌊 zwei bewegte Wellenfelder
    float wave1 = sin(vWaveUV.x * 4.0 + u_time * 1.5);
    float wave2 = cos(vWaveUV.y * 5.0 + u_time * 1.2);

    float wave = (wave1 + wave2) * 0.03;

    // 💧 Basiswasserfarbe
    vec3 shallow = vec3(0.0, 0.55, 0.85);
    vec3 deep    = vec3(0.0, 0.25, 0.55);

    // Tiefe simulieren
    float depthFactor = clamp((vHeight - 8.0) * -0.15, 0.0, 1.0);

    vec3 waterColor = mix(shallow, deep, depthFactor);

    // Wellen leicht einfärben
    waterColor += wave;

    waterColor *= vColor.rgb;

    // ✨ Fresnel Effekt
    float fresnel = pow(1.0 - gl_FragCoord.z, 3.0);

    vec3 reflection = vec3(1.0);

    waterColor = mix(waterColor, reflection, fresnel * 0.25);

    // 🌫 Fog
    float fog = exp(-(vDist * u_fogDensity) * (vDist * u_fogDensity));
    fog = clamp(fog, 0.0, 1.0);

    vec3 finalColor = mix(u_fogColor, waterColor, fog);

    // Transparenz
    gl_FragColor = vec4(finalColor, 0.65);
}