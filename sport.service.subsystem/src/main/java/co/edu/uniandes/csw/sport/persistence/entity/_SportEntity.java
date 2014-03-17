
package co.edu.uniandes.csw.sport.persistence.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class _SportEntity {

	@Id
	@GeneratedValue(generator = "Sport")
	private Long id;
	private String name;
	private int minAge;
	private int maxAge;

	public Long getId(){
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	public int getMinAge(){
		return minAge;
	}
	
	public void setMinAge(int minAge){
		this.minAge = minAge;
	}
	public int getMaxAge(){
		return maxAge;
	}
	
	public void setMaxAge(int maxAge){
		this.maxAge = maxAge;
	}
}