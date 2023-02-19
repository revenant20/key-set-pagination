package com.evilcorp.keysetpagination.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataTuple {
    @CsvBindByName(column = "page")
    private int page;
    @CsvBindByName(column = "time")
    private long time;
}
