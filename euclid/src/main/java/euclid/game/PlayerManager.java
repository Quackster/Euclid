package euclid.game;

import euclid.storage.database.access.UserDao;
import euclid.storage.database.data.PlayerData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

    public static final PlayerManager INSTANCE = new PlayerManager();

    private final ConcurrentHashMap<Player, Integer> authenticatedPlayers;

    private PlayerManager() {
        this.authenticatedPlayers = new ConcurrentHashMap<>();
    }

    public static PlayerManager getInstance() {
        return INSTANCE;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(authenticatedPlayers.keySet());
    }

    public void addPlayer(Player player) {
        authenticatedPlayers.putIfAbsent(player, player.getEntityData().getId());
    }

    public void removePlayer(Player player) {
        authenticatedPlayers.remove(player);
    }

    public Player getPlayerByName(String username) {
        return authenticatedPlayers.keySet().stream()
                .filter(x -> x.getDetails().getName().equals(username))
                .findFirst()
                .orElse(null);
    }

    public Player getPlayerById(int id) {
        return authenticatedPlayers.keySet().stream()
                .filter(x -> x.getDetails().getId() == id)
                .findFirst()
                .orElse(null);
    }

    public PlayerData getDataById(int userId) {
        Player player = getPlayerById(userId);
        if (player != null)
            return player.getDetails();
        return UserDao.getById(userId);
    }

    public String getName(int userId) {
        Player player = getPlayerById(userId);
        if (player != null)
            return player.getDetails().getName();
        return UserDao.getNameById(userId);
    }
}
