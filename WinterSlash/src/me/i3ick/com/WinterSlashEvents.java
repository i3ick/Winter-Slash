package me.i3ick.com;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class WinterSlashEvents implements Listener {

    private WinterSlashMain plugin;

    public WinterSlashEvents(WinterSlashMain plugin) {
        this.plugin = plugin;
    }

    //saves location before player dies/gets frozen

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.wsplayersHM.containsValue(player.getName())) {
                if (player.getHealthScale() < 6) {
                    player.getLocation();
                    plugin.getConfig().set(player.getName() + ".X", player.getLocation().getBlockX());
                    plugin.getConfig().set(player.getName() + ".Y", player.getLocation().getBlockY());
                    plugin.getConfig().set(player.getName() + ".Z", player.getLocation().getBlockZ());
                    plugin.saveConfig();
                }

            }
        }
    }

    //Keeps frozen players still
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent e) {
        if (plugin.frozenred.contains(e.getPlayer().getName())) {
            e.getPlayer().teleport(e.getPlayer().getLocation());
        }else if (plugin.frozengreen.contains(e.getPlayer().getName())) {
            e.getPlayer().teleport(e.getPlayer().getLocation());
        }
    }

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
    }

    //prevent damage from frozen players / prevent friendly fire / prevent killing frozen players
    @EventHandler
    public void onPlayerDamage_frozen_damage(EntityDamageByEntityEvent event) {
        Entity victim_entity = event.getEntity();
        Entity damager_entity = event.getDamager();
        if (victim_entity instanceof Player) {
            if (damager_entity instanceof Player) {
                Player victim = (Player) victim_entity;
                Player damager = (Player) damager_entity;
                if (plugin.frozen.contains(victim.getName())) {
                    event.setCancelled(true);
                }


                //disable FF for red team
                if (plugin.wsred.containsValue(victim.getName())) {
                    if (plugin.frozenred.contains(victim.getName())) {
                        if (plugin.wsred.containsValue(damager.getName())) {
                            //do nothing - unfreezeing process
                        } else {
                            event.setCancelled(true);
                            //disables other people to harm frozen people
                        }
                    } else {
                        if (plugin.wsred.containsValue(damager.getName())) {
                            event.setCancelled(true);
                            //disables friendly fire
                        }
                    }
                }


                //disable FF for green team
                else if (plugin.wsgreen.containsValue(victim.getName())) {
                    if (plugin.frozengreen.contains(victim.getName())) {
                        if (plugin.wsgreen.containsValue(damager.getName())) {
                            //do nothing - unfreezeing process
                        } else {
                            event.setCancelled(true);
                            //disables other people to harm frozen people
                        }
                    } else {
                        if (plugin.wsgreen.containsValue(damager.getName())) {
                            event.setCancelled(true);
                            //disables friendly fire
                        }
                    }
                }
            } else {
                return;
            }
        }
    }


    //Pack-a-Punch device

    //sign setup
    @EventHandler
    public void onSignCreation(SignChangeEvent e) {
        Player player = (Player) e.getPlayer();
        if (!player.hasPermission("freezetag.signplace")) {
            player.sendMessage("No permission");
            return;
        }
        if (e.getLine(0).equals("/ftpap")) {
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

    //Pack-a-Punch logic
    @EventHandler
    public void onPaP(PlayerInteractEvent e) {
        Player player = (Player) e.getPlayer();
        ItemStack revivor = new ItemStack(Material.BLAZE_ROD, 1);
        ItemStack revivorUpgrade = new ItemStack(Material.BONE, 1);
        int newItemSlot = player.getInventory().firstEmpty();
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getType().equals(Material.WALL_SIGN)) {
                revivorUpgrade.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
                player.getInventory().setItem(newItemSlot, revivorUpgrade);
                player.getInventory().removeItem(revivor);

            }
        }
    }

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
    }

    //blocks commands once a player is ingame
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onServerCommand(PlayerCommandPreprocessEvent e) {
        if (plugin.wsred.containsValue(e.getPlayer())) {
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
        } else if (plugin.wsgreen.containsValue(e.getPlayer())) {

            if (e.getMessage().equals("/wsl")) {
            } else {
                if (e.getPlayer().isOp()) {
                    return;
                } else {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.YELLOW + "Can't use commands while in game. Use /wsl to leave");
                }
            }
        } else {
            return;
        }

    }

    //Game ending & freezing/unfreezing logic
    @EventHandler(priority = EventPriority.HIGHEST)
    public void checkDeath(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();
        Player p;
        if (entity instanceof Player) {
            p = (Player) entity;
        } else {
            return;
        }

        if (!((p.getHealth() - e.getDamage()) <= 0)) { //This is not wrong. http://forums.bukkit.org/threads/oops-i-didnt-break-your-plugins-ambiguous-gethealth.156975/
            return;
            //If he is not going to die.
        }

        if (plugin.wsplayersHM.containsValue(p)) {
            //p.sendMessage("testdead");
            if (isRedTeam(p)) {
                // if player isn't frozen, freeze him. If he is, unfreeze him.
                if (!isFrozenRed(p)) {
                    //p.sendMessage("testdead");
                    plugin.frozenred.add(p.getName());
                } else {
                    plugin.frozenred.remove(p);
                }
                if (plugin.frozengreen.size() == plugin.wsgreen.values().size()) {
                    //  end the game...
                    Bukkit.broadcastMessage(ChatColor.GREEN + "The RED team has won the game!");
                    return;
                }
            } else if (isGreenTeam(p)) {
                if (!isFrozenGreen(p)) {
                    plugin.frozengreen.add(p.getName());
                }
                if (plugin.frozenred.size() == plugin.wsred.values().size()) {
                    //  end the game...
                    Bukkit.broadcastMessage(ChatColor.GREEN + "The GREEN team has won the game!");
                    return;
                }
            } else {
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


    // RESPAWN EVENTS
    // RESPAWN EVENTS
    // RESPAWN EVENTS


    //teleports players back to the position where they died so they can be frozen
    /*
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e){
        if(plugin.wsred.containsValue(e.getPlayer())){
            String player =  e.getPlayer().getName();
            Player p =  e.getPlayer();
            if (plugin.frozenred.contains(player)){
            	plugin.frozenred.remove(player);
            	e.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR,1));
            }
            else{
            	plugin.frozenred.add(player);
            	e.getPlayer().getInventory().setHelmet(new ItemStack(Material.ICE,1));
            	
            	// tp to death position
                int lastposX = plugin.getConfig().getInt(player + ".X");
                int lastposY = plugin.getConfig().getInt(player + ".Y");
                int lastposZ = plugin.getConfig().getInt(player + ".Z");
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




        else if(plugin.wsgreen.containsValue(e.getPlayer())){
        	String player =  e.getPlayer().getName();
            Player p =  e.getPlayer();
            if (plugin.frozengreen.contains(player)){
            	plugin.frozengreen.remove(player);
            	e.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR,1));
            }
            else{
            	plugin.frozengreen.add(player);
            	e.getPlayer().getInventory().setHelmet(new ItemStack(Material.ICE,1));
            	
            	// tp to death position
                int lastposX = plugin.getConfig().getInt(player + ".X");
                int lastposY = plugin.getConfig().getInt(player + ".Y");
                int lastposZ = plugin.getConfig().getInt(player + ".Z");
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
        return;

    } */


}
