package com.nymoout.skywars.game;

import com.nymoout.skywars.managers.MatchManager;
import com.nymoout.skywars.SkyWars;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.Queue;

public class GameQueue {

    private Queue<PlayerCard> queue = new LinkedList<>();
    private GameMap map;
    private boolean running = false;

    GameQueue(GameMap g) {
        map = g;
    }

    public void add(PlayerCard pCard) {
        queue.add(pCard);
        if (!running) {
            sendToGame();
        }
    }

    private void sendToGame() {
        if (!queue.isEmpty()) {
            running = true;
            if (SkyWars.get().isEnabled()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        MatchManager.get().teleportToArena(map, queue.poll());
                        sendToGame();
                    }
                }.runTaskLater(SkyWars.get(), 2L);
            }
        } else {
            running = false;
        }
    }
}
