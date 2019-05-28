package gec.scf.dummy.bank.kbankws.debit.service;

import gec.scf.dummy.bank.kbankws.debit.domain.DirectDebitRequest;
import gec.scf.dummy.bank.kbankws.debit.domain.DirectDebitResponse;

import java.util.Optional;

public interface DirectDebitService {

	Optional<DirectDebitResponse> createDirectDebit(String bankCode, DirectDebitRequest debitRequest);

	Optional<DirectDebitResponse> getDirectDebit(String bankCode, DirectDebitRequest debit);

}
