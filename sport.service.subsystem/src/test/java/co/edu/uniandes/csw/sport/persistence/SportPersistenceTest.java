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
import java.io.File;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@RunWith(Arquillian.class)
public class SportPersistenceTest {

    public static final String DEPLOY = "Prueba";

    @Deployment
    public static WebArchive createDeployment() {
     
        return ShrinkWrap.create(WebArchive.class, DEPLOY + ".war")
                .addPackage(SportPersistence.class.getPackage())
                .addPackage(SportEntity.class.getPackage())
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("META-INF/beans.xml", "beans.xml");
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
        try {
            utx.begin();
            clearData();
            utx.commit();
            utx.begin();
            insertData();
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void clearData() {
        em.createQuery("delete from SportEntity").executeUpdate();
    }

    private List<SportEntity> data = new ArrayList<SportEntity>();

    private void insertData() {
        PodamFactory factory = new PodamFactoryImpl(); //This will use the default Random Data Provider Strategy
        for (int i = 0; i < 10; i++) {
            SportEntity entity = SportConverter.persistenceDTO2Entity(factory.manufacturePojo(SportDTO.class));
            entity.setId((long)i);
            em.persist(entity);
            data.add(entity);
        }
    }

    @Test
    public void createSportTest() {
        PodamFactory factory = new PodamFactoryImpl(); //This will use the default Random Data Provider Strategy
        boolean fail = false;
        for (int i = 0; i < 10; i++) {

            SportDTO dto = factory.manufacturePojo(SportDTO.class);
            dto.setId((long) 0);
            SportDTO result = sportPersistence.createSport(dto);
            
            Assert.assertNotNull(result);

            SportEntity entity = em.find(SportEntity.class, result.getId());
            fail = !(dto.getName().equals(entity.getName()) && dto.getMinAge() ==  entity.getMinAge() &&  dto.getMaxAge() == entity.getMaxAge());
           
        }
        Assert.assertTrue(!fail);
    }

    @Test
    public void getSportsTest() {

        List<SportDTO> list = sportPersistence.getSports();
        Assert.assertEquals(list.size(), data.size());
        for (SportDTO dto : list) {
            boolean found = false;
            for (SportEntity entity : data) {
                if (dto.getId() == entity.getId()) {
                    found = true;
                }
            }
            
            Assert.assertTrue(found);
        }
    }

    @Test
    public void getSportTest() {
        boolean fail = false;
        for (SportEntity sportEntity : data) {
            SportEntity entity = sportEntity;
            SportDTO dto = sportPersistence.getSport(entity.getId());
          if(dto == null){
              fail = true;
          }else{
              fail = !(dto.getName().equals(entity.getName()) && dto.getMinAge() ==  entity.getMinAge() &&  dto.getMaxAge() == entity.getMaxAge());
          }
            
        }
        
        Assert.assertTrue(!fail);

    }
/*
    @Test
    public void deleteSportTest() {
        SportEntity entity = data.get(0);
        sportPersistence.deleteSport(entity.getId());
        SportEntity deleted = em.find(SportEntity.class, entity.getId());
        Assert.assertNull(deleted);
    }

    @Test
    public void updateSportTest() {
        SportEntity entity = data.get(0);
        PodamFactory factory = new PodamFactoryImpl();
        SportDTO dto = factory.manufacturePojo(SportDTO.class);
        dto.setId(entity.getId());
      

        sportPersistence.updateSport(dto);

        SportEntity resp = em.find(SportEntity.class, entity.getId());

        Assert.assertEquals(dto.getName(), resp.getName());
        Assert.assertEquals(dto.getMinAge(), resp.getMinAge());
        Assert.assertEquals(dto.getMaxAge(), resp.getMaxAge());
    }
*/
}
