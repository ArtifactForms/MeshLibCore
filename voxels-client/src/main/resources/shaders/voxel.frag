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
    // Erhöhter Discard-Schwellenwert für sauberere Kanten bei Gras
    if(texCol.a < 0.1) discard;

    vec4 blockCol = texCol * vColor;

    // =========================
    // 💡 LIGHTING (Optimiert für Dekor/Cross-Mesh)
    // =========================
    vec3 norm = normalize(vNormal);
    vec3 lDir = normalize(u_lightDir);

    // Nutze abs(), damit beide Seiten des Grases beleuchtet werden
    float dotProduct = dot(norm, lDir);
    float diff = abs(dotProduct); 

    // "Wrap-Around" Lighting: Verhindert, dass Flächen im 90° Winkel komplett schwarz sind
    diff = mix(diff, 1.0, 0.15); 

    // Ambient Anteil: Etwas kräftiger, damit die Vegetation im Schatten "lebt"
    vec3 ambient = u_ambient * u_lightColor * 0.45; 
    vec3 diffuse = diff * u_lightColor;

    vec3 lighting = ambient + diffuse;
    vec3 color = blockCol.rgb * lighting;

    // =========================
    // 🎨 DISTANCE LOOK (Atmosphäre)
    // =========================
    float distFactor = clamp(vDist / 200.0, 0.0, 1.0);

    // Leichte Entsättigung in der Ferne
    float desaturate = mix(1.0, 0.75, distFactor);
    float gray = dot(color, vec3(0.299, 0.587, 0.114));
    color = mix(vec3(gray), color, desaturate);

    // Bläulicher Dunst für die Weitsicht
    vec3 atmosphereTint = vec3(0.65, 0.8, 1.0);
    color = mix(color, atmosphereTint, distFactor * 0.25);

    // Gamma-Korrektur (leichtes Aufhellen der Mitteltöne)
    color = pow(color, vec3(0.9));

    // =========================
    // 🌫 BASE FOG
    // =========================
    float fog = 1.0 - exp(-vDist * u_fogDensity * 0.5);
    fog *= smoothstep(50.0, 240.0, vDist);

    // Horizont-Dunst (stärker bei flachen Winkeln)
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