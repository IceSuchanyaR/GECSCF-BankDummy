package gec.scf.dummy.bank.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tbl_mapping_code_detail")
public class MasterMappingCodeDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mapping_detail_id", nullable = false)
	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	private Long mappingDetailID;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "mapping_id", nullable = false)
	private Long mappingID;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "bankcase_code", nullable = false, length = 100)
	private String bankCaseCode;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "transaction_status", nullable = false, length = 20)
	private String transactionStatus;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "failure_reason_code", nullable = true, length = 255)
	private String failureReasonCode;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "failure_reason", nullable = true, length = 255)
	private String failureReason;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time", nullable = false)
	private Date updateTime;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@ManyToOne(optional = false)
	@JoinColumn(name = "mapping_id", referencedColumnName = "mapping_id", insertable = false, updatable = false)
	private MasterMappingCode masterMappingCode;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private Long itemNo;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private String updateTimeDisplay;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private String bankCaseDisplay;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private String transactionStatusDisplay;

	public MasterMappingCodeDetail() {
		super();
	}

	public MasterMappingCodeDetail(Long mappingID, String bankCaseCode, String transactionStatus,
			String failureReasonCode, String failureReason) {
		super();

		this.mappingID = mappingID;
		this.bankCaseCode = bankCaseCode;
		this.transactionStatus = transactionStatus;
		this.failureReasonCode = failureReasonCode;
		this.failureReason = failureReason;

	}

	public Long getMappingDetailID() {
		return mappingDetailID;
	}

	public void setMappingDetailID(Long mappingDetailID) {
		this.mappingDetailID = mappingDetailID;
	}

	public Long getMappingID() {
		return mappingID;
	}

	public void setMappingID(Long mappingID) {
		this.mappingID = mappingID;
	}

	public String getBankCaseCode() {
		return bankCaseCode;
	}

	public void setBankCaseCode(String bankCaseCode) {
		this.bankCaseCode = bankCaseCode;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getFailureReasonCode() {
		return failureReasonCode;
	}

	public void setFailureReasonCode(String failureReasonCode) {
		this.failureReasonCode = failureReasonCode;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	public MasterMappingCode getMasterMappingCode() {
		return masterMappingCode;
	}

	public void setMasterMappingCode(MasterMappingCode masterMappingCode) {
		this.masterMappingCode = masterMappingCode;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateTimeDisplay() {
		return updateTimeDisplay;
	}

	public void setUpdateTimeDisplay(String updateTimeDisplay) {
		this.updateTimeDisplay = updateTimeDisplay;
	}

	public String getBankCaseDisplay() {
		return bankCaseDisplay;
	}

	public void setBankCaseDisplay(String bankCaseDisplay) {
		this.bankCaseDisplay = bankCaseDisplay;
	}

	public Long getItemNo() {
		return itemNo;
	}

	public void setItemNo(Long itemNo) {
		this.itemNo = itemNo;
	}

	public String getTransactionStatusDisplay() {
		return transactionStatusDisplay;
	}

	public void setTransactionStatusDisplay(String transactionStatusDisplay) {
		this.transactionStatusDisplay = transactionStatusDisplay;
	}

}
