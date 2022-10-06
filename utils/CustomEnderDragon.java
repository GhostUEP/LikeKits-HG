package me.ghost.hg.utils;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_7_R4.EntityComplexPart;
import net.minecraft.server.v1_7_R4.EntityEnderDragon;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.MathHelper;

public class CustomEnderDragon extends EntityEnderDragon {

	public CustomEnderDragon(org.bukkit.World world) {
		this(((CraftWorld) world).getHandle());
		width = .0f;
		length = .0f;
	}

	public CustomEnderDragon(net.minecraft.server.v1_7_R4.World world) {
		super(world);
		width = .0f;
		length = .0f;
	}

	public int bo = -1;
	public double[][] bn = new double[64][3];
	private EntityComplexPart[] children;
	private EntityComplexPart head;
	private EntityComplexPart body;
	private EntityComplexPart tail1;
	private EntityComplexPart tail2;
	private EntityComplexPart tail3;
	private EntityComplexPart wing1;
	private EntityComplexPart wing2;
	private float bx;
	private float by;

	@Override
	public void e() {
		float f;
		float f1;

		if (this.passenger != null && (this.passenger instanceof EntityHuman)) {
			EntityHuman human = (EntityHuman) this.passenger;
			if (!human.getBukkitEntity().isDead()) {
				float forw = ((EntityLiving) this.passenger).be;
				float side = ((EntityLiving) this.passenger).bd;

				Vector v = new Vector();
				Location l = new Location(this.world.getWorld(), this.locX, this.locY, this.locZ);

				if (side < 0.0F) {
					l.setYaw(this.passenger.yaw - 90);
					v.add(l.getDirection().normalize().multiply(-0.5));
				} else if (side > 0.0F) {
					l.setYaw(this.passenger.yaw + 90);
					v.add(l.getDirection().normalize().multiply(-0.5));
				}

				if (forw < 0.0F) {
					l.setYaw(this.passenger.yaw);
					v.add(l.getDirection().normalize().multiply(0.5));
				} else if (forw > 0.0F) {
					l.setYaw(this.passenger.yaw);
					v.add(l.getDirection().normalize().multiply(0.5));
				}

				if (this.passenger.pitch >= 0) {
					l.setPitch(this.passenger.pitch);
					v.add(l.getDirection().normalize().multiply(0.4));
				}
				if (this.passenger.pitch <= 0) {
					l.setPitch(this.passenger.pitch);
					v.add(l.getDirection().normalize().multiply(0.4));
				}

				this.lastYaw = this.yaw = this.passenger.yaw - 180;
				this.pitch = this.passenger.pitch * 0.5F;
				this.b(this.yaw, this.pitch);
				this.aP = this.aN = this.yaw;

				l.add(v.multiply(Math.pow(0.5, 0.5)));
				this.setPos(l.getX(), l.getY(), l.getZ());
				this.updateComplexParts();
				return;
			}
		}

		if (this.world.isStatic) {
			f = MathHelper.cos(this.by * 3.1415927F * 2.0F);
			f1 = MathHelper.cos(this.bx * 3.1415927F * 2.0F);
			if (f1 <= -0.3F && f >= -0.3F) {
				this.world.a(this.locX, this.locY, this.locZ, "mob.enderdragon.wings", 5.0F,
						0.8F + this.random.nextFloat() * 0.3F, false);
			}
		}

		this.bx = this.by;
		float f2;

		if (this.getHealth() <= 0.0F) {
			f = (this.random.nextFloat() - 0.5F) * 8.0F;
			f1 = (this.random.nextFloat() - 0.5F) * 4.0F;
			f2 = (this.random.nextFloat() - 0.5F) * 8.0F;
			this.world.addParticle("largeexplode", this.locX + (double) f, this.locY + 2.0D + (double) f1,
					this.locZ + (double) f2, 0.0D, 0.0D, 0.0D);
		}
	}

	private void setPos(double x, double y, double z) {
		double[] d0 = new double[] { x, y, z };
		double[] d1 = new double[] { this.locX, this.locY, this.locZ };
		for (int i = 0; i < 3; i++) {
			if (this.world.getWorld().getBlockAt((int) x, (int) y, (int) z).getType().isSolid()) {
				d0[i] = d1[i];
			}
		}
		this.setPosition(d0[0], d0[1], d0[2]);
	}

