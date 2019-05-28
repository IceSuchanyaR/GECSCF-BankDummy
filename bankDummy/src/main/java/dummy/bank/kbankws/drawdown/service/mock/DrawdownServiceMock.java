package gec.scf.dummy.bank.kbankws.drawdown.service.mock;

import gec.scf.dummy.bank.kbankws.Constants;
import gec.scf.dummy.bank.kbankws.account.domain.AccountBalanceResponse;
import gec.scf.dummy.bank.kbankws.debit.domain.TransactionStatus;
import gec.scf.dummy.bank.kbankws.drawdown.domain.DrawdownRequest;
import gec.scf.dummy.bank.kbankws.drawdown.domain.DrawdownResponse;
import gec.scf.dummy.bank.kbankws.drawdown.service.DrawdownService;
import gec.scf.dummy.bank.kbankws.credit.limit.service.mock.EnquiryCreditLimitServiceMockBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Service
@Profile(Constants.Profiles.MOCK)
public class DrawdownServiceMock implements DrawdownService {

	@Override
	public Optional<DrawdownResponse> createDrawdown(String bankCode, DrawdownRequest drawdownRequest) {
		AccountBalanceResponse sourceAccount = EnquiryCreditLimitServiceMockBuilder.builder()
				.accountNo(drawdownRequest.getSourceAccountNo()).termloan().success().build();
		return Optional
				.of(DrawdownResponseMockBuilder.builder(drawdownRequest).success().withAccount(sourceAccount).build());
	}

	@Override
	public Optional<DrawdownResponse> getDrawdown(String bankCode, DrawdownRequest drawdownRequest) {
		return Optional.of(DrawdownResponseMockBuilder.builder(drawdownRequest).success().build());
	}

	/////////////////////

	private static final class DrawdownResponseMockBuilder {
		private DrawdownRequest drawdownRequest;
		private TransactionStatus transactionStatus;
		private String failureReasonCode;
		private String failureReason;
		private AccountBalanceResponse sourceAccount;

		private DrawdownResponseMockBuilder(DrawdownRequest drawdownRequest) {
			this.drawdownRequest = drawdownRequest;
		}

		public DrawdownResponse build() {
			DrawdownResponse drawdownResponse = new DrawdownResponse();
			drawdownResponse.setTransactionNo(drawdownRequest.getTransactionNo());
			drawdownResponse.setDrawdownTime(simpleDateTimeFormat.format(new Date()));
			drawdownResponse.setTransactionStatus(transactionStatus);
			drawdownResponse.setFailureReason(failureReason);
			drawdownResponse.setFailureReason(failureReasonCode);
			drawdownResponse.setSourceAccount(sourceAccount);
			return drawdownResponse;
		}

		public DrawdownResponseMockBuilder success() {
			this.transactionStatus = TransactionStatus.SUCCESS;
			return this;
		}

		public DrawdownResponseMockBuilder status(TransactionStatus transactionStatus) {
			this.transactionStatus = transactionStatus;
			return this;
		}

		public DrawdownResponseMockBuilder withError(String failureReasonCode) {
			return withError(failureReasonCode, null);
		}

		public DrawdownResponseMockBuilder withError(String failureReasonCode, String failureReason) {
			this.failureReasonCode = failureReasonCode;
			this.failureReason = failureReason;
			return this;
		}

		public DrawdownResponseMockBuilder withAccount(AccountBalanceResponse accountBalanceResponse) {
			this.sourceAccount = accountBalanceResponse;
			return this;
		}

		public static DrawdownResponseMockBuilder builder(DrawdownRequest drawdownRequest) {
			return new DrawdownResponseMockBuilder(drawdownRequest);
		}

		private SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("en"));
	}

}
