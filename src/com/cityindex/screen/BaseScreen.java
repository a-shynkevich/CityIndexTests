package com.cityindex.screen;

import com.cityindex.Base;
import com.cityindex.exception.TestException;
import com.cityindex.manager.TestManager;
import com.cityindex.param.ParamsParser;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.config.IConfig;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.enams.UIAElementType;
import com.sofment.testhelper.driver.ios.helpers.*;
import com.sofment.testhelper.driver.ios.models.IDevice;


public class BaseScreen extends Base {
    protected IDevice iDevice;
    protected TestHelper testHelper;
    protected TestManager testManager;
    protected ParamsParser paramsParser;
    protected Waiter waiter;
    protected Drager drager;
    protected Getter getter;
    protected Clicker clicker;
    protected Scroller scroller;

    public BaseScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        this.testManager = testManager;
        this.testHelper = testHelper;
        this.paramsParser = paramsParser;
        this.iDevice = iDevice;
        this.waiter = this.iDevice.getWaiter();
        this.getter = this.iDevice.getGetter();
        this.clicker = this.iDevice.getClicker();
        this.scroller = this.iDevice.getScroller();
        this.drager = this.iDevice.getDrager();
    }


    public Element waitForTextField(int instance) throws TestException {
        return waiter.waitForElementByClassExists(UIAElementType.UIATextField, 1, new IConfig().setMaxLevelOfElementsTree(4).setInstance(instance));
    }

    public boolean clearTextField(Element textField) {
        clicker.clickOnElement(textField);
        Element clearText = iDevice.getWaiter().waitForElementByNameVisible("Clear text", 1, new IConfig().setMaxLevelOfElementsTree(4).setParentElement(textField));
        return clearText != null && iDevice.getClicker().clickOnElement(clearText);
    }

    public boolean inputText(String text, Element textField) {
        testManager.addStep("Input text " + text);
        return iDevice.inputText(text, textField);
    }


    public boolean waitForLogin(long timeout){
//        long startTime = System.currentTimeMillis();
//        while (true){
//            if (waiter.waitForElementByClassExists(UIAElementType.UIACollectionView, 1, new IConfig().setMaxLevelOfElementsTree(2))!= null &&
//                    waiter.waitForElement(1, new ElementQuery().addElement(UIAElementType.UIAWindow, 0).addElement(UIAElementType.UIAButton, Constants.Settings.SIGN_IN)) == null){
//                return true;
//            }
//            if (System.currentTimeMillis() - startTime > timeout)
//                break;
//        }
         return false;
    }
}
