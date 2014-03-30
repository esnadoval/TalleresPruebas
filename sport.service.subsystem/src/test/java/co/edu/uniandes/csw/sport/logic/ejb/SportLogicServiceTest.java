package co.edu.uniandes.csw.sport.logic.ejb;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.*;

import co.edu.uniandes.csw.sport.logic.dto.SportDTO;
import co.edu.uniandes.csw.sport.logic.api.ISportLogicService;
import co.edu.uniandes.csw.sport.persistence.SportPersistence;
import co.edu.uniandes.csw.sport.persistence.api.ISportPersistence;
import co.edu.uniandes.csw.sport.persistence.converter.SportConverter;
import co.edu.uniandes.csw.sport.persistence.entity.SportEntity;
import co.edu.uniandes.csw.sport.test.rules.SportTestRule;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Rule;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(Arquillian.class)
public class SportLogicServiceTest {

    public static final String DEPLOY = "Prueba";
    //Regla implementada para manejar múltiples escenarios de datos
    @Rule
    public SportTestRule rule = new SportTestRule();
//Almacena el dato actual de la regla.
    private SportDTO dataSample;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class, DEPLOY + ".jar")
                //Añade el paquete en el que se encuentra la clase 'ISportLogicService.java'
                .addPackage(ISportLogicService.class.getPackage())
                //Añade el paquete en el que se encuentra la clase 'SportLogicService.java'
                .addPackage(SportLogicService.class.getPackage())
                //Añade el paquete en el que se encuentra la clase 'SportPersistance.java'
                .addPackage(SportPersistence.class.getPackage())
                //Añade el paquete en el que se encuentra la clase 'SportEntity.java'
                .addPackage(SportEntity.class.getPackage())
                //Finalmente se añaden los archivos persistance.xml y beans.xml para laa Unidad de peristencia y CDI del paquete mínimo
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/beans.xml", "META-INF/beans.xml");

    }
//Atributo que contiene la referencia al componente que se va a probar (la lógica)
    @Inject
    private ISportLogicService sportLogicService;
//Atributo que contiene la referencia al componente de persistencia para operaciones del oráculo (previamente probado)
    @Inject
    private ISportPersistence sportPersistence;
//Método que configura las pruebas antes de ejecutarlas

    @Before
    public void configTest() {
        try {
            //se limpia la data de prueba en la base de datos
            clearData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Método auxiliar para limpiar los datos de la tabla de prueba
    private void clearData() {
        List<SportDTO> dtos = sportPersistence.getSports();
        for (SportDTO dto : dtos) {
            sportPersistence.deleteSport(dto.getId());
        }
        data.clear();
    }

    private List<SportDTO> data = new ArrayList<SportDTO>();
//Método auxiliar para poblar las bases de datos de prueba

    private void insertData() {
        // se instancia el generador de datos Podam
        PodamFactory factory = new PodamFactoryImpl();
        //Luego, se generan 3 datos de prueba diferentes
        for (int i = 0; i < 3; i++) {
            SportDTO pdto = factory.manufacturePojo(SportDTO.class);
//Se establece el id del elemento anterior a 0 para que éste se almacene según el valor que le asigna la base de datos
            pdto.setId((long) 0);
//Persiste el objeto en base de datos
            pdto = sportPersistence.createSport(pdto);
            //Se añade a la lista del oráculo
            data.add(pdto);
        }
    }

    @Test
    public void createSportTest() {
        SportDTO ldto = dataSample;
        System.out.println("Test with: " + dataSample.getName());
        // Se crea un sport utilizando el dato de prueba  de la regla
        SportDTO result = sportLogicService.createSport(ldto);
// Por último se verifica que el deporte exista.
        Assert.assertNotNull(result);

        SportDTO pdto = sportPersistence.getSport(result.getId());

        Assert.assertEquals(ldto.getName(), pdto.getName());
        Assert.assertEquals(ldto.getMinAge(), pdto.getMinAge());
        Assert.assertEquals(ldto.getMaxAge(), pdto.getMaxAge());
    }

    @Test
    public void getSportsTest() {
        //Se inserta la data de prueba para obtener la lista.
        insertData();
        System.out.println("Test with: " + dataSample.getName());
        // Se prueba el método getSports
        List<SportDTO> list = sportLogicService.getSports();
        Assert.assertEquals(list.size(), data.size());
        //Finalmente se verifican que cada elemento de la lista coincida con la del oráculo
        for (SportDTO ldto : list) {
            boolean found = false;
            for (SportDTO pdto : data) {
                if (ldto.getId() == pdto.getId()) {
                    found = true;
                }
            }
            Assert.assertTrue(found);
        }
    }

    @Test
    public void getSportTest() {
        System.out.println("Test with: " + dataSample.getName());
        //se persiste un dato de prueba (note que se usa directamente la persistencia).
        SportDTO ret = sportPersistence.createSport(dataSample);
        SportDTO pdto = dataSample;
        //Finalmente prueba el método getSport
        SportDTO ldto = sportLogicService.getSport(ret.getId());
        //Verifica que el dato de prueba coincida con el obtenido
        Assert.assertNotNull(ldto);
        Assert.assertEquals(pdto.getName(), ldto.getName());
        Assert.assertEquals(pdto.getMinAge(), ldto.getMinAge());
        Assert.assertEquals(pdto.getMaxAge(), ldto.getMaxAge());

    }

    @Test
    public void deleteSportTest() {
        System.out.println("Test with: " + dataSample.getName());
        //se persiste un dato de prueba.
        SportDTO ret = sportPersistence.createSport(dataSample);
        // finalmente se prueba el método deleteSport
        sportLogicService.deleteSport(ret.getId());
        SportDTO deleted = sportPersistence.getSport(ret.getId());
        Assert.assertNull(deleted);
    }

    @Test
    public void updateSportTest() {
        System.out.println("Test with: " + dataSample.getName());
        //Una vez más, se persiste un dato de prueba.
        SportDTO ret = sportPersistence.createSport(dataSample);
        // Con podam se generan un DTO que corresponde a la modificación del dato de prueba.
        PodamFactory factory = new PodamFactoryImpl();
        SportDTO ldto = factory.manufacturePojo(SportDTO.class);
        ldto.setId(ret.getId());
        //Finalmente, se prueba updateSport
        sportLogicService.updateSport(ldto);
        //Obtengo la respuesta utilizando directamente el componente de persistencia
        SportDTO resp = sportPersistence.getSport(ldto.getId());

        Assert.assertEquals(ldto.getName(), resp.getName());
        Assert.assertEquals(ldto.getMinAge(), resp.getMinAge());
        Assert.assertEquals(ldto.getMaxAge(), resp.getMaxAge());
    }

}
