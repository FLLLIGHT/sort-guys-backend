package fudan.adweb.project.sortguysbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public class Authority implements GrantedAuthority {

    private static final long serialVersionUID = -8569508605766960263L;
    private Integer aid;
    private String authority;

    private Set<User> users;

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "aid=" + aid +
                ", authority='" + authority + '\'' +
                ", users=" + users +
                '}';
    }
}
