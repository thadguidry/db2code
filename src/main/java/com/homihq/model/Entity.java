package com.homihq.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;

import java.util.List;

@Data
public class Entity {
    private String tableName;
    private String name;

    private List<Field> fields;
    private boolean autoIncrementingFieldPresent;
    private boolean compositePk;

    private String pkType;


    public static String toName(String tableName, String prefix, char delim) {
        String name = tableName;
        if(StringUtils.isNotBlank(prefix) && StringUtils.startsWith(tableName, prefix)) {
            name = tableName.replaceFirst(prefix, "");
        }

        name = CaseUtils.toCamelCase(name, true, delim);

        return name;
    }

}
