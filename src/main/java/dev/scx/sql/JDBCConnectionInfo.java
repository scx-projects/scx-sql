package dev.scx.sql;

/// JDBCConnectionInfo
///
/// @author scx567888
/// @version 0.0.1
public record JDBCConnectionInfo(String url, String username, String password, String... parameters) {

}
