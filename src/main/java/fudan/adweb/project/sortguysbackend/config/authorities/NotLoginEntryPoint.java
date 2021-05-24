package fudan.adweb.project.sortguysbackend.config.authorities;

import fudan.adweb.project.sortguysbackend.controller.request.AjaxResponseBody;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class NotLoginEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException{
        AjaxResponseBody responseBody = new AjaxResponseBody();

        responseBody.setStatus("403");
        responseBody.setMessage("Need login!");

        httpServletResponse.getWriter().write(JSON.toJSONString(responseBody));
    }
}
