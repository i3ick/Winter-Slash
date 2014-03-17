package me.i3ick.com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;


public class WinterSlashMain extends JavaPlugin{
    	  
    	 // Arrays
	 public ArrayList<String> frozen = new ArrayList<String>();
	 public ArrayList<String> frozenred = new ArrayList<String>();
	 public ArrayList<String> frozengreen = new ArrayList<String>();
	 public ArrayList<String> ftag = new ArrayList<String>();
	 public ArrayList<String> beaconlist = new ArrayList<String>();

	 
	 public HashMap<String, Player> wsplayersHM = new HashMap<String, Player>();
	 public HashMap<String, Player> wsred = new HashMap<String, Player>();
	 public HashMap<String, Player> wsgreen = new HashMap<String, Player>();
	 Map<String,ArrayList<Player>> wsredmap = new HashMap<String,ArrayList<Player>>();
	 Map<String,ArrayList<String>> wsgreenmap = new HashMap<>();
	 private WinterSlashScoreboard WinterSlashScoreboard;
	 public WinterSlashScoreboard scoreboad = WinterSlashScoreboard;
	 
	 // We create a Variable of this Plugin so we can use it lateron to retrieve the Config
	 private static WinterSlashMain main;
	 
	 
	 // Now we do a getter Method so we can retrieve the Variable
	 public static WinterSlashMain getInstance() {
	 	return main;
	 }
	 
	
	@Override
	public void onDisable() {
	
		getLogger().info("WinterSlash Plugin Disabled!");
	
	}
	
	
	
