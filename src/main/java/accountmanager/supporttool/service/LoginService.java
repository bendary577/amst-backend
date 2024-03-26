package accountmanager.supporttool.service;

import accountmanager.supporttool.constants.ResponseMessages;
import accountmanager.supporttool.http.response.LoginResponseDTO;
import accountmanager.supporttool.security.jwt.JwtTokenUtil;
import accountmanager.supporttool.security.userDetails.UserDetailsImpl;
import accountmanager.supporttool.security.userDetails.UserDetailsServiceImpl;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public Authentication authenticate(String username, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new InsufficientAuthenticationException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        }
    }

    public UserDetails buildUserDetailsObjectUsingUsername(String username){
        return userDetailsService
                .loadUserByUsername(username);
    }

    public LoginResponseDTO buildLoginResponse(UserDetailsImpl userDetailsObject, UserDetails userDetails){
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority()).toList();

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setEmail(userDetailsObject.getEmail());
        loginResponseDTO.setToken(jwtTokenUtil.generateToken(userDetails));
        loginResponseDTO.setRoles(roles);
        loginResponseDTO.setMessage(ResponseMessages.SUCCESSFUL_LOGIN);
        loginResponseDTO.setData("");
        return loginResponseDTO;
    }

}
