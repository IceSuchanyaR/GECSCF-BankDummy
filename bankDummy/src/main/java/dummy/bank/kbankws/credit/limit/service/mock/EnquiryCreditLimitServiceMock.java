package gec.scf.dummy.bank.kbankws.credit.limit.service.mock;

import gec.scf.dummy.bank.kbankws.Constants;
import gec.scf.dummy.bank.kbankws.account.domain.AccountBalanceResponse;
import gec.scf.dummy.bank.kbankws.credit.limit.service.EnquiryCreditLimitService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Profile(Constants.Profiles.MOCK)
public class EnquiryCreditLimitServiceMock implements EnquiryCreditLimitService {

	@Override
	public Optional<AccountBalanceResponse> getCreditLimitAccount(String bankCode, String accountNo) {
		return Optional.of(EnquiryCreditLimitServiceMockBuilder.builder()
				.accountNo(accountNo).termloan().success().build());
	}

}
