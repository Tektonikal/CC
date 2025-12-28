#version 150

#moj_import <projection.glsl>
#moj_import <fog.glsl>

in vec3 Position;
in vec2 UV0;
in vec4 Color;
uniform int FogShape;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out float vertexDistance;
out vec4 texProj0;
out vec2 texCoord0;
out vec4 overlayColor;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    vertexDistance = fog_distance(Position, FogShape);
    texProj0 = projection_from_position(gl_Position);
    overlayColor = Color;
    texCoord0 = UV0;
}
