package gdw.entityCore;

import gdw.collisionReaction.CollisionReactionComponentTemplate;
import gdw.control.PlayerInputComponentTemplate;
import gdw.genericBehavior.AttachableComponentTemplate;
import gdw.genericBehavior.AttachmentComponentTemplate;
import gdw.genericBehavior.FollowComponentTemplate;
import gdw.graphics.AnimatedSpriteComponent;
import gdw.graphics.AnimatedSpriteComponentTemplate;
import gdw.graphics.CameraComponent;
import gdw.graphics.CameraComponentTemplate;
import gdw.graphics.OverlayedAnimatedSpriteComponent;
import gdw.graphics.OverlayedAnimatedSpriteComponentTemplate;
import gdw.graphics.StaticSpriteComponent;
import gdw.graphics.StaticSpriteComponentTemplate;
import gdw.network.NetComponentTemplate;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import collisionDetection.AABoxCollisionDetectionComponentTemplate;
import collisionDetection.CircleCollisionDetectionComponentTemplate;
import collisionDetection.OOBoxCollisionDetectionComponentTemplate;

import Physics.SimulationComponentTemplate;

public class ComponentTemplateFactory {
	//Singleton-Stuff:
	private static ComponentTemplateFactory instance = null;
	public static ComponentTemplateFactory getInstance(){
		if(instance==null){
			instance = new ComponentTemplateFactory();
		}
		return instance;
	}
	private ComponentTemplateFactory(){
		componentTemplateClasses.put("StaticSprite", StaticSpriteComponentTemplate.class);
		componentTemplateClasses.put("AnimatedSprite", AnimatedSpriteComponentTemplate.class);
		componentTemplateClasses.put("OverlayedAnimatedSprite", OverlayedAnimatedSpriteComponentTemplate.class);
		componentTemplateClasses.put("Camera", CameraComponentTemplate.class);
		componentTemplateClasses.put("Simulation ", SimulationComponentTemplate.class);
		componentTemplateClasses.put("PlayerInput", PlayerInputComponentTemplate.class);
		componentTemplateClasses.put("AABoxCollisonDetection", AABoxCollisionDetectionComponentTemplate.class);
		componentTemplateClasses.put("OOBoxCollisonDetection", OOBoxCollisionDetectionComponentTemplate.class);
		componentTemplateClasses.put("CircleCollisonDetection", CircleCollisionDetectionComponentTemplate.class);
		componentTemplateClasses.put("CollisionReaction", CollisionReactionComponentTemplate.class);
		componentTemplateClasses.put("Follow", FollowComponentTemplate.class);
		componentTemplateClasses.put("Attachment", AttachmentComponentTemplate.class);
		componentTemplateClasses.put("Attachable", AttachableComponentTemplate.class);
		componentTemplateClasses.put("Network", NetComponentTemplate.class);
		//TODO: Auskommentierung entfernen, wenn Komponenten geschrieben sind.
//		componentTemplateClasses.put("PlayerWeapon", PlayerWeaponComponentTemplate.class);
//		componentTemplateClasses.put("Duckable", DuckableComponentTemplate.class);
//		componentTemplateClasses.put("PlayerBehavior", PlayerBehaviorComponentTemplate.class);
//		componentTemplateClasses.put("EnemyBehavior", EnemyBehaviorComponentTemplate.class);
//		componentTemplateClasses.put("EnemyProjectile", EnemyProjectileComponentTemplate.class);
//		componentTemplateClasses.put("SwitchUser", SwitchUserComponentTemplate.class);
//		componentTemplateClasses.put("Switch", SwitchComponentTemplate.class);
//		componentTemplateClasses.put("Door", DoorComponentTemplate.class);
//		componentTemplateClasses.put("PathFollow", PathFollowComponentTemplate.class);
//		componentTemplateClasses.put("Rainbow", RainbowComponentTemplate.class);
//		componentTemplateClasses.put("StartSpawn", StartSpawnComponentTemplate.class);
//		componentTemplateClasses.put("LevelGoal", LevelGoalComponentTemplate.class);
//		componentTemplateClasses.put("ColorSource", ColorSourceComponentTemplate.class);
//		componentTemplateClasses.put("Colorable", ColorableComponentTemplate.class);
	}

	private HashMap<String, Class<? extends ComponentTemplate>> componentTemplateClasses = new HashMap<>();
	
	public ComponentTemplate createComponentTemplate(String name, HashMap<String, String> params){
		if(componentTemplateClasses.containsKey(name)){
			try {
				return componentTemplateClasses.get(name).getConstructor(HashMap.class).newInstance(params);
			} catch (Exception e) {
				System.err.println("Warnung: " + e.getMessage());
				return null;
			} 
		}
		else return null;
	}
}
