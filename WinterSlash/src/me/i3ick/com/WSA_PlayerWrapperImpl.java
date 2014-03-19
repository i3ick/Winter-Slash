package me.i3ick.com;

import java.util.HashMap;

import org.bukkit.entity.Player;

@SuppressWarnings("all")
public class WSA_PlayerWrapperImpl
{
	private static HashMap<String,WSA_PlayerWrapperImpl> MAP = new HashMap<String, WSA_PlayerWrapperImpl>();
	//this is class inside class inside class
	//it hard to understand but very effective way of storing things
	
	/**
	 * CONSTANT SECTION
	 * magic numbers are not OK, soo it will be good move to store teams as named kays
	 */
	static final int TEAM_RED  = 0;
	static final int TEAM_BLUE = 1;
	
	/**
	 * START ARBITRARY DATA SECTION
	 */
	public int Team;
	public int Deaths;
	public int Kills;
	public int Saves;
	public int Frozed; //boolean and int in java same size
	public int Whatever;
	//you can store as many fields on every player as you want
	
	/**
	 * END DATA SECTION
	 */
	public WSA_PlayerWrapperImpl(Player Who,int Team)
	{
		this.Team = Team;
		MAP.put(Player2Key(Who), this);
	}
	
	static private String Player2Key(Player p)
	{
		if (p == null) return "null";
		return p.getName().toLowerCase();
	}
	
	static public WSA_PlayerWrapperImpl getWrapperContainer(Player p)
	{
		return MAP.get(Player2Key(p));
	}
	
	
}
