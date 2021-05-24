package fudan.adweb.project.sortguysbackend.security;

import fudan.adweb.project.sortguysbackend.config.authorities.AccessDenied;
import fudan.adweb.project.sortguysbackend.config.authorities.NotLoginEntryPoint;
import fudan.adweb.project.sortguysbackend.security.jwt.JwtRequestFilter;
import fudan.adweb.project.sortguysbackend.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final NotLoginEntryPoint notLoginEntryPoint;
    private final AccessDenied accessDenied;

    @Autowired
    public SecurityConfig(JwtUserDetailsService jwtUserDetailsService, JwtRequestFilter jwtRequestFilter,
                          NotLoginEntryPoint notLoginEntryPoint, AccessDenied accessDenied) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.notLoginEntryPoint = notLoginEntryPoint;
        this.accessDenied = accessDenied;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.jwtUserDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // We dont't need CSRF for this project.
//        http.csrf().ignoringAntMatchers("/**");
        http.httpBasic().authenticationEntryPoint(notLoginEntryPoint).and()
                    .authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/user").permitAll()
                    .antMatchers("/sortResult").hasAuthority("admin")
                    .antMatchers("/**").hasAuthority("player");

        http.csrf().disable();

        http.exceptionHandling().accessDeniedHandler(accessDenied);

        // Here we use JWT(Json Web Token) to authenticate the user.
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/**");
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
