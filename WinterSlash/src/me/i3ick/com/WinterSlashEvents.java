package me.i3ick.com;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
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

import com.mysql.jdbc.integration.jboss.ExtendedMysqlExceptionSorter;

public class WinterSlashEvents implements Listener {

    private WinterSlashMain plugin;

    public WinterSlashEvents(WinterSlashMain plugin) {
        this.plugin = plugin;
    }
    
	public static boolean isInt(String sender) {
	    try {
	        Integer.parseInt(sender);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}

    //saves location before player dies/gets frozen

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            
            FileConfiguration ins = WinterSlashMain.getInstance().getConfig();
            Logger debug = WinterSlashMain.getInstance().getLogger();
            ConfigurationSection sec = WinterSlashMain.getInstance().getConfig().getConfigurationSection("arenas");
            for (String arenas: sec.getKeys(false)) {
            	WinterSlashArena arena = WinterSlashManager.getManager().getArena(arenas);
            	if(arena.getPlayers().contains(player.getName())){
            		debug.info("error1");
                    player.getLocation();
                    ins.set("DeathPosition." + player.getName() + ".X", player.getLocation().getBlockX());
                    ins.set("DeathPosition." + player.getName() + ".Y", player.getLocation().getBlockY());
                    ins.set("DeathPosition." + player.getName() + ".Z", player.getLocation().getBlockZ());
                    WinterSlashMain.getInstance().saveConfig();
                 return;
            }
        }
    }
    }

    //Keeps frozen players still - Disable movement
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent e) {
    	
    	ConfigurationSection sec = WinterSlashMain.getInstance().getConfig().getConfigurationSection("arenas");
        for (String arenas: sec.getKeys(false)) {
        	WinterSlashArena arena = WinterSlashManager.getManager().getArena(arenas);
        	if(arena.getPlayers().contains(e.getPlayer().getName())){
    	
    
    	 if (!(arena.GetFrozen().contains(e.getPlayer().getName()))) {
            e.getPlayer().teleport(e.getPlayer().getLocation());
        }
    	 return;
        }
        	}
        }

    /*
    //unfreezing a red player
    @EventHandler
    public void onPlayerDamage_frozen(EntityDamageByEntityEvent event) {
        Entity e = event.getEntity();
        Entity attacker = event.getDamager();
        if (e instanceof Player) {
            Player player = (Player) e;
            if (plugin.frozenred.contains(player.getName())) {
                if (plugin.wsred.containsValue(attacker)) {
                    if (player.getHealthScale() < 4.0) {
                        plugin.frozen.remove(player);
                        plugin.frozenred.remove(player);
                    }
                }
            } else {
            }
        }
    } 
    
    
    //unfreezing a green player
    @EventHandler
    public void onPlayerDamage_frozen_green(EntityDamageByEntityEvent event) {
        Entity e = event.getEntity();
        Entity attacker = event.getDamager();
        if (e instanceof Player) {
            Player player = (Player) e;
            if (plugin.frozengreen.contains(player.getName())) {
                if (plugin.wsgreen.containsValue(attacker)) {
                    if (player.getHealthScale() < 4.0) {
                        plugin.frozen.remove(player);
                        plugin.frozengreen.remove(player);
                    }
                }
            }
        }
    } */

    //prevent damage from frozen players / prevent friendly fire / prevent killing frozen players
    @EventHandler
    public void onPlayerDamage_frozen_damage(EntityDamageByEntityEvent event) {
        Entity victim_entity = event.getEntity();
        Entity damager_entity = event.getDamager();
        if (victim_entity instanceof Player) {
            if (damager_entity instanceof Player) {
                Player victim = (Player) victim_entity;
                Player damager = (Player) damager_entity;
                
                ConfigurationSection sec = WinterSlashMain.getInstance().getConfig().getConfigurationSection("arenas");
                for (String arenas: sec.getKeys(false)) {
                	WinterSlashArena arena = WinterSlashManager.getManager().getArena(arenas);
                	if(arena.getPlayers().contains(victim.getName())){
                	
                	
           

                //disable FF for red team
             	if (arena.ifPlayerIsRed(victim)) {
                    if (arena.GetRedFrozenTeam().contains(victim)) {
                       	if (arena.GetRedTeam().contains(damager.getName())) {
                            //do nothing - unfreezeing process
                        } else {
                            event.setCancelled(true);
                            //disables other people to harm frozen people
                        }
                    } else {
                       	 if (arena.GetRedTeam().contains(damager.getName())) {	
                        
                            event.setCancelled(true);
                            //disables friendly fire
                        }
                    }
                }
             	
             	 //disable FF for green team
             	else if (!(arena.ifPlayerIsRed(victim))) {
                    if (arena.GetGreenFrozenTeam().contains(victim)) {
                       	if (arena.GetGreenTeam().contains(damager.getName())) {
                            //do nothing - unfreezeing process
                        } else {
                            event.setCancelled(true);
                            //disables other people to harm frozen people
                        }
                    } else {
                       	 if (arena.GetGreenTeam().contains(damager.getName())) {	
                        
                            event.setCancelled(true);
                            //disables friendly fire
                        }
                    }
                }
             	return;
             	}
                }
            }
        }
     }


  //Force start device

    //sign setup
    @EventHandler
    public void onSignCreation1(SignChangeEvent e) {
        Player player = (Player) e.getPlayer();
        if (!player.hasPermission("freezetag.signplace")) {
            player.sendMessage("No permission");
            return;
        }

        if (e.getLine(0).equals("/forcestart")) {
        	String arenan = e.getLine(1).toString();
            ConfigurationSection sec = WinterSlashMain.getInstance().getConfig().getConfigurationSection("arenas");
            for (String arenas: sec.getKeys(false)) {
	        	WinterSlashArena arena = WinterSlashManager.getManager().getArena(arenas);
	        	if(arena.getName().equals(arenan)){
	        		e.setLine(0, ChatColor.YELLOW + ChatColor.BOLD.toString() + "Punch 2");
	                e.setLine(1, ChatColor.YELLOW + ChatColor.BOLD.toString() + "Force Start");
	        		e.setLine(2, arenan);
	                e.setLine(3, ChatColor.MAGIC.toString() + ChatColor.BOLD.toString() + "stinky fish");
	                return;
	        	}
        }
      	}
        }
    
    
  //Force start logic
    @EventHandler
    public void onForceStart(PlayerInteractEvent e) {
        Player player = (Player) e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getType() == Material.SIGN_POST|| e.getClickedBlock().getType() == Material.WALL_SIGN) {
            	Sign sign = (Sign) e.getClickedBlock().getState();
            	if(sign.getLine(0).equalsIgnoreCase(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Punch 2")){
            	 ConfigurationSection sec = WinterSlashMain.getInstance().getConfig().getConfigurationSection("arenas");
                 for (String arenas: sec.getKeys(false)) {
                 	WinterSlashArena arena = WinterSlashManager.getManager().getArena(arenas);
                 	if(arena.getPlayers().contains(e.getPlayer().getName())){
            	
            	int playersLeft = WinterSlashMain.getInstance().getConfig().getInt(".MinPlayerNumber") - arena.GetSign().size();
            	if(playersLeft < 1){
            		String arenan = sign.getLine(2).toString();
            		WinterSlashManager.getManager().startArena(arenan);
            	}else{
            	arena.sendMessage(ChatColor.DARK_PURPLE.toString() + playersLeft + " players still need to click the sign.");
            	}return;}
                 	else{
                 	}
                 	}
            }
            }
        }
    }
    
    
    
    
    //Pack-a-Punch device

    //sign setup
    @EventHandler
    public void onSignCreation2(SignChangeEvent e) {
        Player player = (Player) e.getPlayer();
        if (!player.hasPermission("freezetag.signplace")) {
            player.sendMessage("No permission");
            return;
        }
        if (e.getLine(0).equals("/wspap")) {
            e.setLine(0, ChatColor.BLUE + ChatColor.BOLD.toString() + "Pack");
            e.setLine(1, ChatColor.YELLOW + ChatColor.BOLD.toString() + "a");
            e.setLine(2, ChatColor.GREEN + ChatColor.BOLD.toString() + "Punch");
            e.setLine(3, ChatColor.MAGIC.toString() + ChatColor.BOLD.toString() + "fresh fish");
        }
    }

    //remove from game on disconnect
    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        plugin.wsplayersHM.remove(player.getName());

    }
    
    

    Material[] REVIVEITEMS = {Material.BLAZE_ROD,Material.BONE};
        
    //Prevent reviving tools from causing damage
    	@EventHandler(priority = EventPriority.NORMAL,ignoreCancelled = true)
    	public void revividamage(EntityDamageByEntityEvent event)
    	{
    		if (!(event.getEntity() instanceof Player)) {
    			return;
    		}
    		if (!(event.getDamager() instanceof Player)) {
   			return;
    		}
    		//ranged attacks ignored, they have arrow proxy as damage source
    		
    		Player px = (Player) event.getDamager();
    		
    		//obviously we don't want to cancel tool damage outside arena
    		//if player not registered to PlayerWrapper - he outside arena
    		if (WSA_PlayerWrapperImpl.getWrapperContainer(px) == null) return;
    		Material m = px.getItemInHand().getType();
    		
    		for (Material xm : REVIVEITEMS)
    		{
    			if (xm == m)
    			{
    				event.setCancelled(true);
    				return;
    			}
    		}
    		
   
    		
    	}

    //Pack-a-Punch logic
    @EventHandler
    public void onPaP(PlayerInteractEvent e) {
        Player player = (Player) e.getPlayer();
        ItemStack revivor = new ItemStack(Material.BLAZE_ROD, 1);
        ItemStack revivorUpgrade = new ItemStack(Material.BONE, 1);
        int newItemSlot = player.getInventory().firstEmpty();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getType() == Material.SIGN_POST|| e.getClickedBlock().getType() == Material.WALL_SIGN) {
            	Sign sign = (Sign) e.getClickedBlock().getState();
            	if(sign.getLine(0).equalsIgnoreCase(ChatColor.BLUE + ChatColor.BOLD.toString() + "Pack")){
                revivorUpgrade.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
                player.getInventory().setItem(newItemSlot, revivorUpgrade);
                player.getInventory().removeItem(revivor);
            	}
            }
        }
    }

    
    /*
    //Block damage while in air
    //Think i fixed
    @EventHandler
    public void onJump(EntityDamageByEntityEvent e) {
        Entity ev = e.getEntity();
        if (ev instanceof Player) {
            Player player = (Player) e.getEntity();
            if (player.getLocation().getBlock().getRelative(0, -1, 0).isEmpty()) {
                e.setCancelled(true);
            }
        }
    } */

    //blocks commands once a player is ingame
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerCommand(PlayerCommandPreprocessEvent e) {
        //get arena
    	 ConfigurationSection sec = WinterSlashMain.getInstance().getConfig().getConfigurationSection("arenas");
         for (String arenas: sec.getKeys(false)) {
         	WinterSlashArena arena = WinterSlashManager.getManager().getArena(arenas);
         	if(arena.getPlayers().contains(e.getPlayer().getName())){
            if (e.getMessage().equals("/wsl")) {
                return;
            } else {
                if (e.getPlayer().isOp()) {
                    return;
                } else {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.YELLOW + "Can't use commands while in game. Use /wsl to leave");
                }
            }
            return;
        } 
      }
    }

    
    //Game ending & freezing/unfreezing logic
    @EventHandler(priority = EventPriority.HIGHEST)
    public void checkDeath(PlayerDeathEvent e) {
        Entity entity = e.getEntity();
        Player p;
        if (entity instanceof Player) {
            p = (Player) entity;
        } else {
            return;
        }

/*
        if (!((p.getHealthScale() == 0))) { //This is not wrong. http://forums.bukkit.org/threads/oops-i-didnt-break-your-plugins-ambiguous-gethealth.156975/
           WinterSlashMain.getInstance().getLogger().info("debug1");
        	return;
            //If he is not going to die.
        }
        */
        

      // GAME WINNING LOGIC  
        
        //Method for getting the arena name which contains specific player
        ConfigurationSection sec = WinterSlashMain.getInstance().getConfig().getConfigurationSection("arenas");
        for (String arenas: sec.getKeys(false)) {
        	WinterSlashArena arena = WinterSlashManager.getManager().getArena(arenas);
        	if(arena.getPlayers().contains(p.getName())){
        	
        		/*
        		// End game if green wins
        		if (arena.GetGreenFrozenTeam().size() == arena.GetGreenTeam().size()) {
                    //  end the game...
                    Bukkit.broadcastMessage(ChatColor.GREEN + "The GREEN team has won the game!");
                    return;
                }
        		// End game if red wins
                if (arena.GetRedFrozenTeam().size() == arena.GetRedTeam().size()) {
                    //  end the game...
                    Bukkit.broadcastMessage(ChatColor.GREEN + "The RED team has won the game!");
                    return;
                } */
        		
                
                if (arena.ifPlayerIsRed(p)) {
                    // if player isn't frozen, freeze him. If he is, unfreeze him.
                    if (!arena.GetFrozen().contains(p.getName())) {
                        arena.SetFrozen(p.getName());
                        arena.FrozenRedAdd(p.getName());
                    }
                    else{
                    	arena.UnsetFrozen(p.getName());
                    	arena.FrozenRedRemove(p.getName());
                    }
                } else if (!(arena.ifPlayerIsRed(p))) {
                    if (!arena.GetFrozen().contains(p.getName())) {
                    	arena.SetFrozen(p.getName());
                    	arena.FrozenGreenAdd(p.getName());
                    }
                    else{
                    	arena.UnsetFrozen(p.getName());
                    	arena.FrozenGreenRemove(p.getName());
                    }
                }
        		return;
        	}
        }
    }

    public boolean isRedTeam(Player p) {
        return plugin.wsred.containsValue(p);
    }

    public boolean isGreenTeam(Player p) {
        return plugin.wsgreen.containsValue(p);
    }

    public boolean isFrozenGreen(Player p) {
        return plugin.frozengreen.contains(p);
    }

    public boolean isFrozenRed(Player p) {
        return plugin.frozenred.contains(p);
    }
    
    
    //datastorage layout
    public int isFrozen(Player p)
    	{
    		return WSA_PlayerWrapperImpl.getWrapperContainer(p).Frozen;
    	}
    	
    	public boolean isTeamRed(Player p)
    	{
    		return WSA_PlayerWrapperImpl.getWrapperContainer(p).Team == WSA_PlayerWrapperImpl.TEAM_RED;
    	}
    	
    	public boolean isTeamRedAndFrozen(Player p)
    	{
    		return WSA_PlayerWrapperImpl.getWrapperContainer(p).Team == WSA_PlayerWrapperImpl.TEAM_RED
    			&& WSA_PlayerWrapperImpl.getWrapperContainer(p).Frozen != 0;
    	}
    

    // RESPAWN EVENTS
    // RESPAWN EVENTS
    // RESPAWN EVENTS


    //teleports players back to the position where they died so they can be frozen
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e){
    	Player p = e.getPlayer();
    	 ConfigurationSection sec = WinterSlashMain.getInstance().getConfig().getConfigurationSection("arenas");
         for (String arenas: sec.getKeys(false)) {
         	WinterSlashArena arena = WinterSlashManager.getManager().getArena(arenas);
         	if(arena.getPlayers().contains(p.getName())){
         		
         		
         		//if p is red
                 if (arena.ifPlayerIsRed(p)) {
            if (arena.GetFrozen().contains(p.getName())){
            	e.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR,1));
            	p.teleport(arena.getRedSpawn());
            }
            else{
            	e.getPlayer().getInventory().setHelmet(new ItemStack(Material.ICE,1));
            	 FileConfiguration ins = WinterSlashMain.getInstance().getConfig();
            	// tp to death position
                int lastposX = ins.getInt("DeathPosition." + p.getName() + ".X");
                int lastposY = ins.getInt("DeathPosition." + p.getName() + ".Y");
                int lastposZ = ins.getInt("DeathPosition." + p.getName() + ".Z");
                String playerWorld = plugin.getConfig().getString("Worlds" + ".World");
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
        }



               //if p is green
                else if(!(arena.ifPlayerIsRed(p))) {
            if (arena.GetFrozen().contains(p.getName())){
            	e.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR,1));
            	p.teleport(arena.getGreenSpawn());
            }
            else{
            	e.getPlayer().getInventory().setHelmet(new ItemStack(Material.ICE,1));
            	  FileConfiguration ins = WinterSlashMain.getInstance().getConfig();
            	// tp to death position
                int lastposX = ins.getInt("DeathPosition." + p.getName() + ".X");
                int lastposY = ins.getInt("DeathPosition." + p.getName() + ".Y");
                int lastposZ = ins.getInt("DeathPosition." + p.getName() + ".Z");
                String playerWorld = ins.getString("Worlds" + ".World");
                World world = Bukkit.getWorld(playerWorld);

                if(world != null)
                {
                    Location lastpos = new Location((World) world, lastposX, lastposY, lastposZ);
                    e.setRespawnLocation(lastpos);
                    p.teleport(lastpos);
                }
                else
                {
                    Bukkit.getServer().createWorld(new WorldCreator(playerWorld).environment(World.Environment.NORMAL));
                    plugin.getLogger().warning("The '" + "redspawn" + ".World" + "' world from config.yml does not exist or is not loaded !");
                } 	
            }
            }
                 return;
                 }
         	}
         }
    
    
    
}
