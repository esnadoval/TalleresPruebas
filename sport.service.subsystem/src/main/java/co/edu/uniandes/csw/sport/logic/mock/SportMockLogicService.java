
package co.edu.uniandes.csw.sport.logic.mock;
import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;

import co.edu.uniandes.csw.sport.logic.api.ISportLogicService;

@Alternative
@Singleton
public class SportMockLogicService extends _SportMockLogicService implements ISportLogicService {
	
}