package model.dao;

import java.sql.Connection;
import java.util.List;

import model.entities.Seller;

public interface SellerDao {

	public Connection getConn();
	void insert(Seller sel);
	void update(Integer id, Seller sel);
	void deleteById(Integer id);
	Seller findById(Integer id);
	List<Seller> findByDepartment(String depName);
	List<Seller> findAll();
	
}
