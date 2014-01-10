package me.i3ick.com;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;


     public class freezetagMain extends JavaPlugin{
    	  
    	 // Arrays
	 public ArrayList<String> frozen = new ArrayList<String>();
	 public ArrayList<String> frozenred = new ArrayList<String>();
	 public ArrayList<String> frozengreen = new ArrayList<String>();
	 public ArrayList<String> ftag = new ArrayList<String>();
	 public ArrayList<String> ftagplayers = new ArrayList<String>();
	 public ArrayList<String> beaconlist = new ArrayList<String>();
	 public ArrayList<String> ftgreen = new ArrayList<String>();
	 public ArrayList<String> ftred = new ArrayList<String>();
	 

	
	@Override
	public void onDisable() {
	
		getLogger().info("WinterSlash Plugin Disabled!");
	
	}
	
	
	
	@Override
	public void onEnable() {
		
		//load world
		String playerWorld = this.getConfig().getString("Worlds" + ".World" );
		getLogger().info(playerWorld);
		getServer().createWorld(new WorldCreator(playerWorld));
		//register events
		this.getServer().getPluginManager().registerEvents(new helmet(this), this);
		
		getLogger().info("Plugin Enabled!");
		}
	
	

	
			
	public static boolean isInt(String sender) {
	    try {
	        Integer.parseInt(sender);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args){
		Player player = (Player) sender;
		Player[] onlinep = Bukkit.getServer().getOnlinePlayers();
	
		if(cmd.getName().equalsIgnoreCase("ftlist")){
			ConfigurationSection sec = getConfig().getConfigurationSection("ArenaList");
			String arenas = sec.getValues(false).keySet().toString();
			player.sendMessage(ChatColor.GREEN + arenas);
		}
		
        
		//join game
		if(cmd.getName().equalsIgnoreCase("ftj")){
			 int maxplayers = this.getConfig().getInt("Settings" + ".playernumber");
			
			//checks permission
			if(!sender.hasPermission("freezetag.ftj")){
				sender.sendMessage("No permission");
				return true;
			}
			
			if(!(args.length == 1)){
				player.sendMessage(ChatColor.YELLOW + "Proper forumalation is: /ftj <arenaname>"); ;return true;
			}
			else{

			if(ftagplayers.contains(player.getName())){
				player.sendMessage(ChatColor.GREEN + "You are already ingame, waiting for more players...");
				return true;
			}
			
			ftagplayers.add(player.getName());
			player.sendMessage(ChatColor.YELLOW + "You have been put on the games waiting list.");
			
			
			
			//max player size
			int redamount = maxplayers*2;
			if(ftred.size() >= redamount){
				player.sendMessage(ChatColor.YELLOW + "Can't join now, game in progress");
				return true;
			}
			
			// This is the player sorter. It balances the teams so they are equal or differe by one player.
			if(ftagplayers.size() >= maxplayers){
				ItemStack revivor = new ItemStack(Material.BLAZE_ROD,1);
				ItemStack sword = new ItemStack(Material.WOOD_SWORD,1);
				player.sendMessage("hi");
				for(Player p: onlinep){
					if(ftagplayers.contains(p.getName())){
						
						if(ftred.size()>ftgreen.size()){
							ftgreen.add(p.getName());
							p.sendMessage("green");

						}
						else if(ftgreen.size()>ftred.size()){
							ftred.add(p.getName());
							p.sendMessage("red");

						}
						else{
							ftred.add(p.getName());
							p.sendMessage("red2");

						}
					}
				}
				
				//get location before teleportation - to be added

				
				//teleporting to green spawn
				for(Player p: Bukkit.getOnlinePlayers()){
				    if(ftgreen.contains(p.getName())){
				    	 int greenspawnX = this.getConfig().getInt("ArenaList." + args[0] + ".greenspawn" + ".X");
				         int greenspawnY = this.getConfig().getInt("ArenaList." + args[0] + ".greenspawn" + ".Y");
				         int greenspawnZ = this.getConfig().getInt("ArenaList." + args[0] + ".greenspawn" + ".Z");
				         int greenspawnYaw = this.getConfig().getInt("ArenaList." + args[0] + ".greenspawn" + ".Yaw");
				         int greenspawnPitch = this.getConfig().getInt("ArenaList." + args[0] + ".greenspawn" + ".Pitch");
				         String playerWorld = this.getConfig().getString("ArenaList." + args[0] + ".greenspawn" + ".World");
				         
				         World world = Bukkit.getWorld(playerWorld);

				         if(world != null)
				         {
				        	 Location greenspawn = new Location((World) world, greenspawnX, greenspawnY, greenspawnZ, greenspawnYaw, greenspawnPitch);
				        	 p.teleport(greenspawn);
				        	 p.sendMessage(ChatColor.GREEN + "You have joined the Green team!");
				        	 revivor.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
							 p.getInventory().addItem(revivor);
							 p.getInventory().addItem(sword);
							//armor
							 ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
							 LeatherArmorMeta am = (LeatherArmorMeta)helmet.getItemMeta();
							 am.setColor(Color.fromRGB(0, 100, 0));
							 helmet.setItemMeta(am);
							 
							 ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
							 am.setColor(Color.fromRGB(0, 100, 0));
							 chest.setItemMeta(am);
							 
							 ItemStack pants = new ItemStack(Material.LEATHER_LEGGINGS, 1);
							 am.setColor(Color.fromRGB(0, 100, 0));
							 pants.setItemMeta(am);
							 
							 ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
							 am.setColor(Color.fromRGB(0, 100, 0));
							 boots.setItemMeta(am);
							 
							 
							 //set armor
							 p.getInventory().setHelmet(helmet);
							 p.getInventory().setChestplate(chest);
							 p.getInventory().setLeggings(pants);
							 p.getInventory().setBoots(boots);
				        	 
				         }
				         else
				         {
				        	 Bukkit.getServer().createWorld(new WorldCreator(playerWorld).environment(World.Environment.NORMAL));
				             getLogger().warning("The '" + "greenspawn" + ".World" + "' world from config.yml does not exist or is not loaded !");
				         }
				    }
				    else if(ftred.contains(p.getName())){
				    	//teleporting to red spawn
				    	 int redspawnX = this.getConfig().getInt("ArenaList." + args[0] + ".redspawn" + ".X");
				         int redspawnY = this.getConfig().getInt("ArenaList." + args[0] + ".redspawn" + ".Y");
				         int redspawnZ = this.getConfig().getInt("ArenaList." + args[0] + ".redspawn" + ".Z");
				         int redspawnYaw = this.getConfig().getInt("ArenaList." + args[0] + ".redspawn" + ".Yaw");
				         int redspawnPitch = this.getConfig().getInt("ArenaList." + args[0] + ".redspawn" + ".Pitch");
	                     String playerWorld = this.getConfig().getString("ArenaList." + args[0] + ".redspawn" + ".World");
	                     
				         
				         World world = Bukkit.getWorld(playerWorld);

				         if(world != null)
				         {
				        	 Location redspawn = new Location((World) world, redspawnX, redspawnY, redspawnZ, redspawnYaw, redspawnPitch);
				        	 p.teleport(redspawn);
				        	 p.sendMessage(ChatColor.GREEN + "You have joined the Red team!");
				        	 revivor.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
							 p.getInventory().addItem(revivor);
							 p.getInventory().addItem(sword);
							 //armor
							 ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
							 LeatherArmorMeta am = (LeatherArmorMeta)helmet.getItemMeta();
							 am.setColor(Color.fromRGB(100, 0, 0));
							 helmet.setItemMeta(am);
							 
							 ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
							 am.setColor(Color.fromRGB(100, 0, 0));
							 chest.setItemMeta(am);
							 
							 ItemStack pants = new ItemStack(Material.LEATHER_LEGGINGS, 1);
							 am.setColor(Color.fromRGB(100, 0, 0));
							 pants.setItemMeta(am);
							 
							 ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
							 am.setColor(Color.fromRGB(100, 0, 0));
							 boots.setItemMeta(am);
							 
							 
							 
							 p.getInventory().setHelmet(helmet);
							 p.getInventory().setChestplate(chest);
							 p.getInventory().setLeggings(pants);
							 p.getInventory().setBoots(boots);
				         }
				         else
				         {
				        	 Bukkit.getServer().createWorld(new WorldCreator(playerWorld).environment(World.Environment.NORMAL));
				             getLogger().warning("The '" + "redspawn" + ".World" + "' world from config.yml does not exist or is not loaded !");
				         }
				    }
				}	
			}
		}
		}
		
		
		
		//set player number
		else if(cmd.getName().equals("ftpn")){
			if(!sender.hasPermission("freezetag.ftpn")){
				sender.sendMessage("No permission");
				return true;
			}
			if(args.length==1){
				if(isInt(args[0])){
					 int num = Integer.parseInt(args[0]);
				 this.getConfig().set("Settings" + ".playernumber", num-1);
		         this.getConfig().options().copyDefaults(true);
		   		 this.saveConfig();
				 player.sendMessage(ChatColor.YELLOW + "Player number set.");
			}
		}
			else{
				player.sendMessage(ChatColor.YELLOW + "Use correct command format!");
			}
		}
		
		
		//leaving the game
		else if(cmd.getName().equalsIgnoreCase("ftl")){
			if(!sender.hasPermission("freezetag.ftl")){
				sender.sendMessage("No permission");
				return true;
			}
			if(ftagplayers.contains(player.getName())){
				ftagplayers.remove(player.getName());
				frozen.remove(player.getName());
				ftred.remove(player.getName());
				ftgreen.remove(player.getName());
				player.getInventory().clear();
				player.sendMessage(ChatColor.GREEN + "You have left the game!");
				player.teleport(player.getWorld().getSpawnLocation());
				return true;
			}
			else{
				player.sendMessage(ChatColor.GREEN + "You are not in a game!");
			}
			
		}

		
		//save red spawn
		else if(cmd.getName().equalsIgnoreCase("ftsetred")){
			if(!sender.hasPermission("freezetag.ftsetred")){
				sender.sendMessage("No permission");
				return true;
			}
			
			if(args.length == 1){
				if(!(isInt(args[0]))){
			 this.getConfig().set("ArenaList." + args[0] + ".redspawn" + ".X", player.getLocation().getBlockX());
	            this.getConfig().set("ArenaList." + args[0] + ".redspawn" + ".Y", player.getLocation().getBlockY());
	            this.getConfig().set("ArenaList." + args[0] +".redspawn" + ".Z", player.getLocation().getBlockZ());
	            this.getConfig().set("ArenaList." + args[0] + ".redspawn" + ".Yaw", player.getLocation().getYaw());
	            this.getConfig().set("ArenaList." + args[0] + ".redspawn" + ".Pitch", player.getLocation().getPitch());
	            this.getConfig().set("ArenaList." + args[0] + ".redspawn" + ".World", Bukkit.getName());
	            this.getConfig().set("Worlds" + ".World", Bukkit.getName());
	            this.getConfig().options().copyDefaults(true);
	   		    this.saveConfig();
    	        player.sendMessage(ChatColor.YELLOW + "Red Spawn saved successfully");    
				}
				else{
					player.sendMessage(ChatColor.RED + "Arena can't be an integer!");
				}
			}
			else{
				player.sendMessage("Name the arana to set the spawn in: /ftsetred <arena>");
			}
		}
		
		//save green spawn
		else if(cmd.getName().equalsIgnoreCase("ftsetgreen")){
			if(!sender.hasPermission("freezetag.ftsetgreen")){
				sender.sendMessage("No permission");
				return true;
			}
			
			if(args.length == 1){
				if(!(isInt(args[0]))){
			 this.getConfig().set("ArenaList." + args[0] + ".greenspawn" + ".X", player.getLocation().getBlockX());
	            this.getConfig().set("ArenaList." + args[0] + ".greenspawn" + ".Y", player.getLocation().getBlockY());
	            this.getConfig().set("ArenaList." + args[0] + ".greenspawn" + ".Z", player.getLocation().getBlockZ());
	            this.getConfig().set("ArenaList." + args[0] + ".greenspawn" + ".Yaw", player.getLocation().getYaw());
	            this.getConfig().set("ArenaList." + args[0] + ".greenspawn" + ".Pitch", player.getLocation().getPitch());
	            this.getConfig().set("ArenaList." + args[0] + ".greenspawn" + ".World", Bukkit.getName());
	            this.getConfig().options().copyDefaults(true);
	   		    this.saveConfig();
    	        player.sendMessage(ChatColor.YELLOW + "Green Spawn saved successfully");   
				}
    	        else{
					player.sendMessage(ChatColor.RED + "Arena can't be an integer!");
				}
			}
			else{
				player.sendMessage("Name the arana to set the spawn in: /ftsetred <arena>");
			}
		}
		
		//go to red spawn
		else if(cmd.getName().equalsIgnoreCase("red")){
			if(!sender.hasPermission("freezetag.red")){
				sender.sendMessage("No permission");
				return true;
			}
			 int redspawnX = this.getConfig().getInt("ArenaList." + args[0] + ".redspawn" + ".X");
	         int redspawnY = this.getConfig().getInt("ArenaList." + args[0] + ".redspawn" + ".Y");
	         int redspawnZ = this.getConfig().getInt("ArenaList." + args[0] + ".redspawn" + ".Z");
	         int redspawnYaw = this.getConfig().getInt("ArenaList." + args[0] + ".redspawn" + ".Yaw");
	         int redspawnPitch = this.getConfig().getInt("ArenaList." + args[0] + ".redspawn" + ".Pitch");
	         String playerWorld = this.getConfig().getString("ArenaList." + args[0] + ".redspawn" + ".World");
	         
	         World world = Bukkit.getWorld(playerWorld);

	         if(world != null)
	         {
	        	 Location redspawn = new Location((World) world, redspawnX, redspawnY, redspawnZ, redspawnYaw, redspawnPitch);
	        	 player.teleport(redspawn);
	         }
	         else
	         {
	        	 Bukkit.getServer().createWorld(new WorldCreator(playerWorld).environment(World.Environment.NORMAL));
	             getLogger().warning("The '" + "redspawn" + ".World" + "' world from config.yml does not exist or is not loaded !");
	         }
		}	
		
		//Go to green spawn
		
		else if(cmd.getName().equalsIgnoreCase("green")){
			if(!sender.hasPermission("freezetag.green")){
				sender.sendMessage("No permission");
				return true;
			}
			 int greenspawnX = this.getConfig().getInt("ArenaList." + args[0] + ".greenspawn" + ".X");
	         int greenspawnY = this.getConfig().getInt("ArenaList." + args[0] + ".greenspawn" + ".Y");
	         int greenspawnZ = this.getConfig().getInt("ArenaList." + args[0] + ".greenspawn" + ".Z");
	         int greenspawnYaw = this.getConfig().getInt("ArenaList." + args[0] + ".greenspawn" + ".Yaw");
	         int greenspawnPitch = this.getConfig().getInt("ArenaList." + args[0] + ".greenspawn" + ".Pitch");
	         String playerWorld = this.getConfig().getString("ArenaList." + args[0] + ".greenspawn" + ".World");
	         
	         World world = Bukkit.getWorld(playerWorld);

	         if(world != null)
	         {
	        	 Location greenspawn = new Location((World) world, greenspawnX, greenspawnY, greenspawnZ, greenspawnYaw, greenspawnPitch);
	        	 player.teleport(greenspawn);
	         }
	         else
	         {
	        	 Bukkit.getServer().createWorld(new WorldCreator(playerWorld).environment(World.Environment.NORMAL));
	             getLogger().warning("The '" + "greenspawn" + ".World" + "' world from config.yml does not exist or is not loaded !");
	         }
	   
			
		}
		
		
		

		
		
		
		
		return false;
	}
		
}
