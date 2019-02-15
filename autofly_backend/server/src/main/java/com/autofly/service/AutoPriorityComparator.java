package com.autofly.service;

import java.util.Comparator;

import com.autofly.repository.model.AutoDriver;

public class AutoPriorityComparator implements Comparator<AutoDriver>{

	@Override
	public int compare(AutoDriver a1, AutoDriver a2) {
		
		if(a1.getNoOfPassengersBoarded() < 3 && a2.getNoOfPassengersBoarded() < 3 &&
				a1.getNoOfPassengersBoarded() > a2.getNoOfPassengersBoarded()) {
			return 1;
			
		} else if (a1.getNoOfPassengersBoarded() < 3 && a2.getNoOfPassengersBoarded() < 3 && 
				a1.getNoOfPassengersBoarded() < a2.getNoOfPassengersBoarded()) {
			return -1;
		}
		
		return 0;
	}

	
}
