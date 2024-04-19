package accountmanager.supporttool.controller;

import accountmanager.supporttool.http.response.ResponseMessage;
import accountmanager.supporttool.model.amstate.AccountState;
import accountmanager.supporttool.dto.AccountStateDTO;
import accountmanager.supporttool.service.AccountStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Set;

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
        List<AccountState> sisAccountStates = this.accountStateService.getAccountSISData(officialEmail);
        List<AccountState> ssoAccountStates = this.accountStateService.getAccountSSOData(officialEmail);
        AccountStateDTO accountStateDTO = this.accountStateService.buildAccountStateDTO(sisAccountStates, ssoAccountStates);
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("account state returned successfully");
        responseMessage.setData(accountStateDTO);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping("/{officialEmail}/fixState")
    public ResponseEntity<ResponseMessage> fixAccountState(@PathVariable String officialEmail) {
        List<AccountState> sisAccountStates = this.accountStateService.getAccountSISData(officialEmail);
        List<AccountState> ssoAccountStates = this.accountStateService.getAccountSSOData(officialEmail);
        AccountStateDTO accountStateDTO = this.accountStateService.buildAccountStateDTO(sisAccountStates, ssoAccountStates);
        accountStateDTO =  this.accountStateService.fixAccountState(accountStateDTO);
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("account state was fixed successfully");
        responseMessage.setData(accountStateDTO);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    //TODO : needs refactoring to move the processing to service class .. and to organize the fix operation in one place
    @PostMapping("/fixStateCSV")
    public ResponseEntity<ResponseMessage> fixAccountStateFromCSV(@RequestParam("file") MultipartFile file) {
        Set<String> emails = this.accountStateService.parseAccountsCSVFile(file);
        for(String officialEmail : emails){
            List<AccountState> sisAccountStates = this.accountStateService.getAccountSISData(officialEmail);
            List<AccountState> ssoAccountStates = this.accountStateService.getAccountSSOData(officialEmail);
            AccountStateDTO accountStateDTO = this.accountStateService.buildAccountStateDTO(sisAccountStates, ssoAccountStates);
            this.accountStateService.fixAccountState(accountStateDTO);
        }
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("processing emails file fixing operation is done");
        responseMessage.setData("");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
