package ak.chaindestruction.command;

import static ak.akapi.Constants.COMMAND_RESET_PLAYER_STATUS;

import ak.chaindestruction.capability.CDPlayerStatus;
import ak.chaindestruction.capability.CapabilityCDPlayerStatusHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;

/**
 * プレイヤーの連鎖破壊設定を初期化するコマンド Created by A.K. on 2016/09/25.
 */
public class CommandResetCDPlayerStatus {

  public static void register(CommandDispatcher<CommandSource> commandDispatcher) {
    commandDispatcher.register(
        Commands.literal(COMMAND_RESET_PLAYER_STATUS).requires(e -> e.hasPermissionLevel(2))
            .executes(e -> execute(e.getSource(), null))
            .then(Commands.argument("target", EntityArgument.player())
                .executes(e -> execute(e.getSource(), EntityArgument.getPlayer(e, "target")))
            ));
  }

  private static int execute(CommandSource commandSource, @Nullable PlayerEntity PlayerEntity) {
    if (Objects.isNull(PlayerEntity)) {
      try {
        PlayerEntity = commandSource.asPlayer();
      } catch (CommandSyntaxException e) {
        e.printStackTrace();
        return 1;
      }
    }
    //noinspection ConstantConditions
    if (Objects.nonNull(PlayerEntity)) {
      CDPlayerStatus.get(PlayerEntity).ifPresent(status -> {
        CapabilityCDPlayerStatusHandler
            .copyPlayerStatus(CDPlayerStatus.DEFAULT_PLAYER_STATUS, status);
      });
    } else {
      return 1;
    }
    return 0;
  }
}
