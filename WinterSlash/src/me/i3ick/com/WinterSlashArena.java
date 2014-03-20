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


public class WinterSlashArena {
	

	
	
	//A list of all the Arena Objects
	public static ArrayList<WinterSlashArena> arenaObjects = new ArrayList<WinterSlashArena>();

	
	//Some fields we want each Arena object to store:
	private Location redspawn, greenspawn, joinLocation, startLocation, endLocation; //Some general arena locations
	private HashMap<String, Team> players = new HashMap<String, Team>();
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
	
	@Deprecated
	public void addPlayer(Player p){
		players.put(p.getName(), LesserTeam());
	}
	
	
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
	
	private Team LesserTeam(){
		int red =0,  green =0;
		for(String p : players.keySet()) {
			if(players.get(p) == Team.RED) red++;
			else green++;
		}
		if(red > green) return Team.GREEN;
		else return Team.RED;
	}
	
	
	
	// array lists
	private ArrayList<String> playersm = new ArrayList<String>();
	private ArrayList<String> frozen = new ArrayList<String>();
	private ArrayList<String> frozenred = new ArrayList<String>();
	private ArrayList<String> frozengreen = new ArrayList<String>();

	private ArrayList<String> wsplayers = new ArrayList<String>();
	private ArrayList<String> wsred = new ArrayList<String>();
	private ArrayList<String> wsgreen = new ArrayList<String>();

	 
	private int maxPlayers;
	private boolean inGame = false; //Boolean to determine if an Arena is ingame or not, automaticly make it false




	 
	 
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
		else{
			return false;
		}
		}
		return false;
		}

}
