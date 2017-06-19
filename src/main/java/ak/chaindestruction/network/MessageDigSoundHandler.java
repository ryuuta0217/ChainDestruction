package ak.chaindestruction.network;

import ak.chaindestruction.ChainDestruction;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * ブロック採掘用メッセージハンドラクラス
 * Created by AKIRA on 15/01/13.
 */
public class MessageDigSoundHandler implements IMessageHandler<MessageDigSound, IMessage> {
    @Override
    public IMessage onMessage(MessageDigSound message, MessageContext ctx) {
        EntityPlayer player = ChainDestruction.proxy.getEntityPlayer();
        World world = player.getEntityWorld();
        BlockPos blockPos = message.getBlockPos();
        world.playBroadcastSound(2001, blockPos, Block.getStateId(world.getBlockState(blockPos)));
        return null;
    }
}