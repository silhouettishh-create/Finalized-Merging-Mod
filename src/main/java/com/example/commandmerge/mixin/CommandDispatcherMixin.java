package com.example.commandmerge.mixin;

import com.example.commandmerge.CommandChainProcessor;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.ParseResults;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandDispatcher.class)
public class CommandDispatcherMixin {

    @Inject(method = "execute(Lcom/mojang/brigadier/context/ParseResults;)I", at = @At("HEAD"), cancellable = true)
    private void commandmerge$onExecute(ParseResults<?> parseResults, CallbackInfoReturnable<Integer> cir) {
        Object sourceObj = parseResults.getContext().getSource();
        if (!(sourceObj instanceof CommandSourceStack source)) {
            return;
        }

        String command = parseResults.getReader().getString();

        if (!CommandChainProcessor.needsProcessing(command)) {
            return;
        }

        CommandChainProcessor.process(source.getServer().getCommands(), source, command);
        cir.setReturnValue(1);
    }
}
