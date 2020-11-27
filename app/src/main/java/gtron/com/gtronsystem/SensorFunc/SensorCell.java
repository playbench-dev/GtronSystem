package gtron.com.gtronsystem.SensorFunc;

import java.util.ArrayList;

/**
 * Created by euibosim on 2017. 10. 9..
 */

public class SensorCell {

    public String sensorId;
    public String sensorName;
    public ArrayList<String> colorList;
    public ArrayList<String> textColorList;
    public ArrayList<Integer> port1;
    public ArrayList<Integer> priority;
    ArrayList<String> nameList;
    int modelIdList;
    String processList;
    String lineList;
    String locationList;
    String groupList;
    ArrayList<String> dataList;

    public SensorCell() {

    }

    public ArrayList<String> getTextColorList() {
        return textColorList;
    }

    public void setTextColorList(ArrayList<String> textColorList) {
        this.textColorList = textColorList;
    }

    public ArrayList<String> getNameList() {
        return nameList;
    }

    public void setNameList(ArrayList<String> nameList) {
        this.nameList = nameList;
    }

    public int getModelIdList() {
        return modelIdList;
    }

    public void setModelIdList(int modelIdList) {
        this.modelIdList = modelIdList;
    }

    public String getProcessList() {
        return processList;
    }

    public void setProcessList(String processList) {
        this.processList = processList;
    }

    public String getLineList() {
        return lineList;
    }

    public void setLineList(String lineList) {
        this.lineList = lineList;
    }

    public String getLocationList() {
        return locationList;
    }

    public void setLocationList(String locationList) {
        this.locationList = locationList;
    }

    public String getGroupList() {
        return groupList;
    }

    public void setGroupList(String groupList) {
        this.groupList = groupList;
    }

    public ArrayList<String> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<String> dataList) {
        this.dataList = dataList;
    }

    public ArrayList<Integer> getPriority() {
        return priority;
    }

    public void setPriority(ArrayList<Integer> priority) {
        this.priority = priority;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public ArrayList<String> getColorList() {
        return colorList;
    }

    public void setColorList(ArrayList<String> colorList) {
        this.colorList = colorList;
    }

    public ArrayList<Integer> getPort1() {
        return port1;
    }

    public void setPort1(ArrayList<Integer> port1) {
        this.port1 = port1;
    }

}
