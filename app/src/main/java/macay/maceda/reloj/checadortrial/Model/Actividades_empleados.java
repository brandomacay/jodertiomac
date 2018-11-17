package macay.maceda.reloj.checadortrial.Model;

/**
 * Created by Vlover on 06/02/2018.
 */

public class Actividades_empleados {
    private long id;
    private String userid;
    private String working;
    private String workout;
    private String breaking;
    private String breakout;
    private String formatdate;

    public Actividades_empleados() {
    }

    public Actividades_empleados( String userid,String working, String workout, String breaking,
                                 String breakout, String formatdate) {
        this.userid = userid;
        this.working = working;
        this.workout = workout;
        this.breaking = breaking;
        this.breakout = breakout;
        this.formatdate = formatdate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getWorking() {
        return working;
    }

    public void setWorking(String working) {
        this.working = working;
    }
    public String getWorkout() {
        return workout;
    }

    public void setWorkout(String workout) {
        this.workout = workout;
    }

    public String getBreaking() {
        return breaking;
    }

    public void setBreaking(String breaking) {
        this.breaking = breaking;
    }
    public String getBreakout() {
        return breakout;
    }

    public void setBreakout(String breakout) {
        this.breakout = breakout;
    }

    public String getFormatdate() {
        return formatdate;
    }

    public void setFormatdate(String formatdate) {
        this.formatdate = formatdate;
    }
}