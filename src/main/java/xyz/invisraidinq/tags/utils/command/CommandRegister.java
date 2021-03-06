package xyz.invisraidinq.tags.utils.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import xyz.invisraidinq.tags.utils.CC;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

public class CommandRegister {

    private CommandMap commandMap;

    public CommandMap getCommandMap() {
        if (commandMap != null) {
            return commandMap;
        }

        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);

            commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return commandMap;
    }

    public PluginCommand getCommand(String name, Plugin owner) {
        PluginCommand command = null;

        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            command = constructor.newInstance(name, owner);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return command;
    }

    public void registerCommand(ExecutableCommand executableCommand, Plugin owner, boolean requiresPermission) {
        PluginCommand command = getCommand(executableCommand.getName(), owner);

        command.setPermissionMessage(CC.RED + "No Permission");

        if (requiresPermission) {
            String prefix = owner.getName();
            if(prefix.contains("plugin")) prefix.replace("plugin", "");
            command.setPermission((prefix + ".command." + executableCommand.getName()).toLowerCase());
        }

        if (executableCommand.getDescription() != null) {
            command.setDescription(executableCommand.getDescription());
        }

        command.setAliases(Arrays.asList(executableCommand.getAliases()));

        command.setExecutor(executableCommand);
        command.setTabCompleter(executableCommand);

        if (!getCommandMap().register(executableCommand.getName(), command)) {
            command.unregister(getCommandMap());
            getCommandMap().register(executableCommand.getName(), command);
        }
    }
}

