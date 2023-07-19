package net.hyper.mc.inventories.party;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import net.hyper.mc.inventories.InventoriesPlugin;
import net.hyper.mc.inventories.ItemError;
import net.hyper.mc.spigot.player.party.Party;
import net.hyper.mc.spigot.player.party.PartyRole;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class PartyMenu implements InventoryProvider {

    @Override
    public void init(Player player, InventoryContents contents) {
        set(player, contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        set(player, contents);
    }

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");

    public void set(Player player, InventoryContents content){
        Party party = player.getParty();
        if(party != null){

            ItemStack infoStack = Bukkit.createItemCreator(Material.PAPER)
                    .addLore(new ArrayList<>(Arrays.asList(
                            "§7Dono: §f" + party.getOwner().getName(),
                            "§7Nome: §f" + party.getName(),
                            "§7Limite de jogadores: §f" + party.getMembers().size() + "/" + party.getMaxSize(),
                            "§7Estado da Party: §f" + (party.isOpen() ? "Pública" : "Privada"),
                            "§7Estado do Chat: §f" + (party.isChatMuted() ? "Apenas para Moderadores" : "Liberado para Todos"),
                            "§7Criado em: §f" + dateFormat.format(party.getCreateTime()))))
                    .withName("§aInformações")
                    .removeFlags()
                    .done();
            ItemStack convidarStack = Bukkit.createItemCreator(Material.SIGN)
                    .addLore(new ArrayList<>(Arrays.asList(
                            "§7Convide um jogador para",
                            "§7sua party §bclicando aqui§7.")))
                    .withName("§aConvidar um jogador")
                    .removeFlags().done();
            ItemStack members = Bukkit.createItemCreator(player.getItemHead())
                    .addLore(new ArrayList<>(Arrays.asList("§7Clique para ver os membros.")))
                    .withName("§aMembros da Party")
                    .removeFlags()
                    .done();
            ItemStack partidaExclusiva = Bukkit.createItemCreator(Material.ENDER_PEARL)
                    .addLore(new ArrayList<>(Arrays.asList("§7Clique para criar uma partida privada.")))
                    .withName("§aPartida Privada")
                    .done();
            ItemStack convites = Bukkit.createItemCreator(Material.NAME_TAG)
                    .addLore(new ArrayList<>(Arrays.asList("§7Clique para ver os convites enviados.")))
                    .withName("§aConvites Enviados")
                    .done();
            ItemStack excluir = Bukkit.createItemCreator(Material.BARRIER)
                    .addLore(new ArrayList<>(Arrays.asList("§7Clique para deletar a party.")))
                    .withName("§cDeletar Party").done();
            content.set(10, IntelligentItem.empty(infoStack));
            content.set(11, IntelligentItem.of(convites, new ItemError(), i -> InventoriesPlugin.partyInvitesInventory.open((Player) i.getWhoClicked())));
            content.set(12, IntelligentItem.of(members, new ItemError(), i -> InventoriesPlugin.partyMembersInventory.open((Player) i.getWhoClicked())));
            //meio vazio
            content.set(14, IntelligentItem.of(partidaExclusiva, new ItemError(), i -> {}));
            content.set(15, IntelligentItem.of(convidarStack, new ItemError(), i -> {}));
            content.set(16, IntelligentItem.of(excluir, new ItemError(), i -> {
                ((Player) i.getWhoClicked()).openConfirmMenu((pl, evt) -> {
                    Party p = pl.getParty();
                    if(p == null){
                        pl.sendMessage("§cVocê não está em uma party!");
                        return;
                    }
                    if(!p.getOwner().getName().equals(pl.getName())){
                        pl.sendMessage("§cVocê não é dono da party para excluí-la!");
                        return;
                    }
                    Bukkit.getHyperSpigot().getPartyManager().delete(pl);
                }, (pl, evt) -> pl.openPartyMenu());
            }));
        }
    }
}
