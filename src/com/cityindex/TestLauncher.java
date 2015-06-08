package com.cityindex;

import com.cityindex.exception.TestException;
import com.cityindex.json.Status;
import com.cityindex.json.TestCaseInfo;
import com.cityindex.manager.AlertManager;
import com.cityindex.manager.TestManager;
import com.cityindex.model.TestCase;
import com.cityindex.param.ParamsParser;
import com.cityindex.tests.acceptance.AcceptanceTests;

import java.io.IOException;
import java.util.Random;

public class TestLauncher {

    private static ParamsParser paramsParser = null;
    private static AcceptanceTests acceptanceTests = null;

    public static void main(String[] args) throws TestException, IOException {

        paramsParser = ParamsParser.getInstance();

        if(!paramsParser.parse(args)) return;

        launchTest();
//        for (int i = 0; i < 100; i ++) {
//            System.out.println("random int: " + getRandom(1, 15));
//        }
    }

    private static int getRandom(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    public static TestCase.TestCaseConfig testCaseConfig = new TestCase.TestCaseConfig() {
        @Override

        public boolean isReinstallApp() {
            return false;
        }

        @Override
        public String getPathToApp() {
            return TestManager.getInstance().getPathToBuild();
        }

        @Override
        public void mainLogic() throws TestException {
            executeTest();
        }

        @Override
        public void tearDown() throws TestException {
            if(acceptanceTests != null)
                acceptanceTests.tearDown();
        }
    };

    private static void launchTest() throws TestException, IOException {
        TestCase testCase = new TestCase(testCaseConfig);
        testCase.start();
    }

    private static void executeTest() throws TestException {
        TestCaseInfo testCaseInfo = TestManager.getInstance().getTestCaseInfo();
        testCaseInfo.setStatusId(Status.FAILED);
        testCaseInfo.writeResult(paramsParser.getPathToResultsFolder() + "/result.json");

        AlertManager alertManager = new AlertManager();
        switch (paramsParser.getTestId()) {
            case "123456":
//                alertManager.defaultHandler();
                acceptanceTests = new AcceptanceTests();
                testCaseInfo.setId(123456L);
                testCaseInfo.setTitle("demo");
                TestManager.getInstance().setTestCaseInfo(testCaseInfo);
                acceptanceTests.testCase123456();
                break;
        }
    }
}
