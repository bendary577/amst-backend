package accountmanager.supporttool.controller;

import accountmanager.supporttool.dto.AccountStateDTO;
import accountmanager.supporttool.http.request.LoginRequestDTO;
import accountmanager.supporttool.http.response.LoginResponseDTO;
import accountmanager.supporttool.http.response.ResponseMessage;
import accountmanager.supporttool.security.userDetails.UserDetailsImpl;
import accountmanager.supporttool.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final LoginService loginService;

    public AuthenticationController(LoginService loginService) {
        this.loginService = loginService;
    }

    /***
     *
     * @param authenticationRequest
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@RequestBody LoginRequestDTO authenticationRequest) {
        logger.info("AuthenticationController:login:Starting login request for user = " + authenticationRequest.getEmail());
        LoginResponseDTO loginResponse = new LoginResponseDTO();
        try {
            Authentication authentication = loginService.authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetailsObject = (UserDetailsImpl) authentication.getPrincipal();
            UserDetails userDetails = loginService.buildUserDetailsObjectUsingUsername(authenticationRequest.getEmail());
            loginResponse = loginService.buildLoginResponse(userDetailsObject, userDetails);

            logger.info("AuthenticationController:login:successful login request for user = " + authenticationRequest.getEmail());
            return new ResponseEntity<>(loginResponse, new HttpHeaders(), HttpStatus.OK);
        } catch (InsufficientAuthenticationException insufficientAuthenticationException) {
            logger.error("AuthenticationController:login:invalid credentials for user = " + authenticationRequest.getEmail() + " " + insufficientAuthenticationException.getMessage());
            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setMessage("You're blocked .. you can't login to this app");
            responseMessage.setData("");
            return new ResponseEntity<>(responseMessage, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        } catch (BadCredentialsException badCredentialsException) {
            logger.error("AuthenticationController:login:invalid credentials for user = " + authenticationRequest.getEmail() + " " + badCredentialsException.getMessage());
            loginResponse.setMessage(badCredentialsException.getCause().getMessage());
            loginResponse.setData("");
            return new ResponseEntity<>(loginResponse, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        } catch (Exception exception) {
            logger.error("AuthenticationController:login:failed login request for user = " + authenticationRequest.getEmail() + " " + exception.getMessage());
            loginResponse.setMessage(exception.getCause().getMessage());
            loginResponse.setData("");
            return new ResponseEntity<>(loginResponse, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


}