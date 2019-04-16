package org.jaiken.main;

import org.jaiken.tools.WriteExcel;

public class test {

	public static void main(String[] args) {
		
		String [] data=new String[3];
		data[0]="test1";
		data[1]="test2";
		data[2]="test3";
		WriteExcel writeExcel=new WriteExcel();
		try {
			writeExcel.writeExcel("E://0416/2.xls", data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
