package flustix.fluxifyed.database;

import java.sql.ResultSet;

public interface IQueryCallback {
    void onResult(ResultSet result);
}
