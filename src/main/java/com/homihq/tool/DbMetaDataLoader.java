package com.homihq.tool;

import com.homihq.model.Entity;
import com.homihq.model.Field;
import com.homihq.model.One2One;
import com.homihq.strategy.OneToOneDetectionStrategy;
import lombok.extern.slf4j.Slf4j;
import schemacrawler.schema.*;
import schemacrawler.tools.utility.SchemaCrawlerUtility;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public final class DbMetaDataLoader {

  private final Configuration configuration;
  private final OneToOneDetectionStrategy oneToOneStrategy;

  public DbMetaDataLoader(Configuration configuration) {
    this.configuration = configuration;
    oneToOneStrategy = new OneToOneDetectionStrategy();
  }

  public List<Entity> load(String jdbcUrl, String username, String password) throws Exception {

    // Get the schema definition
    final Catalog catalog = SchemaCrawlerUtility.getCatalog(DbConnectionSource.getDataSource(jdbcUrl, username, password), configuration.createSchemaCrawlerOption());

    List<Entity> entityList = new ArrayList<>();

    for (final Schema schema : catalog.getSchemas()) {
      for (final Table table : catalog.getTables(schema)) {

        if (!(table instanceof View)) {
          log.info("Loading Table - {}" , table);

          Entity entity = new Entity();
          entity.setTableName(table.getName());
          String entityName = entity.toName(table.getName(), configuration.tableNamePrefix, configuration.tableNameDelimiter);
          System.out.println("entityName -> " + entityName);
          entity.setName(entity.toName(table.getName(), configuration.tableNamePrefix, configuration.tableNameDelimiter));

          List<Field> fields = new ArrayList<>();
          entity.setFields(fields);

          boolean compositePk =  table.getColumns().stream()
                  .filter(c -> c.isPartOfPrimaryKey())
                  .count() > 1;
          entity.setCompositePk(compositePk);

          String pkType = "";

          for (final Column column : table.getColumns()) {

            Field field = new Field();
            field.setTableName(entity.getTableName());
            field.setEntityName(entity.getName());
            field.setColumnName(column.getName());
            field.setName(field.toName(column.getName(), configuration.colNameDelimiter));
            field.setPartOfPk(column.isPartOfPrimaryKey());


            if(column.isAutoIncremented()) {
              entity.setAutoIncrementingFieldPresent(true);
              field.setAutoIncremented(true);
            }

            if(column.isPartOfPrimaryKey()) {
              pkType = column.getColumnDataType().getTypeMappedClass().getSimpleName();
            }
            field.setJavaType(column.getColumnDataType().getTypeMappedClass().getSimpleName());

            One2One one2One =
            oneToOneStrategy.detect(column, configuration.tableNamePrefix, configuration.tableNameDelimiter,
                    configuration.colNameDelimiter);

            field.setOne2One(one2One);
            fields.add(field);

          }

          if(compositePk) {
            pkType = entityName + "Id";
          }
          entity.setPkType(pkType);

          entityList.add(entity);
        }
      }


    }

    return entityList;

  }


}
