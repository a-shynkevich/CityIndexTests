package com.cityindex.exception;

import com.cityindex.json.TestCaseInfo;
import com.cityindex.manager.TestManager;
import com.cityindex.param.ParamsParser;
import com.sofment.testhelper.driver.ios.models.IDevice;


public class TestException extends Exception{

    private final IDevice iDevice;
    private String errorMessage;

    public TestException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage.replaceAll(" ", "_").replaceAll("\\.", "").replaceAll(",", "");
        iDevice = TestManager.getInstance().getIDevice(ParamsParser.getInstance().getDeviceUuid());
    }

    public TestException failTest() {
        stopApplication(true);
        StackTraceElement[] stackTrace = this.getStackTrace();
        iDevice.i("Exception in thread \"main\" " + TestException.class.getName() + ": " + this.getMessage());
        for(StackTraceElement stackTraceElement : stackTrace) {
            iDevice.i(stackTraceElement.toString());
        }
        return this;
    }

    private void stopApplication(boolean isFail) {
        IDevice iDevice = TestManager.getInstance().getIDevice(ParamsParser.getInstance().getDeviceUuid());
        if((iDevice) != null) {
            //ScreenShotTaker.getInstance().takeScreenShot((isFail ? "FAIL" : "RETEST") + "_" + this.errorMessage);
            stopApplication();
        }
        TestCaseInfo testCaseInfo = TestManager.getInstance().getTestCaseInfo();
        if(testCaseInfo != null)
            testCaseInfo.writeResult(ParamsParser.getInstance().getPathToResultsFolder() + "/result.json");
    }

    public TestException retest() {
        stopApplication(false);
        StackTraceElement[] stackTrace = this.getStackTrace();
        iDevice.i("Exception in thread \"main\" " + TestException.class.getName() + ": " + this.getMessage());
        for(StackTraceElement stackTraceElement : stackTrace) {
            iDevice.i(stackTraceElement.toString());
        }
        return this;
    }

    private void stopApplication() {
        IDevice iDevice = TestManager.getInstance().getIDevice(ParamsParser.getInstance().getDeviceUuid());
        if((iDevice) != null) {
            iDevice.stopApplication();
        }
    }

//    public String getFormattedTime() {
//        SimpleDateFormat simpleFormatter = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
//        return simpleFormatter.format(System.currentTimeMillis());
//    }
//
//    public String takeScreenShot(IDevice iDevice, String name) {
//        String screenShotName = getFormattedTime() + " " + name;
//        iDevice.takeScreenShot(screenShotName);
//        File file = waitForScreenshotCreated(ParamsParser.getInstance().getPathToResultsFolder() + "/" + screenShotName + ".png", 5000);
//        return file != null ? file.getAbsolutePath() : ParamsParser.getInstance().getPathToResultsFolder() + "/" + screenShotName + ".png";
//    }
//
//    private File waitForScreenshotCreated(String screenShotName, long timeoutMs) {
//        long start = System.currentTimeMillis();
//        File file = new File(screenShotName);
//        while (!file.exists()) {
//            if(System.currentTimeMillis() - start > timeoutMs) return file;
//        }
//        return file;
//    }
}
