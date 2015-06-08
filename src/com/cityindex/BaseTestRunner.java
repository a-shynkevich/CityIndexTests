package com.cityindex;

import com.cityindex.annotation.Condition;
import com.cityindex.annotation.PostCondition;
import com.cityindex.annotation.PreCondition;
import com.cityindex.assistant.Preparer;
import com.cityindex.constants.Constants;
import com.cityindex.exception.TestException;
import com.cityindex.manager.TestManager;
import com.cityindex.model.TestCase;
import com.cityindex.param.ParamsParser;
import com.cityindex.screen.*;
import com.cityindex.utils.LoggerUtil;
import com.cityindex.utils.CityIndexUtil;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.helpers.*;
import com.sofment.testhelper.driver.ios.models.IDevice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static com.cityindex.utils.LoggerUtil.i;

public class BaseTestRunner extends Base implements TestManager.ITestManager{

    protected PreCondition mPreCondition = null;
    protected PostCondition mPostCondition = null;

    protected IDevice iDevice;
    protected TestHelper testHelper;
    protected TestManager testManager;
    protected ParamsParser paramsParser;
    protected Waiter waiter;
    protected Getter getter;
    protected Clicker clicker;
    protected Scroller scroller;
    protected Drager drager;
    protected CityIndexUtil cityIndexUtil;
    protected BaseScreen baseScreen;
    protected Preparer preparer;
    private LoginScreen loginScreen;

    public BaseTestRunner() {
        testManager = TestManager.getInstance();
        testManager.setObjectITestManager((TestManager.ITestManager) this);

        testHelper = testManager.getTestHelper();
        paramsParser = ParamsParser.getInstance();
        iDevice = testManager.getIDevice(paramsParser.getDeviceUuid());
        if(iDevice == null) {
            LoggerUtil.e("Device with uuid " + paramsParser.getDeviceUuid() + " is not found");
            System.exit(0);
        }
        waiter = iDevice.getWaiter();
        getter = iDevice.getGetter();
        clicker = iDevice.getClicker();
        scroller = iDevice.getScroller();
        drager = iDevice.getDrager();

        cityIndexUtil = new CityIndexUtil(testManager, testHelper, paramsParser, iDevice);
        preparer = new Preparer(iDevice, cityIndexUtil, paramsParser);
//        setUpAlertHandler(iDevice);

        setUp();
    }

    private void launchApp() {
        TestCase.TestCaseConfig testCaseConfig = TestLauncher.testCaseConfig;
        String pathToBuild = testCaseConfig.getPathToApp();
        if(testCaseConfig.isReinstallApp()) {
            iDevice.uninstallApplication(pathToBuild);
            iDevice.installApplication(pathToBuild);
        }
        iDevice.launchApplication(pathToBuild, 5);
    }

    protected void setUp() {
        i("setUp");
        launchApp();
        paramsParser = ParamsParser.getInstance();
        parseAnnotations();

        try {
            processPreConditions();
        } catch (TestException e) {
            e.printStackTrace();
        }
    }

    private void parseAnnotations() {
        for(Method m: this.getClass().getMethods()) {
            if (!m.getName().contains(paramsParser.getTestId())) continue;

            i("ANNOTATIONS FOUND!");
            for(Annotation a: m.getDeclaredAnnotations()) {
                if(a instanceof PreCondition) {
                    i("PreCondition FOUND!");
                    mPreCondition = (PreCondition) a;
                } else if(a instanceof PostCondition) {
                    i("PostCondition FOUND!");
                    mPostCondition = (PostCondition) a;
                }
            }
        }
        testManager.testCaseInfo.setTitle(mPreCondition.testTitle());
        testManager.testCaseInfo.setId(mPreCondition.testId());
    }

    private void processPreConditions() throws TestException {
        if (mPreCondition == null) return;

        Condition[] preconditionActions = mPreCondition.preConditions();
        for(Condition precondition : preconditionActions) {
            testManager.addStep("PRECONDITION: " + precondition.name());
            executeCondition(precondition);
        }
    }

    private void processPostConditions() throws Exception {
        if(mPostCondition == null) return;
        Condition[] postConditions = mPostCondition.postConditions();
        for(Condition postCondition : postConditions) {
            testManager.addStep("POST CONDITION: " + postCondition.name());
            executeCondition(postCondition);
        }
    }

    private void executeCondition(Condition condition) throws TestException {
        iDevice.i("Annotation condition : " + condition.name());
        testManager.addStep("Annotation condition:" + condition.name());
       switch (condition) {
            case LOGIN:
                baseScreen = cityIndexUtil.getCurrentScreen(true);
                iDevice.i("##################### Detected " + cityIndexUtil.screenModel.name());
                if(cityIndexUtil.screenModel == ScreenModel.LOGIN) {
                    loginScreen = (LoginScreen) baseScreen;
                    loginScreen.signIn(Constants.DEFAULT_TIMEOUT);
                    baseScreen = cityIndexUtil.getCurrentScreen(true);
                }
                break;
            case SIGN_OUT:
               //todo logic
                break;

            default: break;
        }
    }

    public void initLoginScreen() throws TestException {
        cityIndexUtil.waitForScreenModel(ScreenModel.LOGIN, Constants.DEFAULT_TIMEOUT);
        baseScreen = cityIndexUtil.getCurrentScreen(false);
        if(cityIndexUtil.screenModel != ScreenModel.LOGIN) testManager.retest("Necessary Login  screen is not found. Expected: " + ScreenModel.LOGIN.name() + ", actual : " + cityIndexUtil.screenModel.name());
        loginScreen = ((LoginScreen) baseScreen);
    }

    protected void tearDown() {
        i("tearDown");

        iDevice.stopConsoleReading();
        try {
            processPostConditions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testManagerTearDown() {
        tearDown();
    }
}
