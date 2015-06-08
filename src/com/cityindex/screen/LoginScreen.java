package com.cityindex.screen;

import com.cityindex.manager.TestManager;
import com.cityindex.param.ParamsParser;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.models.IDevice;


public class LoginScreen extends BaseScreen {
    public LoginScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        super(testManager, testHelper, paramsParser, iDevice);
    }

    public void signIn(long defaultTimeout) {
        //todo logic
    }
}
