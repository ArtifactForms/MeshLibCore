precision mediump float;
uniform sampler2D texture;
uniform vec3 u_fogColor;
uniform float u_fogDensity;
uniform vec3 u_lightDir;
uniform vec3 u_lightColor;
uniform float u_ambient;
varying vec2 vTexCoord;
varying float vDist;
varying vec4 vColor;
varying vec3 vNormal;

void main() {
vec4 texCol = texture2D(texture, vTexCoord);
// Falls Textur transparent ist (z.B. Blätter), hier abbrechen:
if(texCol.a < 0.1) discard;


//float aoStrength = 1.2;
// vec3 enhancedAO = pow(vColor.rgb, vec3(aoStrength));
// vec4 blockCol = texCol * vec4(enhancedAO, 1.0);

vec4 blockCol = texCol * vColor;

// Licht-Reparatur:
// Wir normalisieren die Vektoren zur Sicherheit nochmal
vec3 norm = normalize(vNormal);
vec3 lDir = normalize(u_lightDir);

// Einfache Beleuchtung berechnen
float diff = max(dot(norm, lDir), 0.0);

// Mix aus Umgebungslicht und Sonnenlicht
vec3 lighting = vec3(u_ambient) + (diff * u_lightColor);

// Licht auf Block anwenden und auf 1.0 begrenzen
vec3 lightedRGB = min(blockCol.rgb * lighting, vec3(1.0));

// Nebel berechnen
float fog = exp(-pow(vDist * u_fogDensity, 2.0));
fog = clamp(fog, 0.0, 1.0);

gl_FragColor = vec4(mix(u_fogColor, lightedRGB, fog), blockCol.a);
}