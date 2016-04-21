package graphics;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import driver.OGL;

public class Particle {
	float[] position = new float[3];
	float velocityx = 0.001f;
	float velocityy = 0.000f;
	float radius;
	float[] color = new float[4];
	boolean left;
	
	float life;
	
	public Particle(){
		position[0] = 0.0f;
		position[1] = 0.0f;
		
		for(int i = 0; i < 4; i++){
			color[i] = 1.0f;
		}
		
		life = 0.0f;
		
		radius = (float)Math.random() * 0.001f;
		
		if(Math.random() >= 0.5){
			left = true;
		}
	}
	
	protected void update(GLU glu, Camera camera){
		OGL.gl.glPushMatrix();
		
		OGL.gl.glColor4f(color[0], color[1], color[2], color[3]);

		
		GLUquadric quad = glu.gluNewQuadric();
		glu.gluQuadricNormals(quad, glu.GLU_SMOOTH);   // Create Smooth Normals ( NEW )
		glu.gluQuadricTexture(quad, false); 
		
		OGL.gl.glTranslatef(position[0], position[1], position[2]);
		OGL.gl.glScalef(1, 1, 2);
		glu.gluSphere(quad, radius, 10, 10);
		
		OGL.gl.glPopMatrix();

	}
	
	protected void setLife(float decay){
		life = life - decay;
		color[3] = color[3] - 3 * decay;
	}
	
	protected void newLife(){
		// Random life makes it trail off much nicer
		life = (float)Math.random();
	}
	
	protected float getLife(){
		return life;
	}
	
	protected void move(int count){
		if(left){
			//position[0] += velocityy * Math.random() * 0.0005f;
			position[1] += velocityx * Math.random() * 0.0005f;
		}

		//else{
			//position[0] -= velocityy * Math.random() * 0.0005f;
			//position[1] -= velocityx * Math.random() * 0.0005f;
		//}
		
			
		position[2] += Math.random() * 0.001;
	}
}


