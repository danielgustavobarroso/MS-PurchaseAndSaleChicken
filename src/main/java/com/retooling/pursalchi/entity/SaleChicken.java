package com.retooling.pursalchi.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Document(collection = "saleChickens")
public class SaleChicken {

	@Id
	private String id;
	
	@NotEmpty
	private String farmId;
	
	@NotNull
	private Date saleDate;
	
	@Min(value=1)
	private long units;
	
	@Min(value=0)
	private double price;
	
	@Min(value=0)
	private double totalAmount;

	public SaleChicken() {
		super();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public long getUnits() {
		return units;
	}

	public void setUnits(long units) {
		this.units = units;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Override
	public String toString() {
		return "SaleChicken [id=" + id + ", farmId=" + farmId + ", saleDate=" + saleDate + ", units=" + units
				+ ", price=" + price + ", totalAmount=" + totalAmount + "]";
	}
	
}
