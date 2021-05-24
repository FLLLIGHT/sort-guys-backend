package fudan.adweb.project.sortguysbackend.config.authorities;

import com.alibaba.fastjson.JSON;
import fudan.adweb.project.sortguysbackend.controller.request.AjaxResponseBody;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDenied implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        AjaxResponseBody responseBody = new AjaxResponseBody();

        responseBody.setStatus("403");
        responseBody.setMessage("No authority!");

        httpServletResponse.getWriter().write(JSON.toJSONString(responseBody));
    }
}
