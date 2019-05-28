package gec.scf.dummy.bank.kbankws.drawdown.service;

import gec.scf.dummy.bank.kbankws.drawdown.domain.DrawdownRequest;
import gec.scf.dummy.bank.kbankws.drawdown.domain.DrawdownResponse;

import java.util.Optional;

public interface DrawdownService {

	Optional<DrawdownResponse> createDrawdown(String bankCode, DrawdownRequest drawdownRequest);
	
	Optional<DrawdownResponse> getDrawdown(String bankCode, DrawdownRequest drawdownRequest);
}
