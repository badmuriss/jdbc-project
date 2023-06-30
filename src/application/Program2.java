package application;

import java.sql.SQLException;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

			public static void main(String[] args) throws SQLException {
				
				DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

				departmentDao.getConn().setAutoCommit(false);
				
				System.out.println("=== TEST 1: findById =======");
				Department dep = departmentDao.findById(1);
				System.out.println(dep);
				
				System.out.println("\n=== TEST 2: findAll =======");
				List<Department> list = departmentDao.findAll();
				for (Department d : list) {
					System.out.println(d);
				}

				System.out.println("\n=== TEST 3: insert =======");
				Department newDepartment = new Department(null, "Almeida");
				departmentDao.insert(newDepartment);
				System.out.println("Inserted! New id: " + newDepartment.getId());

				System.out.println("\n=== TEST 4: update =======");
				Department dep2 = departmentDao.findById(1);
				dep2.setName("Food");
				departmentDao.update(1, dep2);
				System.out.println("Update completed");
				
				System.out.println("\n=== TEST 5: delete =======");
				System.out.print("Enter id for delete test: ");
				departmentDao.deleteById(5);
				System.out.println("Delete completed");

				
				System.out.println("\n\n\n\n\n\n\n\n ");
				
				List<Department> list2 = departmentDao.findAll();
				for (Department d : list2) {
					System.out.println(d);
				}
	
				departmentDao.getConn().commit();
				
			}


}


