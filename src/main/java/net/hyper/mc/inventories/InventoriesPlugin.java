package net.hyper.mc.inventories;

import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import net.hyper.mc.inventories.confirm.ConfirmMenu;
import net.hyper.mc.inventories.party.PartyMembersMenu;
import net.hyper.mc.inventories.party.PartyMenu;
import net.hyper.mc.inventories.server.LobbiesMenu;
import net.hyper.mc.inventories.server.ServerMenu;
import net.hyper.mc.spigot.model.ConfirmModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class InventoriesPlugin extends JavaPlugin {

    public static InventoryManager inventoryManager;

    public static RyseInventory serverInventory;
    public static RyseInventory partyInventory;
    public static RyseInventory partyMembersInventory;

    @Override
    public void onEnable() {
        inventoryManager = new InventoryManager(this);
        inventoryManager.invoke();
        serverInventory = RyseInventory.builder()
                .provider(new ServerMenu())
                .size(9*6)
                .title("§7Modos de Jogo:")
                .build(this);
        partyInventory = RyseInventory.builder()
                .provider(new PartyMenu())
                .size(3*9)
                .title("§7Informações da Party:")
                .build(this);
        partyMembersInventory = RyseInventory.builder()
                .provider(new PartyMembersMenu())
                .size(6*9)
                .title("§7Membros da Party:")
                .build(this);
    }

    @Override
    public void onDisable() {
    }

    public void openProfile(Player player){

    }

    public void openServer(Player player){
        serverInventory.open(player);
    }

    public void openLobbies(String type, Player player){
        RyseInventory.builder()
                .title("§7Lobbies: "+type)
                .provider(new LobbiesMenu(type))
                .size(9*6)
                .build(this).open(player);
    }

    public void openParty(Player player){
        partyInventory.open(player);
    }

    public void openConfirm(Player player, ConfirmModel YES, ConfirmModel NO){
        RyseInventory.builder()
                .title("§7Confirme sua ação")
                .provider(new ConfirmMenu(YES, NO))
                .size(9*3)
                .build(this).open(player);
    }
}
