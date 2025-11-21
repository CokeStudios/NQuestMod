package cn.zbx1425.nquestmod.mixin;

import cn.zbx1425.nquestmod.interop.GenerationStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.RelativeMovement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDFF)V", at = @At("HEAD"))
    void onTeleportTo1(ServerLevel serverLevel, double d, double e, double f, float g, float h, CallbackInfo ci) {
        GenerationStatus.getClientState((ServerPlayer)(Object)this).hasWarped().set();
    }

    @Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FF)Z", at = @At("HEAD"))
    void onTeleportTo2(ServerLevel serverLevel, double d, double e, double f, Set<RelativeMovement> set, float g, float h, CallbackInfoReturnable<Boolean> cir) {
        GenerationStatus.getClientState((ServerPlayer)(Object)this).hasWarped().set();
    }
}
