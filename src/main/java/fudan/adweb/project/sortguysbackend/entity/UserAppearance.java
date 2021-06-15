package fudan.adweb.project.sortguysbackend.entity;

// 用户形象
public class UserAppearance {
    private Integer uid;
    private String color;
    private String url;  // 发请求时不设置该项，直接为空或者 null 都可以

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
