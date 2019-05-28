package gec.scf.dummy.bank.kbankws.drawdown.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gec.scf.dummy.bank.kbankws.drawdown.domain.DrawdownRequest;
import gec.scf.dummy.bank.kbankws.drawdown.domain.DrawdownResponse;
import gec.scf.dummy.bank.kbankws.drawdown.service.DrawdownService;

@RestController
@PreAuthorize("hasAnyAuthority('MANAGE_SPONSOR','MANAGE_ALL')")
public class DrawdownController {

	@Autowired
	DrawdownService drawdownService;

	@PostMapping(path = "/v1/banks/{bankCode}/drawdown-transactions")
	public ResponseEntity<DrawdownResponse> createDrawdown(@PathVariable String bankCode,
			@RequestBody DrawdownRequest drawdownRequest) {
		return ResponseEntity.ok(drawdownService.createDrawdown(bankCode, drawdownRequest).orElse(null));
	}

	@GetMapping(path = "/v1/banks/{bankCode}/drawdown-transactions/{transactionNo}")
	public ResponseEntity<DrawdownResponse> getDrawdown(@PathVariable String bankCode,
			@PathVariable String transactionNo) {
		DrawdownRequest drawdownRequest = new DrawdownRequest();
		drawdownRequest.setTransactionNo(transactionNo);
		return ResponseEntity.ok(drawdownService.getDrawdown(bankCode, drawdownRequest).orElse(null));
	}

}
