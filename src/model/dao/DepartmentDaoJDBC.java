package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection conn;
	
	public Connection getConn() {
		return conn;
	}

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Department dep) {
		
		if(dep.getName() == null) {
			throw new DbException("Department name can't be null");
		}
		
		PreparedStatement findPs = null;
		ResultSet findRs = null;
		List<Department> list = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			findPs = conn.prepareStatement("SELECT * "
					+ "FROM department ");
			
			
			findRs = findPs.executeQuery();
			
			while(findRs.next()) {
				list.add(new Department(findRs.getInt("Id"), findRs.getString("Name")));
			}
				
			list.forEach(d -> {
				if(d.equals(dep)) {
					throw new DbException("Duplicate department");
				}
			});
			
			
			ps = conn.prepareStatement("INSERT INTO department "
					+ "(Name) "
					+ "VALUES "
					+ "(?)"
					, Statement.RETURN_GENERATED_KEYS);
			
			
			ps.setString(1, dep.getName());
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				rs = ps.getGeneratedKeys();
				while(rs.next()) {
					int id = rs.getInt(1);
					dep.setId(id);
					System.out.println("Insert Done! ID: " + id + " - " + dep);
					
				}
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.close(ps);
			DB.close(rs);	
		}

	}

	@Override
	public void update(Integer id, Department dep) {
		if(dep.getName() == null) {
			throw new DbException("Department name can't be null");
		}
		
		PreparedStatement findPs = null;
		ResultSet findRs = null;
		List<Department> list = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			findPs = conn.prepareStatement("SELECT * "
					+ "FROM department WHERE NOT Id = ?");
			
			findPs.setInt(1, id);
			
			findRs = findPs.executeQuery();
			
			while(findRs.next()) {
				list.add(new Department(findRs.getInt("Id"), findRs.getString("Name")));
			}
				
			list.forEach(d -> {
				if(d.equals(dep)) {
					throw new DbException("Duplicate department");
				}
			});
			
			ps = conn.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?");
			ps.setString(1, dep.getName());
			ps.setInt(2, id);
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				dep.setId(id);
				System.out.println("Update Done! ID: " + id + " - " + dep);
			}
			
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.close(ps);
			DB.close(rs);	
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
				
			ps = conn.prepareStatement("DELETE department FROM department WHERE Id = ?");
			
			ps.setInt(1, id);
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				System.out.println("Deletion complete! ID: " + id + " deleted.");
			}
			
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.close(ps);
			DB.close(rs);	
		}
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
				
			ps = conn.prepareStatement("SELECT * "
					+ "FROM department "
					+ "WHERE department.Id = ?;");
			
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				return new Department(rs.getInt("Id"), rs.getString("Name"));
			}
			
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.close(ps);
			DB.close(rs);	
		}
		
		return null;
		
	}

	
	@Override
	public List<Department> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Department> list = new ArrayList<>();
		
		try {
				
			ps = conn.prepareStatement("SELECT * "
					+ "FROM department ");
			
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				list.add(new Department(rs.getInt("Id"), rs.getString("Name")));
			}
			
			if(list.size() > 0) {
				return list;
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.close(ps);
			DB.close(rs);	
		}
		
		return null;
		
	}

	
}
