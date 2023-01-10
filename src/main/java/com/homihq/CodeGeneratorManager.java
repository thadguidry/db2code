package com.homihq;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import lombok.extern.slf4j.Slf4j;
import schemacrawler.inclusionrule.RegularExpressionInclusionRule;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.Column;
import schemacrawler.schema.Schema;
import schemacrawler.schema.Table;
import schemacrawler.schema.View;
import schemacrawler.schemacrawler.LimitOptionsBuilder;
import schemacrawler.schemacrawler.LoadOptionsBuilder;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
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

    // Create the options
    final LimitOptionsBuilder limitOptionsBuilder =
        LimitOptionsBuilder.builder()
            .includeSchemas(new RegularExpressionInclusionRule("author"))
            .includeTables(tableFullName -> !tableFullName.contains("ΒΙΒΛΊΑ"));
    final LoadOptionsBuilder loadOptionsBuilder =
        LoadOptionsBuilder.builder()
            // Set what details are required in the schema - this affects the
            // time taken to crawl the schema
            .withSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
    final SchemaCrawlerOptions options =
        SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions()
            .withLimitOptions(limitOptionsBuilder.toOptions())
            .withLoadOptions(loadOptionsBuilder.toOptions());

    // Get the schema definition
    final DatabaseConnectionSource dataSource = getDataSource();
    final Catalog catalog = SchemaCrawlerUtility.getCatalog(dataSource, options);

    List<Entity> entityList = new ArrayList<>();
    metaData.setEntityList(entityList);

    for (final Schema schema : catalog.getSchemas()) {
      System.out.println(schema);
      for (final Table table : catalog.getTables(schema)) {
        System.out.print("o--> " + table);
        if (table instanceof View) {
          System.out.println(" (VIEW)");
        } else {
          System.out.println();
          Entity entity = new Entity();
          entity.setTableName(table.getName());
          entity.setName(entity.toName(
                  table.getName(), tableNamePrefix, tableNameDelimiter));

          List<Field> fields = new ArrayList<>();
          entity.setFields(fields);
          boolean autoIncrementingFieldPresent = false;

          for (final Column column : table.getColumns()) {
            System.out.printf("     o--> %s (%s)%n", column, column.getType());

            Field field = new Field();
            field.setColumnName(column.getName());
            field.setName(
                    field.toName(column.getName(), colNameDelimiter)
                    );
            System.out.println("Col : " +  column.getName());
            System.out.println("DB Type : " +  column.getType().getName());
            System.out.println("Java SQL Type : " +  column.getColumnDataType().getJavaSqlType().getName());
            System.out.println("Java type " +  column.getColumnDataType().getTypeMappedClass());
            System.out.println("Auto increment : " +  column.isAutoIncremented());

            if(column.isAutoIncremented()) {
              entity.setAutoIncrementingFieldPresent(true);
              field.setAutoIncremented(true);
            }

            field.setJavaType(column.getColumnDataType().getTypeMappedClass().getSimpleName());

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