	@Override
	public void onEnable() {
		
		//ready config
		final FileConfiguration config = this.getConfig();
		
		//load world
		String playerWorld = this.getConfig().getString("Worlds" + ".World" );
		getLogger().info(playerWorld);
		getServer().createWorld(new WorldCreator(playerWorld));

				
		//register events
		this.getServer().getPluginManager().registerEvents(new WinterSlashEvents(this), this);
		WinterSlashScoreboard.init();
		WinterSlashManager.getManager().loadArenas();
		WinterSlashManager.getInstance().setup();
	
		
		
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
	
		if(cmd.getName().equalsIgnoreCase("wslist")){
			ConfigurationSection sec = getConfig().getConfigurationSection("ArenaList");
			String arenas = sec.getValues(false).keySet().toString();
			player.sendMessage(ChatColor.GREEN + arenas);
		}
		
        
		//join game
		if(cmd.getName().equalsIgnoreCase("wsj")){
			 int maxplayers = this.getConfig().getInt("Settings" + ".playernumber");
			 if(args.length < 1){
				 player.sendMessage(ChatColor.RED + "You didn't specify arena name");
				 return true;
			 }
			String arena = args[0].toString();
			//checks permission
			if(!sender.hasPermission("freezetag.ftj")){
				sender.sendMessage("No permission");
				return true;
			}
			
			if(WinterSlashManager.getInstance().getArena(arena) == null){
				player.sendMessage(ChatColor.RED + "This arena doesn't exist");
				return true;
			}

			
			if(WinterSlashManager.getInstance().getArena(arena).isInGame()){
				player.sendMessage(ChatColor.YELLOW + "There is a game currently running in this arena!");
				return true;
			}
			
			if(!(args.length == 1)){
				player.sendMessage(ChatColor.YELLOW + "Proper forumalation is: /ftj <arenaname>"); ;return true;
			}
			else{

			if(wsplayersHM.containsKey(player)){
				player.sendMessage(ChatColor.GREEN + "You are already ingame, waiting for more players...");
				return true;
			}
			
			WinterSlashManager.getManager().addPlayers(player, arena);
			player.sendMessage(ChatColor.YELLOW + "You have been put on the games waiting list.");
			
			
			
			//max player size
			int redamount = maxplayers*2;
			if(wsredmap.size() >= redamount){
				player.sendMessage(ChatColor.YELLOW + "Can't join now, game in progress");
				return true;
			}
			
			
			
			// This starts the game
			  if(wsplayersHM.size() >= maxplayers){
				  
				  //saves player location
				  
						 this.getConfig().set("PlayerData." + player.getName() + ".X", player.getLocation().getBlockX());
				         this.getConfig().set("PlayerData." + player.getName() + ".Y", player.getLocation().getBlockY());
				         this.getConfig().set("PlayerData." + player.getName() + ".Z", player.getLocation().getBlockZ());
				         this.getConfig().set("PlayerData." + player.getName() + ".Yaw", player.getLocation().getYaw());
				         this.getConfig().set("PlayerData." + player.getName() + ".Pitch", player.getLocation().getPitch());
				         this.getConfig().set("PlayerData." + player.getName() + ".World", Bukkit.getName());
				         this.getConfig().set("PlayerData." + player.getName() + ".World", Bukkit.getName());
				         this.getConfig().options().copyDefaults(true);
				         this.saveConfig();
				  
				  //initiates arena manager
				  WinterSlashManager.getManager().startArena(arena);
				  ItemStack revivor = new ItemStack(Material.BLAZE_ROD,1);
				  ItemStack sword = new ItemStack(Material.WOOD_SWORD,1);
				  
				  
				/*	for(Player p: onlinep){
					if(wsplayersHM.containsKey(arena)){
						if (wsplayersHM.containsValue(p)){
						

						WinterSlashScoreboard boardmanager = new WinterSlashScoreboard(this);
						boardmanager.aliveRed.setScore(wsredmap.get(arena).size());
						boardmanager.aliveGreen.setScore(wsgreenmap.get(arena).size());
						
						if(wsredmap.values().size()>wsgreenmap.size()){
							wsgreenmap.get(arena).add(player.getName());

							p.sendMessage("green");
							player.setScoreboard(boardmanager.board);
							
							//add player to green team

						}
						else if(wsgreenmap.values().size()>wsredmap.size()){
							wsredmap.get(arena).add(player.getName());
							wsredmap.put(arena, p);
							p.sendMessage("red");
							player.setScoreboard(boardmanager.board);
							//add player to red team

						}
						else{
							wsredmap.get(arena).add(player.getName());
							p.sendMessage("red");
							player.setScoreboard(boardmanager.board);
							
							//add player to red team
						}
					}
				}	
				} */


				/*
				//teleporting to green spawn
				for(Player p: Bukkit.getOnlinePlayers()){
				    if(wsgreenmapmap.containsKey(arena)){
				    	if(wsgreenmapmap.containsValue(player)){
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
				}
				    else if(wsredmap.containsKey(arena)){
				    	if(wsredmap.containsValue(player)){

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
				}	*/
			}
		}
		}
		
		
		
		//set player number
		else if(cmd.getName().equals("wspn")){
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
				player.sendMessage(ChatColor.YELLOW + "Use correct command format: /wspn <number>");
			}
		}
		
		
		//leaving the game
		else if(cmd.getName().equalsIgnoreCase("wsl")){
			if(!sender.hasPermission("freezetag.ftl")){
				sender.sendMessage("No permission");
				return true;
			}
			String arena = args[0];
			if(wsplayersHM.containsValue(player)){
				wsplayersHM.remove(player);
				frozen.remove(player.getName());
				wsredmap.remove(player.getName());
				wsgreenmap.remove(player.getName());
				WinterSlashManager.getManager().removePlayer(player, arena);
				player.sendMessage(ChatColor.GREEN + "You have left the game!");
				player.teleport(player.getWorld().getSpawnLocation());
				return true;
			}
			else{
				player.sendMessage(ChatColor.GREEN + "You are not in a game!");
			}
			
		}

		
		//save lobby spawn
				else if(cmd.getName().equalsIgnoreCase("wssetlobby")){
					if(!sender.hasPermission("freezetag.ftsetlobby")){
						sender.sendMessage("No permission");
						return true;
					}
				
					 this.getConfig().set("Lobby" + ".X", player.getLocation().getBlockX());
			            this.getConfig().set("Lobby" + ".Y", player.getLocation().getBlockY());
			            this.getConfig().set("Lobby" + ".Z", player.getLocation().getBlockZ());
			            this.getConfig().set("Lobby" + ".Yaw", player.getLocation().getYaw());
			            this.getConfig().set("Lobby" + ".Pitch", player.getLocation().getPitch());
			            this.getConfig().set("Lobby" + ".World", Bukkit.getName());
			            this.getConfig().set("Worlds" + ".World", Bukkit.getName());
			            this.getConfig().options().copyDefaults(true);
			   		    this.saveConfig();
		    	        player.sendMessage(ChatColor.YELLOW + "Lobby location saved successfully");
		    	        

				}
		
		
		//save red spawn
		else if(cmd.getName().equalsIgnoreCase("wssetred")){
			if(!sender.hasPermission("freezetag.ftsetred")){
				sender.sendMessage("No permission");
				return true;
			}
		
			
			 this.getConfig().set("Redspawn" + ".X", player.getLocation().getBlockX());
	            this.getConfig().set("Redspawn" + ".Y", player.getLocation().getBlockY());
	            this.getConfig().set("Redspawn" + ".Z", player.getLocation().getBlockZ());
	            this.getConfig().set("Redspawn" + ".Yaw", player.getLocation().getYaw());
	            this.getConfig().set("Redspawn" + ".Pitch", player.getLocation().getPitch());
	            this.getConfig().set("Redspawn" + ".World", Bukkit.getName());
	            this.getConfig().set("Worlds" + ".World", Bukkit.getName());
	            this.getConfig().options().copyDefaults(true);
	   		    this.saveConfig();
    	        player.sendMessage(ChatColor.YELLOW + "Red Spawn saved successfully");
		}
		
