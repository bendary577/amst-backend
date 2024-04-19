package accountmanager.supporttool.controller;

import accountmanager.supporttool.dto.RoleDTO;
import accountmanager.supporttool.http.response.ResponseMessage;
import accountmanager.supporttool.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    private final RoleService roleService;
    private final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseMessage> getRoles() {
        List<RoleDTO> roles = this.roleService.getAllRoles();
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setData(roles);
        if(roles.isEmpty()){
            responseMessage.setMessage("no roles are currently found in the system");
        }else{
            responseMessage.setMessage("system roles returned successfully");
        }
        return new ResponseEntity<>(responseMessage, new HttpHeaders(), HttpStatus.OK);
    }


}
