package com.rotimi.finder.main.publicreports;
import java.io.Serializable;


/**
 * Created by mayowa on 7/23/16.
 */
public class ReportItem implements Serializable {

    public String name;
    public String comment;
    public String police;
    public String age;
    public String height;
    public String sex;
    public String complexion;
    public String type;
    public String imageUrl;
    public String id;
    public String found = "0";
    public String mobile_number;

    public ReportItem(){}
}
