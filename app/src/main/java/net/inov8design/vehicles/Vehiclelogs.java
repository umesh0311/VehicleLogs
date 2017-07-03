package net.inov8design.vehicles;

import java.util.Date;

/**
 * Created by allan on 26/03/2017.
 */

public class Vehiclelogs {

    private int vehicleid;
    private String driver;
    private String rego;
    private String starttime;
    private String fb;
    private String sb;
    private String endtime;


    public Vehiclelogs(int vehicleid, String driver, String rego, String starttime, String fb, String sb, String endtime ){
        this.vehicleid = vehicleid;
        this.driver = driver;
        this.rego = rego;
        this.starttime = starttime;
        this.fb = fb;
        this.sb = sb;
        this.endtime = endtime;
    }
    public int getVehicleid(){
        return this.vehicleid;
    }
    public void setVehicleid(int id){
        this.vehicleid = id;
    }
    public String getDriver(){
        return this.driver;
    }
    public void setDriver(String driver){
        this.driver = driver;
    }
    public String getRego(){
        return this.rego;
    }
    public void setRego(String rego){
        this.rego = rego;
    }
    public String getStarttime(){
        return this.starttime;
    }
    public void setStarttime(String starttime){
        this.starttime = starttime;
    }
    public String getFb(){
        return this.fb;
    }
    public void setFb(String fb){
        this.fb = fb;
    }
    public String getSb(){
        return this.sb;
    }
    public void setSb(String sb){
        this.sb = sb;
    }
    public String getEndtime(){
        return this.endtime;
    }
    public void setEndtime(String endtime){
        this.endtime = endtime;
    }

    @Override
    public String toString() {
        return
                 vehicleid + " "+
                 driver +" "+ '\'' +
                 rego +" "+ '\'' +
                 starttime +" "+ '\'' +
                 fb +" "+ '\'' +
                 sb +" "+ '\'' +
                endtime +" "+"\n";
    }
}