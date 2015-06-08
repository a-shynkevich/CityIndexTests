package com.cityindex;

import com.cityindex.manager.TestManager;
import com.cityindex.param.ParamsParser;
import com.cityindex.utils.ScreenShotTaker;
import com.sofment.testhelper.driver.ios.models.IDevice;

import java.util.Random;

public class Base {

    private IDevice iDevice;

    public Base() {
        TestManager testManager = TestManager.getInstance();
        ParamsParser paramsParser = ParamsParser.getInstance();
        iDevice = testManager.getIDevice(paramsParser.getDeviceUuid());
    }

    public String takeScreenShot(String name) {
        return ScreenShotTaker.getInstance().takeScreenShot(name);
    }

    public int getRandomInt(int min, int max) {
        Random rand = new Random();

        return rand.nextInt((max - min) + 1) + min;
    }
}
