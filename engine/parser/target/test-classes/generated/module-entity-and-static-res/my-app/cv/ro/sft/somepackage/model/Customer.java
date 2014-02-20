package ro.sft.somepackage.model;


import ro.sft.somepackage.model.Experience;
import javax.persistence.Entity;
import java.util.Date;

public class Customer { 

	private Date startDate;

	private Date endDate;

	private Experience exp;



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

	public Experience getExp() {
		return this.exp;
	}

	public void setExp(Experience exp) {
		return this.exp = exp;
	}


}