package ndy.game.material;

public class NDYMaterial {
	public float[] ambient = { 0.1f, 0.1f, 0.1f, 1.0f };
	public float[] diffuse = { 0.6f, 0.6f, 0.6f, 1.0f };
	public float[] specular = { 0.6f, 0.6f, 0.6f, 1.0f };
	public int shininess = 1;
	public NDYTexture texture = null;
	
	public void setAmbient(float r, float g, float b, float a) {
		ambient[0] = r;
		ambient[1] = g;
		ambient[2] = b;
		ambient[3] = a;
	}
	
	public void setDiffuse(float r, float g, float b, float a) {
		diffuse[0] = r;
		diffuse[1] = g;
		diffuse[2] = b;
		diffuse[3] = a;
	}
	
	public void setSpecular(float r, float g, float b, float a) {
		specular[0] = r;
		specular[1] = g;
		specular[2] = b;
		specular[3] = a;
	}
	
	public static NDYMaterial Copper() {
		NDYMaterial m = new NDYMaterial();
		float r = 0.894117647f; float g = 0.482352941f; float b = 0.341176471f;
		m.setAmbient(0.3f*r, 0.3f*g, 0.3f*b, 1.f);
		m.setDiffuse(0.7f*r, 0.7f*g, 0.7f*b, 1.f);
		m.setSpecular(0.6f*r, 0.6f*g, 0.6f*b, 1.f);
		m.shininess = 6;
		
		return m;
	}
	
	public static NDYMaterial Rubber() {
		NDYMaterial m = new NDYMaterial();
		float r = 0.011764706f; float g = 0.545098039f; float b = 0.984313725f;
		m.setAmbient(0.3f*r, 0.3f*g, 0.3f*b, 1.f);
		m.setDiffuse(0.7f*r, 0.7f*g, 0.7f*b, 1.f);
		m.setSpecular(0f, 0f, 0f, 1.f);
		m.shininess = 1;
		
		return m;
	}
	
	public static NDYMaterial Brass() {
		NDYMaterial m = new NDYMaterial();
		float r = 0.894117647f; float g = 0.733333333f; float b = 0.133333333f;
		m.setAmbient(0.3f*r, 0.3f*g, 0.3f*b, 1.f);
		m.setDiffuse(0.7f*r, 0.7f*g, 0.7f*b, 1.f);
		m.setSpecular(0.7f*r, 0.7f*g, 0.7f*b, 1.f);
		m.shininess = 8;
		
		return m;
	}
	
	public static NDYMaterial Glass() {
		NDYMaterial m = new NDYMaterial();
		float r = 0.780392157f; float g = 0.890196078f; float b = 0.815686275f;
		m.setAmbient(0.3f*r, 0.3f*g, 0.3f*b, 1.f);
		m.setDiffuse(0.7f*r, 0.7f*g, 0.7f*b, 1.f);
		m.setSpecular(0.7f*r, 0.7f*g, 0.7f*b, 1.f);
		m.shininess = 32;
		
		return m;
	}
	
	public static NDYMaterial Plastic() {
		NDYMaterial m = new NDYMaterial();
		float r = 0.f; float g = 0.074509804f; float b = 0.988235294f;
		m.setAmbient(0.3f*r, 0.3f*g, 0.3f*b, 1.f);
		m.setDiffuse(0.9f*r, 0.9f*g, 0.9f*b, 1.f);
		m.setSpecular(0.9f*r, 0.9f*g, 0.9f*b, 1.f);
		m.shininess = 32;
		
		return m;
	}
	
	public static NDYMaterial Pearl() {
		NDYMaterial m = new NDYMaterial();
		float r = 1f; float g = 0.541176471f; float b = 0.541176471f;
		m.setAmbient(1.5f*r, 1.5f*g, 1.5f*b, 1.f);
		m.setDiffuse(-0.5f*r, -0.5f*g, -0.5f*b, 1.f);
		m.setSpecular(2f*r, 2f*g, 2f*b, 1.f);
		m.shininess = 99;
		
		return m;
	}
}
