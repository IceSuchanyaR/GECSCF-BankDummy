package gec.scf.dummy.bank.kbankws.credit.limit.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gec.scf.dummy.bank.kbankws.account.domain.AccountBalanceResponse;
import gec.scf.dummy.bank.kbankws.credit.limit.service.EnquiryCreditLimitService;
import gec.scf.dummy.common.annotation.EncryptedPathVariable;

@RestController
public class EnquiryCreditLimitController {

	@Autowired
	EnquiryCreditLimitService enquiryCreditLimitService;

	@GetMapping(path = "/v1/banks/{bankCode}/term-loan-accounts/{accountNo}")
	public ResponseEntity<AccountBalanceResponse> inquiryDebit(@PathVariable String bankCode,
			@PathVariable String accountNo) {
		return ResponseEntity.ok(enquiryCreditLimitService.getCreditLimitAccount(bankCode, accountNo).orElseGet(null));

	}
}
