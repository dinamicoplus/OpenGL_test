precision mediump float;
uniform vec2 fResolution;
uniform float fTime;
varying vec3 fColor;

void main()
{
   gl_FragColor = vec4(fColor,1.0);
}