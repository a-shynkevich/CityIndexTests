package com.cityindex.assistant;

import com.cityindex.constants.Constants;
import com.cityindex.exception.TestException;
import com.cityindex.manager.TestManager;
import com.cityindex.param.ParamsParser;
import com.cityindex.utils.CityIndexUtil;
import com.cityindex.screen.*;
import com.sofment.testhelper.driver.ios.helpers.Clicker;
import com.sofment.testhelper.driver.ios.helpers.Getter;
import com.sofment.testhelper.driver.ios.helpers.Scroller;
import com.sofment.testhelper.driver.ios.helpers.Waiter;
import com.sofment.testhelper.driver.ios.models.IDevice;

public class Preparer {
    protected IDevice iDevice;
    protected TestManager testManager;
    protected Waiter waiter;
    protected Getter getter;
    protected Clicker clicker;
    protected Scroller scroller;
    protected BaseScreen baseScreen;
    protected LoginScreen loginScreen;
    protected CityIndexUtil cityIndexUtil;
    protected ParamsParser paramsParser;

    public Preparer(IDevice iDevice, CityIndexUtil cityIndexUtil, ParamsParser paramsParser) {
        this.testManager = TestManager.getInstance();
        this.iDevice = iDevice;
        this.cityIndexUtil = cityIndexUtil;
        this.paramsParser = paramsParser;
        this.waiter = this.iDevice.getWaiter();
        this.getter = this.iDevice.getGetter();
        this.clicker = this.iDevice.getClicker();
        this.scroller = this.iDevice.getScroller();
    }

    public void login() throws TestException {
        baseScreen = cityIndexUtil.getCurrentScreen(true);
        iDevice.i("##################### Detected " + cityIndexUtil.screenModel.name());
        if (cityIndexUtil.screenModel == ScreenModel.LOGIN) {
            loginScreen = (LoginScreen) baseScreen;
            loginScreen.signIn(Constants.DEFAULT_TIMEOUT);
        }
    }

    public boolean signOut() throws TestException {
        baseScreen = cityIndexUtil.getCurrentScreen(true);
        iDevice.i("##################### Detected " + cityIndexUtil.screenModel.name());
        //todo logic
        return false;
    }

    public void initLoginScreen() throws TestException {
        cityIndexUtil.waitForScreenModel(ScreenModel.LOGIN, Constants.DEFAULT_TIMEOUT);
        baseScreen = cityIndexUtil.getCurrentScreen(false);
        if(cityIndexUtil.screenModel != ScreenModel.LOGIN) testManager.retest("Necessary Login  screen is not found. Expected: " + ScreenModel.LOGIN.name() + ", actual : " + cityIndexUtil.screenModel.name());
        loginScreen = ((LoginScreen) baseScreen);
    }

}

