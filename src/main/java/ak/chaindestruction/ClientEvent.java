package ak.chaindestruction;

import ak.akapi.Constants;
import ak.chaindestruction.network.MessageKeyPressed;
import ak.chaindestruction.network.MessageMousePressed;
import ak.chaindestruction.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Objects;

/**
 * クライアント側のマウス・キーボードイベントクラス
 * Created by A.K. on 14/08/01.
 */
public class ClientEvent {
    private static final Minecraft mc = Minecraft.getInstance();

    /** チャタリング防止用 */
    private int mouseCounter = 0;

    private byte getKeyIndex() {
        byte key = -1;
        if (ClientProxy.REGISTER_ITEM_KEY.consumeClick()) {
            key = Constants.RegKEY;
        } else if (ClientProxy.DIG_UNDER_KEY.consumeClick()) {
            key = Constants.DigKEY;
        } else if (ClientProxy.TREE_KEY.consumeClick()) {
            key = Constants.ModeKEY;
        }
        return key;
    }

    private void keyPressEvent() {
        if (mc.isWindowActive() && Objects.nonNull(mc.player)) {
            byte keyIndex = getKeyIndex();
            if (keyIndex != -1) {
//                PlayerEntity player = mc.player;
//                doKeyClient(null, player, keyIndex);
                PacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(keyIndex));
            }
        }
    }

    private byte getMouseIndex() {
        byte mouse = -1;
//        if (mc.gameSettings.keyBindUseItem.getIsKeyPressed()) {
//            mouse = 1;
//        }
        if (mc.options.keyPickItem.isDown()) {
            mouse = Constants.MIDDLE_CLICK;
        }
//        if (mouse != -1 && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
//            mouse += 3;
//        }
        return mouse;
    }

    private void mouseClickEvent() {
        if (mc.isWindowActive()) {
            if (mouseCounter > 0) {
                mouseCounter--;
            }
            byte mouseIndex = getMouseIndex();
            if (mouseIndex == Constants.MIDDLE_CLICK) {
                if (mouseCounter == 0) {
                    mouseCounter = 5;
                    boolean isFocusObject = (Objects.nonNull(mc.hitResult) && mc.hitResult.getType() != Type.MISS) || Objects.nonNull(mc.crosshairPickEntity);
                    PacketHandler.INSTANCE.sendToServer(new MessageMousePressed(mouseIndex, isFocusObject));
                }
            }
        }
    }

//    public void doKeyClient(ItemStack item, PlayerEntity player, byte key) {
//        if (key == Constants.DigKEY) {
//            ChainDestruction.digUnder = CDPlayerStatus.get(player).isDigUnder();
//        }
//    }
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void clientTickEvent(ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            mouseClickEvent();
            keyPressEvent();
        }
    }
}
