package ro.sft.somepackage.model;


import javax.persistence.Entity;
import ro.sft.somepackage.model.Client;
import java.util.Date;

public class Order { 

	private Date startDate;

	private Date endDate;

	private byte[] field;

	private Client client;



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

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		return this.client = client;
	}


}