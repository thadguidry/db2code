package com.homihq;

import lombok.Data;

import java.util.List;

@Data
public class MetaData {

    private String domainPackageName;
    private List<Entity> entityList;

}
