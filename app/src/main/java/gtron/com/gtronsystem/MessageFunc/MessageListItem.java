package gtron.com.gtronsystem.MessageFunc;

/**
 * Created by euibosim on 2017. 10. 11..
 */

public class MessageListItem {
    private String titleStr ;
    private String messageStr ;
    private String dateStr;
    private Boolean readFlag ;

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public String getMessageStr() {
        return messageStr;
    }

    public void setMessageStr(String messageStr) {
        this.messageStr = messageStr;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Boolean getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(Boolean readFlag) {
        this.readFlag = readFlag;
    }
}
