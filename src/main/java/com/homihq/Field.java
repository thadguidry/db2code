package com.homihq;

import lombok.Data;
import org.apache.commons.text.CaseUtils;

@Data
public class Field {

    private String columnName;
    private String name;
    private String javaType;
    private boolean autoIncremented;


    public String toName(String columnName,  char delim) {
        return CaseUtils.toCamelCase(columnName, false, delim);
    }
}
