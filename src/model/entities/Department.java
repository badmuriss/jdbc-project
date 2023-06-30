package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Department implements Serializable {

	static final long serialVersionUID = 1L;
	
	
	private Integer id;
	private String name;
	
	public Department(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public int hashCodeId() {
		return Objects.hash(id);
	}
	
	public int hashCodeName() {
		return Objects.hash(name);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Department other = (Department) obj;
		return Objects.equals(id, other.id) || Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + "]";
	}
	
	
	
	
}
