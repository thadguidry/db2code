package com.homihq.model;

import lombok.Data;

import java.util.List;

@Data
public class MetaData {

    private String domainPackageName;
    private String repositoryPackageName;
    private List<Entity> entityList;

}
