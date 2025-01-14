package ak.chaindestruction;

import ak.chaindestruction.network.MessageDigSound;
import ak.chaindestruction.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.LinkedHashSet;

/**
 * ブロック破壊のタスククラス Created by A.K. on 15/01/13.
 */
public class DigTask {

  private final LinkedHashSet<BlockPos> blockToDestroySet = new LinkedHashSet<>();
  private final PlayerEntity digger;
  private final ItemStack heldItem;
  private int counter;

  public DigTask(PlayerEntity player, ItemStack itemStack, LinkedHashSet<BlockPos> blockPosSet) {
    this.digger = player;
    this.heldItem = itemStack;
    this.blockToDestroySet.addAll(blockPosSet);
  }

  //return true : when all block destroyed or heldItem broken
  public boolean increaseCount() {
    counter++;
    if (counter >= ConfigUtils.COMMON.digTaskMaxCounter) {
      counter = 0;
      return destroyBlock();
    }
    return false;
  }

  public boolean destroyBlock() {
    if (blockToDestroySet.isEmpty()) {
      return true;
    }
    BlockPos first = blockToDestroySet.iterator().next();
    blockToDestroySet.remove(first);
    if (!(this.digger.getCommandSenderWorld() instanceof ServerWorld)) {
      return true;
    }
    ServerWorld world = (ServerWorld) this.digger.getCommandSenderWorld();
    world.globalLevelEvent(2001, first, Block.getId(world.getBlockState(first)));
    PacketHandler.INSTANCE.sendTo(new MessageDigSound(first),
        ((ServerPlayerEntity) digger).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    return InteractBlockHook.destroyBlockAtPosition(world, digger, first, heldItem);
  }

  public PlayerEntity getDigger() {
    return digger;
  }
}
