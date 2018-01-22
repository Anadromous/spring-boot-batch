/**
 * 
 */
package net.petrikainulainen.springbatch.student;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author or0189783
 *
 */
@XmlRootElement(name="raw_data")
public class RawBankCheckingData {

	private String transactionId; //Transaction ID
	private Date postingDate; //Posting Date	
	private Date effectiveDate; //Effective Date
	private String transactionType;//Transaction Type	
	private Double amount; //Amount	
	private Long checkNumber; //Check_Number
	private String referenceNumber; //Reference Number	
	private String description; //Description							
	private String transactionCategory; //Transaction Category		
	private String type; //Type	
	private Double balance; //Balance	
	
	
	public RawBankCheckingData() {
		// TODO Auto-generated constructor stub
	}
	
	public RawBankCheckingData(String transactionId, Date postingDate, Date effectiveDate, String transactionType, Double amount,
			Long checkNumber, String referenceNumber, String description, String transactionCategory, String type, Double balance) {
		this.transactionId = transactionId;
		this.postingDate = postingDate;
		this.effectiveDate=effectiveDate;
		this.transactionType = transactionType;
		this.amount = amount;
		this.checkNumber = checkNumber;
		this.referenceNumber = referenceNumber;
		this.description=description;
		this.transactionCategory = transactionCategory;
		this.type = type;
		this.balance = balance;
	}


	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Date getPostingDate() {
		return postingDate;
	}

	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(Long checkNumber) {
		this.checkNumber = checkNumber;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTransactionCategory() {
		return transactionCategory;
	}

	public void setTransactionCategory(String transactionCategory) {
		this.transactionCategory = transactionCategory;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RawBankCheckingData [transactionId=" + transactionId + ", postingDate=" + postingDate
				+ ", effectiveDate=" + effectiveDate + ", transactionType=" + transactionType + ", amount=" + amount
				+ ", checkNumber=" + checkNumber + ", referenceNumber=" + referenceNumber + ", payee=" + description
				+ ", memo=" + transactionCategory + ", transactionCategory=" + transactionCategory + ", type=" + type + ", balance="
				+ balance + "]";
	}

}
