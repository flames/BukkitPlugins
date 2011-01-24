package com.elmakers.mine.bukkit.plugins.spells.builtin;

import org.bukkit.Material;

import com.elmakers.mine.bukkit.plugins.spells.Spell;

public class TimeSpell extends Spell {

	@Override
	public boolean onCast(String[] parameters) 
	{
		long targetTime = 0;
		String timeDescription = "day";
		
		if (parameters.length > 0)
		{
			String param = parameters[0];
			if (param.equalsIgnoreCase("night"))
			{
				targetTime = 13000;
				timeDescription = "night";
			}
			else
			{
				try 
				{
					targetTime = Long.parseLong(param);
					timeDescription = "raw: " + targetTime;
				} 
				catch (NumberFormatException ex) 
				{
					targetTime = 0;
				}
			}
		}
		
		setRelativeTime(targetTime);	
		castMessage(player, "Changed time to " + timeDescription);
		
		return true;
	}
	
	@Override
	public String getName() 
	{
		return "time";
	}

	@Override
	public String getCategory() 
	{
		return "help";
	}

	@Override
	public String getDescription() 
	{
		return "Changes the time of day";
	}

	@Override
	public Material getMaterial()
	{
		return Material.FLINT;
	}

}