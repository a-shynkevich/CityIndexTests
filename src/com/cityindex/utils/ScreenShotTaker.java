package com.cityindex.utils;

import com.cityindex.manager.TestManager;
import com.cityindex.param.ParamsParser;
import com.sofment.testhelper.driver.ios.models.IDevice;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by avsupport on 5/22/15.
 */
public class ScreenShotTaker {

    private static ScreenShotTaker screenShotTaker = null;
    private IDevice iDevice = null;

    private ScreenShotTaker() {
        iDevice = TestManager.getInstance().getIDevice(ParamsParser.getInstance().getDeviceUuid());
    }

    public static ScreenShotTaker getInstance() {
        if(screenShotTaker == null) screenShotTaker = new ScreenShotTaker();
        return screenShotTaker;
    }

    public String takeScreenShot(String name) {
        String screenShotName = getFormattedTime() + " " + name;
        iDevice.takeScreenShot(screenShotName);
        File file = waitForScreenshotCreated(ParamsParser.getInstance().getPathToResultsFolder() + "/" + screenShotName + ".png", 5000);
        return file != null ? file.getAbsolutePath() : ParamsParser.getInstance().getPathToResultsFolder() + "/" + screenShotName + ".png";
    }

    private String getFormattedTime(long timeMs) {
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
        return simpleFormatter.format(timeMs);
    }

    private String getFormattedTime() {
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
        return simpleFormatter.format(System.currentTimeMillis());
    }

    private File waitForScreenshotCreated(String screenShotName, long timeoutMs) {
        long start = System.currentTimeMillis();
        File file = new File(screenShotName);
        while (!file.exists()) {
            if(System.currentTimeMillis() - start > timeoutMs) return file;
        }
        return file;
    }
}
