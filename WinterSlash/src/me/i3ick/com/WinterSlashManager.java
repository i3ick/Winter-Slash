package me.i3ick.com;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;



public class WinterSlashManager{
	

	 
	
	
	// This Arena manager is a modified version of JPG200's code: https://forums.bukkit.org/threads/tut-create-a-minigame-with-arena-names.193908/
	
	public enum Team { RED, GREEN; }



	  private JavaPlugin plugin;
	    JavaPlugin plugin2;

    
	private WinterSlashManager() {
		//here we make sure that the plugin2 variable and plugin variable have the correct value
		this.plugin = WinterSlashMain.getInstance();
		this.plugin2 = WinterSlashMain.getInstance(); 
		}
	private static WinterSlashManager am = new WinterSlashManager();
	public static WinterSlashManager getInstance() {
		return am;
	}
	

	private ArrayList<WinterSlashArena> arenas = new ArrayList<WinterSlashArena>();
	
	
	public void setup(){
		//load config
	}
	

	 
	//Usefull for getting the ArenaManager, like so: ArenaManager.getManager()
	public static WinterSlashManager getManager() {
	return am;
	}
	 
	 
	//A method for getting one of the Arenas out of the list by name:
	public WinterSlashArena getArena(String name) {
	for (WinterSlashArena a: WinterSlashArena.arenaObjects) { //For all of the arenas in the list of objects
	if (a.getName().equals(name)) { //If the name of an arena object in the list is equal to the one in the parameter...
	return a; 
	}
	}
	return null; 
	}
	

	 
	 
	//A method for adding players
	public void addPlayers(Player player, String arenaName) {
	 
	if (getArena(arenaName) != null) { 
	 
	WinterSlashArena arena = getArena(arenaName);  
	if (!arena.isFull()) { //If the arena is not full
	if (!arena.isInGame()) { //If there is no game in progress
		player.getInventory().clear(); //Clear the players inventory
		
		

	//Teleport to the arena lobby
		for (String p: arena.getPlayers()) {
		Bukkit.getPlayer(p).teleport(arena.getJoinLocation());
		}
	
	
	
	//Add the player to the arena list
	arena.getPlayers().add(player.getName()); //Add the players name to the arena
	 
	int playersLeft = arena.getMaxPlayers() - arena.getPlayers().size(); //How many players needed to start

	//Send the arena's players a message
	if(!(arena.getPlayers().size() == arena.getMaxPlayers())){
	arena.sendMessage(ChatColor.BLUE + player.getName() + " has joined the arena! We only need " + playersLeft + " to start the game!");
	player.teleport(arena.getJoinLocation());
	return;
	}
	 
	if (playersLeft == 0) { //IF there are 0 players needed to start the game
	startArena(arenaName); //Start the arena, see the method way below :)
	}
	 
	 
	} else { //Specifiend arena is in game, send the player an error message
	player.sendMessage(ChatColor.YELLOW + "Match in progress!");
	 
	}
	} else { //Specified arena is full, send the player an error message
	player.sendMessage(ChatColor.YELLOW + "All slots filled up!");
	}
	 
	} else { //The arena doesn't exsist, send the player an error message
	player.sendMessage(ChatColor.RED + "INVALID ARENA!");
	}
	 
	}
	 
	 
	//A method for removing players
	public void removePlayer(Player player, String arenaName) {
	 
	if (getArena(arenaName) != null) { 
	 
	WinterSlashArena arena = getArena(arenaName);
	 
	if (arena.getPlayers().contains(player.getName())) { //If the arena has the player already
	 
	player.getInventory().clear();
	 
	//Teleport out
	player.teleport(arena.getEndLocation());
	 
	//remove the player from the arena list
	arena.getPlayers().remove(player.getName()); 
	 
	//Send the arena's players a message
	arena.sendMessage(ChatColor.BLUE + player.getName() + " Disconnected! There are " + arena.getPlayers().size() + "players left!");
	 
	 
	 
	 
	} else { //Specified arena doesn't have the player, send the player an error message
	player.sendMessage(ChatColor.YELLOW + "You are not ingame!");
	 
	}
	 
	 
	} else { //The arena doesn't exsist, send the player an error message
	player.sendMessage(ChatColor.RED + "The arena you are looking for could not be found!");
	}
	}
	 
	
	
	
	
	 
	//A method for starting an Arena:
	public void startArena(String arenaName) {
	 
	if (getArena(arenaName) != null) { //If the arena exsists
	 
	WinterSlashArena arena = getArena(arenaName); //Create an arena for using in this method
	 
	arena.sendMessage(ChatColor.GOLD + "Ready, set, GO!");

	//Set ingame
	arena.setInGame(true);
	
	 //Teleports the players to their assigned spawns
	for (String p: arena.getPlayers()) {

			Player pl = Bukkit.getPlayer(p);
			if(arena.ifPlayerIsRed(pl)){
				// debug message
				pl.sendMessage("You are in the Red Team");
				pl.teleport(arena.getRedSpawn());
			}
			else{
				// debug message
				pl.sendMessage("You are in the Green Team");
				pl.teleport(arena.getGreenSpawn());
			}
	 
	//Ready up the players
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
	 Bukkit.getPlayer(p).getInventory().setHelmet(helmet);
	 Bukkit.getPlayer(p).getInventory().setChestplate(chest);
	 Bukkit.getPlayer(p).getInventory().setLeggings(pants);
	 Bukkit.getPlayer(p).getInventory().setBoots(boots);
	 
	 
	 
	}
	 
	 
	}
	 
	}
	 
	 
	
	
	//A method for ending an Arena:
	public void endArena(String arenaName) {
	 
	if (getArena(arenaName) != null) { //If the arena exists 
	 
	WinterSlashArena arena = getArena(arenaName); //Create an arena for using in this method
	 
	//Send them a message
	arena.sendMessage(ChatColor.GOLD + "Match is over!");
	 
	//Set ingame
	arena.setInGame(false);
	 
	//Returning players to initial positions
	for (String p: arena.getPlayers()) {
	Player player = Bukkit.getPlayer(p); 
	player.teleport(arena.getEndLocation());
	player.getInventory().clear(); 
	//Remove them all from the list
	arena.getPlayers().remove(player.getName());
	 
	}
	}
	}
	
	
	
	
	 
	 
	//Arena Loaders
	//Creating arenas from config, along with their representative objects
	  
