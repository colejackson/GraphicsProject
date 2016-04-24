package graphics;

import objects.Orb;
import objects.Particle;

import com.jogamp.opengl.glu.GLU;

public class ParticleEngine {
	int numParticles = 75;
	float decay = 0.008f;
	Particle[] particles = new Particle[numParticles];
	int lastUsed = 0;
	int count = 0;
	
	public ParticleEngine(){
		for(int i = 0; i < numParticles; i++){
			particles[i] = new Particle();
		}
	}
	
	protected void update(Orb ball, GLU glu, Camera camera){
		int numNewParticles = 2;
		
		for(int i = 0; i < numNewParticles; i++){
			int dead = firstDead();
			spawn(particles[dead], ball, glu, camera);
		}
		
		for(int i = 0; i < numParticles; i++){
			particles[i].setLife(decay);
			particles[i].move(count);
			
			particles[i].update(glu, camera);
		}
	}
	
	private int firstDead(){
		for(int i = lastUsed; i < numParticles; i++){
			if(particles[i].getLife() <= 0.0f){
				lastUsed = i;
				return i;
			}
		}
		
		for(int i = 0; i < lastUsed; i++){
			if(particles[i].getLife() < 0.0f){
				lastUsed = i;
				return i;
			}
		}
		
		lastUsed = 0;
		return lastUsed;
	}
	
	private void spawn(Particle part, Orb ball, GLU glu, Camera camera){
		part.newLife();
		for(int i = 0; i < 3; i++){
			part.color[i] = ball.color[i];
		}
		
		part.color[3] = (float)Math.random() * 4.0f;
		
		float randx = (float)Math.random();
		float randy = (float)Math.random();
		float randz = (float)Math.random();
		
		if(randx > 0.5)
			part.position[0] = ball.position[0] + (randx * 0.01f);
		
		else
			part.position[0] = ball.position[0] - (randx * 0.01f);
		
		if(randy > 0.5)
			part.position[1] = ball.position[1] + (randy * 0.01f);
		
		else
			part.position[1] = ball.position[1] - (randy * 0.01f);


		if(randz > 0.5)
			part.position[2] = ball.position[2] + randz * 0.015f;
				
		else
			part.position[2] = ball.position[2] - randz * 0.015f;

		
		part.update(glu, camera);
	}
}