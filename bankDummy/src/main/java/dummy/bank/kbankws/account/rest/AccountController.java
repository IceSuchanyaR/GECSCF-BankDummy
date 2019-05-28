package gec.scf.dummy.bank.kbankws.account.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gec.scf.dummy.bank.kbankws.account.domain.AccountBalanceResponse;
import gec.scf.dummy.bank.kbankws.account.service.AccountService;
import gec.scf.dummy.common.annotation.EncryptedPathVariable;

@RestController
public class AccountController {

	@Autowired
	AccountService accountService;

	@GetMapping(path = "/v1/banks/{bankCode}/direct-debit-accounts/{accountNo}")
	public ResponseEntity<AccountBalanceResponse> inquiryDebit(@PathVariable String bankCode,
			@PathVariable String accountNo) {
		return ResponseEntity.ok(accountService.getAccountBalanceByAccountNo(bankCode, accountNo).orElse(null));
	}
}
