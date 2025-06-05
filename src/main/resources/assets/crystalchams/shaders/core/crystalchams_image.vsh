#version 150

#moj_import <projection.glsl>

in vec3 Position;
in vec2 UV0;


uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec4 texProj0;
out vec2 texCoord0;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    texProj0 = projection_from_position(gl_Position);
    texProj0.y = texProj0.y * -1.0;
    texCoord0 = UV0;
}
