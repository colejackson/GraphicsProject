package graphics;

import com.jogamp.opengl.glu.GLUquadric;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import graphics.ParticleEngine;

public class Orb {
	
	float[] color = new float[3];
	float movement = 0.001f;
	ParticleEngine pEngine = new ParticleEngine();
	float[] position = new float[3];
	
	public Orb(){
		for(int i = 0; i < 3; i++){
			color[i] = (float)Math.random()*0.5f + 0.5f;
		}
		
		position[0] = 0.02f;
		position[1] = 0.02f;
		position[2] = 0.04f;
	}
	
	public void drawOrb(GL2 gl, GLU glu){
		gl.glColor3f(color[0], color[1], color[2]);
		gl.glPushMatrix();

		GLUquadric quad = glu.gluNewQuadric();
		glu.gluQuadricNormals(quad, glu.GLU_SMOOTH);   // Create Smooth Normals ( NEW )
		glu.gluQuadricTexture(quad, true); 

		gl.glTranslatef(position[0], position[1], position[2]);
		gl.glScalef(1, 1, 2);
		glu.gluSphere(quad, 0.01, 32, 32);
		
		gl.glPopMatrix();
		
		position[0] += movement;
		//position[1] += movement;
	
	}
}
