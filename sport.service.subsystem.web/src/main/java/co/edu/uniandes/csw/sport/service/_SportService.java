package co.edu.uniandes.csw.sport.service;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.inject.Inject;

import co.edu.uniandes.csw.sport.logic.api.ISportLogicService;
import co.edu.uniandes.csw.sport.logic.dto.SportDTO;


public abstract class _SportService {

	@Inject
	protected ISportLogicService sportLogicService;
	
	@POST
	public SportDTO createSport(SportDTO sport){
		return sportLogicService.createSport(sport);
	}
	
	@DELETE
	@Path("{id}")
	public void deleteSport(@PathParam("id") Long id){
		sportLogicService.deleteSport(id);
	}
	
	@GET
	public List<SportDTO> getSports(){
		return sportLogicService.getSports();
	}
	
	@GET
	@Path("{id}")
	public SportDTO getSport(@PathParam("id") Long id){
		return sportLogicService.getSport(id);
	}
	
	@PUT
    @Path("{id}")
	public void updateSport(@PathParam("id") Long id, SportDTO sport){
		sportLogicService.updateSport(sport);
	}
	
}