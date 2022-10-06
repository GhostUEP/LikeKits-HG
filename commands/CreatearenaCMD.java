package me.ghost.hg.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.ghost.hg.Main;
import me.ghost.hg.utils.WorldEditAPI;

public class CreatearenaCMD implements CommandExecutor {
	public Main m;

	public CreatearenaCMD(Main m) {
		this.m = m;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("createarena")) {
			Player p = (Player) sender;
			if (m.perm.isMod(p)) {
				if (args.length == 3) {
					int altura = 0;
					try {
						altura = Integer.valueOf(args[2]);
					} catch (Exception e) {
						sender.sendMessage("§cAltura tem que ser um numero");
						return true;
					}
					if (args[0].equalsIgnoreCase("circulo")) {
						int radius = 0;
						try {
							radius = Integer.valueOf(args[1]);
						} catch (Exception e) {
							sender.sendMessage("§cRaio tem que ser um numero");
							return true;
						}
						WorldEditAPI.createArenaCirculo(p.getLocation(), radius, altura);
					} else if (args[0].equalsIgnoreCase("quadrado")) {
						if (!args[1].contains("x")) {
							sender.sendMessage("§cComprimento tem que seguir o padrao");
							return true;
						}
						int comprimentoX = 0;
						String cX = args[1].split("x")[0];
						String cZ = args[1].split("x")[1];
						try {
							comprimentoX = Integer.valueOf(cX);
						} catch (Exception e) {
							sender.sendMessage("§cComprimentoX tem que ser um numero");
							return true;
						}
						int comprimentoZ = 0;
						try {
							comprimentoZ = Integer.valueOf(cZ);
						} catch (Exception e) {
							sender.sendMessage("§cComprimentoZ tem que ser um numero");
							return true;
						}
						WorldEditAPI.createArenaQuadrado(p.getLocation(), comprimentoX, altura, comprimentoZ);
						p.sendMessage("§fArena criada com §6X: " + comprimentoX + " Z: " + comprimentoZ + " Altura: "
								+ altura);
						return true;
					} else {
						sender.sendMessage(
								"§cUse: /createarena [circulo | quadrado] [raio | comprimentoXcomprimento] [altura]");
					}
				} else {
					sender.sendMessage(
							"§cUse: /createarena [circulo | quadrado] [raio | comprimentoXcomprimento] [altura]");
				}
			} else {
				sender.sendMessage("§cVoce nao possui permissao");
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("wand")) {
			Player p = (Player) sender;
			if (m.perm.isMod(p)) {
				if (m.wands.containsKey(p.getUniqueId())) {
					sender.sendMessage("§dVoce desabilitou a Wand");
					m.wands.remove(p.getUniqueId());
					return true;
				} else {
					sender.sendMessage("§dVoce agora pode usar sua Wand");
					p.setItemInHand(new ItemStack(Material.WOOD_AXE));
					m.wands.put(p.getUniqueId(), new me.ghost.hg.utils.Wand(null, null));
					return true;
				}
			} else {
				sender.sendMessage("§cSem permissao");
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("set")) {
			Player p = (Player) sender;
			if (m.perm.isMod(p)) {
				if (args.length == 1) {
					if (!m.wands.containsKey(p.getUniqueId())) {
						return true;
					}
					byte id = 0;
					Material mat = null;
					if (args[0].contains(":")) {
						String[] arg = args[0].split(":");
						boolean eInteger = false;
						int blockId = 0;
						try {
							blockId = Integer.valueOf(arg[0]);
							eInteger = true;
						} catch (Exception e) {
						}
						if (eInteger) {
							try {
								mat = Material.getMaterial(blockId);
							} catch (Exception e) {
								p.sendMessage("§cDigite um id valido!");
								return true;
							}
						} else {
							try {
								mat = Material.valueOf(arg[0].toUpperCase());
							} catch (Exception e) {
								p.sendMessage("§cDigite um item valido!");
								return true;
							}
						}
						try {
							id = Byte.parseByte(arg[1]);
						} catch (Exception e) {
							sender.sendMessage("§cData tem que ser um numero");
							return true;
						}
					} else {
						boolean eInteger = false;
						int blockId = 0;
						try {
							blockId = Integer.valueOf(args[0]);
							eInteger = true;
						} catch (Exception e) {
						}
						if (eInteger) {
							try {
								mat = Material.getMaterial(blockId);
							} catch (Exception e) {
								p.sendMessage("§cDigite um id valido!");
								return true;
							}
						} else {
							try {
								mat = Material.valueOf(args[0].toUpperCase());
							} catch (Exception e) {
								p.sendMessage("§cDigite um item valido!");
								return true;
							}
						}
					}

					if (mat == null) {
						p.sendMessage("§cDigite um item valido!");
						return true;
					}
					me.ghost.hg.utils.Wand wand = m.wands.get(p.getUniqueId());
					if (wand.getFirst() == null) {
						sender.sendMessage("§cVoce nao possui a primeira localizacao setada.");
						return true;
					}
					if (wand.getSecond() == null) {
						sender.sendMessage("§cVoce nao possui a segunda localizacao setada.");
						return true;
					}
					int mudados = WorldEditAPI.set(p.getUniqueId(), wand, mat, id);
					sender.sendMessage("§c" + mudados + " blocos foram mudados para " + mat.toString());
					return true;
				} else {
					sender.sendMessage("§cUse: /set [material:id]");
				}
			} else {
				sender.sendMessage("§cSem permissao");
			}
			return true;
		}
		return false;
	}
}