		//save green spawn
		else if(cmd.getName().equalsIgnoreCase("wssetgreen")){
			if(!sender.hasPermission("freezetag.ftsetgreen")){
				sender.sendMessage("No permission");
				return true;
			}
			

			 this.getConfig().set("Greenspawn" + ".X", player.getLocation().getBlockX());
	            this.getConfig().set("Greenspawn" + ".Y", player.getLocation().getBlockY());
	            this.getConfig().set("Greenspawn" + ".Z", player.getLocation().getBlockZ());
	            this.getConfig().set("Greenspawn" + ".Yaw", player.getLocation().getYaw());
	            this.getConfig().set("Greenspawn" + ".Pitch", player.getLocation().getPitch());
	            this.getConfig().set("Worlds" + ".World", Bukkit.getName());
	            this.getConfig().options().copyDefaults(true);
	   		    this.saveConfig();
    	        player.sendMessage(ChatColor.YELLOW + "Green Spawn saved successfully");   
		}

		
		
		
		// Creating the arena
		else if (cmd.getName().equalsIgnoreCase("wscreate")){
			if(!sender.hasPermission("freezetag.create")){
				sender.sendMessage("No permission");
				return true;
			}
			
			if(args.length == 2){
				String arenaName = args[0].toString();
				if(!isInt(args[1])){
					player.sendMessage(ChatColor.RED + args[1] + " is not a number!");
					return true;
				}
				
				int maxPlayers = Integer.parseInt(args[1]);
				
				// load player location
				int playerX = this.getConfig().getInt("PlayerData." + player.getName() + ".X");
		         int playerY = this.getConfig().getInt("PlayerData." + player.getName() + ".Y");
		         int playerZ = this.getConfig().getInt("PlayerData." + player.getName() + ".Z");
		         int playerYaw = this.getConfig().getInt("PlayerData." + player.getName() + ".Yaw");
		         int playerPitch = this.getConfig().getInt("PlayerData." + player.getName() + ".Pitch");
				
				// load lobby information
				int lobbyX = this.getConfig().getInt("lobby" + ".X");
		         int lobbyY = this.getConfig().getInt("lobby" + ".Y");
		         int lobbyZ = this.getConfig().getInt("lobby" + ".Z");
		         int lobbyYaw = this.getConfig().getInt("lobby" + ".Yaw");
		         int lobbyPitch = this.getConfig().getInt("lobby" + ".Pitch");
				
				// load red spawn information
				int redspawnX = this.getConfig().getInt("Redspawn" + ".X");
		         int redspawnY = this.getConfig().getInt("Redspawn" + ".Y");
		         int redspawnZ = this.getConfig().getInt("Redspawn" + ".Z");
		         int redspawnYaw = this.getConfig().getInt("Redspawn" + ".Yaw");
		         int redspawnPitch = this.getConfig().getInt("Redspawn" + ".Pitch");
		         String playerWorld = this.getConfig().getString("Redspawn" + ".World"); 
				
				// load green spawn information
				 int greenspawnX = this.getConfig().getInt("Greenspawn" + ".X");
		         int greenspawnY = this.getConfig().getInt("Greenpawn" + ".Y");
		         int greenspawnZ = this.getConfig().getInt("Greenspawn" + ".Z");
		         int greenspawnYaw = this.getConfig().getInt("Greenspawn" +".Yaw");
		         int greenspawnPitch = this.getConfig().getInt("Greenspawn" + ".Pitch");
		         
		         // Get current World
		         World world = Bukkit.getWorld(playerWorld);

		         if(world != null)
		         {
		        	 Location endLocation = new Location((World) world, playerX, playerY, playerZ, playerYaw, playerPitch);
		        	 Location joinLocation = new Location((World) world, lobbyX, lobbyY, lobbyZ, lobbyYaw, lobbyPitch);
		        	 Location greenspawn = new Location((World) world, redspawnX, redspawnY, redspawnZ, redspawnYaw, redspawnPitch);
		        	 Location redspawn = new Location((World) world, greenspawnX, greenspawnY, greenspawnZ, greenspawnYaw, greenspawnPitch);
		        	 WinterSlashManager.getManager().createArena(arenaName, joinLocation, redspawn, greenspawn, endLocation, maxPlayers);
		        	 
		        	 
		         }
		         else
		         {
		        	 Bukkit.getServer().createWorld(new WorldCreator(playerWorld).environment(World.Environment.NORMAL));
		             getLogger().warning("The '" + "greenspawn" + ".World" + "' world from config.yml does not exist or is not loaded !");
		         }
				}	
			else{
				player.sendMessage(ChatColor.YELLOW + ("Please use the following format: /wscreate <arenaname>"));
			}
		}
		
		
		

		
		
		
		
		return false;
	}

		
}
