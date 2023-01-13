package com.homihq.example;

import java.util.List;

import com.homihq.model.MetaData;
import com.homihq.tool.Configuration;
import com.homihq.model.Entity;
import com.homihq.strategy.OneToOneDetectionStrategy;
import com.homihq.generator.JpaEntityGenerator;
import com.homihq.tool.DbMetaDataLoader;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public final class PostgreSqlExample {

  public static void main(final String[] args) throws Exception {
    //config - external
    String domainPackageName = System.getenv().get("domainPackageName");
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
    metaData.setEntityList(entityList);

    //JpaEntityGenerator jpaEntityGenerator = new JpaEntityGenerator();
    //jpaEntityGenerator.generate(metaData);
  }

}
