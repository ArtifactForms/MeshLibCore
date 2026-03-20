precision mediump float;

varying vec3 vDir;
uniform float u_timeOfDay;

void main() {

    vec3 dir = normalize(vDir);

    float angle = u_timeOfDay * 3.14159 * 2.0;

    vec3 sunDir = normalize(vec3(
        sin(angle),
        -cos(angle),
        0.2
    ));

    vec3 moonDir = -sunDir;

    float sunHeight = clamp(-sunDir.y * 0.5 + 0.5, 0.0, 1.0);
    sunHeight = smoothstep(0.0, 1.0, sunHeight);

    float t = (-dir.y) * 0.5 + 0.5;

    vec3 dayTop = vec3(0.2, 0.5, 1.0);
    vec3 dayHorizon = vec3(0.8, 0.9, 1.0);

    vec3 sunsetTop = vec3(0.8, 0.3, 0.6);
    vec3 sunsetHorizon = vec3(1.0, 0.6, 0.3);

    vec3 nightTop = vec3(0.02, 0.04, 0.08);
    vec3 nightHorizon = vec3(0.05, 0.08, 0.15);

    float sunsetFactor = 1.0 - abs(sunDir.y);
    sunsetFactor = pow(sunsetFactor, 1.5);
    float sunsetBlend = sunsetFactor * 0.6;

    vec3 skyTop = mix(nightTop, dayTop, sunHeight);
    skyTop = mix(skyTop, sunsetTop, sunsetBlend);

    vec3 skyHorizon = mix(nightHorizon, dayHorizon, sunHeight);
    skyHorizon = mix(skyHorizon, sunsetHorizon, sunsetBlend);

    vec3 col = mix(skyHorizon, skyTop, t);

    // 🌙 RESTLICHT
    float nightOnly = pow(1.0 - sunHeight, 2.0);

    float horizonFade = 1.0 - abs(dir.y);
    horizonFade = pow(horizonFade, 2.0);

    vec3 nightGlow = vec3(0.08, 0.12, 0.18);

    col += nightGlow * horizonFade * nightOnly * 0.6;

    // 🌅 Sunset Glow
    float horizon = 1.0 - abs(dir.y);
    horizon = pow(horizon, 6.0);

    float glowStrength = horizon * sunsetFactor;
    vec3 glowColor = vec3(1.0, 0.5, 0.2);

    col += glowColor * glowStrength * 0.8;

    // ☀️ Sun
    float sunDot = max(dot(dir, sunDir), 0.0);
    float sunDisc = pow(sunDot, 800.0);
    float sunGlow = pow(sunDot, 8.0) * 0.5;

    col += vec3(1.0, 0.9, 0.6) * (sunDisc + sunGlow) * sunHeight;

    // 🌙 Moon
    float moonDot = max(dot(dir, moonDir), 0.0);
    float moonDisc = pow(moonDot, 400.0);

    col += vec3(0.8, 0.85, 1.0) * moonDisc * (1.0 - sunHeight);

    // ✨ Stars
    vec3 p = normalize(dir) * 300.0;

    float star = step(0.9985,
        fract(sin(dot(floor(p), vec3(12.9898,78.233,45.164))) * 43758.5453)
    );

    col += vec3(star) * pow(1.0 - sunHeight, 3.0);

    gl_FragColor = vec4(col, 1.0);
}