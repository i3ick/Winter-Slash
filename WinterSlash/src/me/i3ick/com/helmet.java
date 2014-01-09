package me.i3ick.com;






import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;



public class helmet implements Listener{
	
	private freezetagMain plugin;

	public helmet(freezetagMain plugin){
		this.plugin = plugin;
	}

	//saves location before player dies/gets frozen

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event){
		if (event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			if(plugin.ftagplayers.contains(player.getName())){
			if(player.getHealthScale() < 6){
				player.getLocation();
				plugin.getConfig().set(player.getName() + ".X", player.getLocation().getBlockX());
				plugin.getConfig().set(player.getName() + ".Y", player.getLocation().getBlockY());
				plugin.getConfig().set(player.getName() + ".Z", player.getLocation().getBlockZ());
	            plugin.getConfig().options().copyDefaults(true);
	   		    plugin.saveConfig();
				}
			
			}
		}	
	}
    	 
	 
	//Keeps frozen players still
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent e){
		if(plugin.frozen.contains(e.getPlayer().getName())){
			e.getPlayer().teleport(e.getPlayer().getLocation());
		}
	}

	
	//unfreezing a red player 
	@EventHandler
	public void onPlayerDamage_frozen(EntityDamageByEntityEvent event){
		Entity e = event.getEntity();
		Entity attacker = event.getDamager();
		if(e instanceof Player){
		Player player = (Player) e;
		if(plugin.frozenred.contains(player.getName())){
		  if(plugin.ftred.contains(attacker)){
			  if(player.getHealthScale()<4.0){
				  plugin.frozen.remove(player);
				  plugin.frozenred.remove(player);
			  }
		  }
		}
		else{
	}
	}
	}
	
	
	//unfreezing a green player 
	@EventHandler
	public void onPlayerDamage_frozen_green(EntityDamageByEntityEvent event){
		Entity e = event.getEntity();
		Entity attacker = event.getDamager();
		if(e instanceof Player){
		Player player = (Player) e;
		if(plugin.frozengreen.contains(player.getName())){
		  if(plugin.ftgreen.contains(attacker)){
			  if(player.getHealthScale()<4.0){
				  plugin.frozen.remove(player);
				  plugin.frozengreen.remove(player);
			  }
		  }
		}
		else{
	}
	}
	}
	
	
	
	//prevent damage from frozen players / prevent friendly fire / prevent killing frozen players
	@EventHandler
	public void onPlayerDamage_frozen_damage(EntityDamageByEntityEvent event){
		Entity victim_entity = event.getEntity();
		Entity damager_entity = event.getDamager();
		if(victim_entity instanceof Player){
			if(damager_entity instanceof Player){
				Player victim = (Player) victim_entity;
				Player damager = (Player) damager_entity;
				if(plugin.frozen.contains(victim.getName())){
					event.setCancelled(true);
					}
				
				
				//disable FF for red team
				if(plugin.ftred.contains(victim.getName())){
					if(plugin.frozenred.contains(victim.getName())){
						if(plugin.ftred.contains(damager.getName())){
							//do nothing - unfreezeing process
						}
						else{
							event.setCancelled(true);
							//disables other people to harm frozen people
							}
						}
					else{
						if(plugin.ftred.contains(damager.getName())){
							event.setCancelled(true);
							//disables friendly fire
						}
					}
					}
				
				
				//disable FF for green team
				else if(plugin.ftgreen.contains(victim.getName())){
					if(plugin.frozengreen.contains(victim.getName())){
						if(plugin.ftgreen.contains(damager.getName())){
							//do nothing - unfreezeing process
						}
						else{
							event.setCancelled(true);
							//disables other people to harm frozen people
							}
						}
					else{
						if(plugin.ftgreen.contains(damager.getName())){
							event.setCancelled(true);
							//disables friendly fire
						}
					}
					}
				}
			else{
				return;
				}
			}
		}
			
	
	//Pack-a-Punch device
	     
	    //sign setup
	@EventHandler
	public void onSignCreation(SignChangeEvent e){
		Player player = (Player) e.getPlayer();
		if(!player.hasPermission("freezetag.signplace")){
			player.sendMessage("No permission");
			return;
		}
		if(e.getLine(0).equals("/ftpap")){
			e.setLine(0, ChatColor.BLUE + ChatColor.BOLD.toString() + "Pack");
			e.setLine(1, ChatColor.YELLOW + ChatColor.BOLD.toString() + "a");
			e.setLine(2, ChatColor.GREEN + ChatColor.BOLD.toString() + "Punch");
			e.setLine(3, ChatColor.MAGIC.toString() + ChatColor.BOLD.toString() + "fresh fish");
		}
	}
	
	//remove from game on disconnect
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e){
		Player player = e.getPlayer();
		plugin.ftagplayers.remove(player.getName());
		
	}
	
	
	
	    //Pack-a-Punch logic
	@EventHandler
	public void onPaP(PlayerInteractEvent e){
		Player player = (Player) e.getPlayer();
		ItemStack revivor = new ItemStack(Material.BLAZE_ROD,1);
		ItemStack revivorUpgrade = new ItemStack(Material.BONE,1);
		int newItemSlot = player.getInventory().firstEmpty();
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if(e.getClickedBlock().getType().equals(Material.WALL_SIGN)){	
				    revivorUpgrade.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
				    player.getInventory().setItem(newItemSlot, revivorUpgrade);
				    player.getInventory().removeItem(revivor);
				
			}
		}
	}
	
	
	
	
	//should disable damage done when jumping but not working => needs fixing
	@EventHandler
	public void onJump(EntityDamageByEntityEvent e){
		Entity ev = e.getEntity();
		if(ev instanceof Player){
		Player player = (Player)e.getEntity();
		 if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR){
			 e.setCancelled(true);
		 }
		}
	}
	
	//blocks commands once a player is ingame
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onServerCommand(PlayerCommandPreprocessEvent e){
		if(plugin.ftred.contains(e.getPlayer().getName())){
			if(e.getMessage().equals("/ftl")){
			return;
			}
			else{
				if(e.getPlayer().isOp()){
					return;
				}
				else{
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.YELLOW + "Can't use commands while in game. Use /ftl to leave");
				}
			}
		}
		
		
		else if(plugin.ftgreen.contains(e.getPlayer().getName())){
				
			if(e.getMessage().equals("/ftl")){	
			}
			
			else{
                if(e.getPlayer().isOp()){
					return;
				}
				else{
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.YELLOW + "Can't use commands while in game. Use /ftl to leave");
				}
			}
			}
		else{
			return;
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void checkDeath(PlayerDeathEvent event) {
		Player p = event.getEntity().getPlayer();
		if (plugin.ftagplayers.contains(p.getName())) {
			p.sendMessage("test");
		  if (isRedTeam(p)) {
			  // if player isn't frozen, freeze him. If he is, unfreeze him.
		    if (!isFrozenRed(p)) {
		       plugin.frozenred.add(p.getName());
		       p.sendMessage("test");
		       }
		    else{
		    	plugin.frozenred.remove(p.getName());
		    }
		    if (plugin.frozengreen.size() > plugin.frozenred.size()) {
		    //  end the game...
		    Bukkit.broadcastMessage(ChatColor.GREEN + "The RED team has won the game!");
		    return;
		    }
		  }
		    else if (isGreenTeam(p)) {
		      if (!isFrozenGreen(p)) {
	        	plugin.frozengreen.add(p.getName());
		          }
		      if (plugin.frozenred.size() > plugin.frozengreen.size()) {
		      //  end the game...
		      Bukkit.broadcastMessage(ChatColor.GREEN + "The GREEN team has won the game!");
		      return;
	        	}
		    }
		   else{
		   return;
	    	}
		}
	}
		  
		  
		
		 
		public boolean isRedTeam(Player p) {
		return plugin.ftred.contains(p);
		}
		 
		public boolean isGreenTeam(Player p) {
		return plugin.ftgreen.contains(p);
		}
		 
		public boolean isFrozenGreen(Player p) {
		return plugin.frozengreen.contains(p);
		}
		 
		public boolean isFrozenRed(Player p) {
		return plugin.frozenred.contains(p);
		}


	

	//teleports players back to the position where they died so they can be frozen
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e){
		if(plugin.ftred.contains(e.getPlayer().getName())){
			String player =  e.getPlayer().getName().toString();

			e.getPlayer().getInventory().setHelmet(new ItemStack(Material.ICE,1));

			
			
			// tp to death position
			int lastposX = plugin.getConfig().getInt(player + ".X");
			int lastposY = plugin.getConfig().getInt(player + ".Y");
			int lastposZ = plugin.getConfig().getInt(player + ".Z");
			String playerWorld = plugin.getConfig().getString("redspawn" + ".World");
			 World world = Bukkit.getWorld(playerWorld);
			 
			  if(world != null)
		         {
		        	 Location lastpos = new Location((World) world, lastposX, lastposY, lastposZ);
		        	 e.setRespawnLocation(lastpos);
		         }
		         else
		         {
		        	 Bukkit.getServer().createWorld(new WorldCreator(playerWorld).environment(World.Environment.NORMAL));
		             plugin.getLogger().warning("The '" + "redspawn" + ".World" + "' world from config.yml does not exist or is not loaded !");
		         }	  
			 
			 
	}
		
		
		
		
		else if(plugin.ftgreen.contains(e.getPlayer().getName())){
			Player player = (Player) e.getPlayer();
			plugin.frozen.add(player.getName());
			plugin.frozengreen.add(player.getName());
			player.getInventory().setHelmet(new ItemStack(Material.ICE,1));
			
			int lastposX = plugin.getConfig().getInt(e.getPlayer().getName() + ".X");
			int lastposY = plugin.getConfig().getInt(e.getPlayer().getName() + ".Y");
			int lastposZ = plugin.getConfig().getInt(e.getPlayer().getName() + ".Z");
			String playerWorld = plugin.getConfig().getString("redspawn" + ".World");
			 World world = Bukkit.getWorld(playerWorld);
			 
			  if(world != null)
		         {
		        	 Location lastpos = new Location((World) world, lastposX, lastposY, lastposZ);
		        	 e.setRespawnLocation(lastpos);
		         }
		         else
		         {
		        	 Bukkit.getServer().createWorld(new WorldCreator(playerWorld).environment(World.Environment.NORMAL));
		             plugin.getLogger().warning("The '" + "redspawn" + ".World" + "' world from config.yml does not exist or is not loaded !");
		         }	  
			 
			 
	
		}
		return;
		
	}
	
	
	
	
	
	
}
