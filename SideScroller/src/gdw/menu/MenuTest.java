package gdw.menu;

import java.io.IOException;

import gdw.collisionDetection.CollisionDetectionComponentManager;
import gdw.control.PlayerInputComponent;
import gdw.control.PlayerInputComponentManager;
import gdw.entityCore.Entity;
import gdw.entityCore.EntityManager;
import gdw.entityCore.EntityTemplate;
import gdw.entityCore.EntityTemplateManager;
import gdw.entityCore.Level;
import gdw.gameplay.player.PlayerSubSystem;
import gdw.gameplay.progress.GameplayProgressManager;
import gdw.genericBehavior.AttachmentComponent;
import gdw.graphics.CameraComponent;
import gdw.graphics.SpriteManager;
import gdw.graphics.StaticSpriteComponent;
import gdw.network.NetSubSystem;
import gdw.physics.SimulationComponent;
import gdw.physics.SimulationComponentManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

//TODO: Zeugs dazuschreiben, dass das Game startet und rendert
public class MenuTest extends BasicGame
{
	private Menu m = null;
	private boolean shutdown = false;
	private boolean gameStarted = false;
	private boolean offline = true;

	public MenuTest()
	{
		super("INSERT GAMENAME HERE!!");
	}

	@Override
	public void render(GameContainer arg0, Graphics arg1) throws SlickException
	{
		if (gameStarted)
			SpriteManager.getInstance().render();
		else
			m.draw(arg0, arg1);
	}

	@Override
	public void init(GameContainer arg0) throws SlickException
	{
		m = new Menu()
		{
			@Override
			protected void onGameStart(CharacterSelectionMenu c, boolean offlineGame)
			{
				gameStarted = true;
				Level.getInstance().start();
				SimulationComponentManager.getInstance().setGravity(90);
				offline = offlineGame; 
				if (offline)
				{
					Entity p1 = EntityTemplateManager.getInstance().getEntityTemplate("Player1").createEntity(0, 0, 0);
					Entity spawn = GameplayProgressManager.getInstance().getCurrentSpawnComponent().getOwner();
					p1.setPos(spawn.getPosX(), spawn.getPosY());
					AttachmentComponent ac = (AttachmentComponent) p1.getComponent(AttachmentComponent.COMPONENT_TYPE);
					Entity hat = EntityTemplateManager.getInstance().getEntityTemplate("Hat").createEntity(0, 0, 0);
					StaticSpriteComponent hatSprite = (StaticSpriteComponent) hat.getComponent(StaticSpriteComponent.COMPONENT_TYPE);
					hatSprite.setImage(c.getPlayer1Hat());
					ac.attachEntity(hat.getReference());
					
					Entity cam = EntityTemplateManager.getInstance().getEntityTemplate("Camera").createEntity(0, 0, 0);
					ac.attachEntity(cam.getReference());
				}
				else
				{
					try
					{
						EntityTemplateManager.getInstance().loadEntityTemplates("general.templates");
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		};
		m.init(arg0);
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException
	{
		if (shutdown)
			arg0.exit();
		
		if (gameStarted)
		{
			if (offline)
			{
	//			PlayerInputComponentManager.getInstance().sendInputToPlayerInputComponents(null)
	//			SimulationComponentManager.getInstance().simulate(arg1);
				EntityManager.getInstance().tick((float) arg1/1000f);
				SimulationComponentManager.getInstance().simulate((float) arg1/1000f);
				CollisionDetectionComponentManager.getInstance().detectCollisionsAndNotifyEntities();
				PlayerInputComponentManager.getInstance().sendInputToPlayerInputComponents(arg0.getInput());
			}
			else
			{
				float delta = arg1/1000f;
				//System.out.println(delta);
				
				PlayerInputComponentManager.getInstance().sendInputToPlayerInputComponents(arg0.getInput());
				NetSubSystem.getInstance().pollMessages();
				// CollisionDetectionComponentManager.getInstance().detectCollisionsAndNotifyEntities();
				NetSubSystem.getInstance().simulateGhosts(delta);
				EntityManager.getInstance().tick(delta);
				NetSubSystem.getInstance().sendBufferedMessages();
				
				EntityManager.getInstance().cleanUpEntities();
			}
		}
		else
			m.update(arg0, arg1);
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount)
	{
		m.mouseClicked(button, x, y, clickCount);
	}

	@Override
	public void mousePressed(int button, int x, int y)
	{
		m.mousePressed(button, x, y);
	}

	@Override
	public void mouseReleased(int button, int x, int y)
	{
		m.mouseReleased(button, x, y);
	}

	@Override
	public void keyPressed(int key, char c)
	{
		m.keyPressed(key, c);
	}

	@Override
	public void keyReleased(int key, char c)
	{
		if (key == Input.KEY_ESCAPE)
			shutdown = true;
		m.keyReleased(key, c);
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy)
	{
		m.mouseMoved(oldx, oldy, newx, newy);
	}

	@Override
	public void mouseWheelMoved(int change)
	{
		change = Math.max(-1, Math.min(change, 1));
		m.mouseWheelMoved(change);
	}

	/**
	 * @param args
	 * @throws SlickException
	 */
	public static void main(String[] args) throws SlickException
	{
		AppGameContainer app = new AppGameContainer(new MenuTest());
		//app.setDisplayMode(400, 600, false);
		app.setUpdateOnlyWhenVisible(false);
		app.start();
	}

}
