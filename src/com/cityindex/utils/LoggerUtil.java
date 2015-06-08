package com.cityindex.utils;

import com.sofment.testhelper.driver.ios.models.IDevice;

/**
 * Created by avsupport on 4/13/15.
 */
public class LoggerUtil {
    private static boolean isDebug = true;
    private static IDevice mIDevice = null;

    private static final String INFO = "INFO:";
    private static final String ERROR = "ERROR:";
    private static final String DEBUG = "DEBUG:";

    public static void setIDevice(IDevice iDevice) {
        mIDevice = iDevice;
    }

    public static void i(String logMessage) {
        if(isDebug) {
            if(mIDevice == null) {
                System.out.println(INFO + " " + logMessage);
            } else {
                mIDevice.i(INFO + " " + logMessage);
            }
        }
    }

    public static void e(String logMessage) {
        if(isDebug) {
            if(mIDevice == null) {
                System.out.println(ERROR + " " + logMessage);
            } else {
                mIDevice.i(ERROR + " " + logMessage);
            }
        }
    }

    public static void d(String logMessage) {
        if(isDebug) {
            if(mIDevice == null) {
                System.out.println(DEBUG + " " + logMessage);
            } else {
                mIDevice.i(DEBUG + " " + logMessage);
            }
        }
    }
}
