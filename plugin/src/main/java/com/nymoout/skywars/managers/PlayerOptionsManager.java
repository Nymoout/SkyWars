package com.nymoout.skywars.managers;

import com.google.common.collect.Maps;
import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.GameType;
import com.nymoout.skywars.game.Crate;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.menus.playeroptions.*;
import com.nymoout.skywars.menus.playeroptions.objects.ParticleEffect;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PlayerOptionsManager {

    private final Map<Projectile, List<ParticleEffect>> projectileMap = Maps.newConcurrentMap();
    private final Map<UUID, List<ParticleEffect>> playerMap = Maps.newConcurrentMap();
    private final List<ParticleEffect> crateEffects = new ArrayList<>();

    public PlayerOptionsManager() {
        if (SkyWars.getConfigManager().particlesEnabled()) {
            crateEffects.add(new ParticleEffect("CRIT", 0, 2, 0, 8, 4));
            crateEffects.add(new ParticleEffect("CRIT_MAGIC", 0, 2, 0, 8, 4));
            SkyWars.get().getServer().getScheduler().scheduleSyncRepeatingTask(SkyWars.get(), () -> {
                for (Projectile projectile : projectileMap.keySet()) {
                    if (projectile.isDead()) {
                        projectileMap.remove(projectile);
                    } else {
                        List<ParticleEffect> effects = projectileMap.get(projectile);
                        doEffects(projectile.getLocation(), effects, true);
                    }
                }
                for (UUID p : playerMap.keySet()) {
                    Player player = Bukkit.getPlayer(p);
                    if (player == null) {
                        playerMap.remove(p);
                    } else {
                        List<ParticleEffect> effects = playerMap.get(p);
                        doEffects(player.getLocation(), effects, false);
                    }
                }

                for (GameMap gMap : GameMap.getPlayableArenas(GameType.ALL)) {
                    for (Crate crate : gMap.getCrates()) {
                        doEffects(crate.getEntity().getLocation(), crateEffects, false);
                    }
                }
            }, SkyWars.getConfigManager().getTicksPerUpdate(), SkyWars.getConfigManager().getTicksPerUpdate());
        }
        GlassColorOption.loadPlayerOptions();
        ParticleEffectOption.loadPlayerOptions();
        com.nymoout.skywars.menus.playeroptions.ProjectileEffectOption.loadPlayerOptions();
        WinSoundOption.loadPlayerOptions();
        KillSoundOption.loadPlayerOptions();
        TauntOption.loadPlayerOptions();
    }

    /*Handles projectile effects*/

    public void addProjectile(Projectile p, List<ParticleEffect> e) {
        projectileMap.put(p, e);
    }

    void addPlayer(UUID p, List<ParticleEffect> e) {
        playerMap.put(p, e);
    }

    void removePlayer(UUID p) {
        playerMap.remove(p);
    }

    private void doEffects(Location location, List<ParticleEffect> effects, boolean isProjectile) {
        Random random = new Random();
        if (isProjectile) {
            for (ParticleEffect p : effects) {
                Util.get().sendParticles(location.getWorld(), p.getType(), (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, getData(p), 2);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 3; i++) {
                            Util.get().sendParticles(location.getWorld(), p.getType(), (float) location.getX(), (float) location.getY(), (float) location.getZ(), (float) (random.nextFloat() * (0.5 - -0.5) + -0.5), (float) (random.nextFloat() * (0.5 - -0.5) + -0.5), (float) (random.nextFloat() * (0.5 - -0.5) + -0.5), getData(p), 1);
                        }
                    }
                }.runTaskLater(SkyWars.get(), 3);
            }
        } else {
            for (ParticleEffect p : effects) {
                Util.get().sendParticles(location.getWorld(), p.getType(), (float) location.getX(), (float) location.getY(), (float) location.getZ(), random.nextFloat(), random.nextFloat() * (p.getOffsetYU() - p.getOffsetYL()) + p.getOffsetYL(), random.nextFloat(), getData(p), random.nextInt((p.getAmountU() - p.getAmountL()) + p.getAmountL()) + 1);
            }
        }
    }

    private float getData(ParticleEffect p) {
        Random random = new Random();
        float data = p.getData();
        if (p.getData() == -1) {
            data = random.nextFloat();
        }
        return data;
    }

}
