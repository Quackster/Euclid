package euclid.game.permissions;

import java.util.HashSet;
import java.util.List;

public class UserGroup {

    private String name;
    private int rank;
    private List<String> defaultPermissions;
    private List<String> inheritsGroups;
    private List<String> excludesPermissions;
    private HashSet<String> permissions;

    public UserGroup(String name, int rank) {
        this.name = name;
        this.rank = rank;
        this.defaultPermissions = new java.util.ArrayList<>();
        this.inheritsGroups = new java.util.ArrayList<>();
        this.excludesPermissions = new java.util.ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public List<String> getDefaultPermissions() {
        return defaultPermissions;
    }

    public void setDefaultPermissions(List<String> defaultPermissions) {
        this.defaultPermissions = defaultPermissions;
    }

    public List<String> getInheritsGroups() {
        return inheritsGroups;
    }

    public void setInheritsGroups(List<String> inheritsGroups) {
        this.inheritsGroups = inheritsGroups;
    }

    public List<String> getExcludesPermissions() {
        return excludesPermissions;
    }

    public void setExcludesPermissions(List<String> excludesPermissions) {
        this.excludesPermissions = excludesPermissions;
    }

    public HashSet<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(HashSet<String> permissions) {
        this.permissions = permissions;
    }

    public void buildPermissions(UserGroup[] groups) {
        permissions = new HashSet<>();

        for (String permission : defaultPermissions)
            permissions.add(permission);

        for (UserGroup group : groups) {
            for (String inheritGroup : inheritsGroups) {
                if (inheritGroup.equals(group.getName())) {
                    for (String permission : group.getDefaultPermissions())
                        permissions.add(permission);
                }
            }
        }

        for (String excludePermission : excludesPermissions)
            permissions.remove(excludePermission);
    }

    public boolean hasPermission(String permission) {
        if (permissions.contains("root"))
            return true;

        return permissions.contains(permission);
    }
}
