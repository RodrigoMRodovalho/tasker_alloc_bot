package br.com.rrodovalho.taskerbot.bot;

import br.com.rrodovalho.taskerbot.domain.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.InvalidObjectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rrodovalho on 13/04/16.
 */
public class TaskerBot {

    private Tasker taskerInfo;
    private WebDriver mDriver;
    private final static String CHROME_DRIVER_PATH ="/home/rrodovalho/Downloads/chromedriver";
    public final static String BASE_URL = "https://muxi.tasker.com.br/tasker/T5";
    public final static String USER_LOGIN_INPUT_ELEMENT_ID = "txtUsuario";
    public final static String USER_PASSWORD_LOGIN_INPUT_ELEMENT_ID = "txtSenha";
    public final static String LOGIN_SUBMIT_ELEMENT_ID = "botaoDeLogin";

    private List<Integer> sucessIndexes;
    private List<Integer> failureIndexes;
    private List<String> failureReasons;

    String PHANTOMJS_BINARY;

    WebElement loginElement;
    WebElement registroEsforco;
    WebElement incluirRegistro;
    WebElement finalHora;

    public TaskerBot(Tasker taskerInfo) {
        this.taskerInfo = taskerInfo;
        resolvePhantomJSBinaryFile();
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability("takesScreenshot", false);
        capabilities.setCapability(
                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                PHANTOMJS_BINARY
        );

        mDriver = new PhantomJSDriver(capabilities);
        sucessIndexes = new ArrayList<>();
        failureIndexes = new ArrayList<>();
        failureReasons = new ArrayList<>();

    }

    private void resolvePhantomJSBinaryFile(){

        String osPJSPath = null;
        String osName = System.getProperty("os.name");
        String osNameMatch = osName.toLowerCase();
        if(osNameMatch.contains("linux")) {
            osPJSPath = "phantomjs-2.1.1-linux-x86_64";
        }else if(osNameMatch.contains("windows")) {
            osPJSPath = "phantomjs-2.1.1-windows";
        }else if(osNameMatch.contains("mac")  || osNameMatch.contains("darwin")) {
            osPJSPath = "phantomjs-2.1.1-macosx";
        }

        PHANTOMJS_BINARY = "phantomJS_binaryDriver/"+osPJSPath+"/phantomjs";
    }

