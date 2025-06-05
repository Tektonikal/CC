#version 150

#moj_import <matrix.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform sampler2D Sampler2;

uniform float GameTime;
uniform int EndPortalLayers;

in vec4 texProj0;
in vec2 texCoord0;
in vec4 overlayColor;

const mat4 SCALE_TRANSLATE = mat4(
    0.5, 0.0, 0.0, 0.25,
    0.0, 0.5, 0.0, 0.25,
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
);

mat4 end_portal_layer(float layer) {
    mat4 translate = mat4(
        1.0, 0.0, 0.0, 17.0 / layer,
        0.0, 1.0, 0.0, (2.0 + layer / 1.5) * (GameTime * 1.5),
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    );

    mat2 rotate = mat2_rotate_z(radians((layer * layer * 4321.0 + layer * 9.0) * 2.0));

    mat2 scale = mat2((4.5 - layer / 4.0) * 2.0);

    return mat4(scale * rotate) * translate * SCALE_TRANSLATE;
}
vec3 rgb2hsv(vec3 c)
{
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}
vec3 hsv2rgb(vec3 c)
{
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

out vec4 fragColor;

void main() {
    vec4 texCol = texture(Sampler2, texCoord0);
    if(texCol.a < 0.1){
        discard;
    }
    vec3 color = textureProj(Sampler0, texProj0).rgb * vec3(0.022087, 0.098399, 0.110818);
    for (int i = 0; i < EndPortalLayers; i++) {
        vec3 shiftedCol = rgb2hsv(overlayColor.rgb);
        shiftedCol.r += (15 - i) * 0.0104166667 * (i % 2 == 0 ? -1 : 1);
        color += textureProj(Sampler1, texProj0 * end_portal_layer(float(i + 1))).rgb * (vec3(i * 0.0625) * hsv2rgb(shiftedCol));
    }
    fragColor = vec4(color, overlayColor.a);
}
