
#ifdef GL_ES
precision mediump float;
#endif

attribute vec3 a_position;
attribute vec3 a_normal;

uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

uniform vec4 u_eyePos;

uniform vec4 u_lightPos;
uniform vec4 u_lightPos2;
uniform vec4 u_lightPos3;
uniform vec4 u_lightColor;
uniform vec4 u_lightColor2;
uniform vec4 u_lightColor3;
uniform vec4 u_lightBoxColor;
//uniform vec4 u_lightColor4;

uniform vec4 u_globalAmbient;

uniform vec4 u_materialDiffuse;
uniform vec4 u_materialSpecular;

uniform float u_materialShininess;

uniform vec4 u_materialEmission;

varying vec4 v_normal;
varying vec4 v_color;

void main()
{

	vec4 position = vec4(a_position.x, a_position.y, a_position.z, 1.0);
	position = u_modelMatrix * position;

	vec4 normal = vec4(a_normal.x, a_normal.y, a_normal.z, 0.0);
	normal = u_modelMatrix * normal;

	//global coordinates


	//lighting

	v_normal = normal;

	vec4 v = u_eyePos - position;


	// Light 1
	vec4 s = u_lightPos - position;
	vec4 h = s + v;

	float lambert = max(0.0, dot(v_normal, s) / (length(v_normal) * length(s)));
	float phong = max(0.0, dot(v_normal, h) / (length(v_normal) * length(h)));

	vec4 diffuseColor = lambert * u_lightColor * u_materialDiffuse;
	vec4 specularColor = pow(phong, u_materialShininess) * u_lightColor * u_materialSpecular;

	vec4 lightCalcColor1 = diffuseColor + specularColor;

	// Light 2
	s = u_lightPos2 - position;
	h = s + v;

	lambert = max(0.0, dot(v_normal, s) / (length(v_normal) * length(s)));
	phong = max(0.0, dot(v_normal, h) / (length(v_normal) * length(h)));

	diffuseColor = lambert * u_lightColor2 * u_materialDiffuse;
	specularColor = pow(phong, u_materialShininess) * u_lightColor2 * u_materialSpecular;

	vec4 lightCalcColor2 = diffuseColor + specularColor;

	// Light 3
	s = u_lightPos3 - position;
	h = s + v;

	lambert = max(0.0, dot(v_normal, s) / (length(v_normal) * length(s)));
	phong = max(0.0, dot(v_normal, h) / (length(v_normal) * length(h)));

	diffuseColor = lambert * u_lightColor3 * u_materialDiffuse;
	specularColor = pow(phong, u_materialShininess) * u_lightColor3 * u_materialSpecular;

	vec4 lightCalcColor3 = diffuseColor + specularColor;

	// BoxLight
    s = u_lightBoxColor - position;
    h = s + v;

    lambert = max(0.0, dot(v_normal, s) / (length(v_normal) * length(s)));
    phong = max(0.0, dot(v_normal, h) / (length(v_normal) * length(h)));

    diffuseColor = lambert * u_lightBoxColor * u_materialDiffuse;
    specularColor = pow(phong, u_materialShininess) * u_lightBoxColor * u_materialSpecular;

    vec4 lightCalcColorBox = diffuseColor + specularColor;


	v_color = u_globalAmbient * u_materialDiffuse + u_materialEmission + lightCalcColor1 + lightCalcColor2 + lightCalcColor3 + lightCalcColorBox;

	position = u_viewMatrix * position;
	gl_Position = u_projectionMatrix * position;
}