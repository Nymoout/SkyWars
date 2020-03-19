package com.nymoout.skywars.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.nymoout.skywars.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import com.nymoout.skywars.enums.LeaderType;
import com.nymoout.skywars.objects.PlayerStat;
import com.nymoout.skywars.SkyWars;

public class DataStorage {

	private static DataStorage instance;
	
    public static DataStorage get() {
        if (DataStorage.instance == null) {
            DataStorage.instance = new DataStorage();
        }
        return DataStorage.instance;
    }
    
	public void saveStats(final PlayerStat pData) {
		boolean sqlEnabled = SkyWars.get().getConfig().getBoolean("sqldatabase.enabled");
		if (!sqlEnabled) {
			try {
	            File dataDirectory = SkyWars.get().getDataFolder();
	            File playerDataDirectory = new File(dataDirectory, "player_data");

	            if (!playerDataDirectory.exists() && !playerDataDirectory.mkdirs()) {
	            	return;
	            }

	            File playerFile = new File(playerDataDirectory, pData.getId().toString() + ".yml");
	            if (!playerFile.exists()) {
	            	SkyWars.get().getLogger().info("File doesn't exist!");
	            	return;
	            }

	            copyDefaults(playerFile);
	            FileConfiguration fc = YamlConfiguration.loadConfiguration(playerFile);
	            fc.set("uuid", pData.getId());
	            fc.set("wins", pData.getWins());
	            fc.set("losses", pData.getLosses());
	            fc.set("kills", pData.getKills());
	            fc.set("deaths", pData.getDeaths());
	            fc.set("elo", pData.getElo());
	            fc.set("xp", pData.getXp());
	            fc.set("pareffect", pData.getParticleEffect());
	            fc.set("proeffect", pData.getProjectileEffect());
	            fc.set("glasscolor", pData.getGlassColor());
	            fc.set("killsound", pData.getKillSound());
	            fc.set("winsound", pData.getWinSound());
	            fc.set("taunt", pData.getTaunt());
	            fc.save(playerFile);
	            
	        } catch (IOException ioException) {
	            System.out.println("Failed to load faction " + pData.getId() + ": " + ioException.getMessage());
	        }
		} else {
            Database database = SkyWars.getDb();

            if (!database.checkConnection()) {
                return;
            }

            Connection connection = database.getConnection();
            PreparedStatement preparedStatement = null;

            try {
            	 StringBuilder queryBuilder = new StringBuilder();
                 queryBuilder.append("UPDATE `sw_player` SET ");
                 queryBuilder.append("`player_name` = ?, `wins` = ?, `losses` = ?, ");
                 queryBuilder.append("`kills` = ?, `deaths` = ?, `elo` = ?, `xp` = ?, `pareffect` = ?, `proeffect` = ?, `glasscolor` = ?,`killsound` = ?, `winsound` = ?, `taunt` = ? ");
                 queryBuilder.append("WHERE `uuid` = ?;");
                 
                 preparedStatement = connection.prepareStatement(queryBuilder.toString());
                 preparedStatement.setString(1, pData.getPlayerName());
                 preparedStatement.setInt(2, pData.getWins());
                 preparedStatement.setInt(3, pData.getLosses());
                 preparedStatement.setInt(4, pData.getKills());
                 preparedStatement.setInt(5, pData.getDeaths());
                 preparedStatement.setInt(6, pData.getElo());
                 preparedStatement.setInt(7, pData.getXp());
                 preparedStatement.setString(8, pData.getParticleEffect());
                 preparedStatement.setString(9, pData.getProjectileEffect());
                 preparedStatement.setString(10, pData.getGlassColor());
                 preparedStatement.setString(11, pData.getKillSound());
                 preparedStatement.setString(12, pData.getWinSound());
                 preparedStatement.setString(13, pData.getTaunt());
                 preparedStatement.setString(14, pData.getId());
                 preparedStatement.executeUpdate();

            } catch (final SQLException sqlException) {
                sqlException.printStackTrace();

            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (final SQLException ignored) {
                    }
                }
            }
		}
    }

	
	public void loadStats(final PlayerStat pData) {
		new BukkitRunnable() {
			@Override
			public void run() {
				boolean sqlEnabled = SkyWars.get().getConfig().getBoolean("sqldatabase.enabled");
				if (sqlEnabled) {
			                Database database = SkyWars.getDb();

			                if (!database.checkConnection()) {
			                    return;
			                }

			                if (!database.doesPlayerExist(pData.getId())) {
			                    database.createNewPlayer(pData.getId(), pData.getPlayerName());
		                        pData.setWins(0);
		                        pData.setLosts(0);
		                        pData.setKills(0);
		                        pData.setDeaths(0);
		                        pData.setElo(1500);
		                        pData.setXp(0);
		                        pData.setParticleEffect("none");
		                        pData.setProjectileEffect("none");
		                        pData.setGlassColor("none");
		                        pData.setKillSound("none");
		                        pData.setWinSound("none");
		                        pData.setTaunt("none");
			                } else {
			                    Connection connection = database.getConnection();
			                    PreparedStatement preparedStatement = null;
			                    ResultSet resultSet = null;

			                    try {
			                        StringBuilder queryBuilder = new StringBuilder();
			                        queryBuilder.append("SELECT `wins`, `losses`, `kills`, `deaths`, `elo`, `xp`, `pareffect`, `proeffect`, `glasscolor`, `killsound`, `winsound`, `taunt` ");
			                        queryBuilder.append("FROM `sw_player` ");
			                        queryBuilder.append("WHERE `uuid` = ? ");
			                        queryBuilder.append("LIMIT 1;");

			                        preparedStatement = connection.prepareStatement(queryBuilder.toString());
			                        preparedStatement.setString(1, pData.getId());
			                        resultSet = preparedStatement.executeQuery();

			                        if (resultSet != null && resultSet.next()) {
			                            pData.setWins(resultSet.getInt("wins"));
			                            pData.setLosts(resultSet.getInt("losses"));
			                            pData.setKills(resultSet.getInt("kills"));
			                            pData.setDeaths(resultSet.getInt("deaths"));
			                            pData.setElo(resultSet.getInt("elo"));
			                            pData.setXp(resultSet.getInt("xp"));
			                            pData.setParticleEffect(resultSet.getString("pareffect"));
			                            pData.setProjectileEffect(resultSet.getString("proeffect"));
			                            pData.setGlassColor(resultSet.getString("glasscolor"));
			                            pData.setKillSound(resultSet.getString("killsound"));
			                            pData.setWinSound(resultSet.getString("winsound"));
			                            pData.setTaunt(resultSet.getString("taunt"));
			                        }

			                    } catch (final SQLException sqlException) {
			                        sqlException.printStackTrace();

			                    } finally {
			                        if (resultSet != null) {
			                            try {
			                                resultSet.close();
			                            } catch (final SQLException ignored) {
			                            }
			                        }

			                        if (preparedStatement != null) {
			                            try {
			                                preparedStatement.close();
			                            } catch (final SQLException ignored) {
			                            }
			                        }
			                    }
			                }
				} else {
			    			try {
			    	            File dataDirectory = SkyWars.get().getDataFolder();
			    	            File playerDataDirectory = new File(dataDirectory, "player_data");

			    	            if (!playerDataDirectory.exists() && !playerDataDirectory.mkdirs()) {
			    	            }

			    	            File playerFile = new File(playerDataDirectory, pData.getId() + ".yml");
			    	            
			    	            if (!playerFile.exists() && !playerFile.createNewFile()) {
			    	            	SkyWars.get().getLogger().info("Something strange is happening while saving!");
			    	            }

			    	            copyDefaults(playerFile);
			    	            FileConfiguration fc = YamlConfiguration.loadConfiguration(playerFile);
		                        pData.setWins(fc.getInt("wins"));
		                        pData.setLosts(fc.getInt("losses"));
		                        pData.setKills(fc.getInt("kills"));
		                        pData.setDeaths(fc.getInt("deaths"));
		                        pData.setElo(fc.getInt("elo"));
		                        pData.setXp(fc.getInt("xp"));
		                        pData.setParticleEffect(fc.getString("pareffect"));
		                        pData.setProjectileEffect(fc.getString("proeffect"));
		                        pData.setGlassColor(fc.getString("glasscolor"));
		                        pData.setKillSound(fc.getString("killsound"));
		                        pData.setWinSound(fc.getString("winsound"));
		                        pData.setTaunt(fc.getString("taunt"));
			    	        } catch (IOException ioException) {
			    	            System.out.println("Failed to load player " + pData.getId() + ": " + ioException.getMessage());
			    	        }
				}
				pData.setInitialized(true);
			}
		}.runTaskAsynchronously(SkyWars.get());
	}
	
	private void copyDefaults(File playerFile) {
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
		Reader defConfigStream = new InputStreamReader(SkyWars.get().getResource("playerFile.yml"));
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			playerConfig.options().copyDefaults(true);
			playerConfig.setDefaults(defConfig);
			try {
				playerConfig.save(playerFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void removePlayerData(String uuid) {
		boolean sqlEnabled = SkyWars.get().getConfig().getBoolean("sqldatabase.enabled");
		if (!sqlEnabled) {
			try {
	            File dataDirectory = SkyWars.get().getDataFolder();
	            File playerDataDirectory = new File(dataDirectory, "player_data");

	            if (!playerDataDirectory.exists() && !playerDataDirectory.mkdirs()) {
	            }

	            File playerFile = new File(playerDataDirectory, uuid.toString() + ".yml");
	            if (!playerFile.exists() && !playerFile.createNewFile()) {
	            }
	            playerFile.delete();            
	        } catch (IOException ioException) {
	            System.out.println("Failed to load faction " + uuid + ": " + ioException.getMessage());
	        }
		} else {
            Database database = SkyWars.getDb();

            if (!database.checkConnection()) {
                return;
            }

            Connection connection = database.getConnection();
            PreparedStatement preparedStatement = null;

            try {
            	 StringBuilder queryBuilder = new StringBuilder();
                 queryBuilder.append("DELETE FROM `sw_player` ");
                 queryBuilder.append("WHERE `uuid` = ?;");

                 preparedStatement = connection.prepareStatement(queryBuilder.toString());
                 preparedStatement.setString(1, uuid);
                 preparedStatement.executeUpdate();

            } catch (final SQLException sqlException) {
                sqlException.printStackTrace();

            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (final SQLException ignored) {
                    }
                }
            }
		}
	}
	
	public void updateTop(final LeaderType type, final int size) {
		new BukkitRunnable() {

			@Override
			public void run() {
				boolean sqlEnabled = SkyWars.get().getConfig().getBoolean("sqldatabase.enabled");
				if (sqlEnabled) {
		            Database database = SkyWars.getDb();

		            if (!database.checkConnection()) {
		                return;
		            }

		            Connection connection = database.getConnection();
		            PreparedStatement preparedStatement = null;
		            ResultSet resultSet = null;

		            try {
		        		StringBuilder queryBuilder = new StringBuilder();
		                queryBuilder.append("SELECT `uuid`, `wins`, `losses`, `kills`, `deaths`, `elo`, `xp` ");
		                queryBuilder.append("FROM `sw_player` ");
		                queryBuilder.append("GROUP BY `uuid` ");
		                queryBuilder.append("ORDER BY `" + type.toString().toLowerCase() + "` DESC LIMIT " + size + ";");
		                
		                preparedStatement = connection.prepareStatement(queryBuilder.toString());
		                resultSet = preparedStatement.executeQuery();
		                SkyWars.getLB().resetLeader(type);
		                while (resultSet.next()) {
		                	SkyWars.getLB().addLeader(type, resultSet.getString(1), Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString(1))).getName(), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getInt(5), resultSet.getInt(6), resultSet.getInt(7));
		                }
		                
		            } catch (final SQLException sqlException) {
		                sqlException.printStackTrace();

		            }
				} else {
					File dataDirectory = SkyWars.get().getDataFolder();
			        File playerDirectory = new File(dataDirectory, "player_data");

			        if (!playerDirectory.exists()) {
			            if (!playerDirectory.mkdirs())  {
			                return;
			            }
			        }

			        File[] playerFiles = playerDirectory.listFiles();
			        if (playerFiles == null) {
			            return;
			        }

			        SkyWars.getLB().resetLeader(type);
			        for (File playerFile : playerFiles) {
			            if (!playerFile.getName().endsWith(".yml")) {
			                continue;
			            }

			            FileConfiguration fc = YamlConfiguration.loadConfiguration(playerFile);
			            String uuid = playerFile.getName().replace(".yml", "");
		                SkyWars.getLB().addLeader(type, uuid, Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName(), fc.getInt("wins"), fc.getInt("losses"), fc.getInt("kills"), fc.getInt("deaths"), fc.getInt("elo"), fc.getInt("xp"));
			        }
				}
				SkyWars.getLB().finishedLoading(type);
			}
		}.runTaskLaterAsynchronously(SkyWars.get(), 10L);
	}

	public void loadperms(final PlayerStat playerStat) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!SkyWars.get().getConfig().getBoolean("sqldatabase.enabled")) {
					try {
	    	            File dataDirectory = SkyWars.get().getDataFolder();
	    	            File playerDataDirectory = new File(dataDirectory, "player_data");

	    	            if (!playerDataDirectory.exists() && !playerDataDirectory.mkdirs()) {
	    	                System.out.println("Failed to load player " + playerStat.getPlayerName() + ": Could not create player_data directory.");
	    	                return;
	    	            }

	    	            File playerFile = new File(playerDataDirectory, playerStat.getId() + ".yml");
	    	            if (!playerFile.exists() && !playerFile.createNewFile()) {
	    	                System.out.println("Failed to load player " + playerStat.getPlayerName() + ": Could not create player file.");
	    	                return;
	    	            }
	    	            copyDefaults(playerFile);
	    	            FileConfiguration fc = YamlConfiguration.loadConfiguration(playerFile);
	    	            
	    	            List<String> perms = fc.getStringList("permissions");
	    	            for (String perm: perms) {
	    	            	playerStat.addPerm(perm, false);
	    	            }	    	            
					} catch (IOException ioException) {
	    	            System.out.println("Failed to load player " + playerStat.getPlayerName() + ": " + ioException.getMessage());
					}
				} else {
					Database database = SkyWars.getDb();
	                if (!database.checkConnection()) {
	                    return;
	                }
	                Connection connection = database.getConnection();
	                PreparedStatement preparedStatement = null;
	                ResultSet resultSet = null;

	                try {
	                    StringBuilder queryBuilder = new StringBuilder();
	                    queryBuilder.append("SELECT `permissions` ");
	                    queryBuilder.append("FROM `sw_permissions` ");
	                    queryBuilder.append("WHERE `uuid` = ?;");

	                    preparedStatement = connection.prepareStatement(queryBuilder.toString());
	                    preparedStatement.setString(1, playerStat.getId());
	                    resultSet = preparedStatement.executeQuery();
	                    
	                    while (resultSet != null && resultSet.next()) {
	                    	playerStat.addPerm(resultSet.getString("permissions"), false);
	                    }
	                } catch (final SQLException sqlException) {
	                    sqlException.printStackTrace();

	                } finally {
	                    if (resultSet != null) {
	                        try {
	                            resultSet.close();
	                        } catch (final SQLException ignored) {
	                        }
	                    }
	                    if (preparedStatement != null) {
	                        try {
	                            preparedStatement.close();
	                        } catch (final SQLException ignored) {
	                        }
	                    }
	                }
				}
			}
				
		}.runTaskAsynchronously(SkyWars.get());
	}
	
	public void savePerms(final PlayerStat playerStat) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!SkyWars.get().getConfig().getBoolean("sqldatabase.enabled")) {
					try {
						File dataDirectory = SkyWars.get().getDataFolder();
	    	            File playerDataDirectory = new File(dataDirectory, "player_data");

	    	            if (!playerDataDirectory.exists() && !playerDataDirectory.mkdirs()) {
	    	                System.out.println("Failed to load player " + playerStat.getPlayerName() + ": Could not create player_data directory.");
	    	                return;
	    	            }

	    	            File playerFile = new File(playerDataDirectory, playerStat.getId() + ".yml");
	    	            if (!playerFile.exists() && !playerFile.createNewFile()) {
	    	                System.out.println("Failed to load player " + playerStat.getPlayerName() + ": Could not create player file.");
	    	                return;
	    	            }
	    	            copyDefaults(playerFile);
	    	            FileConfiguration fc = YamlConfiguration.loadConfiguration(playerFile);
	    	            
	    	            List<String> perms = new ArrayList<String>();
	    	            for (String perm: playerStat.getPerms().getPermissions().keySet()) {
	    	            	perms.add(perm);
	    	            }
	    	            fc.set("permissions", perms);
	    	            fc.save(playerFile);
	    	            
					} catch (IOException ioException) {
	    	            System.out.println("Failed to load player " + playerStat.getPlayerName() + ": " + ioException.getMessage());
					}    	            
				} else {
					if (playerStat.getPerms().getPermissions().size() > 0) {
						Database database = SkyWars.getDb();
		                if (!database.checkConnection()) {
		                    return;
		                }
						Connection connection = database.getConnection();
						PreparedStatement preparedStatement = null;
			            try {
			            	if (playerStat.getPerms().getPermissions().size() >= 1) {
			                	for (String perm: playerStat.getPerms().getPermissions().keySet()) {
			                		StringBuilder queryBuilder = new StringBuilder();
			                        queryBuilder.append("INSERT INTO `sw_permissions` ");
			                        queryBuilder.append("(`uuid`, `playername`, `permissions`) ");
			                        queryBuilder.append("VALUES (?, ?, ?) ");
			                        queryBuilder.append("ON DUPLICATE KEY UPDATE `uuid`=`uuid`, `playername`=`playername`, `permissions`=`permissions` ");
			                        
			                        preparedStatement = connection.prepareStatement(queryBuilder.toString());
			                        preparedStatement.setString(1, playerStat.getId());
			                        preparedStatement.setString(2, playerStat.getPlayerName());
			                        preparedStatement.setString(3, perm);
			                        preparedStatement.executeUpdate();
			                	}
			            	}
			            } catch (final SQLException sqlException) {
			                sqlException.printStackTrace();
			            } finally {
			                if (preparedStatement != null) {
			                    try {
			                        preparedStatement.close();
			                    } catch (final SQLException ignored) {
			                    }
			                }
			            }
					}
				}
			}
		}.runTaskAsynchronously(SkyWars.get());
	}
	
	
}
