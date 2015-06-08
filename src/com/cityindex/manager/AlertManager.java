package com.cityindex.manager;

import com.cityindex.param.ParamsParser;

public class AlertManager {

    private TestManager testManager;
    private ParamsParser paramsParser;

    public AlertManager() {
        testManager = TestManager.getInstance();
        paramsParser = ParamsParser.getInstance();
    }
}
