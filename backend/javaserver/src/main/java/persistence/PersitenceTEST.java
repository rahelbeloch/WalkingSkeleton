package persistence;

import java.util.List;
import java.util.LinkedList;

public class PersitenceTEST {
	
	private static List<Integer> liste = new LinkedList<Integer>();
	
	public static void main(String[] args) {
		liste.add(17);
		liste.add(13);
		liste.add(70);
		print();
		
		for(int i: liste) {
			if(i == 17) {
				i = 99;
			}
		}
		
		print();
		
		
	}
	
	public static void print() {
		for(int i: liste) {
			System.out.println(i);
		}
	}
	
	
}
