package accountmanager.supporttool.controller;

import accountmanager.supporttool.http.response.ResponseMessage;
import accountmanager.supporttool.model.amstate.AccountState;
import accountmanager.supporttool.model.amstate.AccountStateDTO;
import accountmanager.supporttool.service.AccountStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/accountState")
public class AccountStateController {

    private final AccountStateService accountStateService;

    @Autowired
    public AccountStateController(AccountStateService accountStateService) {
        this.accountStateService = accountStateService;
    }

    @GetMapping("/{officialEmail}/getState")
    public ResponseEntity<ResponseMessage> getAccountState(@PathVariable String officialEmail) {
        List<AccountState> sisAccountStates = this.accountStateService.getSISData(officialEmail);
        List<AccountState> ssoAccountStates = this.accountStateService.getSSOData(officialEmail);
        AccountStateDTO accountStateDTO = this.accountStateService.buildAccountStateDTO(sisAccountStates, ssoAccountStates);
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("account state returned successfully");
        responseMessage.setData(accountStateDTO);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping("/{officialEmail}/fixState")
    public ResponseEntity<ResponseMessage> fixAccountState(@PathVariable String officialEmail) {
        List<AccountState> sisAccountStates = this.accountStateService.getSISData(officialEmail);
        List<AccountState> ssoAccountStates = this.accountStateService.getSSOData(officialEmail);
//        List<AccountState> ssoAccountStates = null;
        AccountStateDTO accountStateDTO = this.accountStateService.buildAccountStateDTO(sisAccountStates, ssoAccountStates);
        accountStateDTO =  this.accountStateService.fixAccountState(accountStateDTO);
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("account state was fixed successfully");
        responseMessage.setData(accountStateDTO);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
