package accountmanager.supporttool.controller;

import accountmanager.supporttool.http.response.ResponseMessage;
import accountmanager.supporttool.dto.AccountStateDashboardDTO;
import accountmanager.supporttool.model.amstate.StudentUserUID;
import accountmanager.supporttool.service.AccountStateDashboardService;
import accountmanager.supporttool.util.AsyncDBServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/accountStateDashboard")
public class AccountStateDashboardController {

    private final AsyncDBServiceUtil asyncDBServiceUtil;

    @Autowired
    public AccountStateDashboardController(AsyncDBServiceUtil asyncDBServiceUtil) {
        this.asyncDBServiceUtil = asyncDBServiceUtil;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseMessage> getDashboardInfo() {
        AccountStateDashboardDTO accountStateDashboardDTO = asyncDBServiceUtil.getDashboardDataAsync();
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("account state dashboard returned successfully");
        responseMessage.setData(accountStateDashboardDTO);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }


}
