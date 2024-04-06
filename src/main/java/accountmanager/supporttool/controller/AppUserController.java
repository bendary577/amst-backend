package accountmanager.supporttool.controller;

import accountmanager.supporttool.dto.AppUserDTO;
import accountmanager.supporttool.http.request.NewAppUserRequestDTO;
import accountmanager.supporttool.http.response.PageableResponse;
import accountmanager.supporttool.http.response.ResponseMessage;
import accountmanager.supporttool.security.userDetails.UserDetailsImpl;
import accountmanager.supporttool.service.AppUserService;
import accountmanager.supporttool.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import javax.management.relation.RoleNotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
public class AppUserController {

    private final AppUserService appUserService;
    private Logger logger = LoggerFactory.getLogger(AppUserController.class);
    private final LoginService loginService;

    @Autowired
    public AppUserController(AppUserService appUserService, LoginService loginService) {
        this.appUserService = appUserService;
        this.loginService = loginService;
    }

    @GetMapping("/")
    public ResponseEntity<PageableResponse> getUsers(@RequestParam(defaultValue = "0") int pageNumber,
                                                     @RequestParam(defaultValue = "10") int pageSize,
                                                     @RequestParam(defaultValue = "id") String sortBy) {
        PageableResponse<AppUserDTO> userDTOPageableResponse;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetailsObject = (UserDetailsImpl) authentication.getPrincipal();
        try {
            userDTOPageableResponse = appUserService.getAllUsers(pageNumber, pageSize, sortBy);
            return new ResponseEntity<>(userDTOPageableResponse, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception exception) {
            userDTOPageableResponse = new PageableResponse<>();
            userDTOPageableResponse.setMessage("unknown error");
            return new ResponseEntity<>(userDTOPageableResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> registerNewAppUser(@RequestBody NewAppUserRequestDTO newAppUserRequestDTO) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            AppUserDTO appUserDTO = appUserService.addUser(newAppUserRequestDTO);
            responseMessage.setMessage("User was added successfully");
            responseMessage.setData(appUserDTO);
            return new ResponseEntity<>(responseMessage, new HttpHeaders(), HttpStatus.OK);
        }catch(RoleNotFoundException roleNotFoundException){
            responseMessage.setMessage("the role requested is not found .. please choose a suitable user role");
            return new ResponseEntity<>(responseMessage, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception exception) {
            responseMessage.setMessage("unknown error .. please try again");
            return new ResponseEntity<>(responseMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{email}/block")
    public ResponseEntity<ResponseMessage> blockUser(@PathVariable String email) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            AppUserDTO appUserDTO = appUserService.blockUser(email);
            responseMessage.setMessage("User was blocked successfully");
            responseMessage.setData(appUserDTO);
            return new ResponseEntity<>(responseMessage, new HttpHeaders(), HttpStatus.OK);
        }catch(UsernameNotFoundException usernameNotFoundException){
            responseMessage.setMessage("the required user is not found");
            return new ResponseEntity<>(responseMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            responseMessage.setMessage("unknown error .. please try again");
            return new ResponseEntity<>(responseMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
