package com.nymoout.skywars.game.cages;

import com.nymoout.skywars.enums.MatchState;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.game.TeamCard;
import com.nymoout.skywars.menus.playeroptions.GlassColorOption;
import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.game.PlayerCard;
import com.nymoout.skywars.managers.PlayerStat;
import com.nymoout.skywars.menus.gameoptions.objects.CoordLoc;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public abstract class Cage {

    ArrayList<CoordLoc> bottomCoordOffsets = new ArrayList<>();
    ArrayList<CoordLoc> middleCoordOffsets = new ArrayList<>();
    ArrayList<CoordLoc> topCoordOffsets = new ArrayList<>();
    CageType cageType;

    public void createSpawnPlatforms(GameMap gMap) {
        World world = gMap.getCurrentWorld();
        for (TeamCard tCard : gMap.getTeamCards()) {
            int x = tCard.getSpawn().getX();
            int y = tCard.getSpawn().getY();
            int z = tCard.getSpawn().getZ();
            for (CoordLoc loc : bottomCoordOffsets) {
                world.getBlockAt(x + loc.getX(), y + loc.getY(), z + loc.getZ()).setType(Material.GLASS);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (CoordLoc loc : middleCoordOffsets) {
                        world.getBlockAt(x + loc.getX(), y + loc.getY(), z + loc.getZ()).setType(Material.GLASS);
                    }
                }
            }.runTaskLater(SkyWars.get(), 7L);
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (CoordLoc loc : topCoordOffsets) {
                        world.getBlockAt(x + loc.getX(), y + loc.getY(), z + loc.getZ()).setType(Material.GLASS);
                    }
                }
            }.runTaskLater(SkyWars.get(), 14L);
        }
    }

    public boolean setGlassColor(GameMap gMap, TeamCard tCard) {
        if (gMap.getMatchState() == MatchState.WAITINGSTART) {
            if (tCard != null) {
                World world = gMap.getCurrentWorld();
                int x = tCard.getSpawn().getX();
                int y = tCard.getSpawn().getY();
                int z = tCard.getSpawn().getZ();
                Random rand = new Random();

                ArrayList<MaterialWithByte> colors = new ArrayList<>();

                if (gMap.getTeamSize() > 1 && !SkyWars.getConfigManager().usePlayerGlassColors()) {
                    colors.add(new MaterialWithByte(SkyWars.getNMS().getColorItem(SkyWars.getConfigManager().getTeamMaterial(), tCard.getByte()).getType(), tCard.getByte()));
                } else {
                    for (PlayerCard p : tCard.getPlayerCards()) {
                        Player player = p.getPlayer();
                        if (player != null) {
                            PlayerStat pStat = PlayerStat.getPlayerStats(player);
                            if (pStat != null) {
                                String col = pStat.getGlassColor().toLowerCase();
                                byte cByte = Util.get().getByteFromColor(col);
                                GlassColorOption color = (GlassColorOption) GlassColorOption.getPlayerOptionByKey(col);
                                if (color != null) {
                                    colors.add(new MaterialWithByte(color.getItem().getType(), cByte));
                                } else {
                                    if (cByte <= -1) {
                                        colors.add(new MaterialWithByte(Material.GLASS, cByte));
                                    } else {
                                        colors.add(new MaterialWithByte(SkyWars.getNMS().getColorItem("STAINED_GLASS", cByte).getType(), cByte));
                                    }
                                }
                            }
                        }
                    }
                }

                for (CoordLoc loc : bottomCoordOffsets) {
                    setBlockColor(loc, x, y, z, world, colors.get(rand.nextInt(colors.size())));
                }
                for (CoordLoc loc : middleCoordOffsets) {
                    setBlockColor(loc, x, y, z, world, colors.get(rand.nextInt(colors.size())));
                }
                for (CoordLoc loc : topCoordOffsets) {
                    setBlockColor(loc, x, y, z, world, colors.get(rand.nextInt(colors.size())));
                }
                return true;
            }
        }
        return false;
    }

    private void setBlockColor(CoordLoc loc, int x, int y, int z, World world, MaterialWithByte materialWithByte) {
        if (materialWithByte.cByte <= -1) {
            world.getBlockAt(x + loc.getX(), y + loc.getY(), z + loc.getZ()).setType(materialWithByte.mat);
        } else {
            SkyWars.getNMS().setBlockWithColor(world, x + loc.getX(), y + loc.getY(), z + loc.getZ(), materialWithByte.mat, materialWithByte.cByte);
        }
    }

    public void removeSpawnHousing(GameMap gMap) {
        World world = gMap.getCurrentWorld();
        gMap.setAllowFallDamage(false);
        new BukkitRunnable() {
            @Override
            public void run() {
                gMap.setAllowFallDamage(SkyWars.getConfigManager().allowFallDamage());
            }
        }.runTaskLater(SkyWars.get(), 100L);
        for (TeamCard tCard : gMap.getTeamCards()) {
            int x = tCard.getSpawn().getX();
            int y = tCard.getSpawn().getY();
            int z = tCard.getSpawn().getZ();
            for (CoordLoc loc : bottomCoordOffsets) {
                world.getBlockAt(x + loc.getX(), y + loc.getY(), z + loc.getZ()).setType(Material.AIR);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (CoordLoc loc : middleCoordOffsets) {
                        world.getBlockAt(x + loc.getX(), y + loc.getY(), z + loc.getZ()).setType(Material.AIR);
                    }
                }
            }.runTaskLater(SkyWars.get(), 7L);
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (CoordLoc loc : topCoordOffsets) {
                        world.getBlockAt(x + loc.getX(), y + loc.getY(), z + loc.getZ()).setType(Material.AIR);
                    }
                }
            }.runTaskLater(SkyWars.get(), 14L);
        }
    }

    public CageType getType() {
        return cageType;
    }

    private class MaterialWithByte {
        private Material mat;
        private byte cByte;

        MaterialWithByte(Material mat, byte cByte) {
            this.mat = mat;
            this.cByte = cByte;
        }
    }
}
