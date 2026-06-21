package com.example.commandmerge.mixin;

import com.example.commandmerge.CommandChainProcessor;
import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Commands.class)
public class CommandsMixin {

	@Inject(method = "performPrefixedCommand", at = @At("HEAD"), cancellable = true)
	private void commandmerge$onPerformPrefixedCommand(CommandSourceStack source, String command, CallbackInfo ci) {
		String stripped = command.startsWith("/") ? command.substring(1) : command;
		if (!CommandChainProcessor.needsProcessing(stripped)) return;
		CommandChainProcessor.process((Commands) (Object) this, source, stripped);
		ci.cancel();
	}

	@Inject(method = "performCommand(Lcom/mojang/brigadier/ParseResults;Ljava/lang/String;)I", at = @At("HEAD"), cancellable = true)
	private void commandmerge$onPerformCommand(ParseResults<CommandSourceStack> parseResults, String command, CallbackInfoReturnable<Integer> cir) {
		CommandSourceStack source = parseResults.getContext().getSource();
		if (!CommandChainProcessor.needsProcessing(command)) return;
		CommandChainProcessor.process((Commands) (Object) this, source, command);
		cir.setReturnValue(1);
	}
}
