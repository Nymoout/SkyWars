package com.nymoout.skywars.menus;

import com.google.common.collect.Lists;
import com.nymoout.skywars.SkyWars;
import com.nymoout.skywars.enums.GameType;
import com.nymoout.skywars.enums.MatchState;
import com.nymoout.skywars.game.GameMap;
import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.utilities.Messaging;
import com.nymoout.skywars.utilities.Party;
import com.nymoout.skywars.utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class JoinSingleMenu {

    private static int menuSize = 45;
    private static final String menuName = new Messaging.MessageFormatter().format("menu.joinsinglegame-menu-title");

    public JoinSingleMenu() {
        Inventory menu = Bukkit.createInventory(null, menuSize + 9, menuName);
        ArrayList<Inventory> invs = new ArrayList<>();
        invs.add(menu);

        Runnable update = () -> {
            if ((SkyWars.getIconMenuController().hasViewers("joinsinglemenu") || SkyWars.getIconMenuController().hasViewers("spectatesinglemenu"))) {
                ArrayList<GameMap> games = GameMap.getPlayableArenas(GameType.SINGLE);
                ArrayList<Inventory> invs1 = SkyWars.getIconMenuController().getMenu("joinsinglemenu").getInventories();

                for (Inventory inv : invs1) {
                    for (int i = 0; i < menuSize; i++) {
                        inv.setItem(i, new ItemStack(Material.AIR, 1));
                    }
                }

                for (int iii = 0; iii < games.size(); iii++) {
                    int invent = Math.floorDiv(iii, menuSize);
                    if (invs1.isEmpty() || invs1.size() < invent + 1) {
                        invs1.add(Bukkit.createInventory(null, menuSize + 9, menuName));
                    }

                    GameMap gMap = games.get(iii);

                    List<String> loreList = Lists.newLinkedList();
                    if (gMap.getMatchState() != MatchState.OFFLINE) {
                        if (gMap.getMatchState() == MatchState.WAITINGSTART) {
                            loreList.add((new Messaging.MessageFormatter().format("signs.joinable").toUpperCase()));
                        } else if (gMap.getMatchState().equals(MatchState.PLAYING)) {
                            loreList.add((new Messaging.MessageFormatter().format("signs.playing").toUpperCase()));
                        } else if (gMap.getMatchState().equals(MatchState.ENDING)) {
                            loreList.add((new Messaging.MessageFormatter().format("signs.ending").toUpperCase()));
                        }
                        loreList.add((new Messaging.MessageFormatter().setVariable("playercount", "" + gMap.getAlivePlayers().size()).setVariable("maxplayers", "" + gMap.getMaxPlayers()).format("signs.line4")));
                        for (Player p : gMap.getAllPlayers()) {
                            if (p != null) {
                                if (gMap.getAlivePlayers().contains(p)) {
                                    loreList.add(ChatColor.GREEN + p.getName());
                                } else {
                                    loreList.add(ChatColor.RED + p.getName());
                                }
                            }
                        }

                        double xy = ((double) (gMap.getAlivePlayers().size() / gMap.getMaxPlayers()));

                        ItemStack gameIcon = SkyWars.getNMS().getItemStack(SkyWars.getItemsManager().getItem("blockwaiting"), loreList, ChatColor.translateAlternateColorCodes('&', gMap.getDisplayName()));
                        if (gMap.getMatchState().equals(MatchState.PLAYING)) {
                            gameIcon = SkyWars.getNMS().getItemStack(SkyWars.getItemsManager().getItem("blockplaying"), loreList, ChatColor.translateAlternateColorCodes('&', gMap.getDisplayName()));
                        } else if (gMap.getMatchState().equals(MatchState.ENDING)) {
                            gameIcon = SkyWars.getNMS().getItemStack(SkyWars.getItemsManager().getItem("blockending"), loreList, ChatColor.translateAlternateColorCodes('&', gMap.getDisplayName()));
                        } else if (gMap.getMatchState() == MatchState.WAITINGSTART) {
                            gameIcon = SkyWars.getNMS().getItemStack(SkyWars.getItemsManager().getItem("almostfull"), loreList, ChatColor.translateAlternateColorCodes('&', gMap.getDisplayName()));
                            if (xy < 0.75) {
                                gameIcon = SkyWars.getNMS().getItemStack(SkyWars.getItemsManager().getItem("threefull"), loreList, ChatColor.translateAlternateColorCodes('&', gMap.getDisplayName()));
                            }
                            if (xy < 0.50) {
                                gameIcon = SkyWars.getNMS().getItemStack(SkyWars.getItemsManager().getItem("halffull"), loreList, ChatColor.translateAlternateColorCodes('&', gMap.getDisplayName()));
                            }
                            if (xy < 0.25) {
                                gameIcon = SkyWars.getNMS().getItemStack(SkyWars.getItemsManager().getItem("almostempty"), loreList, ChatColor.translateAlternateColorCodes('&', gMap.getDisplayName()));
                            }
                        }
                        invs1.get(invent).setItem(iii % menuSize, gameIcon);
                    }
                }
                if (SkyWars.getConfigManager().spectateMenuEnabled()) {
                    ArrayList<Inventory> specs = SkyWars.getIconMenuController().getMenu("spectatesinglemenu").getInventories();
                    int i = 0;
                    for (Inventory inv : invs1) {
                        if (specs.get(i) == null) {
                            specs.add(Bukkit.createInventory(null, menuSize, new Messaging.MessageFormatter().format("menu.spectatesinglegame-menu-title")));
                        }
                        specs.get(0).setContents(inv.getContents());
                        i++;
                    }
                }
            }
        };

        SkyWars.getIconMenuController().create("joinsinglemenu", invs, event -> {
            Player player = event.getPlayer();
            GameMap gMap = MatchManager.get().getPlayerMap(player);
            if (gMap != null) {
                return;
            }

            String name = event.getName();
            if (name.equalsIgnoreCase(SkyWars.getNMS().getItemName(SkyWars.getItemsManager().getItem("exitMenuItem")))) {
                player.closeInventory();
                return;
            }
            gMap = GameMap.getMapByDisplayName(ChatColor.stripColor(name));
            if (gMap == null) {
                return;
            }

            if (gMap.getMatchState() != MatchState.WAITINGSTART) {
                Util.get().playSound(player, player.getLocation(), SkyWars.getConfigManager().getErrorSound(), 1, 1);
                return;
            }

            if (player.hasPermission("sw.join")) {
                boolean joined;
                Party party = Party.getParty(player);
                if (party != null) {
                    if (party.getLeader().equals(player.getUniqueId())) {
                        if (gMap.getMatchState() == MatchState.WAITINGSTART && gMap.canAddParty(party)) {
                            player.closeInventory();
                            joined = gMap.addPlayers(null, party);
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
                        joined = gMap.addPlayers(null, player);
                        if (!joined) {
                            player.sendMessage(new Messaging.MessageFormatter().format("error.could-not-join2"));
                        }
                    }
                }
            }
        });
        SkyWars.getIconMenuController().getMenu("joinsinglemenu").setUpdate(update);
    }
}
