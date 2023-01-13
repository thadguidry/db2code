package com.homihq.tool;

import lombok.*;
import schemacrawler.inclusionrule.RegularExpressionInclusionRule;
import schemacrawler.schemacrawler.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Configuration {

    String domainPackageName;
    String tableNamePrefix;
    char tableNameDelimiter;
    char colNameDelimiter;

    List<String> includedSchemas;
    List<String> includedTables;


    public SchemaCrawlerOptions createSchemaCrawlerOption() {
        // Create the options
        final LimitOptionsBuilder limitOptionsBuilder =
                LimitOptionsBuilder.builder()
                        .includeSchemas(new RegularExpressionInclusionRule("o2ofk"));

        final LoadOptionsBuilder loadOptionsBuilder =
                LoadOptionsBuilder.builder()
                        // Set what details are required in the schema - this affects the
                        // time taken to crawl the schema
                        .withSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
        return
                SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions()
                        .withLimitOptions(limitOptionsBuilder.toOptions())
                        .withLoadOptions(loadOptionsBuilder.toOptions());
    }
}
