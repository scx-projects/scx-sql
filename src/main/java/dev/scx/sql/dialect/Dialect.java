package dev.scx.sql.dialect;

import dev.scx.sql.JDBCConnectionInfo;

import javax.sql.DataSource;

/// 关系型数据库方言
///
/// @author scx567888
/// @version 0.0.1
public interface Dialect extends SchemaDialect {

    /// 是否可以处理
    boolean canHandle(String url);

    /// 是否可以处理
    boolean canHandle(DataSource dataSource);

    /// 创建数据源
    DataSource createDataSource(JDBCConnectionInfo connectionInfo);

}
