package co.edu.uniandes.csw.sport.web.test;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SportTest {

    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

   

    @Before
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

    }

    @Test
    public void testCreateSport() throws Exception {
        //Comando que indica al navegador ir a la direcci�n de la p�gina web (localhost:8080/sport.web)
        driver.get(baseUrl + "/sport.web/");
        //Comando que realiza click sobre el boton "create" del toolbar. La funci�n 'find' encuentra el control 
        //y posteriormente hace clic en el mismo. La forma en la que se busca  el control es utilizando expresiones xPath ya que
        // los id de los mismos nucna son iguales (se generan con junto con el valor de componentId que var�a).
        driver.findElement(By.xpath("//button[contains(@id,'createButton')]")).click();
        
        //Comando que duerme el Thread del test por 2 segundos para dejar que el efecto 'slide down' de backbone abra el formulario
        // de createSport.
        Thread.sleep(2000);
        
        //Comando que busca el elemento 'name' en el html actual. Posteriormente limpia su contenido (comando clean).
        driver.findElement(By.id("name")).clear();
        //Comando que simula la escritura de un valor en el elemento(sendKeys) con el String de par�metro sobre
        // el elemento encontrado.
        driver.findElement(By.id("name")).sendKeys("123123");
        
       //Comandos para llenar el campo minAge
        driver.findElement(By.id("minAge")).clear();
        driver.findElement(By.id("minAge")).sendKeys("123123");
        
        //Comandos para llenar el campo maxAge
        driver.findElement(By.id("maxAge")).clear();
        driver.findElement(By.id("maxAge")).sendKeys("123123");
        
        //Comando que encuentra y hace clic sobre el boton "Save" del toolbar (una vez mas encontrado por una expresi�n Xpath)
        driver.findElement(By.xpath("//button[contains(@id,'saveButton')]")).click();
        
        //Comando que duerme el thread para esperar el efecto de slide down que abre la lista
        Thread.sleep(2000);
        // Comando que obtiene el div azul de creaci�n exitosa. Si se obtiene, la prueba va bi�n, si no, saldr� un error y la prueba quedar� como f�llida.
        WebElement dialog = driver.findElement(By.xpath("//div[contains(@style,'display: block;')]"));
        // Comando que obtiene la tabla con el elemento que se cre� anteriormente.
        List<WebElement> table = driver.findElements(By.xpath("//table[contains(@class,'table striped')]/tbody/tr"));
        boolean fail = false;
       //Se itera sobre los elementos de la tabla para ver si el nuevo elento creadoEst� en la lista
        for (WebElement webElement : table) {
            List<WebElement> elems = webElement.findElements(By.xpath("td"));

            if (elems.get(0).getText().equals("123123") && elems.get(1).getText().equals("123123") && elems.get(2).getText().equals("123123")) {
                // si se encuentra la fila, la variable 'fail' pasa a true, indicando que el elemento creado esta en la lista.
                fail = true;
            }

        }
        // la prueba es exitosa si se encontr� el dialogo de creaci�n exitosa y el nuevo elemento est� en la lista.
        assertTrue(dialog != null && fail);
    }

    @Test
    public void testUpdateSport() throws Exception {
        driver.get(baseUrl + "/sport.web/");
        driver.findElement(By.linkText("Edit")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("name")).sendKeys("bien");
        driver.findElement(By.id("minAge")).clear();
        driver.findElement(By.id("minAge")).sendKeys("12");
        driver.findElement(By.id("maxAge")).clear();
        driver.findElement(By.id("maxAge")).sendKeys("16");
        driver.findElement(By.xpath("//button[contains(@id,'saveButton')]")).click();
        Thread.sleep(2000);
        WebElement dialog = driver.findElement(By.xpath("//div[contains(@style,'display: block;')]"));
        List<WebElement> table = driver.findElements(By.xpath("//table[contains(@class,'table striped')]/tbody/tr"));
        boolean fail = false;
        for (WebElement webElement : table) {
            List<WebElement> elems = webElement.findElements(By.xpath("td"));

            if (elems.get(0).getText().equals("bien") && elems.get(1).getText().equals("12") && elems.get(2).getText().equals("16")) {
                fail = true;
            }

        }
        assertTrue(dialog != null && fail);
    }

    @Test
    public void testDeleteSport() throws Exception {
        driver.get(baseUrl + "/sport.web/");

        driver.findElement(By.linkText("Delete")).click();
        Thread.sleep(2000);
        try {
            List<WebElement> table = driver.findElements(By.xpath("//table[contains(@class,'table striped')]/tbody/tr"));
            boolean fail = false;
            for (WebElement webElement : table) {
                List<WebElement> elems = webElement.findElements(By.xpath("td"));

                if (elems.get(0).getText().equals("bien") && elems.get(1).getText().equals("12") && elems.get(2).getText().equals("16")) {
                    fail = true;
                }

            }

            WebElement dialog = driver.findElement(By.xpath("//div[contains(@style,'display: block;')]"));
            assertTrue(dialog != null && !fail);
        } catch (Exception e) {
            assertTrue(true);
        }

    }

    @Test
    public void testListSports() throws Exception {
        driver.get(baseUrl + "/sport.web/");
        testCreateSport();
        driver.findElement(By.xpath("//button[contains(@id,'refreshButton')]")).click();
        Thread.sleep(2000);
        List<WebElement> table = driver.findElements(By.xpath("//table[contains(@class,'table striped')]/tbody/tr"));
        boolean fail = false;
        for (WebElement webElement : table) {
            List<WebElement> elems = webElement.findElements(By.xpath("td"));

            if (elems.get(0).getText().equals("123123") && elems.get(1).getText().equals("123123") && elems.get(2).getText().equals("123123")) {
                fail = true;
            }

        }
        assertTrue(fail);
    }

    @Test
    public void testValidateSports() throws Exception {
        driver.get(baseUrl + "/sport.web/");
        driver.findElement(By.xpath("//button[contains(@id,'createButton')]")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("name")).clear();

        driver.findElement(By.id("minAge")).clear();

        driver.findElement(By.id("maxAge")).clear();

        driver.findElement(By.xpath("//button[contains(@id,'saveButton')]")).click();
        Thread.sleep(2000);
        WebElement dialog = driver.findElement(By.xpath("//div[contains(@style,'display: block;')]"));
        List<WebElement> table = driver.findElements(By.xpath("//table[contains(@class,'table striped')]/tbody/tr"));
        boolean fail = false;
        try {
            for (WebElement webElement : table) {
                List<WebElement> elems = webElement.findElements(By.xpath("td"));

                if (elems.get(0).getText().equals("123123") && elems.get(1).getText().equals("123123") && elems.get(2).getText().equals("123123")) {
                    fail = true;
                }

            }
        } catch (Exception e) {
        }

        assertTrue(dialog != null && !fail);
    }
    /*
     public void testValidateSport() throws Exception {
     driver.get(baseUrl + "/sport.web/");
     driver.findElement(By.xpath("//button[contains(@id,'createButton')]")).click();
     driver.findElement(By.id("name")).clear();
     driver.findElement(By.id("name")).sendKeys("123123");
     driver.findElement(By.id("minAge")).clear();
     driver.findElement(By.id("minAge")).sendKeys("123123");
     driver.findElement(By.id("maxAge")).clear();
     driver.findElement(By.id("maxAge")).sendKeys("123123");
     driver.findElement(By.xpath("//button[contains(@id,'saveButton')]")).click();
     }*/

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    
}
