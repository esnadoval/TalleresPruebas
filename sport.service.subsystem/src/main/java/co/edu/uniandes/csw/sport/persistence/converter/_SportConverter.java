
package co.edu.uniandes.csw.sport.persistence.converter;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniandes.csw.sport.logic.dto.SportDTO;
import co.edu.uniandes.csw.sport.persistence.entity.SportEntity;

public abstract class _SportConverter {


	public static SportDTO entity2PersistenceDTO(SportEntity entity){
		if (entity != null) {
			SportDTO dto = new SportDTO();
				dto.setId(entity.getId());
				dto.setName(entity.getName());
				dto.setMinAge(entity.getMinAge());
				dto.setMaxAge(entity.getMaxAge());
			return dto;
		}else{
			return null;
		}
	}
	
	public static SportEntity persistenceDTO2Entity(SportDTO dto){
		if(dto!=null){
			SportEntity entity=new SportEntity();
			entity.setId(dto.getId());
			entity.setName(dto.getName());
			entity.setMinAge(dto.getMinAge());
			entity.setMaxAge(dto.getMaxAge());
			return entity;
		}else {
			return null;
		}
	}
	
	public static List<SportDTO> entity2PersistenceDTOList(List<SportEntity> entities){
		List<SportDTO> dtos=new ArrayList<SportDTO>();
		for(SportEntity entity:entities){
			dtos.add(entity2PersistenceDTO(entity));
		}
		return dtos;
	}
	
	public static List<SportEntity> persistenceDTO2EntityList(List<SportDTO> dtos){
		List<SportEntity> entities=new ArrayList<SportEntity>();
		for(SportDTO dto:dtos){
			entities.add(persistenceDTO2Entity(dto));
		}
		return entities;
	}
}