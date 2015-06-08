package com.cityindex.param;

import com.cityindex.json.Status;
import com.cityindex.json.TestCaseInfo;
import com.cityindex.manager.TestManager;

import java.util.ArrayList;

import static com.cityindex.utils.LoggerUtil.e;

public class ParamsParser {

    private static ParamsParser paramsParser;

    private ParamsParser() {}

    public static ParamsParser getInstance(){
        if(paramsParser == null) paramsParser = new ParamsParser();
        return paramsParser;
    }

    private ArrayList<String> implementedTests = new ArrayList<String>();
    {
        implementedTests.add("434245");
    }

    private ArrayList<String> notApplicable = new ArrayList<>();
    {
        notApplicable.add("434244");
    }

    private String deviceUuid;
    private String testId;
    private String pathToResultsFolder;
    private String pathToBuild;

    private ArrayList<String> argsName = new ArrayList<String>();
    {
        argsName.add("testId");
        argsName.add("uuid");
        argsName.add("pathToResultFolder");
        argsName.add("pathToBuild");
    }

    public boolean parse(String... args) {
        if(args.length % 3 != 0 || args.length == 0) {
            e("Wrong arguments count or format\n" +
                    "usage: \n" +
                    "java -jar IOsCiTests -e testId testId -uuid deviceUUID -e pathToResultFolder \"path/to/reults/folder\" -e pathToBuild(Optional) \"path/to/build.app(ipa)\"");
            System.exit(0);
            return false;
        }

        String command = "";

        boolean isNotApplicable = false;


        for(int currentIndex = 0; currentIndex < args.length; currentIndex ++){
            if(!args[currentIndex].equals("-e") || !argsName.contains(command = args[currentIndex + 1])) {
                e("Wrong arguments count or format\n" +
                        "usage: \n" +
                        "java -jar IOsCiTests -e testId testId -e uuid deviceUUID -e pathToResultFolder \"path/to/reults/folder\" -e pathToBuild(Optional) \"path/to/build.app(ipa)\"");
                System.exit(0);
                return false;
            }
            currentIndex = currentIndex + 2;
            if(command.equals("testId") && !implementedTests.contains(testId = args[currentIndex])) {
                if(notApplicable.contains(testId)) {
                    isNotApplicable = true;
                    continue;
                }
                String testsId = "";
                for(String testId : implementedTests) {
                    testsId = testsId + (testsId.length() > 0 ? "\n" : "") + testId;
                }
                e("Wrong test id\n" +
                        "Available tests id: \n" +
                        testsId);
                System.exit(0);
                return false;
            } else if(command.equals("uuid")) {
                deviceUuid = args[currentIndex];
            } else if(command.equals("pathToResultFolder")) {
                pathToResultsFolder = args[currentIndex];
            } else if(command.equals("pathToBuild")) {
                pathToBuild = args[currentIndex];
            }
        }
        if(pathToResultsFolder == null || deviceUuid == null || testId == null) {
            e("Missed argument\n" +
                    "usage: \n" +
                    "java -jar IOsCiTests -e testId testId -e uuid deviceUUID -e pathToResultFolder \"path/to/reults/folder\" -e pathToBuild(Optional) \"path/to/build.app(ipa)\"");
            System.exit(0);
            return false;
        }
        if(isNotApplicable) {
            TestCaseInfo testCaseInfo = TestManager.getInstance().getTestCaseInfo();
            e("The test " + testId + " is not applicable");
            testCaseInfo.setStatusId(Status.NOT_APPLICABLE);
            testCaseInfo.writeResult(getPathToResultsFolder() + "/result.json");
            System.exit(0);
        }
        return true;
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public String getTestId() {
        return testId;
    }

    public String getPathToBuild() {
        return pathToBuild;
    }

    public String getPathToResultsFolder() {
        return pathToResultsFolder;
    }
}
