package gdw.genericBehavior;

import java.util.HashMap;

import gdw.entityCore.Component;
import gdw.entityCore.ComponentTemplate;

public class AttachableComponentTemplate extends ComponentTemplate
{

	protected AttachableComponentTemplate(HashMap<String, String> params)
	{
		super(params);
	}

	@Override
	public Component createComponent()
	{
		return new AttachableComponent(this);
	}

}