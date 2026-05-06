package dev.scx.sql;

import dev.scx.transaction.Transaction;
import dev.scx.transaction.exception.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;

/// SQLTransaction
///
/// @author scx567888
/// @version 0.0.1
public final class SQLTransaction implements Transaction {

    private final SQLClient sqlClient;
    private final Connection connection;

    SQLTransaction(SQLClient sqlClient, Connection connection) {
        this.sqlClient = sqlClient;
        this.connection = connection;
    }

    SQLClient sqlClient() {
        return sqlClient;
    }

    Connection connection() {
        return connection;
    }

    @Override
    public void commit() throws TransactionException {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    @Override
    public void rollback() throws TransactionException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

    @Override
    public void close() throws TransactionException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new TransactionException(e);
        }
    }

}
