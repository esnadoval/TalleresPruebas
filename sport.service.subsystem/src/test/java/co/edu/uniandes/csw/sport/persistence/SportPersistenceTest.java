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

    @Rule
    public SportTestRule rule = new SportTestRule();

    private SportDTO dataSample;

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class, DEPLOY + ".jar")
                .addPackage(SportPersistence.class.getPackage())
                .addPackage(SportEntity.class.getPackage())
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/beans.xml", "META-INF/beans.xml");
    }

    @Inject
    private ISportPersistence sportPersistence;

    @PersistenceContext
    private EntityManager em;

    @Inject
    UserTransaction utx;

    @Before
    public void configTest() {

        System.out.println("em: " + em);

        clearData();

    }

    private void clearData() {

        begin();
        em.createQuery("delete from SportEntity").executeUpdate();
        data.clear();
        commit();

    }

    public void begin() {
        try {
            utx.begin();
        } catch (Exception ex) {
            Logger.getLogger(SportPersistenceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void commit() {
        try {
            utx.commit();
        } catch (Exception ex) {
            Logger.getLogger(SportPersistenceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private List<SportEntity> data = new ArrayList<SportEntity>();

    private void insertData() {
        begin();
        PodamFactory factory = new PodamFactoryImpl(); //This will use the default Random Data Provider Strategy
        for (int i = 0; i < 10; i++) {
            SportEntity entity = SportConverter.persistenceDTO2Entity(factory.manufacturePojo(SportDTO.class));
            entity.setId((long) 0);
            em.persist(entity);
            data.add(entity);
        }
        commit();

    }

    @Test
    public void createSportTest() {

        SportDTO result = sportPersistence.createSport(dataSample);

        SportDTO pdto = SportConverter.entity2PersistenceDTO(em.find(SportEntity.class, result.getId()));
        Assert.assertNotNull(em.find(SportEntity.class, result.getId()));
        Assert.assertEquals(result.getName(), pdto.getName());
        Assert.assertEquals(result.getMinAge(), pdto.getMinAge());
        Assert.assertEquals(result.getMaxAge(), pdto.getMaxAge());

    }

    @Test
    public void getSportsTest() {
        insertData();
               System.out.println("Test with: " + dataSample.getName());
        List<SportDTO> list = sportPersistence.getSports();
        Assert.assertEquals(list.size(), data.size());
        for (SportDTO dto : list) {
            boolean found = false;
            
            for (SportEntity entity : data) {
                
                if (dto.getId() == entity.getId()) {
                    System.err.println(">>"+dto.getId() +" - "+ entity.getId());
                    found = true;
                }
            }

            Assert.assertTrue(found);
        }
    }

    @Test
    public void getSportTest() {
        SportEntity result = SportConverter.persistenceDTO2Entity(dataSample);
        begin();
        em.persist(result);
        commit();
        SportDTO dto = sportPersistence.getSport(result.getId());

        Assert.assertNotNull(em.find(SportEntity.class, result.getId()));
        Assert.assertEquals(result.getName(), dto.getName());
        Assert.assertEquals(result.getMinAge(), dto.getMinAge());
        Assert.assertEquals(result.getMaxAge(), dto.getMaxAge());

    }

    @Test
    public void deleteSportTest() {
        SportEntity result = SportConverter.persistenceDTO2Entity(dataSample);
        begin();
        em.persist(result);
        commit();
        sportPersistence.deleteSport(result.getId());
        SportEntity deleted = em.find(SportEntity.class, result.getId());
        Assert.assertNull(deleted);
    }

    @Test
    public void updateSportTest() {
        SportEntity entity = SportConverter.persistenceDTO2Entity(dataSample);
        begin();
        em.persist(entity);
        commit();
        PodamFactory factory = new PodamFactoryImpl();
        SportDTO dto = factory.manufacturePojo(SportDTO.class);
        dto.setId(entity.getId());

        sportPersistence.updateSport(dto);

        SportEntity resp = em.find(SportEntity.class, entity.getId());

        Assert.assertEquals(dto.getName(), resp.getName());
        Assert.assertEquals(dto.getMinAge(), resp.getMinAge());
        Assert.assertEquals(dto.getMaxAge(), resp.getMaxAge());
    }

}
