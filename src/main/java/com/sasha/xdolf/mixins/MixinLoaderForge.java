package com.sasha.xdolf.mixins;

import com.sasha.xdolf.XdolfMod;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Created by Sasha on 06/08/2018 at 1:34 PM
 **/
public class MixinLoaderForge implements IFMLLoadingPlugin {
    protected static boolean isObfuscatedEnvironment;
    @Override
    public String[] getASMTransformerClass() {
        XdolfMod.logger.info("Xdolf is initialising mixins.");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.kami.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        XdolfMod.logger.info("Obfuscation -> " + MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        isObfuscatedEnvironment = (boolean) (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
