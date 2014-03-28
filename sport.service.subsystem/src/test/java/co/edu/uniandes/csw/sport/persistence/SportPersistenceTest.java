package co.edu.uniandes.csw.sport.persistence;

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
import co.edu.uniandes.csw.sport.persistence.api.ISportPersistence;
import co.edu.uniandes.csw.sport.persistence.converter.SportConverter;
import co.edu.uniandes.csw.sport.persistence.converter._SportConverter;
import co.edu.uniandes.csw.sport.persistence.entity.SportEntity;
import co.edu.uniandes.csw.sport.test.rules.SportTestRule;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.Rule;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(Arquillian.class)
public class SportPersistenceTest {

    public static final String DEPLOY = "Prueba";
    //Regla implementada para manejar múltiples escenarios de datos
    @Rule
    public SportTestRule rule = new SportTestRule();
//Almacena el dato actual de la regla.
    private SportDTO dataSample;

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class, DEPLOY + ".jar")
                //Añade el paquete en el que se encuentra la clase 'SportPersistance.java'
                .addPackage(SportPersistence.class.getPackage())
                //Añade el paquete en el que se encuentra la clase 'SportEntity.java'
                .addPackage(SportEntity.class.getPackage())
                //Finalmente se añaden los archivos persistance.xml y beans.xml para laa Unidad de peristencia y CDI del paquete mínimo
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/beans.xml", "META-INF/beans.xml");
    }
    //Atributo que contiene la referencia al componente que se va a probar (la persistencia)
    @Inject
    private ISportPersistence sportPersistence;
    //Atributo que obtiene el persistance unit especificado en persistance.xml
    @PersistenceContext
    private EntityManager em;
    //Atributo que contiene la referencia al manegador de transacciones de JPA (utilizado para inicializar las pruebas)
    @Inject
    UserTransaction utx;

    //Método que configura las pruebas antes de ejecutarlas
    @Before
    public void configTest() {

        System.out.println("em: " + em);
        //se limpia la data de prueba en la base de datos
        clearData();

    }

    //Método auxiliar para limpiar los datos de la tabla de prueba
    private void clearData() {

        begin();
        em.createQuery("delete from SportEntity").executeUpdate();
        data.clear();
        commit();

    }

    //Método auxiliar que inicia una transacción.
    public void begin() {
        try {
            utx.begin();
        } catch (Exception ex) {
            Logger.getLogger(SportPersistenceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//Metodo auxiliar que dá commita a una transacción.

    public void commit() {
        try {
            utx.commit();
        } catch (Exception ex) {
            Logger.getLogger(SportPersistenceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Lista auxiliar de datos que hace de oráculo para saber la respuesta correcta de algunas pruebas.
    private List<SportEntity> data = new ArrayList<SportEntity>();
//Método auxiliar para poblar las bases de datos de prueba

    private void insertData() {
        //Inicio transacción
        begin();
        // se instancia el generador de datos Podam
        PodamFactory factory = new PodamFactoryImpl(); //This will use the default Random Data Provider Strategy
        //Luego, se generan 10 datos de prueba diferentes
        for (int i = 0; i < 10; i++) {
            SportEntity entity = SportConverter.persistenceDTO2Entity(factory.manufacturePojo(SportDTO.class));
            //Se establece el id del elemento anterior a 0 para que éste se almacene según el valor que le asigna la base de datos
            entity.setId((long) 0);
            //Persiste el objeto en base de datos
            em.persist(entity);
            //Se añade a la lista del oráculo
            data.add(entity);
        }
        commit();

    }

    @Test
    public void createSportTest() {
        // Se crea un sport utilizando el dato de prueba  de la regla
        SportDTO result = sportPersistence.createSport(dataSample);

        SportDTO pdto = SportConverter.entity2PersistenceDTO(em.find(SportEntity.class, result.getId()));
        
        // Por último se verifica que el deporte exista.
        Assert.assertNotNull(em.find(SportEntity.class, result.getId()));
        Assert.assertEquals(result.getName(), pdto.getName());
        Assert.assertEquals(result.getMinAge(), pdto.getMinAge());
        Assert.assertEquals(result.getMaxAge(), pdto.getMaxAge());

    }

    @Test
    public void getSportsTest() {
        //Se inserta la data de prueba para obtener la lista.
        insertData();
        System.out.println("Test with: " + dataSample.getName());
        // Se prueba el método getSports
        List<SportDTO> list = sportPersistence.getSports();
        Assert.assertEquals(list.size(), data.size());
      //Finalmente se verifican que cada elemento de la lista coincida con la del oráculo
        for (SportDTO dto : list) {
            boolean found = false;

            for (SportEntity entity : data) {

                if (dto.getId() == entity.getId()) {
                    System.err.println(">>" + dto.getId() + " - " + entity.getId());
                    found = true;
                }
            }

            Assert.assertTrue(found);
        }
    }

    @Test
    public void getSportTest() {
       //se persiste un dato de prueba.
        SportEntity result = SportConverter.persistenceDTO2Entity(dataSample);
        begin();
        em.persist(result);
        commit();
        //Finalmente prueba el método getSport
        SportDTO dto = sportPersistence.getSport(result.getId());
//Verifica que el dato de prueba coincida con el obtenido
        Assert.assertNotNull(em.find(SportEntity.class, result.getId()));
        Assert.assertEquals(result.getName(), dto.getName());
        Assert.assertEquals(result.getMinAge(), dto.getMinAge());
        Assert.assertEquals(result.getMaxAge(), dto.getMaxAge());

    }

    @Test
    public void deleteSportTest() {
        //se persiste un dato de prueba.
        SportEntity result = SportConverter.persistenceDTO2Entity(dataSample);
        begin();
        em.persist(result);
        commit();
        // finalmente se prueca el método deleteSport
        sportPersistence.deleteSport(result.getId());
        SportEntity deleted = em.find(SportEntity.class, result.getId());
        Assert.assertNull(deleted);
    }

    @Test
    public void updateSportTest() {
         //se persiste un dato de prueba.
        SportEntity entity = SportConverter.persistenceDTO2Entity(dataSample);
        begin();
        em.persist(entity);
        commit();
        // Con podan se generan un DTO que corresponde a la modificación del dato de prueba.
        PodamFactory factory = new PodamFactoryImpl();
        SportDTO dto = factory.manufacturePojo(SportDTO.class);
        dto.setId(entity.getId());
//Finalmente, se prueba updateSport
        sportPersistence.updateSport(dto);
//Obtengo la respuesta utilizando JPA
        SportEntity resp = em.find(SportEntity.class, entity.getId());
// Verifico que la respuesta y el objeto modificado correspondan
        Assert.assertEquals(dto.getName(), resp.getName());
        Assert.assertEquals(dto.getMinAge(), resp.getMinAge());
        Assert.assertEquals(dto.getMaxAge(), resp.getMaxAge());
    }

}
