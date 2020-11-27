package gtron.com.gtronsystem.SelectViewFunc;


/**
 * Created by euibosim on 2017. 10. 10..
 */

public class SelectViewListItem {
    private String titleStr ;
    private String viewCntStr ;
    private String sensorCntStr;
    private String viewNo;
    private Boolean selectFlag ;

    public String getViewNo() {
        return viewNo;
    }

    public void setViewNo(String viewNo) {
        this.viewNo = viewNo;
    }

    public Boolean getSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(Boolean selectFlag) {
        this.selectFlag = selectFlag;
    }


    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public String getViewCntStr() {
        return viewCntStr;
    }

    public void setViewCntStr(String viewCntStr) {
        this.viewCntStr = viewCntStr;
    }

    public String getSensorCntStr() {
        return sensorCntStr;
    }

    public void setSensorCntStr(String sensorCntStr) {
        this.sensorCntStr = sensorCntStr;
    }



}
