package gec.scf.dummy.bank.kbankws.debit.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gec.scf.dummy.bank.kbankws.debit.domain.DirectDebitRequest;
import gec.scf.dummy.bank.kbankws.debit.domain.DirectDebitResponse;
import gec.scf.dummy.bank.kbankws.debit.service.DirectDebitService;

@RestController
public class DirectDebitController {

	@Autowired
	DirectDebitService debitService;

	@PostMapping(path = "/v1/banks/{bankCode}/direct-debit-transactions")
	public ResponseEntity<DirectDebitResponse> createDirectDebit(@PathVariable String bankCode,
			@RequestBody DirectDebitRequest debitRequest) {
		return ResponseEntity.ok(debitService.createDirectDebit(bankCode, debitRequest).orElseGet(null));
	}

	@GetMapping(path = "/v1/banks/{bankCode}/direct-debit-transactions/{transactionNo}")
	public ResponseEntity<DirectDebitResponse> getDirectDebit(@PathVariable String bankCode,
			@PathVariable String transactionNo) {
		DirectDebitRequest debit = new DirectDebitRequest();
		debit.setTransactionNo(transactionNo);
		return ResponseEntity.ok(debitService.getDirectDebit(bankCode, debit).orElse(null));
	}
}
