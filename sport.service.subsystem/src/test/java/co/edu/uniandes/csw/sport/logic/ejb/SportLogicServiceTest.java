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
    @Rule
    public SportTestRule rule = new SportTestRule();

    private SportDTO dataSample;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class, DEPLOY + ".jar")
                .addPackage(SportLogicService.class.getPackage())
                .addPackage(SportPersistence.class.getPackage())
                .addPackage(SportEntity.class.getPackage())
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/beans.xml", "META-INF/beans.xml");

    }

    @Inject
    private ISportLogicService sportLogicService;

    @Inject
    private ISportPersistence sportPersistence;

    @Before
    public void configTest() {
        try {
            clearData();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearData() {
        List<SportDTO> dtos = sportPersistence.getSports();
        for (SportDTO dto : dtos) {
            sportPersistence.deleteSport(dto.getId());
        }
        data.clear();
    }

    private List<SportDTO> data = new ArrayList<SportDTO>();

    private void insertData() {
        PodamFactory factory = new PodamFactoryImpl();
        for (int i = 0; i < 3; i++) {
            SportDTO pdto = factory.manufacturePojo(SportDTO.class);

            pdto.setId((long) 0);

            pdto = sportPersistence.createSport(pdto);
            data.add(pdto);
        }
    }

    @Test
    public void createSportTest() {
        SportDTO ldto = dataSample;
        System.out.println("Test with: " + dataSample.getName());
        SportDTO result = sportLogicService.createSport(ldto);

        Assert.assertNotNull(result);

        SportDTO pdto = sportPersistence.getSport(result.getId());

        Assert.assertEquals(ldto.getName(), pdto.getName());
        Assert.assertEquals(ldto.getMinAge(), pdto.getMinAge());
        Assert.assertEquals(ldto.getMaxAge(), pdto.getMaxAge());
    }

    @Test
    public void getSportsTest() {
        insertData();
        System.out.println("Test with: " + dataSample.getName());
        List<SportDTO> list = sportLogicService.getSports();
        Assert.assertEquals(list.size(), data.size());
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
        SportDTO ret = sportPersistence.createSport(dataSample);
        SportDTO pdto = dataSample;
        SportDTO ldto = sportLogicService.getSport(ret.getId());
        Assert.assertNotNull(ldto);
        Assert.assertEquals(pdto.getName(), ldto.getName());
        Assert.assertEquals(pdto.getMinAge(), ldto.getMinAge());
        Assert.assertEquals(pdto.getMaxAge(), ldto.getMaxAge());

    }

    @Test
    public void deleteSportTest() {
        System.out.println("Test with: " + dataSample.getName());
        SportDTO ret = sportPersistence.createSport(dataSample);
        sportLogicService.deleteSport(ret.getId());
        SportDTO deleted = sportPersistence.getSport(ret.getId());
        Assert.assertNull(deleted);
    }

    @Test
    public void updateSportTest() {
        System.out.println("Test with: " + dataSample.getName());
        SportDTO ret = sportPersistence.createSport(dataSample);

        PodamFactory factory = new PodamFactoryImpl();
        SportDTO ldto = factory.manufacturePojo(SportDTO.class);
        ldto.setId(ret.getId());

        sportLogicService.updateSport(ldto);

        SportDTO resp = sportPersistence.getSport(ldto.getId());

        Assert.assertEquals(ldto.getName(), resp.getName());
        Assert.assertEquals(ldto.getMinAge(), resp.getMinAge());
        Assert.assertEquals(ldto.getMaxAge(), resp.getMaxAge());
    }

    

}
