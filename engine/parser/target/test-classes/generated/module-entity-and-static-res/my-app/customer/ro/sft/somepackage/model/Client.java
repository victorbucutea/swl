package ro.sft.somepackage.model;


import javax.persistence.Entity;
import java.util.Set;
import java.util.Date;

public class Client { 

	private Date startDate;

	private Date endDate;

	private Set<Order> orders;



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

	public Set<Order> getOrders() {
		return this.orders;
	}

	public void setOrders(Set<Order> orders) {
		return this.orders = orders;
	}


}