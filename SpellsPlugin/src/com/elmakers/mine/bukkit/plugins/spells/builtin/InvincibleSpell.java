package com.elmakers.mine.bukkit.plugins.spells.builtin;

import java.util.HashMap;

import org.bukkit.Material;

import com.elmakers.mine.bukkit.plugins.spells.Spell;

public class InvincibleSpell extends Spell 
{
	private final HashMap<String, Boolean> invinciblePlayers = new HashMap<String, Boolean>();

	@Override
	public boolean onCast(String[] parameters) 
	{
		invinciblePlayers.put(player.getName(), true);
		return false;
	}

	@Override
	public String getName() 
	{
		return "invincible";
	}

	@Override
	public String getCategory() 
	{
		return "wip";
	}

	@Override
	public String getDescription() 
	{
		return "Makes you impervious to damage";
	}

	@Override
	public Material getMaterial()
	{
		return Material.GOLDEN_APPLE;
	}

}
