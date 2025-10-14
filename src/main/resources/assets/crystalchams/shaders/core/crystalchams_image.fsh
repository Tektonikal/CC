#version 150

#moj_import <matrix.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

in vec4 texProj0;
in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec4 texCol = texture(Sampler1, texCoord0);
    if(texCol.a < 0.1 || vertexColor.a < 0.1){
        discard;
    }
    //TODO: FOG !!!!!!!!!!!!!!!!!!!!!!!
    fragColor = vec4(textureProj(Sampler0, texProj0)) * vertexColor;
}
