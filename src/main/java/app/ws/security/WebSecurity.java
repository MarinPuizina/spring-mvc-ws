package app.ws.security;

import app.ws.service.UserService;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
                .permitAll()
                .antMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL)
                .permitAll()
                .antMatchers(SecurityConstants.H2_CONSOLE)
                .permitAll()
                .anyRequest().authenticated()
                .and().addFilter(authenticationFilter())
                .addFilter(new AuthorizationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Disabling the frame options http header which prevents browser to load page in html tags
        // Adding it only for H2 purposes, however since this project isn't web site it's fine if we leave this
        http.headers().frameOptions().disable();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    /**
     * Used for customizing login URL
     *
     * @return
     * @throws Exception
     */
    public AuthenticationFilter authenticationFilter() throws Exception {

        final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());

        filter.setFilterProcessesUrl("/users/login");

        return filter;
    }

}
