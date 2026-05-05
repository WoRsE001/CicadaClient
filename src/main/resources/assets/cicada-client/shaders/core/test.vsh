#version 330 core

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in vec3 Position;
in float Test;

out float vTest;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    vTest = Test;
}