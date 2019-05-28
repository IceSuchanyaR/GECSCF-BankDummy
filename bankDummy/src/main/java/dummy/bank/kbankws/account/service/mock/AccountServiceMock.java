package gec.scf.dummy.bank.kbankws.account.service.mock;

import gec.scf.dummy.bank.kbankws.Constants;
import gec.scf.dummy.bank.kbankws.account.domain.AccountBalanceResponse;
import gec.scf.dummy.bank.kbankws.account.service.AccountService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Profile(Constants.Profiles.MOCK)
public class AccountServiceMock implements AccountService {

	@Override
	public Optional<AccountBalanceResponse> getAccountBalanceByAccountNo(String bankCode, String accountNo) {
		return Optional
				.of(AccountBalanceMockResponseBuilder.builder().accountNo(accountNo).overdraft().success().build());
	}
}
