package gdw.entityCore;

import gdw.network.NetSubSystem;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityTemplate {
	private String name;
	private ArrayList<String> baseTemplates;
	private HashMap<String,ComponentTemplate> componentTemplateMap=new HashMap<String,ComponentTemplate>();
	private HashMap<String,HashMap<String,String>> componentParamsMap;
	
	public EntityTemplate(String name, ArrayList<String> baseTemplates, HashMap<String, HashMap<String, String> > componentParamsMap){
		this.name = name;
		this.baseTemplates = baseTemplates;
		this.componentParamsMap = componentParamsMap;
		
		//TODO: Merge base template parameters
		
		for(String compName: componentParamsMap.keySet()){
			ComponentTemplate compTemplate = ComponentTemplateFactory.getInstance().createComponentTemplate(compName, componentParamsMap.get(compName));
			this.componentTemplateMap.put(compName, compTemplate);
		}
	}

	//Wird von server-seitigem Code aufgerufen um Entities zu spawnen:
	public Entity createEntity(float whereX,float whereY, float orientation){
		int id = EntityManager.getInstance().getNextID();
		NetSubSystem.instance().sendSpawn(name,id,whereX,whereY,orientation);
		return createEntity(id, whereX, whereY, orientation);
	}
	
	//Wird vom Netzwerkcode auf dem Client aufgerufen, um Entities zu replizieren:
	public Entity createEntity(int id, float whereX,float whereY, float orientation){
		Entity ent = EntityManager.getInstance().createEntity(id, whereX, whereY, orientation, this);
		for(ComponentTemplate compTemplate: componentTemplateMap.values()){
			if(compTemplate.isThingOnly() && !NetSubSystem.instance().isServer()) continue;
			ent.addComponent(compTemplate.createComponent());
		}
		return ent;
	}
}
