package gec.scf.dummy.bank.service.logtransaction;

import gec.scf.dummy.bank.entity.ConfigApplication;
import gec.scf.dummy.bank.entity.LogTransaction;

import java.util.List;


public interface LogtransactionService {
	
	public LogTransaction findLogTransactionBylogID(Long logID) throws Exception;

	public List<ConfigApplication> getAllLogType();

	public List<LogTransaction> getAllLogtransaction(String logType, String bankTransactionNo, String sourceAccountNo,
                                                     String transactionStatus, Long itemNo) throws Exception;

	public Long getTotalLogtransaction(String logType, String bankTransactionNo, String sourceAccountNo,
                                       String transactionStatus, Long itemNo) throws Exception;
}
