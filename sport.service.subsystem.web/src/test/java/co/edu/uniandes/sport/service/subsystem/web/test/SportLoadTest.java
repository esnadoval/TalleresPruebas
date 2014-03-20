/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.sport.service.subsystem.web.test;

import co.edu.uniandes.csw.sport.logic.api.ISportLogicService;
import co.edu.uniandes.csw.sport.logic.dto.SportDTO;
import co.edu.uniandes.csw.sport.logic.ejb.SportLogicService;
import co.edu.uniandes.csw.sport.logic.mock.SportMockLogicService;
import co.edu.uniandes.csw.sport.persistence.SportPersistence;
import co.edu.uniandes.csw.sport.persistence.api.ISportPersistence;
import co.edu.uniandes.csw.sport.persistence.entity.SportEntity;
import co.edu.uniandes.csw.sport.service.SportService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 *
 * @author asistente
 */
@RunWith(Arquillian.class)
public class SportLoadTest {

    public static String APACHE_JMETER_GUI_PATH = "C:\\Users\\asistente\\Desktop\\apache-jmeter-2.11";
    
    @Deployment
	public static WebArchive createDeployment() {
            /*File[] libsDomain = Maven.resolver().loadPomFromFile("pom.xml").resolve("de.ab23.bear:ab23BearDomain").withTransitivity().asFile();  
            EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "test.ear").addAsLibraries(libsDomain);
	return ear;	*/
            WebArchive war=ShrinkWrap.create(WebArchive.class, "prueba.war");
            war.merge(ShrinkWrap.create(WebArchive.class, "prueba.war")
                    .addPackage(SportService.class.getPackage())
                    .addPackage(ISportLogicService.class.getPackage())
                    .addPackage(SportLogicService.class.getPackage())
                    .addPackage(ISportPersistence.class.getPackage())
                    .addPackage(SportPersistence.class.getPackage())
                    .addPackage(SportDTO.class.getPackage())
                    .addPackage(SportEntity.class.getPackage())
                    .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                    
				
            .as(ExplodedImporter.class).importDirectory("src/main/webapp").as(GenericArchive.class));
            return war;
            
	}
    

    @Test

    public void runSportLoadTest() {
        
         try {
            
            System.err.println("->Preparing Test Data...");
            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:8181/prueba/webresources/Sport");
            for (long i = 0; i < 100; i++) {
                PodamFactory factory = new PodamFactoryImpl(); //This will use the default Random Data Provider Strategy
                SportDTO newSport = factory.manufacturePojo(SportDTO.class);
                //newSport.setId(i);
                //System.out.println("CREATED=> " + newSport.getId() + " - " + newSport.getName());
                ObjectMapper map = new ObjectMapper();
                webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(String.class, map.writeValueAsString(newSport));

            }
            System.err.println("->Completed 100 test data.");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
         
         
        File fil = new File("src/test/resources/SportLoadTest.jmx");
        File log = new File("src/test/resources/SportLoadTestReport.txt");
        try {
            System.err.println("->Running JMX Script, Please Wait.......");
            Runtime.getRuntime().exec(APACHE_JMETER_GUI_PATH + "\\bin\\jmeter.bat -n -t "+ fil.getAbsolutePath()+ " -l "+log.getAbsolutePath());
            sleep(20000);
            
        } catch (Exception ex) {
            Logger.getLogger(SportLoadTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
