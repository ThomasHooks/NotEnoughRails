package com.github.thomashooks.notenoughrails.mixin;

import com.github.thomashooks.notenoughrails.entity.vechicle.ExtendedMinecartController;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.DefaultMinecartController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractMinecartEntity.class)
public abstract class MixinAbstractMinecartEntity {
    @Redirect(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V",
    at = @At(value = "NEW", target = "(Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;)Lnet/minecraft/entity/vehicle/DefaultMinecartController;"))
    private DefaultMinecartController redirectToCustomDefaultController(AbstractMinecartEntity abstractMinecartEntity) {
        return new ExtendedMinecartController(abstractMinecartEntity);
    }
}
