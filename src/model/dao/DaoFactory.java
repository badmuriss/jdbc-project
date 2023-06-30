package model.dao;

public class DaoFactory {

	public SellerDao createSellerDao() {
		return new SellerDaoJDBC();
	}
	
}
