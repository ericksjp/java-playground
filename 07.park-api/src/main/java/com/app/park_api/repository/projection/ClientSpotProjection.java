package com.app.park_api.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface ClientSpotProjection {
    String getlicensePlate();
    String getmodel();
    String getbrand();
    String getcolor();
    String getreceipt();
    String getspotCode();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonProperty("check_in")
    LocalDateTime getcheckIn();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonProperty("check_out")
    LocalDateTime getcheckOut();

    BigDecimal getprice();

    BigDecimal getdiscount();
}
