package com.rotimi.finder.api.models;

/**
 * Created by mayowa on 9/20/16.
 */
public class _Login {

    public boolean isActive;
    public String fullName;
    public int id;
    public String firstname;
    public String key;
    public String token;
    public String middlename;
    public String surname;
    public String email;
    public String gender;
    public String phone;
    public String dateOfBirth;
    public String profilePix;
    public School school;
    public City city;
    public String address;
    public String originLGA;
    public String originState;
    public String degree;
    public String bankName;
    public String bankAccount;
    public String bankBVN;
    public boolean facebookDefault;
    public boolean twitterDefault;
    public String registeredDate;
    public String reference;
    public LGA lga;
    public State state;
    public Region region;
    public boolean trained;
    public boolean exited;

    public class School{
        public String name;
        public int id;
    }

    public class LGA{
        public String name;
        public int id;
    }

    public class Region{
        public String name;
        public int id;
    }

    public class City{
        public String name;
        public int id;
    }

    public class State{
        public String name;
        public int id;
    }

}
