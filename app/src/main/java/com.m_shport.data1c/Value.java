package com.m_shport.data1c;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Value {

    @SerializedName("FaktRunMetrTurnover")
    @Expose
    private Double faktRunMetrTurnover;

    public Double getFaktRunMetrTurnover() {
        return faktRunMetrTurnover;
    }

    public void setFaktRunMetrTurnover(Double faktRunMetrTurnover) {
        this.faktRunMetrTurnover = faktRunMetrTurnover;
    }

}