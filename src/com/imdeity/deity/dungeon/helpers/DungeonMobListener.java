package com.imdeity.deity.dungeon.helpers;

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
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.imdeity.deity.dungeon.DeityDungeon;
import com.imdeity.deity.dungeon.objects.Dungeon;
import com.imdeity.deityapi.Deity;

public class DungeonMobListener implements Listener {

	/*
	 * @EventHandler(priority = EventPriority.NORMAL) public void
	 * onEntityDamage(EntityDamageEvent event) { if (event.isCancelled()) {
	 * return; } if (event.getEntity().isDead()) { event.setCancelled(true);
	 * return; } if ((event.getCause() ==
	 * EntityDamageEvent.DamageCause.ENTITY_ATTACK) || (event.getCause() ==
	 * EntityDamageEvent.DamageCause.PROJECTILE)) { Entity defender =
	 * event.getEntity(); Entity attacker = ((EntityDamageByEntityEvent)
	 * event).getDamager(); boolean fireArrow = false; if ((attacker instanceof
	 * Arrow)) { // if its a skeleton Entity newattacker = ((Arrow)
	 * attacker).getShooter(); if ((newattacker instanceof Skeleton)) { attacker
	 * = newattacker; fireArrow = true; } } if (defender instanceof Player) { if
	 * (checkMobs(attacker)) { // mob fighting player int damage = 1; int
	 * fireArrowDamage = 0; for (String s :
	 * Deity.sec.getRegionsAtLocation(attacker.getLocation())) { // used to get
	 * highest damage rate if (DeityDungeon.settings.getRegionMobDamageDealt(s,
	 * DungeonMobs.getMobName(attacker)) > damage) { // config setting found for
	 * region damage = DeityDungeon.settings.getRegionMobDamageDealt(s,
	 * DungeonMobs.getMobName(attacker)); } else if
	 * (DeityDungeon.settings.getRegionMobDamageDealt(s,
	 * DungeonMobs.getMobName(attacker)) == -1) { // -1 = insta death damage =
	 * -1; break; } if (DeityDungeon.settings.getRegionSkeletonFire(s) >
	 * fireArrowDamage) { fireArrowDamage =
	 * DeityDungeon.settings.getRegionSkeletonFire(s); } } if (fireArrow) { if
	 * (fireArrowDamage > 0) { defender.setFireTicks(fireArrowDamage * 20); } }
	 * event.setDamage((damage < 0 ? 0 : damage)); return; } } else if (attacker
	 * instanceof Player) { if (checkMobs(defender)) { // player fighting mob
	 * int damage = 1; for (String s :
	 * Deity.sec.getRegionsAtLocation(attacker.getLocation())) { // // used to
	 * get highest damage rate if
	 * (DeityDungeon.settings.getRegionMobDamageReceived(s,
	 * DungeonMobs.getMobName(defender)) > damage) { // config setting found for
	 * region damage = DeityDungeon.settings.getRegionMobDamageReceived(s,
	 * DungeonMobs.getMobName(defender)); } else if
	 * (DeityDungeon.settings.getRegionMobDamageReceived(s,
	 * DungeonMobs.getMobName(defender)) == -1) { // -1 = set to 2 health so
	 * next punch kills not sure // why // it wont kill them but o well damage =
	 * ((LivingEntity) defender).getMaxHealth(); break; } }
	 * event.setDamage((damage < 0 ? 0 : damage)); return; } } } }
	 */

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		if (isHostileMob(entity)) {
			for (String s : Deity.sec.getRegionsAtLocation(entity.getLocation())) {
				if (DeityDungeon.getDungeon(s) != null) {
					Dungeon dungeon = DeityDungeon.getDungeon(s);
					if (dungeon.isBoss(entity.getEntityId())) {
						dungeon.sendMessage(((LivingEntity) entity).getKiller().getName() + " has killed the boss!");
					}
				}
			}
			System.out.println("Id: " + entity.getEntityId());
		}
	}

	public boolean isHostileMob(Entity entity) {
		if (entity instanceof Blaze || entity instanceof CaveSpider || entity instanceof Creeper || entity instanceof Enderman || entity instanceof EnderDragon || entity instanceof Ghast || entity instanceof MagmaCube || entity instanceof PigZombie || entity instanceof Silverfish || entity instanceof Skeleton || entity instanceof Spider || entity instanceof Wolf || entity instanceof Zombie) {
			return true;
		}
		return false;

	}
}
