package com.homihq.model;

import lombok.Data;

import java.util.List;

@Data
public class MetaData {

    private String domainPackageName;
    private String repositoryPackageName;
    private String dtoPackageName;
    private String servicePackageName;
    private String exceptionPackageName;
    private String controllerPackageName;

    private List<Entity> entityList;

}
