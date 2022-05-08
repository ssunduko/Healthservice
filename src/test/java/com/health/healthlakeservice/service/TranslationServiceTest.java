package com.health.healthlakeservice.service;

import com.health.healthlakeservice.HealthServiceApplication;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.net.util.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.mockserver.client.MockServerClient;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest(classes= HealthServiceApplication.class)
/**
 *
 */
public class TranslationServiceTest {

    static MockServerClient mockServerClient;


    @BeforeAll
    static void init(){

        WebDriverManager.chromedriver().setup();
        /*mockServerClient = new MockServerClient("54.160.224.178", 1080);

        mockServerClient.when(
                        request()
                                .withMethod("POST")
                                .withPath("/validate"),
                        exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(401)
                );

        mockServerClient.when(
                        request()
                                .withMethod("POST")
                                .withPath("/emotionalize"),
                        exactly(1)
                )
                .respond(
                        response()
                                .withBody("NEUTRAL")
                );*/
    }

    public void whenMockPostRequestForEmotion_thenNeutral() throws IOException {

        HttpResponse response = this.hitTheMockServerWithSentimentRequest();
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        Assertions.assertEquals("NEUTRAL", responseString);
    }

    @Test
    public void whenRealPostRequestForEmotion_thenNegative() throws IOException {

        HttpResponse response = this.hitTheRealServerWithSentimentRequest();
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        Assertions.assertEquals("NEGATIVE", responseString);
    }

    @Test
    public void whenRealPostRequestForTranslate_thenTranslation() throws IOException {

        HttpResponse response = this.hitTheRealServerWithTranslateRequest();
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        Assertions.assertEquals(responseString,"Diagnóstico número uno de linfoma difuso de células grandes, estadio 38 número dos de rodillas ME, enfermedad crónica número tres, neutropenia quimioterapia número cuatro, disfasia número cinco, hipopotasemia en la sexta clínica o casi arterial hoy para continuar con la evaluación de su período de metformina. Él, lamentablemente, no tiene que someterse a quimioterapia hasta este viernes, no está claro por qué estaba programado para este período de día. En las últimas semanas, tuvo que ser llevado a un hospital de urgencias 2. Aparentemente tenía una hipopotasemia importante, aunque no tenemos registros, aunque parece que solo ha sido reemplazado por un aura, un potasio distinto del potasio intravenoso. También se dijo que citó un toque de neumonía y citó que aparentemente recibió Z. Pak. Ha mejorado. La arteria carótida aparentemente nunca ha tomado la temperatura en casa, por lo que no sabemos si hoy tuvo fiebre, se siente bastante bien. Su principal queja hoy, además de ser un rostro que ha mejorado más su entumecimiento en el brazo izquierdo que el del brazo derecho. Temperatura objetivo 97.9, BP 154 69 frecuencia cardíaca que pesa 85 libras en el examen de hoy todavía tiene una masa en el espacio retroparenteral. Los laboratorios de hoy en día, las pruebas químicas y funcionales del hígado son normales, excepto glucosa 1 16, el potasio es normal y 4.5. Si trabaja a 7,6, hemoglobina 15,5, hematocrito 46,8, recuento de plaquetas 2 66 diferencial 68,6% fase auricular, general caballero con linfoma de células B grandesDiagnóstico número uno linfoma difuso de células grandes, estadio 38 número dos rodillas ME, enfermedad crónica número tres, neutropenia quimioterapia número cuatro, disfasia número cinco, hipopotasemia en la sexta clínica o casi arterial hoy para continuar la evaluación de su período de metformina. Él, lamentablemente, no tiene que someterse a quimioterapia hasta este viernes, no está claro por qué estaba programado para este período de día. En las últimas semanas, tuvo que ser llevado a un hospital de urgencias 2. Aparentemente tenía una hipopotasemia importante, aunque no tenemos registros, aunque parece que solo ha sido reemplazado por un aura, un potasio distinto del potasio intravenoso. También se dijo que citó un toque de neumonía y citó que aparentemente recibió Z. Pak. Ha mejorado. La arteria carótida aparentemente nunca ha tomado la temperatura en casa, por lo que no sabemos si hoy tuvo fiebre, se siente bastante bien. Su principal queja hoy, además de ser un rostro que ha mejorado más su entumecimiento en el brazo izquierdo que el del brazo derecho. Temperatura objetivo 97.9, BP 154 69 frecuencia cardíaca que pesa 85 libras en el examen de hoy todavía tiene una masa en el espacio retroparenteral. Los laboratorios de hoy en día, las pruebas químicas y funcionales del hígado son normales, excepto glucosa 1 16, el potasio es normal y 4.5. Si trabaja a 7,6, hemoglobina 15,5, hematocrito 46,8, recuento de plaquetas 2 66 diferencial 68,6% fase auricular, general caballero con linfoma de células B grandes");
    }

    public void whenMockPostRequestForInvalidAuth_then401Received() throws IOException {

        HttpResponse response = this.hitTheMockServerWithValidationRequest();
        Assertions.assertEquals(401, response.getStatusLine().getStatusCode());
    }

    private HttpResponse hitTheMockServerWithValidationRequest() throws IOException {

        String url = "http://54.160.224.178:1080/validate";
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-type", "application/json");

        StringEntity stringEntity = new StringEntity("{username: 'foo', password: 'bar'}");
        post.getRequestLine();
        post.setEntity(stringEntity);

        return client.execute(post);

    }

