package co.edu.uniandes.sport.service.subsystem.web.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import co.edu.uniandes.csw.sport.logic.api.ISportLogicService;
import co.edu.uniandes.csw.sport.logic.dto.SportDTO;
import co.edu.uniandes.csw.sport.logic.mock.SportMockLogicService;
import co.edu.uniandes.csw.sport.persistence.SportPersistence;
import co.edu.uniandes.csw.sport.persistence.entity.SportEntity;
import co.edu.uniandes.csw.sport.service.SportService;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertTrue;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SportServiceTest {

    @Deployment
	public static WebArchive createDeployment() {
            /*File[] libsDomain = Maven.resolver().loadPomFromFile("pom.xml").resolve("de.ab23.bear:ab23BearDomain").withTransitivity().asFile();  
            EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "test.ear").addAsLibraries(libsDomain);
	return ear;	*/
            WebArchive war=ShrinkWrap.create(WebArchive.class, "prueba.war");
            war.merge(ShrinkWrap.create(WebArchive.class, "prueba.war")
                    .addPackage(SportService.class.getPackage())
                    .addPackage(ISportLogicService.class.getPackage())
                    .addPackage(SportMockLogicService.class.getPackage())
				
            .as(ExplodedImporter.class).importDirectory("src/main/webapp").as(GenericArchive.class));
            return war;
            
	}
    
    
    @Test
    public void createSportTest() {
        try {
            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:8181/prueba/webresources/Sport");
            SportDTO newSport = new SportDTO();
            newSport.setName("aaaa");
            ObjectMapper map = new ObjectMapper();
            String resp = webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(String.class, map.writeValueAsString(newSport));
            String[] resNoId = resp.split(",");
            String[] expected = ("{\"id:\"\"" + 0 + "\",\"name\":\"" + newSport.getName() + "\",\"minAge\":" + newSport.getMinAge() + ",\"maxAge\":" + newSport.getMaxAge() + "}").split(",");
            System.err.println(resp + "<>{id:\"" + 0+ "\",name:\"" + newSport.getName() + "\",minAge:\"" + newSport.getMinAge() + "\",maxAge:\"" + newSport.getMaxAge() + "\"}");
            try {
                for (int i = 1; i < resNoId.length; i++) {
                    System.err.println(resNoId[i] + "<>" + expected[i]);
                    assertTrue(resNoId[i].equals(expected[i]));
                }

            } catch (Exception e) {
                assertTrue(false);
            }

        } catch (IOException ex) {
           ex.printStackTrace();
        }
    }

    @Test
    public void updateSportTest() {
        try {

            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:8181/prueba/webresources/Sport");
            SportDTO newSport = new SportDTO();
            newSport.setName("aaaamod");
            ObjectMapper map = new ObjectMapper();
            String resp = webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(String.class, map.writeValueAsString(newSport));
            String id = resp.split(",")[0].split(":")[1];

            WebResource webResourcem = client.resource("http://localhost:8181/prueba/webresources/Sport/" + id);
            SportDTO newSportm = new SportDTO();
            newSportm.setId(Long.parseLong(id));

            newSportm.setName("aaaa22");
            ObjectMapper mapm = new ObjectMapper();
            ClientResponse rsp = webResourcem.type(MediaType.APPLICATION_JSON).put(ClientResponse.class, mapm.writeValueAsString(newSportm));
            System.err.println("<<>> " + rsp.getClientResponseStatus().name());
            assertTrue(rsp.getClientResponseStatus().equals(rsp.getClientResponseStatus().NO_CONTENT));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void deleteSportTest() {
        try {

            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:8181/prueba/webresources/Sport");
            SportDTO newSport = new SportDTO();
            newSport.setName("aaaadel");
            ObjectMapper map = new ObjectMapper();
            String resp = webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(String.class, map.writeValueAsString(newSport));
            String id = resp.split(",")[0].split(":")[1];

            WebResource webResourcem = client.resource("http://localhost:8181/prueba/webresources/Sport/" + id);

            ClientResponse rsp = webResourcem.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
            System.err.println("<<>> " + rsp.getClientResponseStatus().name());
            assertTrue(rsp.getClientResponseStatus().equals(rsp.getClientResponseStatus().NO_CONTENT));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void getSportTest() {
        try {
            
            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:8181/prueba/webresources/Sport");
            SportDTO newSport = new SportDTO();
            newSport.setName("aaaaget");
            ObjectMapper map = new ObjectMapper();
            String resp = webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(String.class, map.writeValueAsString(newSport));
            System.err.println("_>"+resp);
            String id = resp.split(",")[0].split(":")[1];
            String expected = ("{\"id\":" + id + ",\"name\":\"" + newSport.getName() + "\",\"minAge\":" + newSport.getMinAge() + ",\"maxAge\":" + newSport.getMaxAge() + "}");

            WebResource webResourcem = client.resource("http://localhost:8181/prueba/webresources/Sport/" + id);

            String respget = webResourcem.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get(String.class);
            System.err.println(">>> " + respget);
            assertTrue(expected.equals(respget));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void getAllSportTest() {
        try {
            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:8181/prueba/webresources/Sport");
            String s = webResource.accept(MediaType.APPLICATION_JSON).get(String.class);
            assertTrue(s.length()>= 2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
