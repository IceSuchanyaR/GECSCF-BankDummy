package gec.scf.dummy.bank.kbankws.credit.limit.service;

import gec.scf.dummy.bank.kbankws.account.domain.AccountBalanceResponse;

import java.util.Optional;

public interface EnquiryCreditLimitService {

	Optional<AccountBalanceResponse> getCreditLimitAccount(String bankCode, String accountNo);
}
