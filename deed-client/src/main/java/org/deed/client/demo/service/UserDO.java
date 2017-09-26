package org.deed.client.demo.service;

public class UserDO {
	private int id;
	
	private String name;
	
	private int sex;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "UserDO [id=" + id + ", name=" + name + ", sex=" + sex + "]";
	}
}
