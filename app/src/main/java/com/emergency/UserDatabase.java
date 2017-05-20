package com.emergency;

/**
 * Created by saivignesh on 12/03/17.
 */
public class UserDatabase {
    public String name1;
    public String name2;
    public String name3;
    public String num1;
    public String num2;
    public String num3;
    public String location;
    public String user;
    public String phone;
    public String mail;

    public UserDatabase(String username,String userphone,String email,String nam1, String nu1,String nam2, String nu2,String nam3, String nu3) {

        this.name1=nam1;
        this.user=username;
        this.phone=userphone;
        this.num1=nu1;
        this.mail=email;
        this.name2=nam2;
        this.num2=nu2;
        this.name3=nam3;

        this.num3=nu3;

    }
    public UserDatabase(String username,String userphone,String nam1, String nu1,String nam2, String nu2,String nam3, String nu3) {

        this.name1=nam1;
        this.user=username;
        this.phone=userphone;
        this.num1=nu1;
        this.name2=nam2;
        this.num2=nu2;
        this.name3=nam3;

        this.num3=nu3;

    }



}