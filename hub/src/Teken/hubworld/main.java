package Teken.hubworld;

import net.minecraft.server.v1_6_R2.Packet201PlayerInfo;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener{
	static final String name = "Tekens Hub World";
	static final String textName = "["+name+"] ";
	static final String perm = "Teken.Hub.Visible"; 

	@Override public void onEnable(){
		getLogger().info(name+" has been enabled");
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this, this);
		this.getCommand("hubreload").setExecutor(this);
	}

	@Override public void onDisable(){
		getLogger().info(name+" has been disabled");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		Player p = (Player)sender;
		if (cmd.getName().equalsIgnoreCase("hubreload")){
			cleanUp();
			update();
			p.sendMessage(textName+"Hub World Reloaded");
			return true;
		}
		return true;
	}

	@EventHandler public void onPlayerJoin(PlayerJoinEvent event){
		event.setJoinMessage(null);
		runPlayer(event.getPlayer());
	}

	@EventHandler public void onPlayerQuit(PlayerQuitEvent event){
		event.setQuitMessage(null);
	}
	
	@EventHandler public void onWorldSave(WorldSaveEvent event){
		cleanUp();
		update();
		getLogger().info(textName+"Hub Refresh Done");
	}

	public void update(){
		for (Player player : getServer().getOnlinePlayers()){
			runPlayer(player);
		}
	}

	public void cleanUp(){
		for (Player player1 : getServer().getOnlinePlayers()){
			for (Player player2 : getServer().getOnlinePlayers()){
				player1.showPlayer(player2);
			}
		}
	}

	private void runPlayer(Player play){
		if(!play.hasPermission(perm)){
			Packet201PlayerInfo packet = new Packet201PlayerInfo(play.getPlayerListName(), false, 9999);
			for(Player p: getServer().getOnlinePlayers()){
				((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
			}
		}
		for (Player player : getServer().getOnlinePlayers()) {
			if(!play.hasPermission(perm))player.hidePlayer(play);
			if(!player.hasPermission(perm))play.hidePlayer(player);
		}
	}
}