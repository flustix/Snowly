package flustix.fluxifyed.database;

import java.sql.ResultSet;

public interface QueryCallback {
    void onResult(ResultSet result);
}
