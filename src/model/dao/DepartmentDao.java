package model.dao;

import java.sql.Connection;
import java.util.List;

import model.entities.Department;

public interface DepartmentDao {

	public Connection getConn();
	void insert(Department dep);
	void update(Integer id, Department dep);
	void deleteById(Integer id);
	Department findById(Integer id);
	List<Department> findAll();
	
}
