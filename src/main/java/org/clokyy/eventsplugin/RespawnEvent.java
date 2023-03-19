package org.clokyy.eventsplugin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RespawnEvent implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        Location respawnLocation = event.getRespawnLocation();


        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(respawnLocation.getWorld()));
        ProtectedRegion eventRegion = regionManager.getRegion("event");

        if(eventRegion != null && eventRegion.contains(respawnLocation.getBlockX(), respawnLocation.getBlockY(), respawnLocation.getBlockZ())){
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 36000, 2));
        }


    }
}