    private HttpResponse hitTheMockServerWithSentimentRequest() throws IOException {

        String url = "http://54.160.224.178:1080/emotionalize";
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-type", "text/plain");

        StringEntity stringEntity = new StringEntity("Just Some Text");
        post.getRequestLine();
        post.setEntity(stringEntity);

        return client.execute(post);
    }

    private HttpResponse hitTheRealServerWithSentimentRequest() throws IOException {

        String url = "https://4m0t4oye4j.execute-api.us-east-1.amazonaws.com/prod/emotionalize";
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        byte[] credentials = Base64.encodeBase64(("user" + ":" + "capstone").getBytes(StandardCharsets.UTF_8));
        post.setHeader("Content-type", "text/plain");
        post.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));

        StringEntity stringEntity = new StringEntity("I am feeling really bad");
        post.getRequestLine();
        post.setEntity(stringEntity);

        return client.execute(post);
    }

    private HttpResponse hitTheRealServerWithTranslateRequest() throws IOException {

        String url = "https://4m0t4oye4j.execute-api.us-east-1.amazonaws.com/prod/translate?destinationLanguage=SPANISH";
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        byte[] credentials = Base64.encodeBase64(("user" + ":" + "capstone").getBytes(StandardCharsets.UTF_8));
        post.setHeader("Content-type", "text/plain");
        post.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));

        StringEntity stringEntity = new StringEntity("Diagnosis number one diffuse large cell lymphoma, stage 38 number two knees ME, chronic disease number three, neutropenia chemotherapy number four, dysphasia number five, hypokalemia at the sixth or near-arterial clinic today to continue the evaluation of his metformin period. He, unfortunately, does not have to undergo chemotherapy until this Friday, it is not clear why he was scheduled for this day period. In the past few weeks, he had to be taken to an emergency hospital 2. He apparently had significant hypokalemia, although we have no records, although it seems that he has only been replaced by an aura, a potassium other than intravenous potassium. It was also said that he quoted a touch of pneumonia and quoted apparently received Z. Pak. It's improved. The carotid artery has apparently never taken the temperature at home, so we do not know if he had a fever today, feels pretty good. His main complaint today, in addition to being a face that has improved his numbness in his left arm more than that of the right arm. Objective temperature 97.9, BP 154 69 heart rate weighing 85 pounds on the exam today still has a mass in the retroparenteral space. Laboratories from today, chemical and functional liver tests are normal, except glucose 1 16, potassium is normal and 4.5. If you work at 7.6, hemoglobin 15.5, hematocrit 46.8, platelet count 2 66 differential 68.6% atrial phase, general gentleman with large B cell lymphomaDiagnosis number one diffuse large cell lymphoma, stage 38 number two knees ME, chronic disease number three, neutropenia chemotherapy number four, dysphasia number five, hypokalemia at the sixth or near-arterial clinic today to continue the evaluation of his metformin period. He, unfortunately, does not have to undergo chemotherapy until this Friday, it is not clear why he was scheduled for this day period. In the past few weeks, he had to be taken to an emergency hospital 2. He apparently had significant hypokalemia, although we have no records, although it seems that he has only been replaced by an aura, a potassium other than intravenous potassium. It was also said that he quoted a touch of pneumonia and quoted apparently received Z. Pak. It's improved. The carotid artery has apparently never taken the temperature at home, so we do not know if he had a fever today, feels pretty good. His main complaint today, in addition to being a face that has improved his numbness in his left arm more than that of the right arm. Objective temperature 97.9, BP 154 69 heart rate weighing 85 pounds on the exam today still has a mass in the retroparenteral space. Laboratories from today, chemical and functional liver tests are normal, except glucose 1 16, potassium is normal and 4.5. If you work at 7.6, hemoglobin 15.5, hematocrit 46.8, platelet count 2 66 differential 68.6% atrial phase, general gentleman with large B cell lymphoma");
        post.getRequestLine();
        post.setEntity(stringEntity);

        return client.execute(post);
    }

    @Test
    public void login() throws InterruptedException, MalformedURLException {

        DesiredCapabilities capability = new DesiredCapabilities();
        capability.setBrowserName("chrome");
        capability.setPlatform(Platform.LINUX);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=en");
        options.merge(capability);

        WebDriver driver = new RemoteWebDriver(new URL("http://54.160.224.178:4444/wd/hub"), capability);
        driver.get("https://capstone.onestepprojects.org/fund");
        driver.manage().window().setSize(new Dimension(1294, 774));
        driver.findElement(By.cssSelector(".login-button")).click();
        driver.findElement(By.cssSelector(".panel #signInFormUsername")).click();
        driver.findElement(By.cssSelector(".panel #signInFormUsername")).sendKeys("lacnus@mailpoof.com");
        driver.findElement(By.cssSelector(".panel #signInFormPassword")).click();
        driver.findElement(By.cssSelector(".panel #signInFormPassword")).sendKeys("Harvard123?");
        driver.findElement(By.cssSelector(".panel-left-border")).click();
        driver.findElement(By.cssSelector(".panel .cognito-asf > .btn")).click();
        Thread.sleep(5000);
        driver.quit();
    }
}
