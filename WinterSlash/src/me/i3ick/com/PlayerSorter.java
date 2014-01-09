// THIS IS NOT A PLAYER SORTER. IT IS A PLAYER FREEZER EVENT!

package me.i3ick.com;


import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerSorter implements Listener{
	
	private freezetagMain plugin;
	
	public PlayerSorter (freezetagMain plugin){
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent e){
		if(plugin.frozen.contains(e.getPlayer().getName())){
			e.getPlayer().teleport(e.getPlayer().getLocation());
		}
	}
}
