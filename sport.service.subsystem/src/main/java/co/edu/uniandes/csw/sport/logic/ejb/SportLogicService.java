
package co.edu.uniandes.csw.sport.logic.ejb;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless; 
import javax.inject.Inject;

import javax.enterprise.inject.Default;

import co.edu.uniandes.csw.sport.logic.api.ISportLogicService;

@Default
@Stateless
@LocalBean
public class SportLogicService extends _SportLogicService implements ISportLogicService {

}