package ak.chaindestruction.command;

import static ak.akapi.Constants.COMMAND_COPY_R_TO_L;

import ak.chaindestruction.capability.CDItemStackStatus;
import ak.chaindestruction.capability.CapabilityCDItemStackStatusHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * 右手のアイテムの設定を左手のアイテムにコピーするコマンド Created by A.K. on 2016/09/26.
 */
public class CommandCopyRtoLCDStatus {

  public static void register(CommandDispatcher<CommandSource> commandDispatcher) {
    commandDispatcher.register(
        Commands.literal(COMMAND_COPY_R_TO_L).requires(e -> e.hasPermission(2))
            .executes(e -> execute(e.getSource(), null))
            .then(Commands.argument("target", EntityArgument.player())
                .executes(e -> execute(e.getSource(), EntityArgument.getPlayer(e, "target")))
            ));
  }

  private static int execute(CommandSource commandSource, @Nullable PlayerEntity PlayerEntity) {
    if (Objects.isNull(PlayerEntity)) {
      try {
        PlayerEntity = commandSource.getPlayerOrException();
      } catch (CommandSyntaxException e) {
        e.printStackTrace();
        return 1;
      }
    }
    //noinspection ConstantConditions
    if (Objects.nonNull(PlayerEntity)) {
      ItemStack itemMainHand = PlayerEntity.getMainHandItem();
      ItemStack itemOffHand = PlayerEntity.getOffhandItem();
      CDItemStackStatus.get(itemMainHand)
          .ifPresent((copyFrom) ->
              CDItemStackStatus.get(itemOffHand).ifPresent(
                  (copyTo) -> CapabilityCDItemStackStatusHandler
                      .copyItemState(copyFrom, copyTo)));
    } else {
      return 1;
    }
    return 0;
  }
}
