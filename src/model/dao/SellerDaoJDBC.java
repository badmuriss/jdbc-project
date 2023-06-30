package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import db.DB;
import db.DbException;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;
	
	public Connection getConn() {
		return conn;
	}
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller sel) {
		PreparedStatement findPs = null;
		ResultSet findRs = null;
		List<Seller> list = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			findPs = conn.prepareStatement("SELECT seller.*, department.Name as DepName "
					+ "FROM seller "
					+ "INNER JOIN department on department.Id = seller.DepartmentId");
			
			findRs = findPs.executeQuery();
			
			while(findRs.next()) {
				String name = findRs.getString("Name");
				String email = findRs.getString("Email");
				Date birthDate = findRs.getDate("BirthDate");
				Double baseSalary = findRs.getDouble("BaseSalary");
				Department department = new Department(findRs.getInt("DepartmentId"));
				department.setName(findRs.getString("DepName"));
				
				list.add(new Seller(name, email, birthDate, baseSalary, department));
			}
				
			list.forEach(s -> {
				if(s.equals(sel)) {
					throw new DbException("Duplicate seller");
				}
			});
			
			ps = conn.prepareStatement("INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)"
					, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, sel.getName());
			ps.setString(2, sel.getEmail());
			ps.setDate(3, new java.sql.Date(sel.getBirthDate().getTime()));
			ps.setDouble(4, sel.getBaseSalary());
			ps.setInt(5, sel.getDepartment().getId());
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				rs = ps.getGeneratedKeys();
				while(rs.next()) {
					int id = rs.getInt(1);
					sel.setId(id);
					System.out.println("Insert Done! ID: " + id + " - " + sel);
					
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
	public void update(Integer id, Seller sel) {
		PreparedStatement findPs = null;
		ResultSet findRs = null;
		List<Seller> list = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			findPs = conn.prepareStatement("SELECT seller.*, department.Name as DepName "
					+ "FROM seller "
					+ "INNER JOIN department on department.Id = seller.DepartmentId WHERE NOT Id = ? ");
			
			
			findPs.setInt(1, id);
			
			findRs = findPs.executeQuery();
			
			while(findRs.next()) {
				String name = findRs.getString("Name");
				String email = findRs.getString("Email");
				Date birthDate = findRs.getDate("BirthDate");
				Double baseSalary = findRs.getDouble("BaseSalary");
				Department department = new Department(findRs.getInt("DepartmentId"));
				department.setName(findRs.getString("DepName"));
				
				list.add(new Seller(name, email, birthDate, baseSalary, department));
			}
				
			list.forEach(s -> {
				if(s.equals(sel)) {
					throw new DbException("Duplicate seller");
				}
			});
			
			ps = conn.prepareStatement("UPDATE seller SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? WHERE Id = ?");
			ps.setString(1, sel.getName());
			ps.setString(2, sel.getEmail());
			ps.setDate(3, new java.sql.Date(sel.getBirthDate().getTime()));
			ps.setDouble(4, sel.getBaseSalary());
			ps.setInt(5, sel.getDepartment().getId());
			ps.setInt(6, id);
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				sel.setId(id);
				System.out.println("Update Done! ID: " + id + " - " + sel);
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
				
			ps = conn.prepareStatement("DELETE * FROM seller WHERE Id = ?");
			
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
	public Seller findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
				
			ps = conn.prepareStatement("SELECT seller.*, department.Name as DepName "
					+ "FROM seller "
					+ "INNER JOIN department on department.Id = seller.DepartmentId "
					+ "WHERE seller.Id = ?;");
			
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				String name = rs.getString("Name");
				String email = rs.getString("Email");
				Date birthDate = rs.getDate("BirthDate");
				Double baseSalary = rs.getDouble("BaseSalary");
				Department department = new Department(rs.getInt("DepartmentId"));
				department.setName(rs.getString("DepName"));
				
				return new Seller(id, name, email, birthDate, baseSalary, department);
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
	public List<Seller> findByDepartment(String depName) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Seller> list = new ArrayList<>();
		
		try {
			
	
			ps = conn.prepareStatement("SELECT seller.*, department.Name as DepName "
					+ "FROM seller "
					+ "INNER JOIN department on department.Id = seller.DepartmentId "
					+ "WHERE department.Name = ?;");
			
			ps.setString(1, depName);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				Integer id = rs.getInt("Id");
				String name = rs.getString("Name");
				String email = rs.getString("Email");
				Date birthDate = rs.getDate("BirthDate");
				Double baseSalary = rs.getDouble("BaseSalary");
				Department department = new Department(rs.getInt("DepartmentId"));
				department.setName(rs.getString("DepName"));
				
				list.add(new Seller(id, name, email, birthDate, baseSalary, department));
			}
			
			if (list.size() > 0) {
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
	
	@Override
	public List<Seller> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Seller> list = new ArrayList<>();
		
		try {
			
	
			ps = conn.prepareStatement("SELECT seller.*, department.Name as DepName "
					+ "FROM seller "
					+ "INNER JOIN department on department.Id = seller.DepartmentId");
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				Integer id = rs.getInt("Id");
				String name = rs.getString("Name");
				String email = rs.getString("Email");
				Date birthDate = rs.getDate("BirthDate");
				Double baseSalary = rs.getDouble("BaseSalary");
				Department department = new Department(rs.getInt("DepartmentId"));
				department.setName(rs.getString("DepName"));
				
				list.add(new Seller(id, name, email, birthDate, baseSalary, department));
			}
			
			if (list.size() > 0) {
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
