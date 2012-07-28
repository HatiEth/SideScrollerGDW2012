package gdw.network;

import java.io.IOException;


import gdw.entityCore.EntityManager;
import gdw.entityCore.EntityTemplateManager;
import gdw.network.server.GDWServerLogger;
import gdw.physics.SimulationComponentManager;

public class ServerCoreLoop extends Thread
{
	
	private final long SLEEPCONST = 300L;
	
	private SideScrollerServer ref;
	
	public ServerCoreLoop(SideScrollerServer ref)
	{
		this.ref = ref;
		this.start();
	}
	
	@Override
	public void run()
	{
		long oldVal = System.currentTimeMillis();

		while(!this.isInterrupted())
		{
			NetSubSystem.getInstance().pollMessages();
			if(this.ref.getCurState()== SideScrollerServer.ServerGameStates.WAITING)
			{
				
			}else if(this.ref.getCurState() == SideScrollerServer.ServerGameStates.START)
			{
				//init
			
				EntityTemplateManager entTempMan = EntityTemplateManager.getInstance();
				
				try
				{
					GDWServerLogger.logMSG("init system");
					//Level.getInstance().start();
					entTempMan.loadEntityTemplates("general.templates");
					//entTempMan.getEntityTemplate("LevelGoal").createEntity(200f, 200f, 0f);
				} catch (IOException e)
				{
					e.printStackTrace();
					return;
				}
				this.ref.startComplete();
			}else
			{
			try
			{
				sleep(SLEEPCONST);
			} catch (InterruptedException e)
			{
				return;
			}	
			
			long curVal = System.currentTimeMillis();
			float delta = curVal -  oldVal;
			
			//updates laufen lassen
			NetSubSystem.getInstance().pollMessages();
		
			EntityTemplateManager.getInstance().getEntityTemplate("Player1").createEntity(200f, 200f, 0f);
			SimulationComponentManager.getInstance().simulate(delta);
			EntityManager.getInstance().tick(delta);
		
			NetSubSystem.getInstance().checkDeadReck();
			NetSubSystem.getInstance().sendBufferedMessages();
			
			}
				
				
		}
	}
	
}
