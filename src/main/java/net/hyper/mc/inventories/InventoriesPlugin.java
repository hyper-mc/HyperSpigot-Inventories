package net.hyper.mc.inventories;

import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import net.hyper.mc.inventories.server.ServerMenu;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class InventoriesPlugin extends JavaPlugin {

    public static InventoryManager inventoryManager;

    private RyseInventory serverInventory;

    @Override
    public void onEnable() {
        inventoryManager = new InventoryManager(this);
        inventoryManager.invoke();
        this.serverInventory = RyseInventory.builder()
                .provider(new ServerMenu())
                .size(9*6)
                .title("§7Modos de Jogo:")
                .build(this);
    }

    @Override
    public void onDisable() {
    }

    public void openProfile(Player player){

    }

    public void openServer(Player player){
        this.serverInventory.open(player);
    }
}
