package euclid.game.permissions;

import euclid.game.ILoadable;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionsManager implements ILoadable {

    public static final PermissionsManager INSTANCE = new PermissionsManager();

    private Map<Integer, UserGroup> ranks;

    private PermissionsManager() {
    }

    public static PermissionsManager getInstance() {
        return INSTANCE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void load() {
        ranks = new HashMap<>();

        Yaml yaml = new Yaml();
        Map<String, Object> config;
        try (InputStream input = new FileInputStream("config/permissions.yml")) {
            config = yaml.load(input);
        } catch (Exception e) {
            return;
        }

        Map<String, Map<String, Object>> groups = (Map<String, Map<String, Object>>) config.get("groups");

        if (groups == null) return;

        for (Map.Entry<String, Map<String, Object>> entry : groups.entrySet()) {
            String groupName = entry.getKey();
            Map<String, Object> groupData = entry.getValue();

            int rank = Integer.parseInt(String.valueOf(groupData.get("rank")));
            UserGroup userGroup = new UserGroup(groupName, rank);

            try {
                List<String> permissions = (List<String>) groupData.get("permissions");
                if (permissions != null) {
                    for (String permission : permissions)
                        userGroup.getDefaultPermissions().add(permission);
                }
            } catch (Exception ignored) {
            }

            try {
                List<String> inherits = (List<String>) groupData.get("inherits");
                if (inherits != null) {
                    for (String inheritsGroup : inherits)
                        userGroup.getInheritsGroups().add(inheritsGroup);
                }
            } catch (Exception ignored) {
            }

            try {
                List<String> excludes = (List<String>) groupData.get("excludes");
                if (excludes != null) {
                    for (String excludesGroup : excludes)
                        userGroup.getExcludesPermissions().add(excludesGroup);
                }
            } catch (Exception ignored) {
            }

            ranks.put(rank, userGroup);
        }

        UserGroup[] groupArray = ranks.values().toArray(new UserGroup[0]);
        for (UserGroup group : groupArray) {
            group.buildPermissions(groupArray);
        }
    }

    public Map<Integer, UserGroup> getRanks() {
        return ranks;
    }
}
