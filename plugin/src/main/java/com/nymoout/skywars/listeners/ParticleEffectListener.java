package com.nymoout.skywars.listeners;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.managers.PlayerStat;
import com.nymoout.skywars.menus.playeroptions.ProjectileEffectOption;
import com.nymoout.skywars.menus.playeroptions.objects.ParticleEffect;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.List;

public class ParticleEffectListener implements Listener {

    @EventHandler
    public void projectileLaunch(ProjectileLaunchEvent e) {
        Projectile projectile = e.getEntity();
        if (projectile instanceof Snowball || projectile instanceof Egg || projectile instanceof Arrow) {
            if (projectile.getShooter() instanceof Player) {
                Player player = (Player) projectile.getShooter();
                GameMap gMap = MatchManager.get().getPlayerMap(player);
                if (gMap != null) {
                    PlayerStat ps = PlayerStat.getPlayerStats(player.getUniqueId());
                    if (ps != null) {
                        String key = ps.getProjectileEffect();
                        ProjectileEffectOption peo = (ProjectileEffectOption) ProjectileEffectOption.getPlayerOptionByKey(key);
                        if (peo != null) {
                            List<ParticleEffect> effects = peo.getEffects();
                            if (key != null && effects != null) {
                                if (!key.equalsIgnoreCase("none")) {
                                    SkyWars.getPlayerOptionsManager().addProjectile(projectile, effects);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
