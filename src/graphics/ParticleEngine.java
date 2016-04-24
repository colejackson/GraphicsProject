package graphics;

import com.jogamp.opengl.glu.GLU;

import objects.Candle;
import objects.Orb;
import objects.Particle;

public class ParticleEngine {
	int numParticles;
	float decay;
	Particle[] particles;
	int lastUsed = 0;
	int count = 0;
	
	public ParticleEngine(int numParticles, float decay){
		this.numParticles = numParticles;
		this.decay = decay;
		particles = new Particle[numParticles];
		
		for(int i = 0; i < numParticles; i++){
			particles[i] = new Particle();
		}
	}
	
	protected void update(Orb ball, GLU glu, Camera camera){
		int numNewParticles = 3;
		
		for(int i = 0; i < numNewParticles; i++){
			int dead = firstDead(ball);
			spawn(particles[dead], ball, glu, camera);
		}
		
		for(int i = 0; i < numParticles; i++){
			particles[i].setLife(decay);
			particles[i].move(count);
			
			particles[i].update(glu, camera, ball);
		}
	}
	
	protected void update(Candle candle, GLU glu, Camera camera){
		int numNewParticles = 2;
		
		for(int i = 0; i < numNewParticles; i++){
			int dead = firstDead(candle);
			if(dead != -1){
				spawn(particles[dead], candle, glu, camera);
			}
		}
		
		for(int i = 0; i < numParticles; i++){
			particles[i].setLife(decay);
			particles[i].position[2] -= 0.00001f;
			
			particles[i].update(glu, camera, candle);
		}
	}
	
	private int firstDead(Orb ball){
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
	
	
	private int firstDead(Candle candle){
		for(int i = lastUsed; i < numParticles; i++){
			if(particles[i].getLife() <= 0.0f){
				lastUsed = i;
				return i;
			}
		}
		return -1;
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

		
		part.update(glu, camera, ball);
	}
	
	private void spawn(Particle part, Candle candle, GLU glu, Camera camera){
		part.life = 7.0f;
		for(int i = 0; i < 4; i++){
			part.color[i] = 255.0f;
		}
		
		part.color[0] = 0.965f;
		part.color[1] = 0.946f;
		part.color[2] = 0.883f;
		part.color[3] = 255.0f;
		
		float randx = (float)Math.random();
		float randy = (float)Math.random();
		
		if(randx > 0.5)
			part.position[0] = (float)candle.getX() + (float)Math.random() * 0.0005f;
		
		else
			part.position[0] = (float)candle.getX() - (float)Math.random() * 0.0005f;
		
		if(randy > 0.5)
			part.position[1] = (float)candle.getY() + (float)Math.random() * 0.0005f;
		
		else
			part.position[1] = (float)candle.getY() - (float)Math.random() * 0.0005f;
		
			
		part.position[2] = 0.022f;

		
		part.update(glu, camera, candle);
	}
}