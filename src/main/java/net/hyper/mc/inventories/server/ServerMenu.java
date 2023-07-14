package net.hyper.mc.inventories.server;

import com.google.gson.Gson;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItemError;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import net.hyper.mc.inventories.ItemError;
import net.hyper.mc.spigot.bungeecord.BungeeAction;
import net.hyper.mc.spigot.bungeecord.BungeeManager;
import net.hyper.mc.spigot.bungeecord.item.Server;
import net.hyper.mc.spigot.bungeecord.item.ServerItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemCreator;

import java.util.Comparator;

public class ServerMenu implements InventoryProvider {

    private Gson gson = new Gson();
    private BungeeManager bungeeManager = Bukkit.getHyperSpigot().getBungeeManager();

    @Override
    public void init(Player player, InventoryContents content) {
        set(content);
    }

    @Override
    public void update(Player player, InventoryContents content) {
        set(content);
    }

    public void set(InventoryContents content){
        for(Server server : bungeeManager.getServerItems()){
            ServerItem si = gson.fromJson(server.getItem(), ServerItem.class);
            ItemCreator creator = si.toItemCreator();
            creator.addLore(server.getDescription()).withName(server.getName()).removeFlags();
            content.add(new IntelligentItem(creator.done(), i -> execute(i, server, si), new ItemError()));
        }
    }

    public void execute(InventoryClickEvent i, Server server, ServerItem si){
        if(!server.isConnectable()){
            return;
        }
        bungeeManager.requestUpdate(BungeeAction.SERVER_LIST, null, null);
        bungeeManager.requestUpdate(BungeeAction.PLAYER_LIST, null, null);
        bungeeManager.requestUpdate(BungeeAction.PLAYER_COUNT, null, null);
        String serverSelected = server.getBungeeserver().stream()
                .filter(s -> bungeeManager.getServerInfo(s) != null)
                .min(Comparator.comparingInt(s -> (int) bungeeManager.getServerInfo(s).getOrDefault("count", 0)))
                .orElse(null);
        if(serverSelected != null){
            i.getWhoClicked().sendMessage("§aConectando...");
            bungeeManager.requestUpdate(BungeeAction.CONNECT, (Player) i.getWhoClicked(), serverSelected);
        }
    }
}