    private void doActionWhenIsPossible(WebElementActionAfterDelay delay){

        while (true){
            try{
                delay.action();
                break;
            }
            catch (Exception e){
//             e.printStackTrace();
            }
            finally {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public AllocationResponse alloc() {

        AllocationResponse allocationResponse = new AllocationResponse();
        Failure failure = new Failure();

        try {
            connect();
        } catch (UnknownHostException e) {
            failureIndexes.add(-1);
            failureReasons.add("UnknownHostException problem to get TASKER WEB SITE");
            failure.setIndex(failureIndexes);
            failure.setReason(failureReasons);
            allocationResponse.setFailure(failure);
            return allocationResponse;
        }
        try {
            logIn();
        } catch (InvalidObjectException e) {
            failureIndexes.add(-2);
            failureReasons.add("InvalidObjectException problem to login");
            failure.setIndex(failureIndexes);
            failure.setReason(failureReasons);
            allocationResponse.setFailure(failure);
            return allocationResponse;
        }
        goToEffortRegistration();

        try {
            insertAllocations();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logOut();
        System.out.println("Logout");
        mDriver.quit();
        System.out.println("browser quit");
        if(sucessIndexes.size() > 0){
            allocationResponse.setSuccess(sucessIndexes);
        }
        else{
            allocationResponse.setSuccess(null);
        }

        if(failureIndexes.size() > 0){

            failure.setIndex(failureIndexes);
            failure.setReason(failureReasons);
            allocationResponse.setFailure(failure);
        }
        return allocationResponse;
    }


    private void connect() throws UnknownHostException {

        mDriver.get(BASE_URL);
        if(mDriver.getPageSource().equalsIgnoreCase("Unknown host")){
            throw new UnknownHostException(BASE_URL);
        }
    }

    private void logIn() throws InvalidObjectException {

        doActionWhenIsPossible(new WebElementActionAfterDelay() {

            public void action() {
//                System.out.println("Searching for user input element...");
                loginElement = mDriver.findElement(By.id(USER_LOGIN_INPUT_ELEMENT_ID));
//                System.out.println("Done...");
//                System.out.println("Sending keys for username...");
                loginElement.sendKeys(taskerInfo.getCredential().getUser());
            }
        });

        WebElement passwordElement = mDriver.findElement(By.id(USER_PASSWORD_LOGIN_INPUT_ELEMENT_ID));
        WebElement submitButton = mDriver.findElement(By.id(LOGIN_SUBMIT_ELEMENT_ID));

        if(!(loginElement!=null && passwordElement!=null && submitButton!=null)){
            throw new InvalidObjectException("Invalid login web elements");
        }

        passwordElement.sendKeys(taskerInfo.getCredential().getPass());
        submitButton.click();

    }

    public void logOut(){
        WebElement logOutButton = mDriver.findElement(By.id("id-menu-btnLogout-btnInnerEl"));
        Actions actions = new Actions(mDriver);
        actions.moveToElement(logOutButton);
        actions.click();
        actions.perform();
    }


    private void goToEffortRegistration(){

        doActionWhenIsPossible(new WebElementActionAfterDelay() {
            public void action() {
//                System.out.println("Searching for registroEsforco element...");
                registroEsforco = mDriver.findElement(By.id("id-toolbarbutton-102-btnIconEl"));
//                System.out.println("Done");
                registroEsforco.click();
//                System.out.println("Clicked on registroEsforco element...");
            }
        });
    }

    public void includeEffort(){

        doActionWhenIsPossible(new WebElementActionAfterDelay() {
            public void action() {
//                System.out.println("Searching for incluirRegistro element...");
                incluirRegistro = mDriver.findElement(By.id("btnAddAlocacao-btnInnerEl"));
//                System.out.println("Done");
                incluirRegistro.click();
//                System.out.println("Clicked on incluirRegistro element...");
            }
        });
    }

    public boolean checkErrorAlert(){

        WebElement alertError = null;

       try {
           alertError = mDriver.findElement(By.id("messagebox-1001"));

           if(alertError.isDisplayed()){

               String text = alertError.getText();
               System.out.println(text);
               WebElement botaoAlert = mDriver.findElement(By.id("button-1005-btnIconEl"));
               botaoAlert.click();
               Thread.sleep (2000);
               WebElement cancel = mDriver.findElement(By.id("btnCancel_win-detail-mdlEsforco-btnInnerEl"));
               cancel.click();
               failureReasons.add(text);
               return true;
           }
           else{
               return false;
           }

       }
       catch (NoSuchElementException e){
            return false;

       } catch (InterruptedException e) {
           return false;
       }
    }

    public void insertAllocations() throws InterruptedException {

        int count = 0;

        for(int i=0;i<taskerInfo.getExpedientes().size();i++){

            String data = taskerInfo.getExpedientes().get(i).getData();

            for(int j=0;j<taskerInfo.getExpedientes().get(i).getAlocacoes().size();j++){
                count++;
                includeEffort();
                makeSingleAllocation(data,taskerInfo.getExpedientes().get(i).getAlocacoes().get(j),count);
            }
        }
    }

    public void  makeSingleAllocation(String allocData,Alocacao alocacao,int index) throws InterruptedException {

        doActionWhenIsPossible(new WebElementActionAfterDelay() {
            public void action() {
//                System.out.println("Sending keys for finalHora...");
                finalHora = mDriver.findElement(By.id("ESFHORARIOTERMINO-inputEl"));
                finalHora.click();
                finalHora.sendKeys(alocacao.getTermino());
            }
        });

        WebElement inicioHora = mDriver.findElement(By.id("ESFHORARIOINICIO-inputEl"));
        inicioHora.click();
//        System.out.println("Sending keys for inicioHora...");
        inicioHora.sendKeys(alocacao.getInicio());
//        System.out.println("Done");
        WebElement comentario = mDriver.findElement(By.id("ESFCOMENTARIO"));
//        System.out.println("Click on comentario element...");
//        System.out.println("Sending keys for comentario...");
        comentario.sendKeys(alocacao.getComentario());
        WebElement data = mDriver.findElement(By.id("ESFDATA-inputEl"));
//        System.out.println("Click on data element...");
        data.click();
//        System.out.println("Sending keys for data...");
        data.sendKeys(allocData.replace("/",""));
        WebElement tarefa = mDriver.findElement(By.id("cmp_ESFSOLCOD-inputEl"));
        tarefa.click();
//        System.out.println("Sending keys for tarefa...");
        tarefa.sendKeys(alocacao.getTarefa());
//        System.out.println("Done...");
        comentario.click();

        while (tarefa.getAttribute("value").length() == 6){
            if(checkErrorAlert()){
                failureIndexes.add(index);
                return ;
            }
//            System.out.println("Waiting for tasker job resolution");
            Thread.sleep(1000);
        }

        WebElement confimar = mDriver.findElement(By.id("btnConfirm_win-detail-mdlEsforco-btnInnerEl"));
        Actions actions = new Actions(mDriver);
        actions.moveToElement(confimar);
        actions.click();
        actions.perform();
        Thread.sleep(3000);

        sucessIndexes.add(index);
    }









}
