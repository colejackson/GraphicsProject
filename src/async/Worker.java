package async;

import java.util.ArrayList;

public class Worker
{
	private ArrayList<WorkerReady> devices = null;
	
	public Worker(WorkerReady... devices)
	{
		for(WorkerReady wr : devices)
			this.devices.add(wr);
	}

}
