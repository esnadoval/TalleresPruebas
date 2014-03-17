
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
import co.edu.uniandes.csw.sport.persistence.entity.SportEntity;
import java.io.File;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

@RunWith(Arquillian.class)
public class SportPersistenceTest {

	public static final String DEPLOY = "Prueba";

	@Deployment
	public static WebArchive createDeployment() {
            /*File[] libsDomain = Maven.resolver().loadPomFromFile("pom.xml").resolve("de.ab23.bear:ab23BearDomain").withTransitivity().asFile();  
            EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "test.ear").addAsLibraries(libsDomain);
	return ear;	*/
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

	private List<SportEntity> data=new ArrayList<SportEntity>();

	private void insertData() {
		for(int i=0;i<3;i++){
			SportEntity entity=new SportEntity();
			entity.setName(generateRandom(String.class));
			entity.setMinAge(generateRandom(int.class));
			entity.setMaxAge(generateRandom(int.class));
			em.persist(entity);
			data.add(entity);
		}
	}
	
	@Test
	public void createSportTest(){
		SportDTO dto=new SportDTO();
		dto.setName(generateRandom(String.class));
		dto.setMinAge(generateRandom(int.class));
		dto.setMaxAge(generateRandom(int.class));
		
		
		SportDTO result=sportPersistence.createSport(dto);
		
		Assert.assertNotNull(result);
		
		SportEntity entity=em.find(SportEntity.class, result.getId());
		
		Assert.assertEquals(dto.getName(), entity.getName());	
		Assert.assertEquals(dto.getMinAge(), entity.getMinAge());	
		Assert.assertEquals(dto.getMaxAge(), entity.getMaxAge());	
	}
	
	@Test
	public void getSportsTest(){
		List<SportDTO> list=sportPersistence.getSports();
		Assert.assertEquals(list.size(), data.size());
        for(SportDTO dto:list){
        	boolean found=false;
            for(SportEntity entity:data){
            	if(dto.getId()==entity.getId()){
                	found=true;
                }
            }
            Assert.assertTrue(found);
        }
	}
	
	@Test
	public void getSportTest(){
		SportEntity entity=data.get(0);
		SportDTO dto=sportPersistence.getSport(entity.getId());
        Assert.assertNotNull(dto);
		Assert.assertEquals(entity.getName(), dto.getName());
		Assert.assertEquals(entity.getMinAge(), dto.getMinAge());
		Assert.assertEquals(entity.getMaxAge(), dto.getMaxAge());
        
	}
	
	@Test
	public void deleteSportTest(){
		SportEntity entity=data.get(0);
		sportPersistence.deleteSport(entity.getId());
        SportEntity deleted=em.find(SportEntity.class, entity.getId());
        Assert.assertNull(deleted);
	}
	
	@Test
	public void updateSportTest(){
		SportEntity entity=data.get(0);
		
		SportDTO dto=new SportDTO();
		dto.setId(entity.getId());
		dto.setName(generateRandom(String.class));
		dto.setMinAge(generateRandom(int.class));
		dto.setMaxAge(generateRandom(int.class));
		
		
		sportPersistence.updateSport(dto);
		
		
		SportEntity resp=em.find(SportEntity.class, entity.getId());
		
		Assert.assertEquals(dto.getName(), resp.getName());	
		Assert.assertEquals(dto.getMinAge(), resp.getMinAge());	
		Assert.assertEquals(dto.getMaxAge(), resp.getMaxAge());	
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