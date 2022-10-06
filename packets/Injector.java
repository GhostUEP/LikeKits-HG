package me.ghost.hg.packets;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Map;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;

import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityTypes;
import net.minecraft.server.v1_7_R4.EnumProtocol;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.util.com.google.common.collect.BiMap;
import net.minecraft.util.gnu.trove.map.TObjectIntMap;

public class Injector {
	public static void inject() {
		try {
			addPacket(EnumProtocol.PLAY, true, 69, PacketPlayOutTitle.class);
			addPacket(EnumProtocol.PLAY, true, 71, PacketPlayOutPlayerListHeaderFooter.class);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void addPacket(EnumProtocol protocol, boolean clientbound, int id, Class<? extends Packet> packet)
			throws NoSuchFieldException, IllegalAccessException {
		Field packets;
		if (!clientbound) {
			packets = EnumProtocol.class.getDeclaredField("h");
		} else {
			packets = EnumProtocol.class.getDeclaredField("i");
		}
		packets.setAccessible(true);
		BiMap<Integer, Class<? extends Packet>> pMap = (BiMap) packets.get(protocol);
		pMap.put(Integer.valueOf(id), packet);
		Field map = EnumProtocol.class.getDeclaredField("f");
		map.setAccessible(true);
		Map<Class<? extends Packet>, EnumProtocol> protocolMap = (Map) map.get(null);
		protocolMap.put(packet, protocol);
	}

	@SuppressWarnings("unchecked")
	public static EntityType addEntity(String name, int id, Class<? extends CraftLivingEntity> entityClass) {
		EntityType entityType = DynamicEnumType.addEnum(EntityType.class, name,
				new Class[] { String.class, entityClass.getClass(), Integer.TYPE },
				new Object[] { name, entityClass.getClass(), id });
		try {
			Field field = EntityType.class.getDeclaredField("NAME_MAP");
			field.setAccessible(true);
			Object object = field.get(null);
			Map<String, EntityType> NAME_MAP = (Map<String, EntityType>) object;
			NAME_MAP.put(name, entityType);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace(System.out);
		}
		try {
			Field field = EntityType.class.getDeclaredField("ID_MAP");
			field.setAccessible(true);
			Object object = field.get(null);
			Map<Short, EntityType> ID_MAP = (Map<Short, EntityType>) object;
			ID_MAP.put((short) id, entityType);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace(System.out);
		}
		return entityType;
	}

	@SuppressWarnings("unchecked")
	public static void registerEntity(Class<? extends Entity> entityClass, String name, int id) {
		try {
			((Map<String, Class<? extends Entity>>) setAccessible(Field.class, EntityTypes.class.getDeclaredField("c"),
					true).get(null)).put(name, entityClass);
			((Map<Class<? extends Entity>, String>) setAccessible(Field.class, EntityTypes.class.getDeclaredField("d"),
					true).get(null)).put(entityClass, name);
			((Map<Integer, Class<? extends Entity>>) setAccessible(Field.class, EntityTypes.class.getDeclaredField("e"),
					true).get(null)).put(id, entityClass);
			((Map<Class<? extends Entity>, Integer>) setAccessible(Field.class, EntityTypes.class.getDeclaredField("f"),
					true).get(null)).put(entityClass, id);
			((Map<String, Integer>) setAccessible(Field.class, EntityTypes.class.getDeclaredField("g"), true).get(null))
					.put(name, id);
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
			e.printStackTrace(System.out);
		}
	}

	@SuppressWarnings("unchecked")
	public static void registerDataWatcherType(Class<?> type, int id) {
		try {
			Field classToIdField = DataWatcher.class.getDeclaredField("classToId");
			classToIdField.setAccessible(true);
			((TObjectIntMap<Class<?>>) classToIdField.get(null)).put(type, id);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace(System.out);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends AccessibleObject> T setAccessible(Class<T> objectType, AccessibleObject object,
			boolean accessible) {
		object.setAccessible(accessible);
		return (T) object;
	}
}
