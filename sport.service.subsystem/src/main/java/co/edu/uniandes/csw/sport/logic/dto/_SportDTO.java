
package co.edu.uniandes.csw.sport.logic.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement 
public abstract class _SportDTO {

	private Long id;
	private String name;
	private int minAge;
	private int maxAge;

	public Long getId() {
		return id;
	}
 
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
	public int getMinAge() {
		return minAge;
	}
 
	public void setMinAge(int minage) {
		this.minAge = minage;
	}
	public int getMaxAge() {
		return maxAge;
	}
 
	public void setMaxAge(int maxage) {
		this.maxAge = maxage;
	}
	
}