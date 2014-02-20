package ro.sft.somepackage.model;


import ro.sft.somepackage.model.String;
import javax.persistence.Entity;
import java.util.Date;

public class Experience { 

	private Date startDate;

	private Date endDate;

	private byte[] field;

	private String someProp;

	private double someProp2;



	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		return this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		return this.endDate = endDate;
	}

	public byte[] getField() {
		return this.field;
	}

	public void setField(byte[] field) {
		return this.field = field;
	}

	public String getSomeProp() {
		return this.someProp;
	}

	public void setSomeProp(String someProp) {
		return this.someProp = someProp;
	}

	public double getSomeProp2() {
		return this.someProp2;
	}

	public void setSomeProp2(double someProp2) {
		return this.someProp2 = someProp2;
	}


}