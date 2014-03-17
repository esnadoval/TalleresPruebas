
package co.edu.uniandes.csw.sport.persistence;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;

import co.edu.uniandes.csw.sport.persistence.api.ISportPersistence;
import javax.ejb.LocalBean;

@Default
@Stateless 
@LocalBean
public class SportPersistence extends _SportPersistence  implements ISportPersistence {

}