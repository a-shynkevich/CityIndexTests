package com.cityindex.json;

import com.cityindex.utils.ScreenShotTaker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
 
public class TestCaseInfo {
    private Long id;
    private long statusId = Status.FAILED;
    private String name = "";
    private String title = "";
    private String deviceId = "";
    private String message = "";
    private List<String> attachments = new ArrayList<String>();
    private List<String> steps = new ArrayList<String>();
 
    public TestCaseInfo() { }
 
    public TestCaseInfo(Long id) {
        this.id = id;
    }

 
    public String getDeviceId() {
        return deviceId;
    }
 
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
 
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
 
    public void resetTestCase() {
        setStatusId(Status.FAILED);
        steps.clear();
        attachments.clear();
        message = "";
    }
 
    public void clearSteps() {
        steps.clear();
    }
 
    public Long getId() {
        return id;
    }
 
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message, boolean isFail) {
        ScreenShotTaker.getInstance().takeScreenShot((isFail ? "FAIL" : "RETEST") + "_" + message);
        this.message = message;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public long getStatusId() {
        return statusId;
    }
 
    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }
 
    public List<String> getAttachments() {
        return attachments;
    }
 
    public void addAttachment(String attachment) {
        attachments.add(attachment);
    }
 
    public void setAttachments(List<String> exceptions) {
        this.attachments = exceptions;
    }
 
    public List<String> getSteps() {
        return steps;
    }
 
    public void addStep(String step) {
        steps.add(step);
    }
 
    public void setSteps(List<String> steps) {
        this.steps = steps;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }

 
    public Object getJsonObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", getName());
            jsonObject.put("id", getId());
            jsonObject.put("result", getStatusId());
            jsonObject.put("message", getMessage());
            jsonObject.put("title", getTitle());
            jsonObject.put("deviceId", getDeviceId());
            jsonObject.put("attachments", createJsonArray(attachments, "attachment"));
            jsonObject.put("steps", createJsonArray(steps, "step"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
 
    private JSONArray createJsonArray(List<String> list, String key) {
        JSONArray jsonArray = new JSONArray();
        if (list.size() > 0) {
            JSONObject obj = null;
            for (int i = 0; i < list.size(); i++) {
                obj = new JSONObject();
                try {
                    obj.put(key, list.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(obj);
            }
        }
        return jsonArray;
    }

    @Override
    public String toString() {
        return "TestCase{" + getJsonObject().toString() + '}';
    }

    public void writeResult(String pathToResultJson) {
        try {
            System.out.println("Result json:" + toString());
            FileWriter fileWriter = new FileWriter(pathToResultJson, false);
            fileWriter.append(this.getJsonObject().toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}