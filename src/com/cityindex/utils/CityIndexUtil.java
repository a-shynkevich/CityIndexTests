package com.cityindex.utils;

import com.cityindex.constants.Constants;
import com.cityindex.exception.TestException;
import com.cityindex.manager.TestManager;
import com.cityindex.param.ParamsParser;
import com.cityindex.screen.*;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.models.IDevice;



public class CityIndexUtil {

    private IDevice iDevice;
    private TestHelper testHelper;
    private TestManager testManager;
    private ParamsParser paramsParser;
    public ScreenModel screenModel;
    private BaseScreen baseScreen;
//

    public CityIndexUtil(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        this.testManager = testManager;
        this.testHelper = testHelper;
        this.paramsParser = paramsParser;
        this.iDevice = iDevice;
    }

    public BaseScreen getCurrentScreen(boolean isUpdate) {
        if(baseScreen != null && !isUpdate) {
            iDevice.i("CurrentScreen is " + screenModel.name());
            return baseScreen;
        }
        //todo make detect Readers via invisible elements!!!
        Element element = iDevice.getGetter().getElementForScreenDetection(1000);
//        Element element = iDevice.getWaiter().waitForElement(Constants.DEFAULT_TIMEOUT * 2,
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIANavigationBar, 0)
//                        .addElement(UIAElementType.UIAButton, Constants.Messages.EDIT),
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAButton, Constants.Screens.SEARCH_SCREEN),
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAToolBar, 0)
//                        .addElement(UIAElementType.UIAButton, Constants.Screens.SEARCH_SCREEN),
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAToolBar, 1)
//                        .addElement(UIAElementType.UIAButton, Constants.Screens.SEARCH_SCREEN),
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAStaticText, Constants.Screens.DeferredSignIn.BUILD_YOUR_OWN_LIBRARY),
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAImage, Constants.Screens.OOBE_SCREEN_LOGO),
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAImage, Constants.Screens.OOBE_SCREEN_LOGO_IPAD),
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIANavigationBar, 0)
//                        .addElement(UIAElementType.UIAButton, Constants.ProductDetails.MANAGE_BUTTON),
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAButton, Constants.Screens.EPUB_READER).setOnlyVisible(false),
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAButton, Constants.Reader.Drp.iPad.BRIGHTNESS).setOnlyVisible(false),
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAToolBar, 0)
//                        .addElement(UIAElementType.UIAButton, Constants.Reader.Drp.LIBRARY_BTN).setOnlyVisible(false),
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAStaticText, Constants.My_Shelves.TITLE_ACTION_BAR),
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAStaticText, Constants.SideMenu.LIBRARY),
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIANavigationBar, iDevice.isIPhone() ? 0 : 2)
//                        .addElement(UIAElementType.UIAStaticText, Constants.ProfileScreen.TITLE_SET_PROFILE),
////                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIANavigationBar, 2)
////                        .addElement(UIAElementType.UIAStaticText, Constants.ProfileScreen.TITLE_SET_PROFILE),
////                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAToolBar, 0).addElement(UIAElementType.UIAButton, Constants.Screens.LIBRARY_SCREEN),
////                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAToolBar, 0).addElement(UIAElementType.UIAButton, Constants.Screens.LIBRARY_SCREEN2),
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIANavigationBar, 0)
//                        .addElement(UIAElementType.UIAStaticText, Constants.Screens.SETTINGS_SCREEN),
//                new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAStaticText, Constants.Screens.SETTINGS_SCREEN_IPAD)
//        );

        if(element == null) {
            screenModel = ScreenModel.NON;
            return null;
        }

        switch (element.getName()) {
            case Constants.Screens.Login.LOGIN_BTN:
                screenModel= ScreenModel.LOGIN;
                iDevice.i("####### CurrentScreen is " + screenModel.name());
                baseScreen = new LoginScreen(testManager, testHelper, paramsParser, iDevice);
                return baseScreen;
            default: screenModel = ScreenModel.NON;
                iDevice.i("####### CurrentScreen is " + screenModel.name());
        }
        return null;
    }

    public boolean waitForScreenModel(ScreenModel screenModel, long timeOut) throws TestException {
        return waitForScreenModel(screenModel, timeOut, true);
    }

    public boolean waitForScreenModel(ScreenModel screenModel, long timeOut, boolean isStrict) throws TestException {
        long start = System.currentTimeMillis();
        iDevice.i("####### Wait for " + screenModel.name());
        while (true) {
            if(System.currentTimeMillis() - start > timeOut) {
                if(isStrict) {
                    testManager.retest("necessary model is not found. \n" +
                            "Expected: " + screenModel.name() + "\n" +
                            "Actual: " + this.screenModel.name());
                }
                return false;
            }
            getCurrentScreen(true);
            if(this.screenModel == screenModel) break;
        }
        return true;
    }
}
