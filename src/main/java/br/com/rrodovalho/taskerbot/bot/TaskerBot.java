package br.com.rrodovalho.taskerbot.bot;

import br.com.rrodovalho.taskerbot.domain.AllocationResponse;
import br.com.rrodovalho.taskerbot.domain.Alocacao;
import br.com.rrodovalho.taskerbot.domain.Failure;
import br.com.rrodovalho.taskerbot.domain.Tasker;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
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
//    public final static String USER = "rrodovalho";
//    public final static String USER_PASS = "computacao@appi7";
    public final static String USER_LOGIN_INPUT_ELEMENT_ID = "txtUsuario";
    public final static String USER_PASSWORD_LOGIN_INPUT_ELEMENT_ID = "txtSenha";
    public final static String LOGIN_SUBMIT_ELEMENT_ID = "botaoDeLogin";

    private List<Integer> sucessIndexes;
    private List<Integer> failureIndexes;
    private List<String> failureReasons;

    public TaskerBot(Tasker taskerInfo) {
        this.taskerInfo = taskerInfo;
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
//        ChromeOptions options= new ChromeOptions();
//        options.addArguments("--no-startup-window");
//
//        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
//        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
//        mDriver = new ChromeDriver(capabilities);
        mDriver = new ChromeDriver();
        sucessIndexes = new ArrayList<>();
        failureIndexes = new ArrayList<>();
        failureReasons = new ArrayList<>();

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
        goToTimeSheet();
        goToEffordRegistration();

        try {
            insertAllocations();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logOut();
        mDriver.quit();
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
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void logIn() throws InvalidObjectException {

        WebElement loginElement = mDriver.findElement(By.id(USER_LOGIN_INPUT_ELEMENT_ID));
        WebElement passwordElement = mDriver.findElement(By.id(USER_PASSWORD_LOGIN_INPUT_ELEMENT_ID));
        WebElement submitButton = mDriver.findElement(By.id(LOGIN_SUBMIT_ELEMENT_ID));

        if(!(loginElement!=null && passwordElement!=null && submitButton!=null)){
            throw new InvalidObjectException("Invalid login web elements");
        }

        loginElement.sendKeys(taskerInfo.getCredential().getUser());
        passwordElement.sendKeys(taskerInfo.getCredential().getPass());
        submitButton.click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void logOut(){
        WebElement registroEsforco = mDriver.findElement(By.id("id-menu-btnLogout-btnInnerEl"));
        try {
            Thread.sleep(3000);
            registroEsforco.click();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void goToTimeSheet(){
        WebElement timeSheet = mDriver.findElement(By.id("id-menu-96-btnIconEl"));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timeSheet.click();
    }

    private void goToEffordRegistration(){

        WebElement registroEsforco = mDriver.findElement(By.id("id-menu-102-textEl"));
        try {
            Thread.sleep(3000);

            registroEsforco.click();

            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void includeEfford(){

        WebElement incluirRegistro = mDriver.findElement(By.id("btnAddAlocacao-btnInnerEl"));
        try {
            Thread.sleep(3000);
            incluirRegistro.click();
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
                includeEfford();
                makeSingleAllocation(data,taskerInfo.getExpedientes().get(i).getAlocacoes().get(j),count);
            }
        }
    }

    public void  makeSingleAllocation(String allocData,Alocacao alocacao,int index) throws InterruptedException {

        WebElement inicioHora = mDriver.findElement(By.id("ESFHORARIOINICIO-inputEl"));
        Thread.sleep(1000);
        inicioHora.sendKeys(alocacao.getInicio());
        Thread.sleep(1000);
        WebElement tarefa = mDriver.findElement(By.id("cmp_ESFSOLCOD-inputEl"));
        tarefa.sendKeys(alocacao.getTarefa());
        Thread.sleep(1000);
        WebElement comentario = mDriver.findElement(By.id("ESFCOMENTARIO"));
        comentario.click();
        Thread.sleep(4000);

        if(checkErrorAlert()){

//            response.append("\nFail to alloc  "+alocacao.toString()+"  at  "+allocData+"  reason:  "+s).toString();
            failureIndexes.add(index);
            return ;
        }
        comentario.sendKeys(alocacao.getComentario());
        WebElement data = mDriver.findElement(By.id("ESFDATA-inputEl"));
        data.click();
        Thread.sleep(6000);
        data.sendKeys(allocData.replace("/",""));
        Thread.sleep(1000);
        WebElement finalHora = mDriver.findElement(By.id("ESFHORARIOTERMINO-inputEl"));
        Thread.sleep(3000);
        finalHora.sendKeys(alocacao.getTermino());
        WebElement confimar = mDriver.findElement(By.id("btnConfirm_win-detail-mdlEsforco-btnInnerEl"));
        confimar.click();
        Thread.sleep(5000);
//        response.append("\nSuccess to alloc  "+alocacao.toString()+"  at  "+allocData).toString();
        sucessIndexes.add(index);
    }









}
