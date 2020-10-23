package me.fluffy.DynmapClaims;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimsCommands implements CommandExecutor {
    private final Loader plugin;
    private Player CurrentPlayer = null;
    private Location firstLoc = null;
    public ClaimsCommands(Loader loader){
        this.plugin = loader;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("Only players can use this command.");
            return true;
        }

        Player p = (Player) commandSender;
        if(label.equalsIgnoreCase("deleteclaim")){
            if(args.length != 0){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),String.format("dmarker deleteline id:%s set:claims",args[0]));
                p.sendMessage(ChatColor.GOLD + "Please check on dynmap if this worked. If not, check your id spelling!");
                return true;
            } else {
                p.sendMessage(ChatColor.RED + "You didn't enter your id!");
                return false;
            }
        }
        if(label.equalsIgnoreCase("claim")){
            if(args.length != 0){ //checks for args
                if(args[0].equalsIgnoreCase("-force")) { // checks for forcing claim
                    CurrentPlayer = null;
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"dmarker clearcorners");
                    // reset dynmap corners then continue to if else as normal
                }
                if(args[0].equalsIgnoreCase("done")){ //checks if claim is done
                    if(args.length != 3){
                        p.sendMessage(ChatColor.RED + "Something isn't right! either you have too many arguments or too little");
                        p.sendMessage(ChatColor.RED + "Common mistake is that ID and name of the claim cannot be multiple words.");
                        p.sendMessage(ChatColor.RED + "So use camelCase or under_scores");
                        // error correction
                        return true;
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),String.format("dmarker addcorner %s %s %s %s",firstLoc.getBlockX(),firstLoc.getBlockY(),firstLoc.getBlockZ(),firstLoc.getWorld().getName()));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),String.format("dmarker addline id:%s %s set:claims",args[1],args[2]));
                    p.sendMessage(ChatColor.GREEN + "Claim Created! If the claim isn't correct, run /deleteclaim <id>");
                    firstLoc = null;
                    CurrentPlayer = null;
                    return true;
                }
            }

            if(CurrentPlayer == null){ // check if no one is currently claiming
                CurrentPlayer = p;
                p.sendMessage(ChatColor.GOLD + "To start, please move to the first corner and execute this command again.");
                p.sendMessage(ChatColor.GOLD + "At the end, this will ask for an ID, this is so you can edit it later, so make it memorable and record it somewhere.");

            } else {

                if(p == CurrentPlayer){ // check if the player sending command is the one claiming
                    if(firstLoc == null){ // checks if this isn't the first time, and saves location for finishing polygon
                        firstLoc = p.getLocation();
                    }
                    Location loc = p.getLocation();
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),String.format("dmarker addcorner %s %s %s %s",loc.getBlockX(),loc.getBlockY(),loc.getBlockZ(),loc.getWorld().getName()));
                    // Dynmap command for adding corner with location arguments
                    p.sendMessage(ChatColor.GREEN + "Corner added! Go to the next corner, or if done, run \"/claim done <id> <nameOfClaim>\"");
                } else { // else for if they aren't the current claimant
                    p.sendMessage(ChatColor.RED + "The Player " + CurrentPlayer.getName() + " is currently claiming something. Please try again later.");
                    p.sendMessage(ChatColor.RED + "If this player is not online, run \"/claim -force\"");
                }

            }
            return true;
        }

        return false;

    }

}
