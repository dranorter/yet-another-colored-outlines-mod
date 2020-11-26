package net.fabricmc.yacom.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@Shadow
	private ClientWorld world;

	@Shadow
	private static void drawShapeOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double d, double e, double f, float g, float h, float i, float j) {};

	@Inject(at = @At("HEAD"), cancellable=true, method = "Lnet/minecraft/client/render/WorldRenderer;drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;DDDLnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V")
	void drawBlockOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState, CallbackInfo info) {
		//drawShapeOutline(matrixStack, vertexConsumer, blockState.getOutlineShape( this.world, blockPos, ShapeContext.of(entity)), (double)blockPos.getX() - d, (double)blockPos.getY() - e, (double)blockPos.getZ() - f, (((blockState.getTopMaterialColor(entity.world, blockPos).color & 255) + 64) % 256) / 255.0F, (((blockState.getTopMaterialColor(entity.world, blockPos).color >> 8 & 255) + 128) % 256) / 255.0F, (((blockState.getTopMaterialColor(entity.world, blockPos).color >> 16 & 255) + 192) % 256)/255.0F, 1.0F);
		int r = blockState.getTopMaterialColor(entity.world, blockPos).color & 255;
		int g = blockState.getTopMaterialColor(entity.world, blockPos).color >> 8 & 255;
		int b = blockState.getTopMaterialColor(entity.world, blockPos).color >> 16 & 255;
		int ave = (r + g + b) / 3;
		if (ave > 128) drawShapeOutline(matrixStack, vertexConsumer, blockState.getOutlineShape( this.world, blockPos, ShapeContext.of(entity)), (double)blockPos.getX() - d, (double)blockPos.getY() - e, (double)blockPos.getZ() - f, 0.0F, 0.0F, 0.0F, 0.4F);
		else drawShapeOutline(matrixStack, vertexConsumer, blockState.getOutlineShape( this.world, blockPos, ShapeContext.of(entity)), (double)blockPos.getX() - d, (double)blockPos.getY() - e, (double)blockPos.getZ() - f, 1.0F, 1.0F, 1.0F, 0.4F);
		info.cancel();
	}
}
