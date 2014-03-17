
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
import co.edu.uniandes.csw.sport.persistence.entity.SportEntity;

@RunWith(Arquillian.class)
public class SportLogicServiceTest {

	public static final String DEPLOY = "Prueba";

	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class, DEPLOY + ".war")
				.addPackage(SportLogicService.class.getPackage())
				.addPackage(SportPersistence.class.getPackage())
				.addPackage(SportEntity.class.getPackage())
				.addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("META-INF/beans.xml", "beans.xml");
	}

	@Inject
	private ISportLogicService sportLogicService;
	
	@Inject
	private ISportPersistence sportPersistence;	

	@Before
	public void configTest() {
		try {
			clearData();
			insertData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void clearData() {
		List<SportDTO> dtos=sportPersistence.getSports();
		for(SportDTO dto:dtos){
			sportPersistence.deleteSport(dto.getId());
		}
	}

	private List<SportDTO> data=new ArrayList<SportDTO>();

	private void insertData() {
		for(int i=0;i<3;i++){
			SportDTO pdto=new SportDTO();
			pdto.setName(generateRandom(String.class));
			pdto.setMinAge(generateRandom(int.class));
			pdto.setMaxAge(generateRandom(int.class));
			pdto=sportPersistence.createSport(pdto);
			data.add(pdto);
		}
	}
	
	@Test
	public void createSportTest(){
		SportDTO ldto=new SportDTO();
		ldto.setName(generateRandom(String.class));
		ldto.setMinAge(generateRandom(int.class));
		ldto.setMaxAge(generateRandom(int.class));
		
		
		SportDTO result=sportLogicService.createSport(ldto);
		
		Assert.assertNotNull(result);
		
		SportDTO pdto=sportPersistence.getSport(result.getId());
		
		Assert.assertEquals(ldto.getName(), pdto.getName());	
		Assert.assertEquals(ldto.getMinAge(), pdto.getMinAge());	
		Assert.assertEquals(ldto.getMaxAge(), pdto.getMaxAge());	
	}
	
	@Test
	public void getSportsTest(){
		List<SportDTO> list=sportLogicService.getSports();
		Assert.assertEquals(list.size(), data.size());
        for(SportDTO ldto:list){
        	boolean found=false;
            for(SportDTO pdto:data){
            	if(ldto.getId()==pdto.getId()){
                	found=true;
                }
            }
            Assert.assertTrue(found);
        }
	}
	
	@Test
	public void getSportTest(){
		SportDTO pdto=data.get(0);
		SportDTO ldto=sportLogicService.getSport(pdto.getId());
        Assert.assertNotNull(ldto);
		Assert.assertEquals(pdto.getName(), ldto.getName());
		Assert.assertEquals(pdto.getMinAge(), ldto.getMinAge());
		Assert.assertEquals(pdto.getMaxAge(), ldto.getMaxAge());
        
	}
	
	@Test
	public void deleteSportTest(){
		SportDTO pdto=data.get(0);
		sportLogicService.deleteSport(pdto.getId());
        SportDTO deleted=sportPersistence.getSport(pdto.getId());
        Assert.assertNull(deleted);
	}
	
	@Test
	public void updateSportTest(){
		SportDTO pdto=data.get(0);
		
		SportDTO ldto=new SportDTO();
		ldto.setId(pdto.getId());
		ldto.setName(generateRandom(String.class));
		ldto.setMinAge(generateRandom(int.class));
		ldto.setMaxAge(generateRandom(int.class));
		
		
		sportLogicService.updateSport(ldto);
		
		
		SportDTO resp=sportPersistence.getSport(pdto.getId());
		
		Assert.assertEquals(ldto.getName(), resp.getName());	
		Assert.assertEquals(ldto.getMinAge(), resp.getMinAge());	
		Assert.assertEquals(ldto.getMaxAge(), resp.getMaxAge());	
	}
	
	public <T> T generateRandom(Class<T> objectClass){
		Random r=new Random();
		if(objectClass.isInstance(String.class)){
			String s="";
			for(int i=0;i<10;i++){
				char c=(char)(r.nextInt()/('Z'-'A')+'A');
				s=s+c;
			}
			return objectClass.cast(s);
		}else if(objectClass.isInstance(Integer.class)){
			Integer s=r.nextInt();
			return objectClass.cast(s);
		}else if(objectClass.isInstance(Long.class)){
			Long s=r.nextLong();
			return objectClass.cast(s);
		}else if(objectClass.isInstance(java.util.Date.class)){
			java.util.Calendar c=java.util.Calendar.getInstance();
			c.set(java.util.Calendar.MONTH, r.nextInt()/12);
			c.set(java.util.Calendar.DAY_OF_MONTH,r.nextInt()/30);
			c.setLenient(false);
			return objectClass.cast(c.getTime());
		} 
		return null;
	}
	
}