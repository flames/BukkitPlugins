package com.elmakers.mine.bukkit.plugins.spells;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.elmakers.mine.bukkit.utilities.MovementListener;
import com.elmakers.mine.bukkit.utilities.PluginProperties;

public class GillsSpell extends Spell implements MovementListener
{
	class PlayerGills
	{
		public long lastHealTick;
		public long lastMoveTick;
		public int timeRemaining;
		
		public PlayerGills(int duration)
		{
			lastMoveTick = System.currentTimeMillis();
			lastHealTick = lastMoveTick;
			timeRemaining = duration;
		}
		
		public void move()
		{
			
			long thisMoveTick = System.currentTimeMillis();
			int timeDelta = (int)(thisMoveTick - lastMoveTick);
			lastMoveTick = thisMoveTick;
			timeRemaining -= timeDelta;
		}
		
		public void heal()
		{
			lastHealTick = System.currentTimeMillis();
		}
		
		public boolean isTimeToHeal(int frequency)
		{
			int healDelta = (int)(lastMoveTick - lastHealTick);
			return healDelta > frequency;
		}
		
		public boolean isTimeToRecede()
		{
			return timeRemaining < 0;
		}
	}
	
	private int gillDuration = 60;
	private int healFrequency = 5000;
	private int healAmount = 1;
	private HashMap<String, PlayerGills> gillPlayers = new HashMap<String, PlayerGills>();
	
	@Override
	public boolean onCast(String[] parameters)
	{
		PlayerGills hasGills = gillPlayers.get(player.getName());
		
		if (hasGills != null)
		{
			sendMessage(player, "Your gills recede");
			gillPlayers.remove(player.getName());
		}
		else
		{
			sendMessage(player, "You grow gills!");
			gillPlayers.put(player.getName(), new PlayerGills(gillDuration * 1000));
		}
		checkListener();
		return true;
	}
	
	protected void checkListener()
	{
		if (gillPlayers.size() == 0)
		{
			plugin.stopListeningTo(this);
		}
		else
		{
			plugin.listenTo(this);
		}
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		PlayerGills gills = gillPlayers.get(player.getName());
		if (gills != null)
		{
			gills.move();
			if (gills.isTimeToRecede())
			{
				sendMessage(player, "Your gills recede!");
				gillPlayers.remove(player.getName());
				checkListener();
			}
			else
			{
			if (gills.isTimeToHeal(healFrequency))
			{
					gills.heal();
					Block playerBlock = getPlayerBlock();
					playerBlock = playerBlock.getFace(BlockFace.UP);
					if (playerBlock.getType() == Material.WATER || playerBlock.getType() == Material.STATIONARY_WATER)
					{
						int health = player.getHealth();
						if (health < 20) 
						{
							health = health + healAmount;
						}
						player.setHealth(health);
					}
				}
			}
		}
	}

	@Override
	public String getName()
	{
		return "gills";
	}

	@Override
	public String getCategory()
	{
		return "help";
	}

	@Override
	public String getDescription()
	{
		return "Restores health while moving underwater";
	}
	
	@Override
	public void onLoad(PluginProperties properties)
	{
		gillDuration = properties.getInteger("spells-gills-duration", gillDuration);
		healFrequency = properties.getInteger("spells-gills-heal-frequency", healFrequency);
		healAmount = properties.getInteger("spells-gills-heal-amount", healAmount);
	}

}