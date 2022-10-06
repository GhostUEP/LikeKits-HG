package me.ghost.hg.manager;

import java.util.HashMap;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.ClassGetter;

import org.bukkit.event.Listener;

public class AbilityManager {
	private HashMap<String, KitInterface> abilities = new HashMap<String, KitInterface>();
	private Main m;

	public AbilityManager(Main m) {
		this.m = m;
		initializeAllAbilitiesInPackage("me.ghost.hg.kits");
	}

	public void initializeAllAbilitiesInPackage(String packageName) {
		int i = 0;
		for (Class<?> abilityClass : ClassGetter.getClassesForPackage(m, packageName)) {
			if (KitInterface.class.isAssignableFrom(abilityClass)) {
				try {
					KitInterface abilityListener;
					try {
						abilityListener = (KitInterface) abilityClass.getConstructor(Main.class).newInstance(m);
					} catch (Exception e) {
						abilityListener = (KitInterface) abilityClass.newInstance();
					}
					abilities.put(abilityClass.getSimpleName(), abilityListener);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("Erro no Kit: " + abilityClass.getSimpleName());
				}
				i++;
			}
		}
		m.getLogger().info(i + " habilidades carregadas");
	}

	public void registerAbilityListeners() {
		for (Listener abilityListener : abilities.values()) {
			m.getServer().getPluginManager().registerEvents(abilityListener, m);
		}
	}
}
