package com.nymoout.skywars.menus;

import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.MatchState;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.game.PlayerCard;
import com.nymoout.skywars.game.TeamCard;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Party;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TeamSelectionMenu {

    private static int menuSize = 27;

    public TeamSelectionMenu(GameMap gMap) {
        String menuName = new Messaging.MessageFormatter().setVariable("mapname", gMap.getDisplayName()).format("menu.teamselection-menu-title");
        if (SkyWars.getNMS().getVersion() < 9) {
            if (menuName.length() > 32) {
                menuName = menuName.substring(0, 31);
            }
        }
        Inventory menu = Bukkit.createInventory(null, menuSize + 9, menuName);
        ArrayList<Inventory> invs = new ArrayList<>();
        invs.add(menu);

        Runnable update = () -> {
            if (SkyWars.getIconMenuController().hasViewers(gMap.getName() + "teamselect") || SkyWars.getIconMenuController().hasViewers(gMap.getName() + "teamspectate")) {
                ArrayList<Inventory> invs1 = SkyWars.getIconMenuController().getMenu(gMap.getName() + "teamselect").getInventories();
                for (Inventory inv : invs1) {
                    for (int i = 0; i < menuSize; i++) {
                        inv.setItem(i, new ItemStack(Material.AIR, 1));
                    }
                }
                String mat = SkyWars.getConfigManager().getTeamMaterial();
                List<String> lores = new ArrayList<>();
                for (TeamCard tCard : gMap.getTeamCards()) {
                    String name = tCard.getTeamName();
                    byte color = tCard.getByte();
                    ItemStack item = SkyWars.getNMS().getColorItem(mat, color);
                    lores.clear();
                    lores.add((new Messaging.MessageFormatter().setVariable("playercount", "" + tCard.getPlayersSize())
                            .setVariable("maxplayers", "" + tCard.getSize()).format("signs.line4")));
                    for (PlayerCard pCard : tCard.getPlayerCards()) {
                        Player p = pCard.getPlayer();
                        if (p != null) {
                            lores.add(ChatColor.GREEN + p.getName());
                        }
                    }
                    invs1.get(0).setItem(tCard.getPosition(), SkyWars.getNMS().getItemStack(item, lores, name));
                }
                if (SkyWars.getConfigManager().spectateMenuEnabled()) {
                    ArrayList<Inventory> specs = SkyWars.getIconMenuController().getMenu(gMap.getName() + "teamspectate").getInventories();
                    int i = 0;
                    for (Inventory inv : invs1) {
                        if (specs.get(i) == null) {
                            specs.add(Bukkit.createInventory(null, menuSize, new Messaging.MessageFormatter().setVariable("mapname", gMap.getDisplayName()).format("menu.teamspectate-menu-title")));
                        }
                        specs.get(0).setContents(inv.getContents());
                        i++;
                    }
                }
            }
        };

        SkyWars.getIconMenuController().create(gMap.getName() + "teamselect", invs, event -> {
            Player player = event.getPlayer();

            String name = event.getName();
            if (name.equalsIgnoreCase(SkyWars.getNMS().getItemName(SkyWars.getItemsManager().getItem("exitMenuItem")))) {
                if (!SkyWars.getIconMenuController().hasViewers("jointeammenu")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            SkyWars.getIconMenuController().getMenu("jointeammenu").update();
                        }
                    }.runTaskLater(SkyWars.get(), 5);
                }
                SkyWars.getIconMenuController().show(player, "jointeammenu");
                return;
            }

            if (gMap.getMatchState() != MatchState.WAITINGSTART) {
                Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getErrorSound(), 1, 1);
                if (!SkyWars.getIconMenuController().hasViewers("jointeammenu")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            SkyWars.getIconMenuController().getMenu("jointeammenu").update();
                        }
                    }.runTaskLater(SkyWars.get(), 5);
                }
                SkyWars.getIconMenuController().show(player, "jointeammenu");
                return;
            }

            TeamCard tCard = gMap.getTeamCardFromName(name);

            if (tCard == null) {
                return;
            }

            if (player.hasPermission("sw.join")) {
                boolean joined;
                Party party = Party.getParty(player);
                if (party != null) {
                    if (party.getLeader().equals(player.getUniqueId())) {
                        if (gMap.getMatchState() == MatchState.WAITINGSTART && gMap.canAddParty(party)) {
                            player.closeInventory();
                            joined = gMap.addPlayers(tCard, party);
                            if (!joined) {
                                player.sendMessage(new Messaging.MessageFormatter().format("error.could-not-join2"));
                            }
                        }
                    } else {
                        player.closeInventory();
                        player.sendMessage(new Messaging.MessageFormatter().format("party.onlyleader"));
                    }
                } else {
                    if (gMap.getMatchState() == MatchState.WAITINGSTART && gMap.canAddPlayer()) {
                        player.closeInventory();
                        joined = gMap.addPlayers(tCard, player);
                        if (!joined) {
                            player.sendMessage(new Messaging.MessageFormatter().format("error.could-not-join2"));
                        }
                    }
                }
            }
        });
        SkyWars.getIconMenuController().getMenu(gMap.getName() + "teamselect").setUpdate(update);
    }

}
