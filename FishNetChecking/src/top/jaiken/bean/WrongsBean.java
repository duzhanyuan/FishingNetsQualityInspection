package top.jaiken.bean;

import java.sql.Time;

public class WrongsBean {

	private int wrong_type;
	private Time time;
	private String machine;
	public int getWrong_type() {
		return wrong_type;
	}
	public void setWrong_type(int wrong_type) {
		this.wrong_type = wrong_type;
	}
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
	/**
	 * @return the meachine
	 */
	public String getMachine() {
		return machine;
	}
	/**
	 * @param meachine the meachine to set
	 */
	public void setMachine(String machine) {
		this.machine = machine;
	}
}
