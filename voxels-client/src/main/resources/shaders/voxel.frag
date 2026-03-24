precision mediump float;

uniform sampler2D texture;

uniform vec3 u_fogColor;
uniform float u_fogDensity;

uniform vec3 u_lightDir;
uniform vec3 u_lightColor;
uniform float u_ambient;

uniform vec3 u_cameraPos;

varying vec2 vTexCoord;
varying float vDist;
varying vec4 vColor;
varying vec3 vNormal;
varying vec3 vWorldPos;

void main() {

    vec4 texCol = texture2D(texture, vTexCoord);
    if(texCol.a < 0.1) discard;

    vec4 blockCol = texCol * vColor;

    // =========================
    // 💡 LIGHTING
    // =========================
    vec3 norm = normalize(vNormal);
    vec3 lDir = normalize(u_lightDir);

    float diff = max(dot(norm, lDir), 0.0);
    diff = pow(diff, 1.4);

    float shadow = 0.5 + diff * 0.5;

    vec3 lighting = vec3(u_ambient * 0.8) + (shadow * u_lightColor);
    vec3 color = min(blockCol.rgb * lighting, vec3(1.0));

    // =========================
    // 🎨 DISTANCE LOOK
    // =========================
    float distFactor = clamp(vDist / 200.0, 0.0, 1.0);

    float desaturate = mix(1.0, 0.75, distFactor);
    float gray = dot(color, vec3(0.299, 0.587, 0.114));
    color = mix(vec3(gray), color, desaturate);

    vec3 atmosphereTint = vec3(0.65, 0.8, 1.0);
    color = mix(color, atmosphereTint, distFactor * 0.25);

    color = pow(color, vec3(0.9));

    // =========================
    // 🌫 BASE FOG
    // =========================
    float fog = 1.0 - exp(-vDist * u_fogDensity * 0.5);
    fog *= smoothstep(50.0, 240.0, vDist);

    float horizon = 1.0 - abs(norm.y);
    fog += horizon * 0.15;

    // =========================
    // 🌫 HEIGHT FOG (FIXED)
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

    vec3 finalColor = mix(color, u_fogColor, fog);

    gl_FragColor = vec4(finalColor, blockCol.a);
}