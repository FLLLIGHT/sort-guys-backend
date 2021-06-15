package fudan.adweb.project.sortguysbackend.entity.game;

public class GarbageBinInfo {
    // 干垃圾：1；湿垃圾：2；有害垃圾：3；可回收垃圾：4（未使用）
    private int type;
    private Double maxX;
    private Double minX;
    private Double maxY;
    private Double minY;
    private Double maxZ;
    private Double minZ;


    public GarbageBinInfo() {
        // empty
    }

    public GarbageBinInfo(Double maxX, Double minX, Double maxZ, Double minZ) {
        this.maxX = maxX;
        this.minX = minX;
        this.maxZ = maxZ;
        this.minZ = minZ;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Double getMaxX() {
        return maxX;
    }

    public void setMaxX(Double maxX) {
        this.maxX = maxX;
    }

    public Double getMinX() {
        return minX;
    }

    public void setMinX(Double minX) {
        this.minX = minX;
    }

    public Double getMaxY() {
        return maxY;
    }

    public void setMaxY(Double maxY) {
        this.maxY = maxY;
    }

    public Double getMinY() {
        return minY;
    }

    public void setMinY(Double minY) {
        this.minY = minY;
    }

    public Double getMaxZ() {
        return maxZ;
    }

    public void setMaxZ(Double maxZ) {
        this.maxZ = maxZ;
    }

    public Double getMinZ() {
        return minZ;
    }

    public void setMinZ(Double minZ) {
        this.minZ = minZ;
    }
}
