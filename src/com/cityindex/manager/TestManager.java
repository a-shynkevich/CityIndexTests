package com.cityindex.manager;

import com.cityindex.constants.Constants;
import com.cityindex.exception.TestException;
import com.cityindex.json.TestCaseInfo;
import com.cityindex.param.ParamsParser;
import com.cityindex.utils.LoggerUtil;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.models.IDevice;
import com.sofment.testhelper.property.PropertiesManager;

import java.awt.*;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.util.Random;

public class TestManager {
    private TestHelper testHelper;
    private static TestManager testManager;
    private IDevice iDevice;
    private String pathToBuild;
    public TestCaseInfo testCaseInfo;
    private String login = "";
    private String pass = "";
    private PropertiesManager propertiesManager;
    private ParamsParser paramsParser;
    private String randomShelfName = null;
    private static ITestManager $iTestManager;

    public interface ITestManager {
        void testManagerTearDown();
    }

    private TestManager () {
        testHelper = new TestHelper();
        testCaseInfo = new TestCaseInfo();
//        testCaseInfo.setStatusId(Status.FAILED);
        initProperties();
    }

    public void setObjectITestManager(ITestManager $iTestManager) {
        TestManager.$iTestManager = $iTestManager;
    }

    private void initProperties() {
        propertiesManager = testHelper.getPropertiesManager("properties/test.properties");
        paramsParser = ParamsParser.getInstance();
        pathToBuild = paramsParser.getPathToBuild() == null ?
                propertiesManager.getProperty("PATH_TO_BUILD") :
                paramsParser.getPathToBuild();
        login = propertiesManager.getProperty("LOGIN");
        pass = propertiesManager.getProperty("PASSWORD");
    }

    public static String getRandomString(int length) {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder( length );
        for(int i = 0; i < length; i++) {
            sb.append(Constants.AB.charAt(rnd.nextInt(Constants.AB.length())));
        }
        return sb.toString();
    }

    public String getTestProperty(String key) {
        return propertiesManager.getProperty(key);
    }

    public static TestManager getInstance() {
        if(testManager == null)
            testManager = new TestManager();
        return testManager;
    }

    public void writeResult() {
        testCaseInfo.writeResult(ParamsParser.getInstance().getPathToResultsFolder() + "/result.json");
    }

    public ParamsParser getParamsParser() {
        return paramsParser;
    }

    public PropertiesManager getPropertiesManager() {
        return propertiesManager;
    }

    public IDevice getIDevice(String UUID) {
        if(iDevice == null) {
            iDevice = testHelper.getIOsDriver().getDevice(UUID, new IDevice.Config()
                    .setPathToResultsFolder(ParamsParser.getInstance().getPathToResultsFolder())
                    .setScreenShotsFolder(ParamsParser.getInstance().getPathToResultsFolder()));
            if(iDevice == null) {
                System.out.println("Device with UUID: " + UUID + " is not found... ");
                System.exit(0);
            }
            iDevice.i("Path to results folder: " + ParamsParser.getInstance().getPathToResultsFolder());
            File file = new File(ParamsParser.getInstance().getPathToResultsFolder());
            iDevice.i("result folder exists: " + file.exists());
            LoggerUtil.setIDevice(iDevice);
        }
        return iDevice;
    }

    public TestHelper getTestHelper() {
        return testHelper;
    }

    public String getPathToBuild() {
        return pathToBuild;
    }

//    public void setPathToBuild(String pathToBuild) {
//        this.pathToBuild = pathToBuild;
//    }


    public void setTestCaseInfo(TestCaseInfo testCaseInfo) {
        this.testCaseInfo = testCaseInfo;
    }

    public TestCaseInfo getTestCaseInfo() {
        return testCaseInfo;
    }

    public void retest(String message) throws TestException {
        testCaseInfo.setMessage(message, false);
        testCaseInfo.setStatusId(4);
        $iTestManager.testManagerTearDown();
        throw new TestException(message).retest();
    }

    public void failTest(String message) throws TestException {
        testCaseInfo.setMessage(message, true);
        testCaseInfo.setStatusId(5);
        $iTestManager.testManagerTearDown();
        throw new TestException(message).failTest();
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public void addStep(String step) {
        iDevice.i("########################################################");
        iDevice.i("STEP: " + step);
//        ScreenShotTaker.getInstance().takeScreenShot("before_" + step.replace("\"? ?\\.?,?:?", "_").replaceAll("\"?'?", ""));
        testCaseInfo.addStep(step);
        iDevice.i("########################################################");
    }

    /*** comparing 2 images
     *  method arguments should be string value of the file path
     *  that should consist of /data/local/tmp/filename.png
     *  where filename.png is the name of your screenshot image
     ***/
    public boolean compareTwoImages(String img1path, String img2path) throws TestException {
        if(!new File(img1path).exists() || !new File(img2path).exists())
            testManager.retest("Some of screenshots not found:\n" +
                    img1path + "\n" +
                    img2path);
        Image image1 = Toolkit.getDefaultToolkit().getImage(img1path);
        Image image2 = Toolkit.getDefaultToolkit().getImage(img2path);

        try {
            PixelGrabber grab1 = new PixelGrabber(image1, 0, 0, -1, -1, false);
            PixelGrabber grab2 = new PixelGrabber(image2, 0, 0, -1, -1, false);
            int[] data1 = null;

            if (grab1.grabPixels()) {
                int width = grab1.getWidth();
                int height = grab1.getHeight();
                data1 = new int[width * height];
                data1 = (int[]) grab1.getPixels();
            }

            int[] data2 = null;

            if (grab2.grabPixels()) {
                int width = grab2.getWidth();
                int height = grab2.getHeight();
                data2 = new int[width * height];
                data2 = (int[]) grab2.getPixels();
            }
            iDevice.i("Pixels equal: " + java.util.Arrays.equals(data1, data2));
            return java.util.Arrays.equals(data1, data2);

        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        throw new TestException("Error in compare image").retest();
    }
}
