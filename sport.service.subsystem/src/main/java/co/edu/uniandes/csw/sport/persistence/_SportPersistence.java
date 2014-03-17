
package co.edu.uniandes.csw.sport.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.edu.uniandes.csw.sport.logic.dto.SportDTO;
import co.edu.uniandes.csw.sport.persistence.api._ISportPersistence;
import co.edu.uniandes.csw.sport.persistence.converter.SportConverter;
import co.edu.uniandes.csw.sport.persistence.entity.SportEntity;

public abstract class _SportPersistence implements _ISportPersistence {

	@PersistenceContext(unitName="SportPU")
	protected EntityManager entityManager;
	
	public SportDTO createSport(SportDTO sport) {
		SportEntity entity=SportConverter.persistenceDTO2Entity(sport);
		entityManager.persist(entity);
		return SportConverter.entity2PersistenceDTO(entity);
	}

	@SuppressWarnings("unchecked")
	public List<SportDTO> getSports() {
		Query q = entityManager.createQuery("select u from SportEntity u");
		return SportConverter.entity2PersistenceDTOList(q.getResultList());
	}

	public SportDTO getSport(Long id) {
		return SportConverter.entity2PersistenceDTO(entityManager.find(SportEntity.class, id));
	}

	public void deleteSport(Long id) {
		SportEntity entity=entityManager.find(SportEntity.class, id);
		entityManager.remove(entity);
	}

	public void updateSport(SportDTO detail) {
		SportEntity entity=entityManager.merge(SportConverter.persistenceDTO2Entity(detail));
		SportConverter.entity2PersistenceDTO(entity);
	}

}