	private void updateComplexParts() {
		if (this.children != null) {
			this.aN = this.yaw;
			this.head.width = this.head.length = .0F;
			this.tail1.width = this.tail1.length = .0F;
			this.tail2.width = this.tail2.length = .0F;
			this.tail3.width = this.tail3.length = .0F;
			this.body.length = .0F;
			this.body.width = .0F;
			this.wing1.length = .0F;
			this.wing1.width = .0F;
			this.wing2.length = .0F;
			this.wing2.width = .0F;
			this.width = .0f;
			this.length = .0f;
			float f1 = (float) (this.b(5, 1.0F)[1] - this.b(10, 1.0F)[1]) * 10.0F / 180.0F * 3.1415927F;
			float f2 = MathHelper.cos(f1);
			float f9 = -MathHelper.sin(f1);
			float f10 = this.yaw * 3.1415927F / 180.0F;
			float f11 = MathHelper.sin(f10);
			float f12 = MathHelper.cos(f10);

			this.body.h();
			this.body.setPositionRotation(this.locX + (double) (f11 * 0.5F), this.locY,
					this.locZ - (double) (f12 * 0.5F), 0.0F, 0.0F);
			this.wing1.h();
			this.wing1.setPositionRotation(this.locX + (double) (f12 * 4.5F), this.locY + 2.0D,
					this.locZ + (double) (f11 * 4.5F), 0.0F, 0.0F);
			this.wing2.h();
			this.wing2.setPositionRotation(this.locX - (double) (f12 * 4.5F), this.locY + 2.0D,
					this.locZ - (double) (f11 * 4.5F), 0.0F, 0.0F);

			/*
			 * if (!this.world.isStatic && this.hurtTicks == 0) { PetGoalAttack
			 * attackGoal = (PetGoalAttack)
			 * this.petGoalSelector.getGoal(PetGoalAttack.class); if (attackGoal
			 * != null && attackGoal.isActive) {
			 * this.launchEntities(this.world.getEntities(this,
			 * this.wing1.boundingBox.grow(4.0D, 2.0D, 4.0D).d(0.0D, -2.0D,
			 * 0.0D))); this.launchEntities(this.world.getEntities(this,
			 * this.wing2.boundingBox.grow(4.0D, 2.0D, 4.0D).d(0.0D, -2.0D,
			 * 0.0D))); this.damageEntities(this.world.getEntities(this,
			 * this.head.boundingBox.grow(1.0D, 1.0D, 1.0D))); } }
			 */

			double[] adouble = this.b(5, 1.0F);
			double[] adouble1 = this.b(0, 1.0F);

			float f3 = MathHelper.sin(this.yaw * 3.1415927F / 180.0F - this.bf * 0.01F);
			float f13 = MathHelper.cos(this.yaw * 3.1415927F / 180.0F - this.bf * 0.01F);

			this.head.h();
			this.head.setPositionRotation(this.locX + (double) (f3 * 5.5F * f2),
					this.locY + (adouble1[1] - adouble[1]) * 1.0D + (double) (f9 * 5.5F),
					this.locZ - (double) (f13 * 5.5F * f2), 0.0F, 0.0F);

			for (int j = 0; j < 3; ++j) {
				EntityComplexPart entitycomplexpart = null;

				if (j == 0) {
					entitycomplexpart = this.tail1;
				}

				if (j == 1) {
					entitycomplexpart = this.tail2;
				}

				if (j == 2) {
					entitycomplexpart = this.tail3;
				}

				double[] adouble2 = this.b(12 + j * 2, 1.0F);
				float f14 = this.yaw * 3.1415927F / 180.0F
						+ (float) MathHelper.g(adouble2[0] - adouble[0]) * 3.1415927F / 180.0F * 1.0F;
				float f15 = MathHelper.sin(f14);
				float f16 = MathHelper.cos(f14);
				float f17 = 1.5F;
				float f18 = (float) (j + 1) * 2.0F;

				entitycomplexpart.h();
				entitycomplexpart.setPositionRotation(this.locX - (double) ((f11 * f17 + f15 * f18) * f2),
						this.locY + (adouble2[1] - adouble[1]) * 1.0D - (double) ((f18 + f17) * f9) + 1.5D,
						this.locZ + (double) ((f12 * f17 + f16 * f18) * f2), 0.0F, 0.0F);
			}
		}
	}

}
