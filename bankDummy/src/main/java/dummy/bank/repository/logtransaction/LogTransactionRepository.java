package gec.scf.dummy.bank.repository.logtransaction;

import java.util.List;

import gec.scf.dummy.bank.entity.LogTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface LogTransactionRepository extends JpaRepository<LogTransaction, Long> {

	@Query("select u from LogTransaction u order by u.updateTime DESC ")
	public List<LogTransaction> findLogAll();

	@Query("select u from LogTransaction u where  u.logType = ?1 order by u.updateTime DESC ")
	public List<LogTransaction> findLogTransactionByLogTypeOrderByUpdateTimeDESC(String logType);

	@Query("select u from LogTransaction u where  u.sourceAccountNo  like %?1% order by u.updateTime DESC ")
	public List<LogTransaction> findLogTransactionBySourceAccountNoOrderByUpdateTimeDESC(String sourceAccountNo);

	@Query("select u from LogTransaction u where  u.transactionStatus  = ?1 order by u.updateTime DESC ")
	public List<LogTransaction> findLogTransactionByTransactionStatusOrderByUpdateTimeDESC(String transactionStatus);

	@Query("select u from LogTransaction u where  u.bankTransactionNo  like %?1% order by u.updateTime DESC ")
	public List<LogTransaction> findLogTransactionByBankTransactionNoOrderByUpdateTimeDESC(String bankTransactionNo);

	@Query("select u from LogTransaction u where  u.logType  = ?1 AND u.sourceAccountNo like %?2% order by u.updateTime DESC ")
	public List<LogTransaction> findLogTransactionByLogTypeAndSourceAccountNoOrderByUpdateTimeDESC(String logType,
																								   String sourceAccountNo);

	@Query("select u from LogTransaction u where  u.logType  = ?1 AND u.transactionStatus = ?2 order by u.updateTime DESC ")
	public List<LogTransaction> findLogTransactionByLogTypeAndTransactionStatusOrderByUpdateTimeDESC(String logType,
																									 String transactionStatus);

	@Query("select u from LogTransaction u where  u.logType  = ?1 AND u.bankTransactionNo like %?2% order by u.updateTime DESC ")
	public List<LogTransaction> findLogTransactionByLogTypeAndBankTransactionNoOrderByUpdateTimeDESC(String logType,
																									 String bankTransactionNo);

	@Query("select u from LogTransaction u where  u.sourceAccountNo  like %?1% AND u.transactionStatus = ?2 order by u.updateTime DESC ")
	public List<LogTransaction> findLogTransactionBySourceAccountNoAndTransactionStatusOrderByUpdateTimeDESC(
			String sourceAccountNo, String transactionStatus);

	@Query("select u from LogTransaction u where  u.sourceAccountNo  like %?1% AND u.bankTransactionNo like %?2% order by u.updateTime DESC ")
	public List<LogTransaction> findLogTransactionBySourceAccountNoAndBankTransactionNoOrderByUpdateTimeDESC(
			String sourceAccountNo, String bankTransactionNo);

	@Query("select u from LogTransaction u where  u.transactionStatus  = ?1 AND u.bankTransactionNo like %?2% order by u.updateTime DESC ")
	public List<LogTransaction> findLogTransactionByTransactionStatusAndBankTransactionNoOrderByUpdateTimeDESC(
			String transactionStatus, String bankTransactionNo);

	@Query("select u from LogTransaction u where  u.logType  = ?1 AND u.sourceAccountNo like %?2% AND u.transactionStatus = ?3 order by u.updateTime DESC ")
	public List<LogTransaction> findLogTransactionByLogTypeAndSourceAccountNoAndTransactionStatusOrderByUpdateTimeDESC(
			String logType, String sourceAccountNo, String transactionStatus);

	@Query("select u from LogTransaction u where  u.logType  = ?1 AND u.sourceAccountNo like %?2% AND u.bankTransactionNo like %?3% order by u.updateTime DESC ")
	public List<LogTransaction> findLogTransactionByLogTypeAndSourceAccountNoAndBankTransactionNoOrderByUpdateTimeDESC(
			String logType, String sourceAccountNo, String bankTransactionNo);

	@Query("select u from LogTransaction u where  u.logType  = ?1 AND u.bankTransactionNo like %?2% AND u.transactionStatus = ?3 order by u.updateTime DESC ")
	public List<LogTransaction> findLogTransactionByLogTypeAndBankTransactionNoAndTransactionStatusOrderByUpdateTimeDESC(
			String logType, String bankTransactionNo, String transactionStatus);

	@Query("select u from LogTransaction u where  u.sourceAccountNo  like %?1% AND u.transactionStatus = ?2 AND u.bankTransactionNo like %?3% order by u.updateTime DESC ")
	public List<LogTransaction> findLogTransactionBySourceAccountNoAndTransactionStatusAndBankTransactionNoOrderByUpdateTimeDESC(
			String sourceAccountNo, String transactionStatus, String bankTransactionNo);

	@Query("select u from LogTransaction u where  u.logType  = ?1 AND u.sourceAccountNo like %?2% AND u.transactionStatus = ?3 AND u.bankTransactionNo  like %?4% order by u.updateTime DESC ")
	public List<LogTransaction> findLogTransactionAllUpdateTimeDESC(String logType, String sourceAccountNo,
																	String transactionStatus, String bankTransactionNo);

	@Query("select u from LogTransaction u where  u.logType  = ?1 AND u.sourceAccountNo like %?2% AND u.transactionStatus = ?3  order by u.updateTime DESC ")
	public List<LogTransaction> findBylogTypesourceAccounttransactionStatusNoUpdateTimeDESC(String logType,
																							String sourceAccountNo, String transactionStatus);

	public LogTransaction findLogTransactionBylogID(Long logID);
	
	@Query("select u from LogTransaction u where u.transactionNo = ?1 AND u.logType = ?2 ") 
	public LogTransaction findLogTransactionByTransactionNo(String transactionNo,String logType); 

	@Query("select u from LogTransaction u where u.transactionNo = ?1 AND u.logType = ?2 ") 
	public LogTransaction findLogTransactionByTransactionNoAndBankCode(String transactionNo , String logType);
}
