package com.gcme.addischurch.addischurch.Model;

/**
 * Created by kzone on 12/10/2016.
 */

public class favorites {
    private String chName,favdeno;
    int id;

    public favorites() {

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
        return favdeno;
    }

    public void setDeno(String chdeno) {
        favdeno = chdeno;
    }


}
