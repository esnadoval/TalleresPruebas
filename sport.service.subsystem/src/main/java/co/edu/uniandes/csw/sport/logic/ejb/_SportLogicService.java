
package co.edu.uniandes.csw.sport.logic.ejb;
import java.util.List;
import javax.inject.Inject;

import co.edu.uniandes.csw.sport.logic.dto.SportDTO;
import co.edu.uniandes.csw.sport.logic.api._ISportLogicService;
import co.edu.uniandes.csw.sport.persistence.api.ISportPersistence;

public abstract class _SportLogicService implements _ISportLogicService {

	@Inject
	protected ISportPersistence persistance;

	public SportDTO createSport(SportDTO sport){
		return persistance.createSport( sport); 
    }

	public List<SportDTO> getSports(){
		return persistance.getSports(); 
	}

	public SportDTO getSport(Long id){
		return persistance.getSport(id); 
	}

	public void deleteSport(Long id){
	    persistance.deleteSport(id); 
	}

	public void updateSport(SportDTO sport){
	    persistance.updateSport(sport); 
	}	
}