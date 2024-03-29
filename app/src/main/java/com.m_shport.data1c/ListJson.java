package com.m_shport.data1c;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListJson {

    @SerializedName("odata.metadata")
    @Expose
    private String odataMetadata;
    @SerializedName("value")
    @Expose
    private List<Value> value = null;

    public String getOdataMetadata() {
        return odataMetadata;
    }

    public void setOdataMetadata(String odataMetadata) {
        this.odataMetadata = odataMetadata;
    }

    public List<Value> getValue() {
        return value;
    }

    public void setValue(List<Value> value) {
        this.value = value;
    }

}