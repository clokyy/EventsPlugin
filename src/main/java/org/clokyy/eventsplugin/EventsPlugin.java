package org.clokyy.eventsplugin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public final class EventsPlugin extends JavaPlugin implements Listener {

    private FileConfiguration config; //Grabs JAVA library FileConfiguration and uses config
    private File configFile; //Grabs JAVA library File

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(new RespawnEvent(), this);


        //              BASIC LICENSEING SYSTEM FOR MY PLUGINS
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "-------------------------------------");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "ENABLING EVENTS");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Created and Updated by Clokyy (BigScaryMan#2495)");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Version 1.1");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "-------------------------------------");
        //              BASIC LICENSEING SYSTEM FOR MY PLUGINS
        saveDefaultConfig();
        config = getConfig();


        //Initialize config file
        configFile = new File(getDataFolder(), "config.yml"); //Find config.yml
        config = new YamlConfiguration();

        if(!configFile.exists()){
            saveDefaultConfig();
        }else{
            try {
                config.load(configFile);
            } catch (IOException | InvalidConfigurationException e ){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean eventStarted = false; // Boolean statement which only allows event to be ran once

    @Override
    public boolean onCommand( CommandSender sender,  Command command,  String label,  String[] args) {
        if (command.getName().equalsIgnoreCase("event") && sender instanceof Player) {  //If the command [args[0]] == event and the sender is not CONSOLE
            Player player = (Player) sender; //Basic player definition
            if (args.length > 0 && args[0].equalsIgnoreCase("start") && player.hasPermission("event.start")) {

                if (eventStarted == true) {
                    sender.sendMessage(ChatColor.RED + "An event has already been started!");
                } else {

                    eventStarted = true;
                    getServer().broadcastMessage(ChatColor.GREEN + " AN EVENT HAS BEEN STARTED, USE /event tp TO JOIN!!!"); //If the admin has the required permissions and starts an event them message will be broadcasted to the entire server.
                }


                return true;
            } else if (args.length > 0 && args[0].equalsIgnoreCase("tp")) {


                if (!eventStarted) {
                    sender.sendMessage(ChatColor.RED + "There is no event running currently.");
                }else{
                    int x = config.getInt("event-location.x"); //grabs coords from config.yml
                    int y = config.getInt("event-location.y");
                    int z = config.getInt("event-location.z");

                    Location eventLocation = new Location(player.getWorld(), x, y, z);
                    player.teleport(eventLocation);

                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 36000, 2)); //gives potion effect for 30 minutes.

                    return true;
                }
            } else if (args.length > 0 && args[0].equalsIgnoreCase("end") && player.hasPermission("event.start"))
                if (!eventStarted) {
                    sender.sendMessage(ChatColor.RED + "There currently is no event running.");
                } else if (eventStarted) {
                    eventStarted = false;
                    sender.sendMessage(ChatColor.DARK_RED + "EVENT HAS BEEN ENDED");
                    getServer().broadcastMessage(ChatColor.RED + "EVENT HAS BEEN ENDED BY " + sender.getName());
                    return false;
                }

            return true;
        }
        return false;

    }




    public boolean RegionCheck(Player player){
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));
        ProtectedRegion eventRegion = regionManager.getRegion("event");
        if(eventRegion == null){
            Bukkit.getConsoleSender().sendMessage("Region is not located please define by /rg define event");
        }


        return eventRegion.contains(BukkitAdapter.asBlockVector(player.getLocation()));
    }




}
