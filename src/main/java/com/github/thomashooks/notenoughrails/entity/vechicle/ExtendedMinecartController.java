/*
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.thomashooks.notenoughrails.entity.vechicle;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.DefaultMinecartController;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ExtendedMinecartController extends DefaultMinecartController {
    public ExtendedMinecartController(AbstractMinecartEntity minecart) {
        super(minecart);
    }

    /**
     * This is called once per tick on the server side by the minecart when it is on a rail
     * @param world - The current server world
     */
    @Override
    public void moveOnRail(ServerWorld world) {
        //This is mostly just a copy of DefaultMinecartController
        BlockPos blockPos = this.minecart.getRailOrMinecartPos();
        BlockState blockState = this.getWorld().getBlockState(blockPos);
        AbstractRailBlock railBlock = (AbstractRailBlock) blockState.getBlock();

        this.minecart.onLanding();
        double d = this.minecart.getX();
        double e = this.minecart.getY();
        double f = this.minecart.getZ();
        Vec3d vec3d = this.snapPositionToRail(d, e, f);
        e = blockPos.getY();

        double g = 0.0078125;
        if (this.minecart.isTouchingWater()) {
            g *= 0.2;
        }

        Vec3d velocity = this.getVelocity();
        RailShape railShape = ((AbstractRailBlock)blockState.getBlock()).notEnoughRails$getRailDirection(blockState, world ,blockPos, minecart);
        switch (railShape) {
            case ASCENDING_EAST:
                this.setVelocity(velocity.add(-g, 0.0, 0.0));
                e++;
                break;
            case ASCENDING_WEST:
                this.setVelocity(velocity.add(g, 0.0, 0.0));
                e++;
                break;
            case ASCENDING_NORTH:
                this.setVelocity(velocity.add(0.0, 0.0, g));
                e++;
                break;
            case ASCENDING_SOUTH:
                this.setVelocity(velocity.add(0.0, 0.0, -g));
                e++;
            default:
                break;
        }

        velocity = this.getVelocity();
        Pair<Vec3i, Vec3i> pair = AbstractMinecartEntity.getAdjacentRailPositionsByShape(railShape);
        Vec3i vec3i = pair.getFirst();
        Vec3i vec3i2 = pair.getSecond();
        double h = vec3i2.getX() - vec3i.getX();
        double i = vec3i2.getZ() - vec3i.getZ();
        double j = Math.sqrt(h * h + i * i);
        double k = velocity.x * h + velocity.z * i;
        if (k < 0.0) {
            h = -h;
            i = -i;
        }

        double l = Math.min(2.0, velocity.horizontalLength());
        velocity = new Vec3d(l * h / j, velocity.y, l * i / j);
        this.setVelocity(velocity);

        Entity entity = this.minecart.getFirstPassenger();
        Vec3d vec3d3;
        if (this.minecart.getFirstPassenger() instanceof ServerPlayerEntity serverPlayerEntity) {
            vec3d3 = serverPlayerEntity.getInputVelocityForMinecart();
        } else {
            vec3d3 = Vec3d.ZERO;
        }

        if (entity instanceof PlayerEntity && vec3d3.lengthSquared() > 0.0) {
            Vec3d vec3d4 = vec3d3.normalize();
            double m = this.getVelocity().horizontalLengthSquared();
            if (vec3d4.lengthSquared() > 0.0 && m < 0.01) {
                this.setVelocity(this.getVelocity().add(vec3d3.x * 0.001, 0.0, vec3d3.z * 0.001));
            }
        }

        double n = blockPos.getX() + 0.5 + vec3i.getX() * 0.5;
        double o = blockPos.getZ() + 0.5 + vec3i.getZ() * 0.5;
        double p = blockPos.getX() + 0.5 + vec3i2.getX() * 0.5;
        double q = blockPos.getZ() + 0.5 + vec3i2.getZ() * 0.5;
        h = p - n;
        i = q - o;
        double r;
        if (h == 0.0) {
            r = f - blockPos.getZ();
        } else if (i == 0.0) {
            r = d - blockPos.getX();
        } else {
            double s = d - n;
            double t = f - o;
            r = (s * h + t * i) * 2.0;
        }

        d = n + h * r;
        f = o + i * r;
        this.setPos(d, e, f);
        double passengerFactor = this.minecart.hasPassengers() ? 0.75 : 1.0;
        double maxSpeed = this.getMaxSpeed(world);
        velocity = this.getVelocity();
        this.minecart.move(MovementType.SELF, new Vec3d(MathHelper.clamp(passengerFactor * velocity.x, -maxSpeed, maxSpeed), 0.0, MathHelper.clamp(passengerFactor * velocity.z, -maxSpeed, maxSpeed)));
        if (vec3i.getY() != 0
                && MathHelper.floor(this.minecart.getX()) - blockPos.getX() == vec3i.getX()
                && MathHelper.floor(this.minecart.getZ()) - blockPos.getZ() == vec3i.getZ()) {
            this.setPos(this.minecart.getX(), this.minecart.getY() + vec3i.getY(), this.minecart.getZ());
        } else if (vec3i2.getY() != 0
                && MathHelper.floor(this.minecart.getX()) - blockPos.getX() == vec3i2.getX()
                && MathHelper.floor(this.minecart.getZ()) - blockPos.getZ() == vec3i2.getZ()) {
            this.setPos(this.minecart.getX(), this.minecart.getY() + vec3i2.getY(), this.minecart.getZ());
        }

        this.setVelocity(this.applySlowdown(this.getVelocity()));
        Vec3d vec3d5 = this.snapPositionToRail(this.minecart.getX(), this.minecart.getY(), this.minecart.getZ());
        if (vec3d5 != null && vec3d != null) {
            double u = (vec3d.y - vec3d5.y) * 0.05;
            Vec3d vec3d6 = this.getVelocity();
            double v = vec3d6.horizontalLength();
            if (v > 0.0) {
                this.setVelocity(vec3d6.multiply((v + u) / v, 1.0, (v + u) / v));
            }

            this.setPos(this.minecart.getX(), vec3d5.y, this.minecart.getZ());
        }

        int w = MathHelper.floor(this.minecart.getX());
        int x = MathHelper.floor(this.minecart.getZ());
        if (w != blockPos.getX() || x != blockPos.getZ()) {
            Vec3d vec3d6 = this.getVelocity();
            double v = vec3d6.horizontalLength();
            this.setVelocity(v * (w - blockPos.getX()), vec3d6.y, v * (x - blockPos.getZ()));
        }

        railBlock.notEnoughRails$onMinecartPass(blockState, world, blockPos, this.minecart);
    }

    @Override
    public @Nullable Vec3d simulateMovement(double x, double y, double z, double movement) {
        return super.simulateMovement(x, y, z, movement);
    }

    @Override
    public @Nullable Vec3d snapPositionToRail(double x, double y, double z) {
        int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        int k = MathHelper.floor(z);
        if (this.getWorld().getBlockState(new BlockPos(i, j - 1, k)).isIn(BlockTags.RAILS)) {
            --j;
        }

        BlockState blockState = this.getWorld().getBlockState(new BlockPos(i, j, k));
        World world = this.getWorld();
        if (AbstractRailBlock.isRail(blockState)) {
            RailShape railShape = ((AbstractRailBlock)blockState.getBlock()).notEnoughRails$getRailDirection(blockState, world, minecart.getRailOrMinecartPos(), minecart);
            Pair<Vec3i, Vec3i> pair = AbstractMinecartEntity.getAdjacentRailPositionsByShape(railShape);
            Vec3i vec3i = pair.getFirst();
            Vec3i vec3i2 = pair.getSecond();
            double d = (double)i + 0.5 + (double)vec3i.getX() * 0.5;
            double e = (double)j + 0.0625 + (double)vec3i.getY() * 0.5;
            double f = (double)k + 0.5 + (double)vec3i.getZ() * 0.5;
            double g = (double)i + 0.5 + (double)vec3i2.getX() * 0.5;
            double h = (double)j + 0.0625 + (double)vec3i2.getY() * 0.5;
            double l = (double)k + 0.5 + (double)vec3i2.getZ() * 0.5;
            double m = g - d;
            double n = (h - e) * 2.0;
            double o = l - f;
            double p;
            if (m == 0.0) {
                p = z - (double)k;
            } else if (o == 0.0) {
                p = x - (double)i;
            } else {
                double q = x - d;
                double r = z - f;
                p = (q * m + r * o) * 2.0;
            }

            x = d + m * p;
            y = e + n * p;
            z = f + o * p;
            if (n < 0.0) {
                ++y;
            } else if (n > 0.0) {
                y += 0.5;
            }

            return new Vec3d(x, y, z);
        } else {
            return null;
        }
    }

    @Override
    public Vec3d limitSpeed(Vec3d velocity) {
        BlockPos blockPos = this.minecart.getRailOrMinecartPos();
        BlockState blockState = this.getWorld().getBlockState(blockPos);
        double railMaxSpeed = 0.4D;
        if (blockState.getBlock() instanceof AbstractRailBlock railBlock) {
            railMaxSpeed = railBlock.notEnoughRails$getMaxSpeed(blockState, blockPos, this.minecart);
        }
        return !Double.isNaN(velocity.x) && !Double.isNaN(velocity.y) && !Double.isNaN(velocity.z) ? new Vec3d(MathHelper.clamp(velocity.x, -railMaxSpeed, railMaxSpeed), velocity.y, MathHelper.clamp(velocity.z, -railMaxSpeed, railMaxSpeed)) : Vec3d.ZERO;
    }

    @Override
    public double getMaxSpeed(ServerWorld world) {
        BlockPos pos = this.minecart.getRailOrMinecartPos();
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof AbstractRailBlock railBlock) {
            return railBlock.notEnoughRails$getMaxSpeed(state, pos, this.minecart);
        }
        return super.getMaxSpeed(world);
    }

    protected Vec3d applySlowdown(@NonNull Vec3d velocityIn) {
        double speedRetention = this.getSpeedRetention();
        Vec3d velocity = velocityIn.multiply(speedRetention, 0.0D, speedRetention);
        if (this.minecart.isTouchingWater()) {
            velocity = velocity.multiply(0.95D);
        }
        return velocity;
    }
}
