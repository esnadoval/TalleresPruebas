/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.sport.service.subsystem.web.test;

import co.edu.uniandes.csw.sport.logic.dto.SportDTO;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 *
 * @author asistente
 */
public class SportLoadTest {

    public static String APACHE_JMETER_GUI_PATH = "C:\\Users\\asistente\\Desktop\\apache-jmeter-2.11";
    @Before
    public void prepareTest() {
        try {
            
            System.err.println("->Preparing Test Data...");
            Client client = Client.create();
            WebResource webResource = client.resource("http://localhost:8080/sport.service.subsystem.web/webresources/Sport");
            for (long i = 0; i < 100; i++) {
                PodamFactory factory = new PodamFactoryImpl(); //This will use the default Random Data Provider Strategy
                SportDTO newSport = factory.manufacturePojo(SportDTO.class);
                newSport.setId(i);
                //System.out.println("CREATED=> " + newSport.getId() + " - " + newSport.getName());
                ObjectMapper map = new ObjectMapper();
                webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(String.class, map.writeValueAsString(newSport));

            }
            System.err.println("->Completed 100 test data.");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test

    public void runSportLoadTest() {
        File fil = new File("src/test/resources/SportLoadTest.jmx");
        File log = new File("src/test/resources/SportLoadTestReport.txt");
        try {
            System.err.println("->Running JMX Script, Please Wait.......");
            Runtime.getRuntime().exec(APACHE_JMETER_GUI_PATH + "\\bin\\jmeter.bat -n -t "+ fil.getAbsolutePath()+ " -l "+log.getAbsolutePath());
            
        } catch (IOException ex) {
            Logger.getLogger(SportLoadTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
