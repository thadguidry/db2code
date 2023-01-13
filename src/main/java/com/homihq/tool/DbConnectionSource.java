package com.homihq.tool;

import us.fatehi.utility.datasource.DatabaseConnectionSource;
import us.fatehi.utility.datasource.DatabaseConnectionSources;
import us.fatehi.utility.datasource.MultiUseUserCredentials;

class DbConnectionSource {

    private DbConnectionSource() {}

    public static DatabaseConnectionSource getDataSource(String connectionUrl, String username, String password) {
       return
                DatabaseConnectionSources.newDatabaseConnectionSource(
                        connectionUrl, new MultiUseUserCredentials(username, password));

    }
}
