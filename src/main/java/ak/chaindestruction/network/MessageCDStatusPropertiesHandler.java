package ak.chaindestruction.network;

import ak.chaindestruction.ClientProxy;
import ak.chaindestruction.capability.CDPlayerStatus;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static ak.chaindestruction.capability.CapabilityCDPlayerStatusHandler.CAPABILITY_CHAIN_DESTRUCTION_PLAYER;

/**
 * 連鎖破壊ステータスハンドラクラス Created by A.K. on 14/07/31.
 */
public class MessageCDStatusPropertiesHandler implements
    BiConsumer<MessageCDStatusProperties, Supplier<Context>> {

  @Override
  public void accept(MessageCDStatusProperties messageCDStatusProperties, Supplier<Context> contextSupplier) {
    if(ClientProxy.INSTANCE == null) return;

    PlayerEntity player = ClientProxy.INSTANCE.getPlayerEntity();
    if (Objects.nonNull(player)) {
      CDPlayerStatus.get(player)
              .ifPresent(instance -> CAPABILITY_CHAIN_DESTRUCTION_PLAYER.getStorage()
                      .readNBT(CAPABILITY_CHAIN_DESTRUCTION_PLAYER, instance, null,
                              messageCDStatusProperties.getData()));
    }
  }
}
