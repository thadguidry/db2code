package com.homihq;

import lombok.extern.slf4j.Slf4j;
import schemacrawler.schema.Column;

@Slf4j
public class OneToOneDetector {

    public One2One detect(Column column, String tableNamePrefix, char tableNameDelimiter, char colNameDelimiter) {
        //case 1

        One2One one2One = new One2One();

        if(column.isPartOfForeignKey() && column.isPartOfPrimaryKey()) {

            System.out.println("shared foreign key found !!!!!!! --> *SHARED*");
            one2One.setAssociation(true);
            one2One.setShared(true);
            one2One.setOwningSide(true);

            Column refColumn =
            column.getReferencedColumn();

            Field referencedField = new Field();
            referencedField.setColumnName(refColumn.getName());
            referencedField.setName(referencedField.toName(refColumn.getName(), colNameDelimiter));
            referencedField.setTableName(refColumn.getParent().getName());
            referencedField.setEntityName(Entity.toName(
                    refColumn.getParent().getName(), tableNamePrefix, tableNameDelimiter));

            one2One.setReferencedField(referencedField);
        }
        else if(column.isPartOfForeignKey()) {
            System.out.println("shared foreign key found !!!!!!! --> *FK*");
            one2One.setAssociation(true);

            one2One.setOwningSide(true);

            Column refColumn =
                    column.getReferencedColumn();

            Field referencedField = new Field();
            referencedField.setColumnName(refColumn.getName());
            referencedField.setName(referencedField.toName(refColumn.getName(), colNameDelimiter));
            referencedField.setTableName(refColumn.getParent().getName());
            referencedField.setEntityName(Entity.toName(
                    refColumn.getParent().getName(), tableNamePrefix, tableNameDelimiter));

            one2One.setReferencedField(referencedField);
        }




        return one2One;
    }
}
