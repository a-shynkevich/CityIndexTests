package com.cityindex.model;

import com.bn.idevices.interfaces.ConsoleReader;
import com.cityindex.exception.TestException;
import com.cityindex.json.Status;
import com.cityindex.manager.TestManager;
import com.cityindex.param.ParamsParser;
import com.cityindex.utils.ScreenShotTaker;
import com.sofment.testhelper.driver.ios.models.IDevice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestCase {

    private TestCaseConfig testCaseConfig;
    private FileWriter fileWriter = null;

    public TestCase (TestCaseConfig testCaseConfig) {
        this.testCaseConfig = testCaseConfig;
    }

    public interface TestCaseConfig {
        boolean isReinstallApp();
        String getPathToApp();
        void mainLogic() throws TestException;
        void tearDown() throws TestException;
    }

    public void start() throws TestException, IOException {
        TestManager testManager = TestManager.getInstance();
        final IDevice iDevice = testManager.getIDevice(ParamsParser.getInstance().getDeviceUuid());
        if(iDevice == null) {
            throw new TestException("device with uuid: " + ParamsParser.getInstance().getDeviceUuid() + " is not found").retest();
        }
//        String pathToBuild = testCaseConfig.getPathToApp();
//        if(testCaseConfig.isReinstallApp()) {
//            iDevice.uninstallApplication(pathToBuild);
//            iDevice.installApplication(pathToBuild);
//        }
//        iDevice.launchApplication(pathToBuild);

        final File deviceConsoleFile = new File(ParamsParser.getInstance().getPathToResultsFolder() + "/deviceConsole.log");
        if(deviceConsoleFile.exists()) deviceConsoleFile.delete();

        new Thread(new Runnable() {
            @Override
            public void run() {
                iDevice.readDeviceConsole(new ConsoleReader() {
                    @Override
                    public void readLine(String line) {
                        try {
                            fileWriter = new FileWriter(deviceConsoleFile, true);
                            fileWriter.append(line + "\n");
                            fileWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public boolean isStopReading() {
                        return false;
                    }
                });
            }
        }).start();

        testManager.testCaseInfo.setStatusId(Status.RETEST);
        testManager.testCaseInfo.writeResult(ParamsParser.getInstance().getPathToResultsFolder() + "/result.json");
        testCaseConfig.mainLogic();
        if(testManager.testCaseInfo.getStatusId() == Status.PASSED) {
            ScreenShotTaker.getInstance().takeScreenShot("pass");
        }
        testCaseConfig.tearDown();

        iDevice.stopApplication();
        testManager.writeResult();
//        TestManager.getInstance().getTestCaseInfo().writeResult(ParamsParser.getInstance().getPathToResultsFolder() + "/result.json");
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
