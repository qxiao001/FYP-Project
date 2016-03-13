package com.chillerpredictor;

import java.sql.Timestamp;

public class PowerElmt {

	Timestamp timestamp;
	Double actual;
	Double predicted;
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public Double getActual() {
		return actual;
	}
	public void setActual(Double actual) {
		this.actual = actual;
	}
	public Double getPredicted() {
		return predicted;
	}
	public void setPredicted(Double predicted) {
		this.predicted = predicted;
	}
	
	
}
