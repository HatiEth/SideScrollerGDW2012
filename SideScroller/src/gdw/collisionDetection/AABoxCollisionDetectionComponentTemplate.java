package gdw.collisionDetection;

import java.util.HashMap;

import gdw.entityCore.Component;
import gdw.entityCore.ComponentTemplate;

public class AABoxCollisionDetectionComponentTemplate extends ComponentTemplate
{
	private float halfExtentX;
	private float halfExtentY;
	
	public AABoxCollisionDetectionComponentTemplate(HashMap<String, String> params)
	{
		super(params);
		
		halfExtentX = getFloatParam("halfExtentX", 1.0f);
		halfExtentY = getFloatParam("halfExtentY", 1.0f);
	}

	@Override
	public Component createComponent()
	{
		return new AABoxCollisionDetectionComponent(this);
	}

	public float getHalfExtentX()
	{
		return halfExtentX;
	}

	public float getHalfExtentY()
	{
		return halfExtentY;
	}
}