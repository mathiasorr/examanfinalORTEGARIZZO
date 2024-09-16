package com.example.examanfinalortegarizzo;

import java.io.Serializable;

public class User implements Serializable {
    public Name name;
    public String email;
    public Picture picture;
    public Location location;
    public String phone;
    public String cell;
    public String nat;

    public static class Name implements Serializable {
        public String first;
        public String last;
    }

    public static class Picture implements Serializable {
        public String large;
        public String medium;
        public String thumbnail;
    }

    public static class Location implements Serializable {
        public Street street;
        public String city;
        public String state;
        public String country;
        public String postcode;
        public Coordinates coordinates;
    }

    public static class Street implements Serializable {
        public int number;
        public String name;
    }

    public static class Coordinates implements Serializable {
        public String latitude;
        public String longitude;
    }
}