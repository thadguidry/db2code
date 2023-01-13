package com.homihq.strategy;

import schemacrawler.schema.Column;

public interface AssociationDetectionStrategy<T>{

    T detect(Column column, String tableNamePrefix, char tableNameDelimiter, char colNameDelimiter);
}
