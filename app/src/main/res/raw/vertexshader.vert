uniform mat4 vMVPMatrix;
attribute vec4 vPosition;
attribute vec3 vColor;
varying vec3 fColor;

void main() {
   gl_Position = vMVPMatrix * vPosition;
   fColor = vColor;
}