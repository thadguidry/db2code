package com.homihq.example;

import java.util.List;
import java.util.logging.Level;

import com.homihq.generator.*;
import com.homihq.model.MetaData;
import com.homihq.tool.Configuration;
import com.homihq.model.Entity;
import com.homihq.strategy.OneToOneDetectionStrategy;
import com.homihq.tool.DbMetaDataLoader;
import lombok.extern.slf4j.Slf4j;
import us.fatehi.utility.LoggingConfig;


@Slf4j
public final class PostgreSqlExample {

  public static void main(final String[] args) throws Exception {
    new LoggingConfig(Level.OFF);
    //config - external
    String domainPackageName = System.getenv().get("domainPackageName");
    String repositoryPackageName = System.getenv().get("repositoryPackageName");
    String dtoPackageName = System.getenv().get("dtoPackageName");
    String servicePackageName = System.getenv().get("servicePackageName");
    String exceptionPackageName = System.getenv().get("exceptionPackageName");
    String controllerPackageName = System.getenv().get("controllerPackageName");

    String tableNamePrefix = System.getenv().get("tableNamePrefix");
    char tableNameDelimiter = System.getenv().get("tableNameDelimiter").charAt(0);
    char colNameDelimiter = System.getenv().get("colNameDelimiter").charAt(0);

    Configuration configuration = Configuration.builder()
            .domainPackageName(domainPackageName)
            .tableNamePrefix(tableNamePrefix)
            .tableNameDelimiter(tableNameDelimiter)
            .colNameDelimiter(colNameDelimiter)
            .includedSchemas(List.of(System.getenv().get("schemas")))
            .build();

    DbMetaDataLoader dbMetaDataLoader = new DbMetaDataLoader(configuration);
    String jdbcUrl = System.getenv().get("jdbcUrl");
    String user = System.getenv().get("user");
    String password = System.getenv().get("password");

    List<Entity> entityList = dbMetaDataLoader.load(jdbcUrl, user, password);
    MetaData metaData = new MetaData();
    metaData.setDomainPackageName(domainPackageName);
    metaData.setRepositoryPackageName(repositoryPackageName);
    metaData.setDtoPackageName(dtoPackageName);
    metaData.setServicePackageName(servicePackageName);
    metaData.setExceptionPackageName(exceptionPackageName);
    metaData.setControllerPackageName(controllerPackageName);

    metaData.setEntityList(entityList);

    String folder = System.getenv().get("folder");
    JpaEntityGenerator jpaEntityGenerator = new JpaEntityGenerator();
    jpaEntityGenerator.generate(metaData, folder);

    RepositoryGenerator repositoryGenerator = new RepositoryGenerator();
    repositoryGenerator.generate(metaData, folder);

    DtoGenerator dtoGenerator = new DtoGenerator();
    dtoGenerator.generate(metaData, folder);

    ServiceGenerator serviceGenerator = new ServiceGenerator();
    serviceGenerator.generate(metaData, folder);

    ExceptionGenerator exceptionGenerator = new ExceptionGenerator();
    exceptionGenerator.generate(metaData, folder);

    ControllerGenerator controllerGenerator = new ControllerGenerator();
    controllerGenerator.generate(metaData, folder);
  }

}
