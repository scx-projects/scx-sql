package dev.scx.sql;

import dev.scx.sql.dialect.Dialect;
import dev.scx.sql.extractor.ResultSetExtractor;
import dev.scx.transaction.TransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.function.Function;

import static dev.scx.sql.TypeSQLResolver.DEFAULT_RESOLVER;
import static dev.scx.sql.dialect.DialectSelector.findDialect;

/// SQLClient
///
/// @author scx567888
/// @version 0.0.1
public interface SQLClient extends TransactionManager<SQLTransaction> {

    /// 手动指定 resolver, 手动指定 dialect.
    static SQLClient of(DataSource dataSource, TypeSQLResolver resolver, Dialect dialect) {
        return new SQLClientImpl(dataSource, resolver, dialect);
    }

    /// 手动 resolver, 自动推断 dialect.
    static SQLClient of(DataSource dataSource, TypeSQLResolver resolver) {
        var dialect = findDialect(dataSource);
        return new SQLClientImpl(dataSource, resolver, dialect);
    }

    /// 默认 resolver, 手动指定 dialect.
    static SQLClient of(DataSource dataSource, Dialect dialect) {
        return of(dataSource, DEFAULT_RESOLVER, dialect);
    }

    /// 默认 resolver, 自动推断 dialect.
    static SQLClient of(DataSource dataSource) {
        return of(dataSource, DEFAULT_RESOLVER);
    }

    /// 从 JDBC URL 推断 dialect, 创建 DataSource, 自定义 resolver。
    @SafeVarargs
    static SQLClient of(JDBCConnectionInfo connectionInfo, TypeSQLResolver resolver, Function<DataSource, DataSource>... wrappers) {
        var dialect = findDialect(connectionInfo.url());
        var dataSource = dialect.createDataSource(connectionInfo);
        for (var wrapper : wrappers) {
            dataSource = wrapper.apply(dataSource);
        }
        return new SQLClientImpl(dataSource, resolver, dialect);
    }

    /// 从 JDBC URL 推断 dialect, 创建 DataSource, 默认 resolver。
    @SafeVarargs
    static SQLClient of(JDBCConnectionInfo connectionInfo, Function<DataSource, DataSource>... wrappers) {
        return of(connectionInfo, DEFAULT_RESOLVER, wrappers);
    }

    DataSource dataSource();

    Dialect dialect();

    /// query (支持事务)
    <T, X extends Throwable> T query(SQL sql, ResultSetExtractor<T, X> extractor) throws SQLException, X;

    /// update (支持事务)
    UpdateResult update(SQL sql) throws SQLException;

    /// update 批量参数 (支持事务)
    UpdateResult update(BatchSQL batchSQL) throws SQLException;

    /// execute (支持事务)
    long execute(SQL sql) throws SQLException;

}
