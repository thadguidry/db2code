package com.homihq;

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



    public String toName(String tableName, String prefix, char delim) {
        String name = tableName;
        if(StringUtils.isNotBlank(prefix)) {
            name = tableName.replace(prefix, "");
        }

        name = CaseUtils.toCamelCase(name, true, delim);

        return name;
    }

}
