package net.hyper.mc.inventories.server;

import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import org.bukkit.entity.Player;

public class LobbiesMenu implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {
        set(player, contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        set(player, contents);
    }

    public void set(Player player, InventoryContents content) {

    }
}
