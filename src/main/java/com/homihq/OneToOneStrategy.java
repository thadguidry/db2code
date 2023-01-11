package com.homihq;

import lombok.extern.slf4j.Slf4j;
import schemacrawler.schema.Column;

@Slf4j
public class OneToOneStrategy {

    public One2One detect(Column column, String tableNamePrefix, char tableNameDelimiter, char colNameDelimiter) {
        //case 1

        One2One one2One = new One2One();

        if(column.isPartOfForeignKey()) {

            System.out.println(" foreign key found !!!!!!!");

            if(column.isPartOfPrimaryKey()) {
                System.out.println(" *SHARED*");
                one2One.setShared(true);
                one2One.setAssociation(true);
                one2One.setOwningSide(true);
            }

            long colCount = column.getParent().getColumns().stream()
                    .filter(c -> c.isPartOfPrimaryKey())
                    .count();

            if(colCount == 1) {
                one2One.setAssociation(true);
                one2One.setOwningSide(true);
            }

            Field referencedField = getReferenceField(column, tableNamePrefix, tableNameDelimiter, colNameDelimiter);
            one2One.setReferencedField(referencedField);
        }

        return one2One;
    }

    private Field getReferenceField(Column column, String tableNamePrefix, char tableNameDelimiter, char colNameDelimiter) {
        Column refColumn =
                column.getReferencedColumn();

        Field referencedField = new Field();
        referencedField.setColumnName(refColumn.getName());
        referencedField.setName(referencedField.toName(refColumn.getName(), colNameDelimiter));
        referencedField.setTableName(refColumn.getParent().getName());
        referencedField.setEntityName(Entity.toName(
                refColumn.getParent().getName(), tableNamePrefix, tableNameDelimiter));
        return referencedField;
    }


}
