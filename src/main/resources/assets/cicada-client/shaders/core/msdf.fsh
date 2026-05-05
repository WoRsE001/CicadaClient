#version 330

#moj_import <minecraft:dynamictransforms.glsl>

uniform sampler2D Sampler0;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

float median(float r, float g, float b) {
    return max(min(r, g), min(max(r, g), b));
}

void main() {
    // UV уже перевёрнуты на стороне клиента – дополнительный flip не нужен
    vec2 texCoords = texCoord0;

    vec3 sample = texture(Sampler0, texCoords).rgb;
    ivec2 sz = textureSize(Sampler0, 0);

    // производные текстурных координат в пикселях текстуры
    float dx = dFdx(texCoords.x) * float(sz.x);
    float dy = dFdy(texCoords.y) * float(sz.y);
    // screenPxRange – сколько экранных пикселей приходится на 8 пикселей текстуры
    float screenPxRange = 8.0 * inversesqrt(dx * dx + dy * dy);

    float sigDist = median(sample.r, sample.g, sample.b);
    // ширина переходной зоны в единицах расстояния (половина диапазона)
    float w = 0.5 / screenPxRange;
    float opacity = smoothstep(0.5 - w, 0.5 + w, sigDist);

    fragColor = vec4(vertexColor.rgb, opacity);
}