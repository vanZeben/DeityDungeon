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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.imdeity.deity.dungeon.DeityDungeon;
import com.imdeity.deity.dungeon.objects.Dungeon;
import com.imdeity.deity.dungeon.objects.Spawner;
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
		if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
			Entity defender = event.getEntity();
			Entity attacker = ((EntityDamageByEntityEvent) event).getDamager();
			if ((attacker instanceof Arrow)) {
				Entity newattacker = ((Arrow) attacker).getShooter();
				if ((newattacker instanceof Skeleton)) {
					attacker = newattacker;
				}
			}
			if (defender instanceof Player) {
				if (isHostileMob(attacker)) {
					for (String s : Deity.sec.getRegionsAtLocation(attacker.getLocation())) {
						if (DeityDungeon.getDungeon(s) != null) {
							Dungeon dungeon = DeityDungeon.getDungeon(s);
							Spawner spawner = dungeon.getSpawnerFromMobId(attacker.getEntityId());
							if (spawner != null) {
								int damage = spawner.damageDealt;
								event.setDamage((damage < 0 ? 20 : damage));
								break;
							}
						}
					}
				}
			} else if (attacker instanceof Player) {
				if (isHostileMob(attacker)) {
					for (String s : Deity.sec.getRegionsAtLocation(attacker.getLocation())) {
						if (DeityDungeon.getDungeon(s) != null) {
							Dungeon dungeon = DeityDungeon.getDungeon(s);
							Spawner spawner = dungeon.getSpawnerFromMobId(attacker.getEntityId());
							if (spawner != null) {
								int damage = spawner.damageReceived;
								event.setDamage((damage < 0 ? ((LivingEntity) defender).getMaxHealth() : damage));
								break;
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		if (isHostileMob(entity)) {
			for (String s : Deity.sec.getRegionsAtLocation(entity.getLocation())) {
				if (DeityDungeon.getDungeon(s) != null) {
					Dungeon dungeon = DeityDungeon.getDungeon(s);
					Spawner spawner = dungeon.getSpawnerFromMobId(entity.getEntityId());
					if (spawner.isBoss) {
						dungeon.sendMessage(((LivingEntity) entity).getKiller().getName() + " has killed the boss!");
					}
					event.setDroppedExp(spawner.expDropped);
					event.getDrops().clear();
					for (ItemStack i : spawner.drops) {
						event.getDrops().add(i);
					}
					break;
				}
			}
		}
	}

	public boolean isHostileMob(Entity entity) {
		if (entity instanceof Blaze || entity instanceof CaveSpider || entity instanceof Creeper || entity instanceof Enderman || entity instanceof EnderDragon || entity instanceof Ghast || entity instanceof MagmaCube || entity instanceof PigZombie || entity instanceof Silverfish || entity instanceof Skeleton || entity instanceof Spider || entity instanceof Wolf || entity instanceof Zombie) {
			return true;
		}
		return false;
	}
}
