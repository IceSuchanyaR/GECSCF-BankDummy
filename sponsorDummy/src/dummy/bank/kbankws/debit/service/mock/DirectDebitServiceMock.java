package gec.scf.dummy.bank.kbankws.debit.service.mock;

import gec.scf.dummy.bank.kbankws.Constants;
import gec.scf.dummy.bank.kbankws.account.domain.AccountBalanceResponse;
import gec.scf.dummy.bank.kbankws.account.service.mock.AccountBalanceMockResponseBuilder;
import gec.scf.dummy.bank.kbankws.debit.domain.DirectDebitRequest;
import gec.scf.dummy.bank.kbankws.debit.domain.DirectDebitResponse;
import gec.scf.dummy.bank.kbankws.debit.domain.TransactionStatus;
import gec.scf.dummy.bank.kbankws.debit.service.DirectDebitService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Service
@Profile(Constants.Profiles.MOCK)
public class DirectDebitServiceMock implements DirectDebitService {

	@Override
	public Optional<DirectDebitResponse> createDirectDebit(String bankCode, DirectDebitRequest debitRequest) {
		AccountBalanceResponse sourceAccount = AccountBalanceMockResponseBuilder.builder()
				.accountNo(debitRequest.getSourceAccountNo()).overdraft().success().build();
		return Optional
				.of(DirectDebitResponseMockBuilder.builder(debitRequest).success().withAccount(sourceAccount).build());
	}

	@Override
	public Optional<DirectDebitResponse> getDirectDebit(String bankCode,DirectDebitRequest debitRequest) {
		return Optional.of(DirectDebitResponseMockBuilder.builder(debitRequest).success().build());
	}

	/////////////////////

	private static final class DirectDebitResponseMockBuilder {

		private DirectDebitRequest debit;
		private TransactionStatus transactionStatus;
		private String failureReasonCode;
		private String failureReason;
		private AccountBalanceResponse sourceAccount;

		private DirectDebitResponseMockBuilder(DirectDebitRequest debit) {
			this.debit = debit;
		}

		public DirectDebitResponse build() {
			DirectDebitResponse directDebitResponse = new DirectDebitResponse();
			directDebitResponse.setTransactionNo(debit.getTransactionNo());
			directDebitResponse.setDebitTime(simpleDateTimeFormat.format(new Date()));
			directDebitResponse.setTransactionStatus(transactionStatus);
			directDebitResponse.setFailureReason(failureReason);
			directDebitResponse.setFailureReason(failureReasonCode);
			directDebitResponse.setSourceAccount(sourceAccount);
			return directDebitResponse;
		}

		public DirectDebitResponseMockBuilder success() {
			this.transactionStatus = TransactionStatus.SUCCESS;
			return this;
		}

		public DirectDebitResponseMockBuilder status(TransactionStatus transactionStatus) {
			this.transactionStatus = transactionStatus;
			return this;
		}

		public DirectDebitResponseMockBuilder withError(String failureReasonCode) {
			return withError(failureReasonCode, null);
		}

		public DirectDebitResponseMockBuilder withError(String failureReasonCode, String failureReason) {
			this.failureReasonCode = failureReasonCode;
			this.failureReason = failureReason;
			return this;
		}

		public DirectDebitResponseMockBuilder withAccount(AccountBalanceResponse accountBalanceResponse) {
			this.sourceAccount = accountBalanceResponse;
			return this;
		}

		public static DirectDebitResponseMockBuilder builder(DirectDebitRequest debit) {
			return new DirectDebitResponseMockBuilder(debit);
		}

		private SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("en"));
	}
}
