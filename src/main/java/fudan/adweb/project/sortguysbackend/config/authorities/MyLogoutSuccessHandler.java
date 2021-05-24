package fudan.adweb.project.sortguysbackend.config.authorities;

import com.alibaba.fastjson.JSON;
import fudan.adweb.project.sortguysbackend.controller.request.AjaxResponseBody;
import fudan.adweb.project.sortguysbackend.entity.User;
import fudan.adweb.project.sortguysbackend.entity.UserLoginInfo;
import fudan.adweb.project.sortguysbackend.mapper.UserLoginInfoMapper;
import fudan.adweb.project.sortguysbackend.mapper.UserMapper;
import fudan.adweb.project.sortguysbackend.security.jwt.JwtTokenUtil;
import fudan.adweb.project.sortguysbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    private final UserLoginInfoMapper userLoginInfoMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;

    @Autowired
    public MyLogoutSuccessHandler(UserLoginInfoMapper userLoginInfoMapper, JwtTokenUtil jwtTokenUtil,
                                  UserMapper userMapper) {
        this.userLoginInfoMapper = userLoginInfoMapper;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userMapper = userMapper;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        AjaxResponseBody responseBody = new AjaxResponseBody();
        String requestTokenHeader = httpServletRequest.getHeader("Authorization");
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            Integer uid = userMapper.getUidByUsername(username);
            if (uid != null){
                UserLoginInfo userLoginInfo = userLoginInfoMapper.findByUid(uid);
                if (userLoginInfo != null){
                    userLoginInfoMapper.deleteByUid(uid);
                    responseBody.setStatus("200");
                    responseBody.setMessage("success");
                    httpServletResponse.getWriter().write(JSON.toJSONString(responseBody));
                    return;
                }
                userLoginInfoMapper.deleteByUid(uid);
                responseBody.setStatus("403");
                responseBody.setMessage("not online");
                httpServletResponse.getWriter().write(JSON.toJSONString(responseBody));
                return;
            }
        }

        responseBody.setStatus("403");
        responseBody.setMessage("log out failed");

        httpServletResponse.getWriter().write(JSON.toJSONString(responseBody));
    }
}
