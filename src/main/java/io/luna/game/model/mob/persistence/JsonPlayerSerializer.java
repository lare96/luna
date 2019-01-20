package io.luna.game.model.mob.persistence;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.luna.LunaConstants;
import io.luna.game.model.Position;
import io.luna.game.model.item.IndexedItem;
import io.luna.game.model.mob.Player;
import io.luna.game.model.mob.PlayerRights;
import io.luna.game.model.mob.Skill;
import io.luna.game.model.mob.attr.AttributeKey;
import io.luna.net.codec.login.LoginResponse;
import io.luna.util.GsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map.Entry;

import static io.luna.util.GsonUtils.getAsType;
import static io.luna.util.GsonUtils.toJsonTree;

/**
 * A {@link PlayerSerializer} implementation that stores persistent player data in local {@code JSON} files. Is
 * also used by other serializers.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class JsonPlayerSerializer extends PlayerSerializer {

    /**
     * The path to the local files.
     */
    private static final Path DIR = Paths.get("./data/saved_players");

    static {
        try {
            // Initialize directory if it doesn't exist.
            if (Files.notExists(DIR)) {
                Files.createDirectories(DIR);
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public LoginResponse load(Player player, String enteredPassword) {
        var filePath = computePath(player);

        // File doesn't exist, new player.
        if (!Files.exists(filePath)) {
            player.setPosition(LunaConstants.STARTING_POSITION);
            return LoginResponse.NORMAL;
        }

        try {
            // Load normally.
            return parseFromJson(player, filePath, enteredPassword);
        } catch (Exception e) {
            LOGGER.catching(e);
            return LoginResponse.COULD_NOT_COMPLETE_LOGIN;
        }
    }

    @Override
    public boolean save(Player player) {
        var filePath = computePath(player);
        
        try {
            GsonUtils.writeJson(toJson(player), filePath);
        } catch (Exception e) {
            LOGGER.catching(e);
            return false;
        }
        
        return true;
    }

    /**
     * Load the persistent data from an {@link JsonObject}.
     *
     * @param player The player.
     * @param data The JSON object.
     * @param enteredPassword The entered password.
     * @return The login response.
     * @throws ClassNotFoundException If the attribute type is not found.
     */
    public LoginResponse fromJson(Player player, JsonObject data, String enteredPassword)
            throws ClassNotFoundException {
        var password = data.get("password").getAsString();
        
        if (!checkPw(enteredPassword, password)) {
            return LoginResponse.INVALID_CREDENTIALS;
        }

        var position = getAsType(data.get("position"), Position.class);
        player.setPosition(position);

        var rights = PlayerRights.valueOf(data.get("rights").getAsString());
        player.setRights(rights);

        var running = data.get("running").getAsBoolean();
        player.getWalking().setRunning(running);

        var appearance = getAsType(data.get("appearance"), int[].class);
        player.getAppearance().setValues(appearance);

        var inventory = getAsType(data.get("inventory"), IndexedItem[].class);
        player.getInventory().init(inventory);

        var bank = getAsType(data.get("bank"), IndexedItem[].class);
        player.getBank().init(bank);

        var equipment = getAsType(data.get("equipment"), IndexedItem[].class);
        player.getEquipment().init(equipment);

        var skills = getAsType(data.get("skills"), Skill[].class);
        player.getSkills().setSkills(skills);

        var friends = getAsType(data.get("friends"), long[].class);
        player.setFriends(friends);

        var ignores = getAsType(data.get("ignores"), long[].class);
        player.setIgnores(ignores);

        var attributes = data.get("attributes").getAsJsonObject();
        
        for (Entry<String, JsonElement> entry : attributes.entrySet()) {
            JsonObject attr = entry.getValue().getAsJsonObject();

            Class<?> type = Class.forName(attr.get("type").getAsString());
            Object value = getAsType(attr.get("value"), type);
            player.getAttributes().get(entry.getKey()).set(value);
        }
        
        return LoginResponse.NORMAL;
    }

    /**
     * Parses and evaluates the {@code JSON} file at {@code path}.
     *
     * @param player The player.
     * @param path The path to the file.
     * @param enteredPassword The entered password.
     * @return The login response.
     * @throws ClassNotFoundException If the attribute type is not found.
     * @throws IOException If any I/O errors occur.
     */
    public LoginResponse parseFromJson(Player player, Path path, String enteredPassword)
            throws ClassNotFoundException, IOException {
        var jsonString = new String(Files.readAllBytes(path));
        var data = new JsonParser().parse(jsonString).getAsJsonObject();
        return fromJson(player, data, enteredPassword);
    }

    /**
     * Saves persistent data into a {@link JsonObject}.
     *
     * @param player The player.
     * @return The serialization object.
     */
    public JsonObject toJson(Player player) {
        var data = new JsonObject();
        
        data.addProperty("password", computePw(player));
        data.add("position", toJsonTree(player.getPosition()));
        data.addProperty("rights", player.getRights().name());
        data.addProperty("running", player.getWalking().isRunning());
        data.add("appearance", toJsonTree(player.getAppearance().toArray()));
        data.add("inventory", toJsonTree(player.getInventory().toIndexedArray()));
        data.add("bank", toJsonTree(player.getBank().toIndexedArray()));
        data.add("equipment", toJsonTree(player.getEquipment().toIndexedArray()));
        data.add("skills", toJsonTree(player.getSkills().toArray()));
        data.add("friends", toJsonTree(player.getFriends().toArray()));
        data.add("ignores", toJsonTree(player.getIgnores().toArray()));

        var attributes = new JsonObject();
        
        for (var entry : player.getAttributes()) {
            var key = AttributeKey.ALIASES.get(entry.getKey());
            var value = entry.getValue();

            if (key.isPersistent()) {
                var attr = new JsonObject();
                attr.addProperty("type", key.getTypeName());
                attr.add("value", toJsonTree(value.get()));
                attributes.add(key.getName(), attr);
            }
        }
        
        data.add("attributes", attributes);
        return data;
    }

    /**
     * Computes the path to the data file of {@code player}.
     *
     * @param player The player.
     * @return The path to the file.
     */
    private Path computePath(Player player) {
        return DIR.resolve(player.getUsername() + ".json");
    }
}