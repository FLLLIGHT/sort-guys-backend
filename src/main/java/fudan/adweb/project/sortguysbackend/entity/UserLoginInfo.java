package fudan.adweb.project.sortguysbackend.entity;

public class UserLoginInfo {
    private Integer id;
    private Integer uid;
    private String token;

    public Integer getId() {
        return id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
