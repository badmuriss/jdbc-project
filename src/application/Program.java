package application;

import java.sql.Date;

import entities.Department;
import entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Department dep = new Department(1, "Computers");
		Seller leo = new Seller(1, "leo", "leo123@gmail.com", Date.valueOf("2001-01-30"), 2000.0, dep);
		
		System.out.println(leo);
		
	}
	
}
