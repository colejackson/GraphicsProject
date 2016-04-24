package async;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import driver.Utilities;
import graphics.Camera;
import graphics.Stage;
import maze.Wall;
import maze.WallPreprocessor;
import objects.LightBall;

public class Worker 
{	
	private ArrayList<Wall> worker_collide = new ArrayList<>();
	private ArrayList<Wall> worker = new ArrayList<>(6);
	private ArrayList<Wall> balls = new ArrayList<>();
	
	private Camera camera;
	private Stage stage;
	private WallPreprocessor wpp;
	private ArrayList<LightBall> lba;
	
	public Worker(Stage stage, WallPreprocessor wpp, Camera camera, ArrayList<LightBall> balls)
	{
		this.camera = camera;
		this.wpp = wpp;
		this.stage = stage;
		this.lba = balls;
		
		// Worker
				ExecutorService es = Executors.newFixedThreadPool(2);
				
				@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
				final Future f = es.submit(
					new Callable() 
					{
						@Override
						public Object call() throws Exception 
						{
								for(;;)
								{
									Thread.sleep(50);
									setBuffer();
								}
						}
					}
				);
				// End Worker
	}
	
	private void setBuffer() 
	{		
		worker = new ArrayList<>();	
		worker_collide = new ArrayList<>();
		
		double posx = camera.getPosition()[0];
		double posy = camera.getPosition()[1];
		double lookx = camera.getLook()[0];
		double looky = camera.getLook()[1];
		
		double neglookx = posx - lookx;
		double neglooky = posy - looky;
		
		Line2D line1 = new Line2D.Double(posx, posy, lookx, looky);
		Line2D line2 = new Line2D.Double(posx, posy, neglookx, neglooky);
						
		for(Wall w : wpp)
		{			
			Point2D.Double avg = w.center();

			double distance = Utilities.distance(avg.getX(), avg.getY(), camera.getPosition()[0], camera.getPosition()[1]);
				
			if((.5 >= line1.ptLineDist(avg.getX(), avg.getY()) &&
			   1.4 >= Point2D.distance(posx, posy, avg.getX(), avg.getY()) &&
			   line1.ptSegDist(avg.getX(), avg.getY()) <= line2.ptSegDist(avg.getX(), avg.getY())) ||
			   .1 > Point2D.distance(posx, posy, avg.getX(), avg.getY()))
			{
				worker.add(w);
			}
			if(distance < .05)
			{
				worker_collide.add(w);
			}
		}
		
		for(LightBall lb : lba)
		{
			balls = new ArrayList<Wall>();
			
			for(Wall w : wpp)
			{
				double distance = w.center().distance(lb);

				if(distance < .0)
				{
					balls.add(w);
				}
			}
			
			lb.setBuffer(balls);
		}
		
		camera.setBuffer(worker_collide);
		stage.setBuffer(worker);	
	}
}
