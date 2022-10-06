package me.ghost.hg.kits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.ghost.hg.Main;
import me.ghost.hg.constructors.Kit;
import me.ghost.hg.enums.Estagio;
import me.ghost.hg.manager.KitInterface;

public class Camel extends KitInterface {

	public Camel(Main main) {
		super(main);
	}

	@EventHandler
	public void move(PlayerMoveEvent e) {
		if (e.getPlayer().getLocation().getBlock().getBiome() == Biome.DESERT && hasAbility(e.getPlayer())) {
			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
		}
	}

	@EventHandler
	public void blockHit(BlockDamageEvent event) {
		if (hasAbility(event.getPlayer()) && event.getBlock().getType() == Material.CACTUS) {
			if (Main.plugin.stage == Estagio.PREGAME) {
				return;
			}
			event.getBlock().breakNaturally();
		}
	}

	@Override
	public Kit getKit() {
		List<ItemStack> kitItems = new ArrayList<>();
		return new Kit("camel",
				Arrays.asList(new String[] {
						"Inicie a partida com uma habilidade que ao andar na areia fique com speed e quebre cactus mais rápido" }),
				kitItems, null, new ItemStack(Material.POTION));
	}

}
