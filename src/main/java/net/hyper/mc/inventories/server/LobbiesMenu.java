package net.hyper.mc.inventories.server;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import lombok.RequiredArgsConstructor;
import net.hyper.mc.inventories.ItemError;
import net.hyper.mc.spigot.lobbies.LobbyManager;
import net.hyper.mc.spigot.lobbies.ServerLobby;
import net.hyper.mc.spigot.lobbies.WorldLobby;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemCreator;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LobbiesMenu implements InventoryProvider {

    private String type;
    private LobbyManager lobbyManager = Bukkit.getHyperSpigot().getLobbyManager();

    public LobbiesMenu(String type) {
        this.type = type;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        set(player, contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        set(player, contents);
    }

    public void set(Player player, InventoryContents content) {
        Pagination pagination = content.pagination();
        List<WorldLobby> worldLobbies = lobbyManager.getLobby(type, "default");
        List<String> serverLobbies = lobbyManager.getNetworkLobby(type, "default");

        pagination.setItemsPerPage(7*3);
        pagination.iterator(SlotIterator.builder()
                .startPosition(1, 1)
                .type(SlotIterator.SlotIteratorType.HORIZONTAL).build());

        for(WorldLobby l : worldLobbies){
            ItemCreator item = Bukkit.createItemCreator(Material.WOOL);
            if(player.getLobby() == l){
                item.setColor(DyeColor.GREEN);
                item.withEnchant(Enchantment.LUCK, 1);
            } else{
                item.setColor(DyeColor.GRAY);
            }
            item.removeFlags();
            pagination.addItem(new IntelligentItem(item.done(), i -> {
                l.teleport((Player) i.getWhoClicked());
            }, new ItemError()));
        }

        for(String l : serverLobbies){
            ItemCreator item = Bukkit.createItemCreator(Material.WOOL);
            item.setColor(DyeColor.GRAY);
            item.removeFlags();
            pagination.addItem(new IntelligentItem(item.done(), i -> {
                lobbyManager.connectServerLobby(l, (Player) i.getWhoClicked());
            }, new ItemError()));
        }

        if(pagination.isFirst()){
            pagination.setItem(47, IntelligentItem.of(
                    Bukkit.createItemCreator(Material.SKULL_ITEM)
                            .withTexture("f6dab7271f4ff04d5440219067a109b5c0c1d1e01ec602c0020476f7eb612180")
                            .withName("§aPágina Anterior").done(), new ItemError(), i -> pagination.previous()));
        }

        pagination.setItem(49, IntelligentItem.of(Bukkit.createItemCreator(Material.BARRIER).withName("§cFechar").done(), new ItemError(), i -> player.closeInventory()));

        if(pagination.isLast()){
            pagination.setItem(51, IntelligentItem.of(
                    Bukkit.createItemCreator(Material.SKULL_ITEM)
                            .withTexture("8aa187fede88de002cbd930575eb7ba48d3b1a06d961bdc535800750af764926")
                            .withName("§aPróxima Página").done(), new ItemError(), i -> pagination.next()));
        }
    }
}
