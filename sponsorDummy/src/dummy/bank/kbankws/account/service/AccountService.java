package gec.scf.dummy.bank.kbankws.account.service;

import gec.scf.dummy.bank.kbankws.account.domain.AccountBalanceResponse;

import java.util.Optional;

public interface AccountService {

	Optional<AccountBalanceResponse> getAccountBalanceByAccountNo(String bankCode, String accountNo);
}
