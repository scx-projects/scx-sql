package dev.scx.sql;

/// JDBCConnectionInfo
///
/// @author scx567888
public record JDBCConnectionInfo(String url, String username, String password, String... parameters) {

}
