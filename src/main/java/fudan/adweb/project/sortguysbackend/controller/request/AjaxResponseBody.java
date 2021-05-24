package fudan.adweb.project.sortguysbackend.controller.request;

import java.io.Serializable;

// 处理权限问题，封装信息
public class AjaxResponseBody implements Serializable {
    private static final long serialVersionUID = 5624199824493491749L;
    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
