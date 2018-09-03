package com.sasha.adorufu.mixins.client;

import com.sasha.adorufu.AdorufuMod;
import com.sasha.adorufu.events.PlayerAdorufuCommandEvent;
import com.sasha.adorufu.events.ServerPlayerInventoryCloseEvent;
import com.sasha.adorufu.module.ModuleManager;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by Sasha on 08/08/2018 at 7:53 AM
 **/
@Mixin(value = EntityPlayerSP.class, priority = 999)
public abstract class MixinEntityPlayerSP extends MixinEntityPlayer {

    @Shadow public abstract void closeScreenAndDropStack();

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String message, CallbackInfo info) {
        if (message.startsWith(AdorufuMod.COMMAND_PROCESSOR.getCommandPrefix())) {
            AdorufuMod.EVENT_MANAGER.invokeEvent(new PlayerAdorufuCommandEvent(message));
            info.cancel();
        }
    }

    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    public void onUpdate(CallbackInfo info) {
        ModuleManager.tickModules();
    }

    @Inject(method = "closeScreen", at = @At(value = "HEAD"), cancellable = true)
    public void closeScreen(CallbackInfo info) {
        ServerPlayerInventoryCloseEvent event = new ServerPlayerInventoryCloseEvent(this.openContainer);
        AdorufuMod.EVENT_MANAGER.invokeEvent(event);
        if (event.isCancelled()) {
            this.closeScreenAndDropStack();
            info.cancel();
        }
    }
}
