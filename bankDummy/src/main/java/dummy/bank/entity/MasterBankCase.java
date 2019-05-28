

package dummy.bank.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tbl_bank_case")
public class MasterBankCase extends BaseBankEntity {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "bankcase_code", nullable = false, length = 100)
	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	private String bankCaseCode;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "bankcase_group", nullable = false, length = 50)
	private String bankCaseGroup;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "bankcase_name", nullable = true, length = 255)
	private String bankCaseName;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "default_reason_code", nullable = true, length = 255)
	private String defaultReasonCode;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "bankcase_status", nullable = true, length = 20)
	private String bankCaseStatus;

	public String getBankCaseCode() {
		return bankCaseCode;
	}

	public void setBankCaseCode(String bankCaseCode) {
		this.bankCaseCode = bankCaseCode;
	}

	public String getBankCaseGroup() {
		return bankCaseGroup;
	}

	public void setBankCaseGroup(String bankCaseGroup) {
		this.bankCaseGroup = bankCaseGroup;
	}

	public String getBankCaseName() {
		return bankCaseName;
	}

	public void setBankCaseName(String bankCaseName) {
		this.bankCaseName = bankCaseName;
	}
	
	public String getDefaultReasonCode() {
		return defaultReasonCode;
	}

	public void setDefaultReasonCode(String defaultReasonCode) {
		this.defaultReasonCode = defaultReasonCode;
	}

	public String getBankCaseStatus() {
		return bankCaseStatus;
	}

	public void setBankCaseStatus(String bankCaseStatus) {
		this.bankCaseStatus = bankCaseStatus;
	}

}
