package fudan.adweb.project.sortguysbackend.entity.game;

public class GarbageSortResultRedisInfo {
    private String username;
    private String garbageName;
    // true: 分对，false:分错
    private boolean sortResult;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGarbageName() {
        return garbageName;
    }

    public void setGarbageName(String garbageName) {
        this.garbageName = garbageName;
    }

    public boolean isSortResult() {
        return sortResult;
    }

    public void setSortResult(boolean sortResult) {
        this.sortResult = sortResult;
    }
}
