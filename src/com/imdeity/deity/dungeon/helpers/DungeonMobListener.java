package com.imdeity.deity.dungeon.helpers;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.imdeity.deity.dungeon.DeityDungeon;
import com.imdeity.deityapi.Deity;

public class DungeonMobListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.isCancelled()) {
			return;
		}
		if (event.getEntity().isDead()) {
			event.setCancelled(true);
			return;
		}
		if ((event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) || (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE)) {
			Entity defender = event.getEntity();
			Entity attacker = ((EntityDamageByEntityEvent) event).getDamager();
			boolean fireArrow = false;
			if ((attacker instanceof Arrow)) {
				// if its a skeleton
				Entity newattacker = ((Arrow) attacker).getShooter();
				if ((newattacker instanceof Skeleton)) {
					attacker = newattacker;
					fireArrow = true;
				}
			}
			if (defender instanceof Player) {
				if (attacker instanceof Blaze || attacker instanceof CaveSpider || attacker instanceof Creeper || attacker instanceof Enderman || attacker instanceof EnderDragon || attacker instanceof Ghast || attacker instanceof MagmaCube || attacker instanceof PigZombie || attacker instanceof Silverfish || attacker instanceof Skeleton || attacker instanceof Spider || attacker instanceof Wolf || attacker instanceof Zombie) {
					// mob fighting player
					int damage = 0;
					int fireArrowDamage = 0;
					for (String s : Deity.sec.getRegionsAtLocation(attacker.getLocation())) {
						// used to get highest damage rate
						if (DeityDungeon.settings.getRegionMobDamageDealt(s, DungeonMobs.getMobName(attacker)) > damage) {
							// config setting found for region
							damage = DeityDungeon.settings.getRegionMobDamageDealt(s, DungeonMobs.getMobName(attacker));
						} else if (DeityDungeon.settings.getRegionMobDamageDealt(s, DungeonMobs.getMobName(attacker)) == -1) {
							// -1 = insta death
							damage = -1;
							break;
						}
						if (DeityDungeon.settings.getRegionSkeletonFire(s) > fireArrowDamage) {
							fireArrowDamage = DeityDungeon.settings.getRegionSkeletonFire(s);
						}
					}
					if (fireArrow) {
						if (fireArrowDamage > 0) {
							defender.setFireTicks(fireArrowDamage * 20);
						}
					}
					event.setDamage((damage < 0 ? 0 : damage));
					return;
				}
			} else if (attacker instanceof Player) {
				if (defender instanceof Blaze || defender instanceof CaveSpider || defender instanceof Creeper || defender instanceof Enderman || defender instanceof EnderDragon || defender instanceof Ghast || defender instanceof MagmaCube || defender instanceof PigZombie || defender instanceof Silverfish || defender instanceof Skeleton || defender instanceof Spider || defender instanceof Wolf || defender instanceof Zombie) {
					// player fighting mob
					int damage = 0;
					for (String s : Deity.sec.getRegionsAtLocation(attacker.getLocation())) {
						// used to get highest damage rate
						if (DeityDungeon.settings.getRegionMobDamageReceived(s, DungeonMobs.getMobName(defender)) > damage) {
							// config setting found for region
							damage = DeityDungeon.settings.getRegionMobDamageReceived(s, DungeonMobs.getMobName(defender));
						} else if (DeityDungeon.settings.getRegionMobDamageReceived(s, DungeonMobs.getMobName(defender)) == -1) {
							// -1 = set to 2 health so next punch kills not sure
							// why
							// it wont kill them but o well
							damage = ((LivingEntity) defender).getMaxHealth();
							break;
						}
					}
					event.setDamage((damage < 0 ? 0 : damage));
					return;
				}
			}
		}
	}
}
