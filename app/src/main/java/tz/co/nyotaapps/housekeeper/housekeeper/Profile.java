package tz.co.nyotaapps.housekeeper.housekeeper;

public class Profile {
    private String idx;
    private String name;
    private String umri;
    private String area;
    private String emailxx;
    private String maelezo;
    private String gender;
    private String phone;
    private String picha;
    private String love;




    public Profile() {
    }

    public Profile(String idx, String name, String umri, String area, String emailxx, String maelezo, String gender, String phone, String picha,String love) {
        this.idx = idx;
        this.name = name;
        this.umri = umri;
        this.area = area;
        this.emailxx = emailxx;
        this.maelezo = maelezo;
        this.gender = gender;
        this.phone = phone;
        this.picha = picha;
        this.love = love;

    }


    public String getidx() {
        return idx;
    }

    public void setidx(String idx) {
        this.idx = idx;
    }



    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }



    public String getumri() {
        return umri;
    }

    public void setumri(String umri) {
        this.umri = umri;
    }



    public String getarea() {
        return area;
    }

    public void setarea(String area) {
        this.area = area;
    }


    public String getemailxx() {
        return emailxx;
    }

    public void setemailxx(String emailxx) {
        this.emailxx = emailxx;
    }



    public String getmaelezo() {
        return maelezo;
    }

    public void setmaelezo(String maelezo) {
        this.maelezo = maelezo;
    }



    public String getgender() {
        return gender;
    }

    public void setgender(String gender) {
        this.gender = gender;
    }



    public String getphone() {
        return phone;
    }

    public void setphone(String gender) {
        this.phone = phone;
    }


    public String getpicha() {
        return picha;
    }

    public void setpicha(String picha) {
        this.picha = picha;
    }


    public String getlove() {
        return love;
    }

    public void setlove(String love) {
        this.love = love;
    }




}