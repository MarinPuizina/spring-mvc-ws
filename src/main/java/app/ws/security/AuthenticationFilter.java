package app.ws.security;

import app.ws.ui.model.request.UserLoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * When our web service receives a request to authenticate the user, Spring framework will be used to authenticate
     * user with username and password which he has provided. And this method is triggered.
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {

            // We are creating UserLoginRequestModel from request JSON payload
            UserLoginRequestModel cred = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequestModel.class);

            // authenticationManager is used to authenticate the user.
            // It gets user name and password from UserLoginRequestModel cred in which we have stored that information from the request
            // Spring framework will do all the work, it will automatically look up the user from our database,
            // using the method which we have overrided in UserServiceImpl "loadUserByUsername".
            // If the data is valid "successfulAuthentication" method will be triggered
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            cred.getEmail(),
                            cred.getPassword(),
                            new ArrayList<>()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Triggered ONLY if we have successfully authenticated the user.
     *
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String userName = ((User)authResult.getPrincipal()).getUsername(); // getting username from authentication object

        // Generating token using JSON Web Token (check pom.xml for dependency info)
        String token = Jwts.builder()
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
                .compact();

        // Token is included in the header, and client that receives this response will need to extract it and store it.
        // For example, if it is an IOS mobile application it will most likely be stored in IOS key chain.
        // Then, every time that application wants to communicate it needs to include this token in header.
        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
    }

}
