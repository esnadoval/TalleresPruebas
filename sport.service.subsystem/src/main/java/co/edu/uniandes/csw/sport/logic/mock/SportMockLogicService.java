
package co.edu.uniandes.csw.sport.logic.mock;
import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;

import co.edu.uniandes.csw.sport.logic.api.ISportLogicService;
import javax.enterprise.inject.Default;

@Default
@Singleton
public class SportMockLogicService extends _SportMockLogicService implements ISportLogicService {
	
}