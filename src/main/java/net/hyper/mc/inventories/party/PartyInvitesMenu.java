package net.hyper.mc.inventories.party;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import net.hyper.mc.inventories.ItemError;
import net.hyper.mc.spigot.player.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Calendar;

public class PartyInvitesMenu implements InventoryProvider {

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
            for(String s : party.getConvites().keySet()){
                long time = party.getConvites().getOrDefault(s, 0L);
                if (time <= Calendar.getInstance().getTimeInMillis() && !party.isOpen()) {
                    party.removeConvite(player.getName());
                    continue;
                }
                ItemStack item = Bukkit.createItemCreator(Material.SKULL_ITEM)
                        .withName("§a"+s)
                        .withOwner(s)
                        .addLore(Arrays.asList("", "§cClique para remover o convite."))
                        .done();
                pagination.addItem(IntelligentItem.of(item, new ItemError(), i -> {
                    party.removeConvite(s);
                }));
            }
        }

        if(!pagination.isFirst()){
            pagination.setItem(47, IntelligentItem.of(
                    Bukkit.createItemCreator(Material.SKULL_ITEM)
                            .withTexture("f6dab7271f4ff04d5440219067a109b5c0c1d1e01ec602c0020476f7eb612180")
                            .withName("§aPágina Anterior").done(), new ItemError(), i -> {}));
        }

        pagination.setItem(49, IntelligentItem.of(Bukkit.createItemCreator(Material.BARRIER).withName("§cVoltar").done(), new ItemError(), i -> player.openPartyMenu()));

        if(!pagination.isLast()){
            pagination.setItem(51, IntelligentItem.of(
                    Bukkit.createItemCreator(Material.SKULL_ITEM)
                            .withTexture("8aa187fede88de002cbd930575eb7ba48d3b1a06d961bdc535800750af764926")
                            .withName("§aPróxima Página").done(), new ItemError(), i -> pagination.next()));
        }
    }
}
