package gtron.com.gtronsystem.SelectViewFunc;

public class SelectTileViewListItem {
    public SelectTileViewListItem() {
    }
    String title;
    String columns;
    String row;
    String tileNo;
    boolean selectFlag;

    public boolean isSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(boolean selectFlag) {
        this.selectFlag = selectFlag;
    }

    public String getTileNo() {
        return tileNo;
    }

    public void setTileNo(String tileNo) {
        this.tileNo = tileNo;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }


}
