package gec.scf.dummy.sponsor.ptt.document.status.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gec.scf.dummy.common.utils.ErrorMessages;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "tbl_update_documentStatus")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateDocumentStatus {

    @Id
    @GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Size(max = 64, message = ErrorMessages.LENGTH_NOT_OVER)
    @Column(length = 64, nullable = false)
    private String id;

    @NotNull(message = ErrorMessages.FIELD_IS_REQUIRED)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private Date requestTime;

    @NotNull(message = ErrorMessages.FIELD_IS_REQUIRED)
    @Size(max = 10, message = ErrorMessages.LENGTH_NOT_OVER)
    @Column(length = 10, nullable = false)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private String documentNumber;

    @NotNull(message = ErrorMessages.FIELD_IS_REQUIRED)
    @Size(max = 10, message = ErrorMessages.LENGTH_NOT_OVER)
    @Column(length = 10, nullable = false)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private String documentVersion;

    @Size(max = 4, message = ErrorMessages.LENGTH_NOT_OVER)
    @Column(length = 4, nullable = true)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private String fiscalYear;

    @NotNull(message = ErrorMessages.FIELD_IS_REQUIRED)
    @Size(max = 5, message = ErrorMessages.LENGTH_NOT_OVER)
    @Column(length = 5, nullable = false)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private String documentType;

    @NotNull(message = ErrorMessages.FIELD_IS_REQUIRED)
    @Size(max = 20, message = ErrorMessages.LENGTH_NOT_OVER)
    @Column(length = 20, nullable = false)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private String salesType;

    @Size(max = 20, message = ErrorMessages.LENGTH_NOT_OVER)
    @Column(length = 20, nullable = true)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private String productGroup;

    @NotNull(message = ErrorMessages.FIELD_IS_REQUIRED)
    @Size(max = 8, message = ErrorMessages.LENGTH_NOT_OVER)
    @Column(length = 8, nullable = false)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private String customerCode;

    @NotNull(message = ErrorMessages.FIELD_IS_REQUIRED)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private Date documentDate;

    @NotNull(message = ErrorMessages.FIELD_IS_REQUIRED)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private Date documentDueDate;

    @Size(max = 35, message = ErrorMessages.LENGTH_NOT_OVER)
    @Column(length = 35, nullable = true)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private String poNumber;

    @Size(max = 20, message = ErrorMessages.LENGTH_NOT_OVER)
    @Column(length = 20, nullable = true)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private String groupNumber;

    @NotNull(message = ErrorMessages.FIELD_IS_REQUIRED)
    @Column(scale = 19, precision = 2, nullable = false)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private BigDecimal documentAmount;

    @Column(scale = 19, precision = 2, nullable = true)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private BigDecimal withholdingTax;

    @NotNull(message = ErrorMessages.FIELD_IS_REQUIRED)
    @Column(scale = 19, precision = 2, nullable = false)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private BigDecimal outstandingAmount;

    @Size(max = 3, message = ErrorMessages.LENGTH_NOT_OVER)
    @Column(length = 3, nullable = false)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private String currencyCode;

    @Size(max = 10, message = ErrorMessages.LENGTH_NOT_OVER)
    @Column(length = 10, nullable = true)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private String referenceNumber;

    @Size(max = 10, message = ErrorMessages.LENGTH_NOT_OVER)
    @Column(length = 10, nullable = true)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private String documentStatus;

    @Size(max = 6, message = ErrorMessages.LENGTH_NOT_OVER)
    @Column(length = 6, nullable = true)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private String failureReasonCode;

    @Size(max = 255, message = ErrorMessages.LENGTH_NOT_OVER)
    @Column(length = 255, nullable = true)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private String failureReason;

    @Size(max = 20, message = ErrorMessages.LENGTH_NOT_OVER)
    @Column(length = 20, nullable = true)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private String transactionNumber;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private Date debitDate;

    @Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private Date debitTime;

    @Column(scale = 19, precision = 2, nullable = true)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private BigDecimal paymentAmount;

    @Column(scale = 19, precision = 2, nullable = true)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private BigDecimal sponsorBankAccount;

    @Size(max = 20, message = ErrorMessages.LENGTH_NOT_OVER)
    @Column(length = 20, nullable = true)
    @JsonView({ UpdateDocumentStatuses.View.ListForAll.class })
    private String bankTransactionNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentVersion() {
        return documentVersion;
    }

    public void setDocumentVersion(String documentVersion) {
        this.documentVersion = documentVersion;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getSalesType() {
        return salesType;
    }

    public void setSalesType(String salesType) {
        this.salesType = salesType;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public Date getDocumentDueDate() {
        return documentDueDate;
    }

    public void setDocumentDueDate(Date documentDueDate) {
        this.documentDueDate = documentDueDate;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public BigDecimal getDocumentAmoun() {
        return documentAmount;
    }

    public void setDocumentAmoun(BigDecimal documentAmoun) {
        this.documentAmount = documentAmoun;
    }

    public BigDecimal getWithholdingTax() {
        return withholdingTax;
    }

    public void setWithholdingTax(BigDecimal withholdingTax) {
        this.withholdingTax = withholdingTax;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
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

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public Date getDebitDate() {
        return debitDate;
    }

    public void setDebitDate(Date debitDate) {
        this.debitDate = debitDate;
    }

    public Date getDebitTime() {
        return debitTime;
    }

    public void setDebitTime(Date debitTime) {
        this.debitTime = debitTime;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getSponsorBankAccount() {
        return sponsorBankAccount;
    }

    public void setSponsorBankAccount(BigDecimal sponsorBankAccount) {
        this.sponsorBankAccount = sponsorBankAccount;
    }

    public String getBankTransactionNumber() {
        return bankTransactionNumber;
    }

    public void setBankTransactionNumber(String bankTransactionNumber) {
        this.bankTransactionNumber = bankTransactionNumber;
    }

}
