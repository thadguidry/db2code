package com.homihq.strategy;

import com.homihq.model.Entity;
import com.homihq.model.Field;
import com.homihq.model.One2One;
import lombok.extern.slf4j.Slf4j;
import schemacrawler.schema.Column;

@Slf4j
public class OneToOneStrategy {

    public One2One detect(Column column, String tableNamePrefix, char tableNameDelimiter, char colNameDelimiter) {
        One2One one2One = new One2One();
        if(column.isPartOfForeignKey()) {
            System.out.println(" foreign key found !!!!!!!");
            if(column.isPartOfPrimaryKey()) {
                System.out.println(" *SHARED*");
                one2One.setShared(true);
                one2One.setAssociation(true);
                one2One.setOwningSide(true);

                Field referencedField = getReferenceField(column, tableNamePrefix, tableNameDelimiter, colNameDelimiter);
                one2One.setReferencedField(referencedField);
            }
            else {
                //check if this fk column is PK in reference table && reference table has only 1 pk
                Column refColumn =
                        column.getReferencedColumn();
                boolean isPk =
                column.getParent().getColumns().stream()
                        .anyMatch
                    (c -> c.isPartOfPrimaryKey() && c.getName().equals(refColumn.getName()));

                long colCount = column.getParent().getColumns().stream()
                        .filter(c -> c.isPartOfPrimaryKey())
                        .count();

                if (colCount == 1 && isPk) {
                    System.out.println(" *NOT SHARED* ");
                    one2One.setAssociation(true);
                    one2One.setOwningSide(true);

                    Field referencedField = getReferenceField(column, tableNamePrefix, tableNameDelimiter, colNameDelimiter);
                    System.out.println("referenced field - " + referencedField);
                    one2One.setReferencedField(referencedField);
                }

            }
        }

        return one2One;
    }

    private Field getReferenceField(Column column, String tableNamePrefix, char tableNameDelimiter, char colNameDelimiter) {
        Column refColumn =
                column.getReferencedColumn();

        System.out.println("referenced col - " + refColumn);

        Field referencedField = new Field();
        referencedField.setColumnName(refColumn.getName());
        referencedField.setName(referencedField.toName(refColumn.getName(), colNameDelimiter));
        referencedField.setTableName(refColumn.getParent().getName());
        referencedField.setEntityName(Entity.toName(
                refColumn.getParent().getName(), tableNamePrefix, tableNameDelimiter));
        return referencedField;
    }


}
