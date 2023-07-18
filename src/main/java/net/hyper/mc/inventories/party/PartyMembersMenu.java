package net.hyper.mc.inventories.party;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import net.hyper.mc.inventories.ItemError;
import net.hyper.mc.spigot.player.party.Party;
import net.hyper.mc.spigot.player.party.PartyPlayer;
import net.hyper.mc.spigot.player.party.PartyRole;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PartyMembersMenu implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {
        set(player, contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        set(player, contents);
    }

    public void set(Player player, InventoryContents content){
        Party party = player.getParty();
        Pagination pagination = content.pagination();
        pagination.setItemsPerPage(7*3);
        pagination.iterator(SlotIterator.builder()
                .type(SlotIterator.SlotIteratorType.HORIZONTAL)
                .startPosition(1,1 ).build());
        if(party != null){
            Map<PartyPlayer, PartyRole> members = new HashMap<>();
            for(PartyPlayer pl : members.keySet()) {
                ItemStack item = Bukkit.createItemCreator(Material.SKULL_ITEM)
                        .withName("§a" + pl.getName())
                        .withOwner(pl.getName())
                        .addLore(Arrays.asList("§7Cargo: §f" + members.get(pl).getName(), "§7"))
                        .removeFlags()
                        .done();
                pagination.addItem(IntelligentItem.of(item, new ItemError(), i -> pagination.previous()));
            }
        }

        if(pagination.isFirst()){
            pagination.setItem(47, IntelligentItem.of(
                    Bukkit.createItemCreator(Material.SKULL_ITEM)
                            .withTexture("f6dab7271f4ff04d5440219067a109b5c0c1d1e01ec602c0020476f7eb612180")
                            .withName("§aPágina Anterior").done(), new ItemError(), i -> {}));
        }

        pagination.setItem(49, IntelligentItem.of(Bukkit.createItemCreator(Material.BARRIER).withName("§cVoltar").done(), new ItemError(), i -> player.openPartyMenu()));

        if(pagination.isLast()){
            pagination.setItem(51, IntelligentItem.of(
                    Bukkit.createItemCreator(Material.SKULL_ITEM)
                            .withTexture("8aa187fede88de002cbd930575eb7ba48d3b1a06d961bdc535800750af764926")
                            .withName("§aPróxima Página").done(), new ItemError(), i -> pagination.next()));
        }
    }
}
