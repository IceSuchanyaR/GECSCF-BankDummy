package gec.scf.dummy.bank.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tbl_mapping_code")
public class MasterMappingCode extends BaseBankEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mapping_id", nullable = false)
	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	private Long mappingID;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "bank_code", nullable = false, length = 20)
	private String bankCode;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "service_type", nullable = false, length = 50)
	private String serviceType;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Column(name = "delay", nullable = false)
	private int delay;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@JsonIgnoreProperties(allowSetters = true, value = { "masterMappingCode" })
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "masterMappingCode", cascade = CascadeType.ALL)
	private List<MasterMappingCodeDetail> masterMappingCodeDetail;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private Long itemNo;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private String serviceTypeDisplay;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private String updateTimeDisplay;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private String createTimeDisplay;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private boolean editMode;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private String errorMessage;

	@JsonView({BankManageEvent.View.DetailForManageBank.class})
	@Transient
	private String errorMessageStatus;

	public Long getMappingID() {
		return mappingID;
	}

	public void setMappingID(Long mappingID) {
		this.mappingID = mappingID;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public Long getItemNo() {
		return itemNo;
	}

	public void setItemNo(Long itemNo) {
		this.itemNo = itemNo;
	}

	public List<MasterMappingCodeDetail> getMasterMappingCodeDetail() {
		return masterMappingCodeDetail;
	}

	public void setMasterMappingCodeDetail(List<MasterMappingCodeDetail> masterMappingCodeDetail) {
		this.masterMappingCodeDetail = masterMappingCodeDetail;
	}

	public String getServiceTypeDisplay() {
		return serviceTypeDisplay;
	}

	public void setServiceTypeDisplay(String serviceTypeDisplay) {
		this.serviceTypeDisplay = serviceTypeDisplay;
	}

	public String getUpdateTimeDisplay() {
		return updateTimeDisplay;
	}

	public void setUpdateTimeDisplay(String updateTimeDisplay) {
		this.updateTimeDisplay = updateTimeDisplay;
	}

	public String getCreateTimeDisplay() {
		return createTimeDisplay;
	}

	public void setCreateTimeDisplay(String createTimeDisplay) {
		this.createTimeDisplay = createTimeDisplay;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessageStatus() {
		return errorMessageStatus;
	}

	public void setErrorMessageStatus(String errorMessageStatus) {
		this.errorMessageStatus = errorMessageStatus;
	}

}