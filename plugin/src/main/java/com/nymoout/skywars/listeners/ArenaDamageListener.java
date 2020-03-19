package com.nymoout.skywars.listeners;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.GameType;
import com.nymoout.skywars.enums.MatchState;
import com.nymoout.skywars.game.Crate;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.game.PlayerData;
import com.nymoout.skywars.managers.MatchManager;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class ArenaDamageListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerDamagedByAlly(EntityDamageByEntityEvent event) {
        Player target;
        Entity damager = event.getDamager();
        if (event.getEntity() instanceof Player) {
            target = (Player) event.getEntity();
            GameMap gameMap = MatchManager.get().getPlayerMap(target);
            if (gameMap != null) {
                if (!gameMap.getSpectators().contains(target.getUniqueId())) {
                    if (gameMap.getMatchState() == MatchState.ENDING || gameMap.getMatchState() == MatchState.WAITINGSTART) {
                        event.setCancelled(true);
                    } else {
                        event.setCancelled(false);
                        if (gameMap.getProjectilesOnly()) {
                            if (damager instanceof Projectile) {
                                doProjectile(gameMap, damager, event, target);
                            } else if (damager instanceof Player) {
                                event.setCancelled(true);
                            }
                        } else {
                            if (damager instanceof Projectile) {
                                doProjectile(gameMap, damager, event, target);
                            } else if (damager instanceof Player) {
                                doPVP(damager, target, event, gameMap);
                            }
                        }
                    }
                }
            }
        }
    }

    private void doProjectile(GameMap gMap, Entity damager, EntityDamageByEntityEvent event, Player target) {
        Projectile proj = (Projectile) damager;
        if (damager instanceof Snowball) {
            event.setDamage(SkyWars.getConfigManager().getSnowDamage());
        }
        if (damager instanceof Egg) {
            event.setDamage(SkyWars.getConfigManager().getEggDamage());
        }
        if (gMap.isDoubleDamageEnabled()) {
            event.setDamage(event.getDamage() * 2);
        }
        if (proj.getShooter() instanceof Player) {
            Player hitter = (Player) proj.getShooter();
            if (hitter != null && hitter != target) {
                PlayerData pd = PlayerData.getPlayerData(target.getUniqueId());
                if (pd != null) {
                    pd.setTaggedBy(hitter);
                }
            }
        }
    }

    private void doPVP(Entity damager, Player target, EntityDamageByEntityEvent event, GameMap gMap) {
        Player hitter = (Player) damager;
        PlayerData pd = PlayerData.getPlayerData(target.getUniqueId());
        if (gMap.isDoubleDamageEnabled()) {
            event.setDamage(event.getDamage() * 2);
        }
        if (pd != null) {
            pd.setTaggedBy(hitter);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void playerDamaged(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            GameMap gameMap = MatchManager.get().getPlayerMap(player);
            if (gameMap != null) {
                if (gameMap.getMatchState() == MatchState.ENDING || gameMap.getMatchState() == MatchState.WAITINGSTART) {
                    event.setCancelled(true);
                    return;
                }
                if (!gameMap.allowFallDamage()) {
                    if (event.getCause().equals(DamageCause.FALL)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void saturationLoss(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            GameMap gameMap = MatchManager.get().getPlayerMap(player);
            if (gameMap != null) {
                if (gameMap.getMatchState() == MatchState.WAITINGSTART) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void regen(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        GameMap gameMap = MatchManager.get().getPlayerMap(player);
        if (gameMap != null) {
            if (!gameMap.allowRegen()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void bowEvent(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        GameMap gameMap = MatchManager.get().getPlayerMap(player);
        if (gameMap != null) {
            if (gameMap.getMatchState() == MatchState.WAITINGSTART || gameMap.getMatchState() == MatchState.ENDING) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void arrowEvent(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            final Arrow arrow = (Arrow) event.getEntity();
            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();
                GameMap gameMap = MatchManager.get().getPlayerMap(player);
                if (gameMap != null) {
                    arrow.remove();
                }
            }
        }
    }

    @EventHandler
    public void onAnvilLand(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock) {
            FallingBlock fb = (FallingBlock) event.getEntity();
            if (SkyWars.getNMS().checkMaterial(fb, Material.ANVIL)) {
                for (GameMap gMap : GameMap.getPlayableArenas(GameType.ALL)) {
                    if (gMap.getAnvils().contains(event.getEntity().getUniqueId().toString())) {
                        event.setCancelled(true);
                        gMap.getAnvils().remove(event.getEntity().getUniqueId().toString());
                        return;
                    }
                }
            } else if (SkyWars.getNMS().checkMaterial(fb, Material.SAND)) {
                for (GameMap gMap : GameMap.getPlayableArenas(GameType.ALL)) {
                    for (Crate crate : gMap.getCrates()) {
                        if (fb.equals(crate.getEntity())) {
                            event.setCancelled(true);
                            fb.setDropItem(false);
                            fb.getWorld().getBlockAt(fb.getLocation()).setType(Material.ENDER_CHEST);
                            crate.setLocation(fb.getWorld().getBlockAt(fb.getLocation()));
                            fb.remove();
                        }
                    }

                }
            }
        }
    }
}
