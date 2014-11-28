package de.hsrm.swt02;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hsrm.swt02.moduledi.SingleModule;

public class app {
	public static void main(String[] args) {
		Injector i = Guice.createInjector(new SingleModule());
		// TODO executable main!!!
		
		
			
	}
}
