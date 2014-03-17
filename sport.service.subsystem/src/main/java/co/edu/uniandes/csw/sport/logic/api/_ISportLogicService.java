
package co.edu.uniandes.csw.sport.logic.api;

import java.util.List; 

import co.edu.uniandes.csw.sport.logic.dto.SportDTO;

public interface _ISportLogicService {

	public SportDTO createSport(SportDTO detail);
	public List<SportDTO> getSports();
	public SportDTO getSport(Long id);
	public void deleteSport(Long id);
	public void updateSport(SportDTO detail);
	
}