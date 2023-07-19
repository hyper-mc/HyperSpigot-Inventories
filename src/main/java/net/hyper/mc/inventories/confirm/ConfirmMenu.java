package net.hyper.mc.inventories.confirm;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import lombok.AllArgsConstructor;
import net.hyper.mc.inventories.ItemError;
import net.hyper.mc.spigot.model.ConfirmModel;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

@AllArgsConstructor
public class ConfirmMenu implements InventoryProvider {

    private ConfirmModel YES_ACTION;
    private ConfirmModel NO_ACTION;

    @Override
    public void init(Player player, InventoryContents contents) {
        set(player, contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        set(player, contents);
    }

    public void set(Player player, InventoryContents content){
        content.set(12, IntelligentItem.of(Bukkit.createItemCreator(Material.STAINED_GLASS_PANE).setColor(DyeColor.GREEN).withName("§aConfirmar").done(), new ItemError(), i -> YES_ACTION.run((Player) i.getWhoClicked(), i)));
        content.set(14, IntelligentItem.of(Bukkit.createItemCreator(Material.STAINED_GLASS_PANE).setColor(DyeColor.RED).withName("§cCancelar").done(), new ItemError(), i -> NO_ACTION.run((Player) i.getWhoClicked(), i)));
    }
}
