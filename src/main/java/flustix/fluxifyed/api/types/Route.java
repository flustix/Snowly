package flustix.fluxifyed.api.types;

import java.sql.SQLException;

public interface Route {
    String execute() throws SQLException;
}
