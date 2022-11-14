package com.example.myapplicationapp;

public class Model {
    private long id;
    private String sta;
    private String district;
    private String name_route;
    private String type_route;
    private String type_road;
    private String mark_route;



    public Model(String sta, String district, String name_route, String type_route, String type_road, String mark_route) {
        this.sta = sta;
        this.district = district;
        this.name_route = name_route;
        this.type_route = type_route;
        this.type_road = type_road;
        this.mark_route = mark_route;
        //this.geom = geom;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getName_route() {
        return name_route;
    }

    public void setName_route(String name_route) {
        this.name_route = name_route;
    }

    public String getType_route() {
        return type_route;
    }

    public void setType_route(String type_route) {
        this.type_route = type_route;
    }

    public String getType_road() {
        return type_road;
    }



    public void setType_road(String type_road) {
        this.type_road = type_road;
    }




    public String getMark_route() {
        return mark_route;
    }

    public void setMark_route(String mark_route) {
        this.mark_route = mark_route;
    }
}
