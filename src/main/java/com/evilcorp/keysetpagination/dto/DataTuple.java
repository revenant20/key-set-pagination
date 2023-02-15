package com.evilcorp.keysetpagination.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class DataTuple {
    @CsvBindByName(column = "page")
    private int page;
    @CsvBindByName(column = "time")
    private long time;
}
