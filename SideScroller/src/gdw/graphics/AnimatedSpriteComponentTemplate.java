package gdw.graphics;

import gdw.entityCore.Component;
import gdw.entityCore.ComponentTemplate;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;


public class AnimatedSpriteComponentTemplate extends ComponentTemplate
 {
	private float scale;
	private Color filter;
	private float pivotX;
	private float pivotY;
	private int layer;
	private boolean flipped = false;
	
	private SpriteSheet spriteSheet;
	private int[] cycleLength;
	private int cycle;
	private int step;
	private float animationSpeed;

	public float getScale() {
		return scale;
	}

	public Color getFilter() {
		return filter;
	}

	public float getPivotX() {
		return pivotX;
	}

	public float getPivotY() {
		return pivotY;
	}

	public int getLayer() {
		return layer;
	}

	public boolean isFlipped() {
		return flipped;
	}
	
	public SpriteSheet getSpriteSheet() {
		return spriteSheet;
	}

	public int[] getCycleLength() {
		return cycleLength;
	}

	public int getCycle() {
		return cycle;
	}

	public int getStep() {
		return step;
	}
	
	public AnimatedSpriteComponentTemplate(HashMap<String, String> params)
	{
		super(params);
		try
		{
			spriteSheet = new SpriteSheet(getStringParam("spriteSheet", getStringParam("path", "./assets/spritesheets/error_sprite.png")), getIntegerParam("tileWidth", 64), getIntegerParam("tileHeight", 64));
		} catch (SlickException e)
		{
			e.printStackTrace();
		}
		
		scale = getFloatParam("scale", 1.0f);
		filter = new Color(getFloatParam("filterRed", 1.0f), getFloatParam("filterGreen", 1.0f), getFloatParam("filterBlue", 1.0f));
		pivotX = getFloatParam("pivotX", 0.0f);
		pivotY = getFloatParam("pivotY", 0.0f);
		layer = getIntegerParam("layer", 1);
		if(getIntegerParam("flipped", 0) == 1) flipped = true;
		String cycleLengthStr = getStringParam("cycleLength", "");
		String[] cycleLengthStrs = cycleLengthStr.split(";");
		
		cycleLength = new int[spriteSheet.getVerticalCount()];
		for(int i = 0; i < cycleLength.length; ++i){
			if(i<cycleLengthStrs.length){
				try {
					cycleLength[i] = Integer.parseInt(cycleLengthStrs[i]);
				} catch (Exception e) {
					cycleLength[i] = spriteSheet.getHorizontalCount();
				}
			}
			else{
				cycleLength[i] = spriteSheet.getHorizontalCount();
			}
		}
			
		cycle = getIntegerParam("cycle", 0);
		step = getIntegerParam("step", 0);
		animationSpeed = getFloatParam("animationSpeed", 1.0f);
	}
	
	public float getAnimationSpeed() {
		return animationSpeed;
	}

	public Component createComponent()
	{
		return new AnimatedSpriteComponent(this);
	}
	
	public static boolean isGhostOnly()
	{
		return true;
	}
}
