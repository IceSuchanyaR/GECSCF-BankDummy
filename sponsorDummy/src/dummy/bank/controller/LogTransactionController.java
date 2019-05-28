package dummy.bank.controller;

import com.fasterxml.jackson.annotation.JsonView;
import gec.scf.dummy.bank.entity.ConfigApplication;
import gec.scf.dummy.bank.entity.LogTransaction;
import gec.scf.dummy.bank.entity.BankManageEvent;
import gec.scf.dummy.bank.service.logtransaction.LogtransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("hasAnyAuthority('MANAGE_BANK','MANAGE_ALL')")
@RequestMapping(value = "/logTransaction")
public class LogTransactionController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private LogtransactionService logtransactionService ;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@RequestMapping(value = "/logTransactionList", method = RequestMethod.GET)
	public List<LogTransaction> getAllLogtransaction(
			@RequestParam(value = "logType", required = false) String logType,
			@RequestParam(value = "bankTransactionNo", required = false) String bankTransactionNo,
			@RequestParam(value = "sourceAccountNo", required = false) String sourceAccountNo,
			@RequestParam(value = "transactionStatus", required = false) String transactionStatus,
			@RequestParam(value = "itemNo", required = false) Long itemNo) {
		List<LogTransaction> results = null;
		try {
			results = logtransactionService.getAllLogtransaction(logType, bankTransactionNo,sourceAccountNo,transactionStatus, itemNo);
		} catch (Exception e) {
			results = new ArrayList<>();
		}
		return results;
	}

	@RequestMapping(value = "/totalLogtransaction", method = RequestMethod.GET)
	public Long getTotalLogtransaction(@RequestParam(value = "logType", required = false) String logType,
			@RequestParam(value = "bankTransactionNo", required = false) String bankTransactionNo,
			@RequestParam(value = "sourceAccountNo", required = false) String sourceAccountNo,
			@RequestParam(value = "transactionStatus", required = false) String transactionStatus,@RequestParam(value = "itemNo", required = false) Long itemNo) throws Exception {
		return logtransactionService.getTotalLogtransaction(logType, bankTransactionNo,sourceAccountNo,transactionStatus,itemNo);
	}

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@RequestMapping(value = "/logTransactionView", method = RequestMethod.GET)
	public LogTransaction getlogTransactionBylogID(
			@RequestParam(value = "logID", required = false) Long logID) {
		LogTransaction result = null;
		try {
			result = logtransactionService.findLogTransactionBylogID(logID);
		} catch (Exception e) {
			result = new LogTransaction();
		}
		return result;
	}

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@RequestMapping(value = "/getLogType", method = RequestMethod.GET)
	public List<ConfigApplication> getAllLogType() throws Exception {
		List<ConfigApplication> results = null;

		try {
			results = logtransactionService.getAllLogType();

		} catch (Exception e) {
			results = new ArrayList<>();
		}
		return results;
	}
}
