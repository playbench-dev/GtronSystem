package gtron.com.gtronsystem.Utils;

/**
 * Created by kmw on 2017. 12. 7..
 */

public class ServerUtils {
    public static String server = "http://52.141.33.133:8080/";

    public static String server1 = "http://192.168.0.4:8080/";

    public static String LoginProcess(){
        String loginProcess = server + "common/loginProcess.do";
        return loginProcess;
    }
    public static String LogoutProcess(){
        String LogoutProcess = server + "common/logoutProcess.do";
        return LogoutProcess;
    }
    public static String GcmConnection(){
        String gcmConnection = server + "GetronManager/gcmConnection.do";
        return gcmConnection;
    }

    public static String SelectSmsCallback(){
        String selectSmsCallback = server + "GetronManager/selectSmsCallback.do";
        return selectSmsCallback;
    }

    public static String SmsCertProc(){
        String smsCertProc = server + "common/smsCertProc.do";
        return smsCertProc;
    }
    public static String SelectSmsByPhoneNo(){
        String selectSMSByPhoneNo = server + "common/selectSMSByPhoneNo.do";
        return selectSMSByPhoneNo;
    }
    public static String UpdateUserPhone(){
        String updateUserPhone = server + "Other/updateUserPhone.do";
        return updateUserPhone;
    }
    public static String SelectOneViewInfo(){
        String selectOneViewInfo = server + "Admin/selectOneViewInfo.do";
        return selectOneViewInfo;
    }
    public static String SelectProcessByViewNo(){
        String selectProcessByViewNo = server + "Admin/selectProcessByViewNo.do";
        return selectProcessByViewNo;
    }
    public static String SelectSensorByProcessNo(){
        String selectSensorByProcessNo = server + "Admin/selectSensorByProcessNo.do";
        return selectSensorByProcessNo;
    }
    public static String SelectSiteColor(){
        String selectSiteColor = server + "Admin/selectSiteColor.do";
        return selectSiteColor;
    }
    public static String SelectMessageList(){
        String selectMessageList = server + "Other/selectMessageList.do";
        return selectMessageList;
    }
    public static String UpdateMessage(){
        String updateMessage = server + "Other/updateMessage.do";
        return updateMessage;
    }
    public static String DeleteMessage(){
        String deleteMessage = server + "Other/deleteMessage.do";
        return deleteMessage;
    }
    public static String SelectTileList(){
        String selectTileList = server + "GetronManager/selectTileList.do";
        return selectTileList;
    }
    public static String SelectViewInfoList(){
        String selectViewInfoList = server + "Admin/selectViewInfoList.do";
        return selectViewInfoList;
    }
    public static String UpdateCurrentView(){
        String updateCurrentView = server + "GetronManager/updateCurrentView.do";
        return updateCurrentView;
    }
    public static String TileMonitoring(){
        String tileMonitoring = server + "GetronManager/tileMonitoring.do";
        return tileMonitoring;
    }
    public static String selectCheckedUserInfo(){
        String selectCheckedUserInfo = server + "common/selectCheckedUserInfo.do";
        return selectCheckedUserInfo;
    }
    public static String updateUserPw(){
        String updateUserPw = server + "GetronManager/updateUserPw.do";
        return updateUserPw;
    }
    public static String updateSettingM(){
        String updateSettingM = server + "common/updateSettingM.do";
        return updateSettingM;
    }

}