	public void loadArenas() {
	 
		
		FileConfiguration config = plugin2.getConfig();

		if(config.getConfigurationSection("arenas") == null){
			WinterSlashMain.getInstance().getLogger().info("There are no arenas.");
			return;
		}
		
	for (String keys: config.getConfigurationSection("arenas").getKeys(false)) {//For each arena name in the arena file
	 
		
	//Get values, make arena object
	String name = config.getString("Worlds" + ".World");
	World world = Bukkit.getWorld(name);
	 
	//Arena names are keys
	double joinX = config.getDouble("arenas." + keys + "." + "joinX");
	double joinY = config.getDouble("arenas." + keys + "." + "joinY");
	double joinZ = config.getDouble("arenas." + keys + "." + "joinZ");
	Location joinLocation = new Location(world, joinX, joinY, joinZ);
	
	double greenX = config.getDouble("arenas." + keys + "." + "greenX");
	double greenY = config.getDouble("arenas." + keys + "." + "greenY");
	double greenZ = config.getDouble("arenas." + keys + "." + "greenZ"); 
	Location greenLocation = new Location(world, greenX, greenY, greenZ);
	
	double redX = config.getDouble("arenas." + keys + "." + "redX");
	double redY = config.getDouble("arenas." + keys + "." + "redY");
	double redZ = config.getDouble("arenas." + keys + "." + "redZ"); 
	Location redLocation = new Location(world, redX, redY, redZ);
	 
	double endX = config.getDouble("arenas." + keys + "." + "endX");
	double endY = config.getDouble("arenas." + keys + "." + "endX");
	double endZ = config.getDouble("arenas." + keys + "." + "endX");
	Location endLocation = new Location(world, endX, endY, endZ);
	 
	int maxPlayers = plugin2.getConfig().getInt("arenas." + keys + ".maxPlayers");
	 
	//Now lets create an object to represent it:
	WinterSlashArena arenaobject = new WinterSlashArena(keys, joinLocation, redLocation, greenLocation, endLocation, maxPlayers);
	 
	}
	WinterSlashMain.getInstance().getLogger().info("Arenas are now loaded!");
	}
	 

	
	
//	CREATING AND SAVING THE ARENA
//	CREATING AND SAVING THE ARENA
//	CREATING AND SAVING THE ARENA
	
	
	public void createArena(String arenaName, Location joinLocation, Location redLocation, Location greenLocation, Location endLocation, int maxPlayers) {
	 
	//Object to represent the arena
	WinterSlashArena arena = new WinterSlashArena(arenaName, joinLocation, redLocation, greenLocation, endLocation, maxPlayers);
	
	
	FileConfiguration config = plugin2.getConfig();
	
	config.set("arenas." + arenaName, null); //Set its name
	//Now sets the other values
	 
	String path = "arenas." + arenaName + "."; 
	//Sets the paths
	config.set(path + "joinX", joinLocation.getX());
	config.set(path + "joinY", joinLocation.getY());
	config.set(path + "joinZ", joinLocation.getZ());
	
	config.set(path + "redX", redLocation.getX());
	config.set(path + "redY", redLocation.getY());
	config.set(path + "redZ", redLocation.getZ());
	
	config.set(path + "greenX", greenLocation.getX());
	config.set(path + "greenY", greenLocation.getY());
	config.set(path + "greenZ", greenLocation.getZ());
	 
	config.set(path + "endX", endLocation.getX());
	config.set(path + "endY", endLocation.getY());
	config.set(path + "endZ", endLocation.getZ());
	 
	config.set(path + "maxPlayers", maxPlayers);

	plugin2.saveConfig();
	}
}
