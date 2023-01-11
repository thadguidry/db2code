package com.homihq;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import lombok.extern.slf4j.Slf4j;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.Column;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schema.View;
import schemacrawler.tools.utility.SchemaCrawlerUtility;
import us.fatehi.utility.LoggingConfig;
import us.fatehi.utility.datasource.DatabaseConnectionSource;
import us.fatehi.utility.datasource.DatabaseConnectionSources;
import us.fatehi.utility.datasource.MultiUseUserCredentials;


@Slf4j
public final class CodeGeneratorManager {

  public static void main(final String[] args) throws Exception {
    //config - external
    String domainPackageName = "com.homihq.event.domain";
    String tableNamePrefix = "t_";
    char tableNameDelimiter = '_';
    char colNameDelimiter = '_';

    MetaData metaData = new MetaData();
    metaData.setDomainPackageName(domainPackageName);

    // Set log level
    new LoggingConfig(Level.OFF);

    CodeGeneratorConfiguration codeGeneratorConfiguration = CodeGeneratorConfiguration.builder()
            .domainPackageName(domainPackageName)
            .tableNamePrefix(tableNamePrefix)
            .tableNameDelimiter(tableNameDelimiter)
            .colNameDelimiter(colNameDelimiter)
            .includedSchemas(List.of("o2ofk"))
            .build();

    // Get the schema definition
    final DatabaseConnectionSource dataSource = getDataSource();
    final Catalog catalog = SchemaCrawlerUtility.getCatalog(dataSource, codeGeneratorConfiguration.createSchemaCrawlerOption());

    List<Entity> entityList = new ArrayList<>();
    metaData.setEntityList(entityList);

    OneToOneStrategy oneToOneStrategy = new OneToOneStrategy();

    for (final Schema schema : catalog.getSchemas()) {
      for (final Table table : catalog.getTables(schema)) {

        if (!(table instanceof View)) {
          System.out.println("table : " + table);
          Entity entity = new Entity();
          entity.setTableName(table.getName());
          entity.setName(entity.toName(table.getName(), tableNamePrefix, tableNameDelimiter));

          List<Field> fields = new ArrayList<>();
          entity.setFields(fields);


          for (final Column column : table.getColumns()) {

            Field field = new Field();
            field.setTableName(entity.getTableName());
            field.setEntityName(entity.getName());
            field.setColumnName(column.getName());
            field.setName(field.toName(column.getName(), colNameDelimiter));


            if(column.isPartOfPrimaryKey()) {
              field.setPartOfPk(true);
            }

            if(column.isAutoIncremented()) {
              entity.setAutoIncrementingFieldPresent(true);
              field.setAutoIncremented(true);
            }

            field.setJavaType(column.getColumnDataType().getTypeMappedClass().getSimpleName());

            One2One one2One =
            oneToOneStrategy.detect(column, tableNamePrefix, tableNameDelimiter, colNameDelimiter);

            field.setOne2One(one2One);

            fields.add(field);

          }

          entityList.add(entity);
        }


      }
    }

    JpaEntityGenerator jpaEntityGenerator = new JpaEntityGenerator();
    jpaEntityGenerator.generate(metaData);
  }

  private static DatabaseConnectionSource getDataSource() {
    final String connectionUrl = "jdbc:postgresql://localhost:5432/cyadoc";
    final DatabaseConnectionSource dataSource =
        DatabaseConnectionSources.newDatabaseConnectionSource(
            connectionUrl, new MultiUseUserCredentials("cyadoc", "postgres2020"));
    return dataSource;
  }
}
