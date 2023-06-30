package application;

import java.sql.Date;
import java.sql.SQLException;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) throws SQLException {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		
		sellerDao.getConn().setAutoCommit(false);
		
		
		sellerDao.insert(new Seller("Laos", "marcddos@gmail.com", Date.valueOf("2000-01-30"), 4000.0, new Department(4)));
		sellerDao.findByDepartment("Electronics").forEach(System.out::println);;
		sellerDao.findAll().forEach(System.out::println);
		
		
		sellerDao.getConn().commit();
		
	}
	
}
