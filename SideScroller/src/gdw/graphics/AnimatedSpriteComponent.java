package gdw.graphics;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import gdw.entityCore.ComponentTemplate;
import gdw.graphics.SpriteComponent;
/**
 * Animated Sprite Component
 * for animated sprites
 * 
 * @author max
 *
 */
public class AnimatedSpriteComponent extends SpriteComponent {
	public float getAnimationSpeed() {
		return animationSpeed;
	}

	public void setAnimationSpeed(float animationSpeed) {
		this.animationSpeed = animationSpeed;
	}

	/**
	 * Sprite sheet where animation frames are read from. Frames of an animation are stored in row.
	 * Multiple animations can be stored beneath each other in the spritesheet. 
	 */
	private SpriteSheet spriteSheet;
	
	/**
	 * number of Frames in per column of sprites in the spritesheet.
	 */
	private int [] cycleLength;
	
	/**
	 * current column (animation)
	 */
	private int cycle;
	
	/**
	 * current frame
	 */
	private int step;
	
	private float pivotX;
	private float pivotY;
	
	private float stepTime=0;
	
	private float animationSpeed;
	
	public SpriteSheet getImage()
	{
		return spriteSheet;
	}
	
	public SpriteSheet getSpriteSheet()
	{
		return spriteSheet;
	}
	
	public void setSpriteSheet(SpriteSheet s)
	{
		spriteSheet = s;
	}
	
	public int[] getCycleLength()
	{
		return cycleLength;
	}
	
	public void setCycleLength(int [] c)
	{
		cycleLength = c;
	}
	
	public int getStep()
	{
		return step;
	}
	
	/* not in uml-diagram
	public void setStep(int s)
	{
		step = s;
	}
	*/
	
	public AnimatedSpriteComponent(ComponentTemplate template)
	{
		super(template);
		AnimatedSpriteComponentTemplate t = (AnimatedSpriteComponentTemplate) template;
		
		setScale(t.getScale());
		setFilter(t.getFilter());
		setPivotX(t.getPivotX());
		setPivotY(t.getPivotY());
		setLayer(t.getLayer());
		setFlipped(t.isFlipped());
		
		spriteSheet = t.getSpriteSheet();
		cycleLength = t.getCycleLength();
		cycle = t.getCycle();
		step = t.getStep();
		setAnimationSpeed(t.getAnimationSpeed());
		
		pivotX = t.getPivotX();
		pivotY = t.getPivotY();
		
		animationSpeed = t.getAnimationSpeed();
		
		SpriteManager.getInstance().addSprite(this);
	}
	
	
	/**
	 *  draws the current frame of the animation
	 */
	public void draw(float camPosX,float camPosY)
	{
		//TODO: verify this is correct, image might have to be drawn with an offset to be centered at the entity
		Image img = spriteSheet.getSprite(step, cycle);
		if(getFlipped())
			img = img.getFlippedCopy(true, false); //TODO: 
		img.setCenterOfRotation(getPivotX(), getPivotY());
		img.setRotation(getOwner().getOrientation());
		if(getFilter() != null)
			img.draw(camPosX + getOwner().getPosX() - ((img.getWidth() / 2f)*getScale()),
					camPosY + getOwner().getPosY() - ((img.getHeight() / 2f)*getScale()), getScale(),
					getFilter());
		else
			img.draw(camPosX + getOwner().getPosX() - ((img.getWidth() / 2f)*getScale()),
					camPosY + getOwner().getPosY() - ((img.getHeight() / 2f)*getScale()), getScale(),
					getFilter());
	}
	
	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
		resetCycle();
	}

	/**
	 * Advances the animation by one frame, loops around if end of animation is reached
	 * 
	 * @param time passed since last tick
	 */
	public void tick(float deltaTime)
	{
		stepTime+=animationSpeed*deltaTime;
		step = (int) stepTime;
		stepTime=stepTime-step;
		step %= cycleLength[cycle]; //loop back to frame 0 
		stepTime = step + stepTime;
	}
	
	/**
	 * sets step back to 0
	 */
	public void resetCycle()
	{
		step = 0;
		stepTime = 0.0f;
	}
	
	/**
	 * destroys the slick spritesheet
	 */
	protected void destroy()
	{
		SpriteManager.getInstance().removeSprite(this);
	}

}
