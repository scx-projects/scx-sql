package dev.scx.sql;

import dev.scx.function.Function0;
import dev.scx.function.Function0Void;
import dev.scx.sql.dialect.Dialect;
import dev.scx.sql.extractor.ResultSetExtractor;
import dev.scx.transaction.exception.TransactionException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/// SQLClientImpl
///
/// @author scx567888
/// @version 0.0.1
final class SQLClientImpl implements SQLClient {

    private final DataSource dataSource;
    private final TypeSQLResolver resolver;
    private final Dialect dialect;
    private final ScopedValue<Connection> connectionScopedValue;

    public SQLClientImpl(DataSource dataSource, TypeSQLResolver resolver, Dialect dialect) {
        this.dataSource = dataSource;
        this.resolver = resolver;
        this.dialect = dialect;
        this.connectionScopedValue = ScopedValue.newInstance();
    }

    @Override
    public DataSource dataSource() {
        return dataSource;
    }

    @Override
    public Dialect dialect() {
        return dialect;
    }

    @Override
    public <T, X extends Throwable> T query(SQL sql, ResultSetExtractor<T, X> extractor) throws SQLException, X {
        // 我们根据 contextConnection 来判断是否处于 Transaction 中
        var contextConnection = getContextConnection();
        if (contextConnection != null) {
            return SQLRunner.query(contextConnection, sql, extractor, resolver);
        } else {
            try (var con = this.createConnection(true)) {
                return SQLRunner.query(con, sql, extractor, resolver);
            }
        }
    }

    @Override
    public UpdateResult update(SQL sql) throws SQLException {
        // 我们根据 contextConnection 来判断是否处于 Transaction 中
        var contextConnection = getContextConnection();
        if (contextConnection != null) {
            return SQLRunner.update(contextConnection, sql, resolver);
        } else {
            try (var con = this.createConnection(true)) {
                return SQLRunner.update(con, sql, resolver);
            }
        }
    }

    @Override
    public UpdateResult update(BatchSQL batchSQL) throws SQLException {
        // 我们根据 contextConnection 来判断是否处于 Transaction 中
        var contextConnection = getContextConnection();
        if (contextConnection != null) {
            return SQLRunner.update(contextConnection, batchSQL, resolver);
        } else {
            try (var con = this.createConnection(true)) {
                return SQLRunner.update(con, batchSQL, resolver);
            }
        }
    }

    @Override
    public long execute(SQL sql) throws SQLException {
        // 我们根据 contextConnection 来判断是否处于 Transaction 中
        var contextConnection = getContextConnection();
        if (contextConnection != null) {
            return SQLRunner.execute(contextConnection, sql, resolver);
        } else {
            try (var con = this.createConnection(true)) {
                return SQLRunner.execute(con, sql, resolver);
            }
        }
    }

    @Override
    public SQLTransaction begin() throws TransactionException {
        try {
            return new SQLTransaction(this, createConnection(false));
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    @Override
    public <R, X extends Throwable> R with(SQLTransaction tx, Function0<R, X> handler) throws X {
        if (tx.sqlClient() != this) {
            throw new IllegalArgumentException("Transaction does not belong to this SQLClient");
        }
        return ScopedValue.where(connectionScopedValue, tx.connection()).call(handler::apply);
    }

    @Override
    public <X extends Throwable> void with(SQLTransaction tx, Function0Void<X> handler) throws X {
        if (tx.sqlClient() != this) {
            throw new IllegalArgumentException("Transaction does not belong to this SQLClient");
        }
        ScopedValue.where(connectionScopedValue, tx.connection()).call(() -> {
            handler.apply();
            return null;
        });
    }

    private Connection createConnection(boolean autoCommit) throws SQLException {
        var con = dataSource.getConnection();
        con.setAutoCommit(autoCommit);
        return con;
    }

    /// 返回当前作用域下可复用的上下文连接.
    /// - 若当前作用域未绑定上下文连接, 返回 null;
    /// - 调用方不得通过其他方式判断是否存在可复用上下文连接, 必须统一经过本方法.
    private Connection getContextConnection() {
        return connectionScopedValue.isBound() ? connectionScopedValue.get() : null;
    }

}
