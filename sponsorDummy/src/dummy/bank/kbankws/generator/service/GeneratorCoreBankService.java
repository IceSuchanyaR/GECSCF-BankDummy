package gec.scf.dummy.bank.kbankws.generator.service;

import gec.scf.dummy.bank.kbankws.enums.ServiceType;

public interface GeneratorCoreBankService {

	public String generateBankTransactionNo(ServiceType serviceType) throws Exception;

}

