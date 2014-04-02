package me.i3ick.com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.i3ick.com.WinterSlashManager.Team;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class WinterSlashArena {
	

	
	
	//A list of all the Arena Objects
	public static ArrayList<WinterSlashArena> arenaObjects = new ArrayList<WinterSlashArena>();

	
	//Some fields we want each Arena object to store:
	private Location redspawn, greenspawn, joinLocation, startLocation, endLocation; //Some general arena locations
	private HashMap<String, Team> players = new HashMap<String, Team>();

	public void UnsetHash(String player){
		players.remove(player);
	}
	
	boolean isRed(Player p) {
		for (String p1: getPlayers()) {
		if(reda.contains(p1)){
			return true;
		}
		}
		return isRed;
		}

	
	public HashMap<String, Team> GetHash() {
		return players;
	}
	
	public ArrayList<String> reda = new ArrayList<String>();
	public ArrayList<String> greena = new ArrayList<String>();
	
	public void addPlayerN(){
	for (String p: getPlayers()) {
        if(reda.size() > greena.size()){
        	greena.add(p);
        	players.put(p, Team.GREEN);
        	return;
        	
       }
       else{

         	reda.add(p);
        	players.put(p, Team.RED);
        	return;
       }
    
        
}
	}
	
	private String name; //Arena name
	
	//red to red spawn, green to green spawn
	
	public Location getLocation(Team team){
		switch(team){
		case RED: return redspawn;
		case GREEN: return greenspawn;
		default: return null;
		}
	}
	
	public Team getTeam(Player p){
		return players.get(p.getName());
	}
	
	

	
	
/*
	public void addPlayer(Player p){
		if(WinterSlashManager.getManager().redbigger()){
		players.put(p.getName(), Team.GREEN);
		WinterSlashManager.getManager().SetReda(p.getName());
		}
		else{
			players.put(p.getName(), Team.RED);
			WinterSlashManager.getManager().SetGreena(p.getName());
		}
	}
	*/
	
	// sample of wrapper allocation
	public void xaddPlayer(Player p)
	 	{
	 		new WSA_PlayerWrapperImpl(p,xLesserTeam());
	 	}
	 	
	 	int xLesserTeam()
	 	{
	 		return 0; /** Teams must be stored just like players - single hashmap and data, when implemented
	 		there is no need if iterations each time, just reading size will be ok*/
	 	}

	 
	 	/*
	 // team sorting 
	 	private ArrayList<String> reda = new ArrayList<String>();
	 	private ArrayList<String> greena = new ArrayList<String>();
	
	 	private Team LesserTeam(){
		int red =0,  green =0;
		if(red == green){
			red++;
			return Team.RED;
		}
		else{
			
			green++;
			return Team.GREEN;
		}
		
	}
	
	*/
	
	// array lists
	private ArrayList<String> playersm = new ArrayList<String>();
	private ArrayList<String> wsplayers = new ArrayList<String>();
	
	//Alive array
	
	private ArrayList<String> alive = new ArrayList<String>();
	public void SetAlive(String player){
		frozen.add(player);
	}

	public void UnsetAlive(String player){
		frozen.remove(player);
	}
	
	
	public ArrayList<String> GetAlive() {
		return frozen;
	}
	
	//Frozen array
	
	private ArrayList<String> frozen = new ArrayList<String>();
	public void SetFrozen(String player){
		frozen.add(player);
	}

	public void UnsetFrozen(String player){
		frozen.remove(player);
	}
	
	
	public ArrayList<String> GetFrozen() {
		return frozen;
	}
	
	//sign click
	
			private ArrayList<String> sign = new ArrayList<String>();
			public void SetSign(String player){
				sign.add(player);
			}

			public void ClearSign(String player){
				sign.remove(player);
			}
			
			
			public ArrayList<String> GetSign() {
				return sign;
			}
	
	
	//Game array
	
		private ArrayList<String> game = new ArrayList<String>();
		public void SetGamers(String player){
			game.add(player);
		}

		public void RemoveGamers(String player){
			game.remove(player);
		}
		
		
		public ArrayList<String> GetGamers() {
			return game;
		}
	
	//red team array
	
	private ArrayList<String> redteam = new ArrayList<String>();
	public void RedTeamAdd(String player){
		redteam.add(player);
	}
	
	
	public ArrayList<String> GetRedTeam() {
		return redteam;
	}
	
	//frozen red team array
	
	private ArrayList<String> frozenred = new ArrayList<String>();
	public void FrozenRedAdd(String player){
		frozenred.add(player);
	}
	
	public void FrozenRedRemove(String player){
		frozenred.remove(player);
	}
	
	
	public ArrayList<String> GetRedFrozenTeam() {
		return frozenred;
	}
	
	//green team array
	
	private ArrayList<String> greenteam = new ArrayList<String>();
	public void GreenTeamAdd(String player){
		greenteam.add(player);
	}
	 
	
	public ArrayList<String> GetGreenTeam() {
		return greenteam;
	}
	
	//frozen green team array
	
	private ArrayList<String> frozengreen = new ArrayList<String>();
	public void FrozenGreenAdd(String player){
		frozengreen.add(player);
	}
	
	public void FrozenGreenRemove(String player){
		frozengreen.remove(player);
	}
	
		
	public ArrayList<String> GetGreenFrozenTeam() {
	return frozengreen;
	}
	
		
		
		
	
	private int maxPlayers;
	private boolean inGame = false; //Boolean to determine if an Arena is in-game or not, automatically make it false
	private boolean isFrozen = false; 
	private boolean isRed = false; 



	 
	 
	//Now for a Constructor:
	public WinterSlashArena (String arenaName, Location joinLocation, Location redLocation, Location greenLocation, Location endLocation, int maxPlayers) { 
	
		//Lets initialize it all:

    //directly
    this.name = arenaName;
	this.joinLocation = joinLocation;
	this.redspawn = redLocation;
	this.greenspawn = greenLocation;
	this.endLocation = endLocation;
	this.maxPlayers = maxPlayers;
	
	
	 
	//Now lets add this object to the list of objects:
	arenaObjects.add(this);
	 
	}
	

	public Location getJoinLocation() {
	return this.joinLocation;
	}
	
	public Location getRedSpawn() {
		return this.redspawn;
	}
	
	public Location getGreenSpawn() {
		return this.greenspawn;
	}
	 
	public void setJoinLocation(Location joinLocation) {
	this.joinLocation = joinLocation;
	}
	 
	 
	public void setStartLocation(Location startLocation) {
	this.startLocation = startLocation;
	}
	 
	public Location getEndLocation() {
	return this.endLocation;
	}
	 
	public void setEndLocation(Location endLocation) {
	this.endLocation = endLocation;
	}
	 
	public String getName() {
	return this.name;
	}
	 
	public void setName(String name) {
	this.name = name;
	}
	 
	public int getMaxPlayers() {
	return this.maxPlayers;
	}
	 
	public void setMaxPlayers(int maxPlayers) {
	this.maxPlayers = maxPlayers;
	}
	 
	public ArrayList<String> getPlayers() {
	return this.playersm;
	}
	 
	 
	 
	//And finally, some booleans:
	public boolean isFull() { //Returns weather the arena is full or not
	if (players.size() >= maxPlayers) {
	return true;
	} else {
	return false;
	}
	}
	 


	 
	public boolean isInGame() {
	return inGame;
	}
	 
	public void setInGame(boolean inGame) {
	this.inGame = inGame;
	}

	 
	//To send each player in the arena a message
	public void sendMessage(String message) {
	for (String s: playersm) {
	Bukkit.getPlayer(s).sendMessage(message);
	}
	}
	
	public boolean ifPlayerIsRed(Player p) {
		for (String p1 : players.keySet()) {
		if(players.get(p1) == Team.RED){
			return true;
		}
		}
		return false;
		}

}
