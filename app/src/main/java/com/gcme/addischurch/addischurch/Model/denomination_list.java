package com.gcme.addischurch.addischurch.Model;

/**
 * Created by kzone on 12/10/2016.
 */

public class denomination_list {
    private String chName,chloc;
    int id;

    public denomination_list() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getchName() {
        return chName;
    }

    public void setchName(String churchName) {
        chName = churchName;
    }

    public String getDeno() {
        return chloc;
    }

    public void setDeno(String chdeno) {
        chloc = chdeno;
    }


}
