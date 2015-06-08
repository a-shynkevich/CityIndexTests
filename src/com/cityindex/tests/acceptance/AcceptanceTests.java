package com.cityindex.tests.acceptance;

import com.cityindex.BaseTestRunner;
import com.cityindex.annotation.Condition;
import com.cityindex.annotation.PreCondition;
import com.cityindex.exception.TestException;

public class AcceptanceTests extends BaseTestRunner {


    @PreCondition(preConditions = {Condition.NONE},
            testId = 123456,
            testTitle = "Demo")
    public void testCase123456() throws TestException {
        iDevice.i("#######START#########");

    }
}
