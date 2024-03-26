package accountmanager.supporttool.controller;

import accountmanager.supporttool.http.response.ResponseMessage;
import accountmanager.supporttool.dto.AccountStateDashboardDTO;
import accountmanager.supporttool.service.AccountStateDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/accountStateDashboard")
public class AccountStateDashboardController {

    private final AccountStateDashboardService accountStateDashboardService;

    @Autowired
    public AccountStateDashboardController(AccountStateDashboardService accountStateDashboardService) {
        this.accountStateDashboardService = accountStateDashboardService;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseMessage> getDashboardInfo() {
        AccountStateDashboardDTO accountStateDashboard = this.accountStateDashboardService.getSISDashboardData();
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("account state dashboard returned successfully");
        responseMessage.setData(accountStateDashboard);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }


}
