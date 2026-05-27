package dev.scx.sql;

import dev.scx.sql.extractor.ResultSetExtractor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static dev.scx.sql.TypeSQLResolver.DEFAULT_TYPE_SQL_RESOLVER;
import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_FORWARD_ONLY;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

/// SQLRunner
///
/// 提供 一些 静态方法
///
/// @author scx567888
/// @version 0.0.1
public final class SQLRunner {

    private static void fillParams(PreparedStatement preparedStatement, SQL sql, TypeSQLResolver resolver) throws SQLException {
        var params = sql.params();
        // 单条参数
        if (params != null) {
            fillPreparedStatement(preparedStatement, params, resolver);
        }
    }

    private static void fillBatchParams(PreparedStatement preparedStatement, BatchSQL batchSQL, TypeSQLResolver resolver) throws SQLException {
        var batchParams = batchSQL.batchParams();
        // 批量参数
        if (batchParams != null) {
            for (var params : batchParams) {
                if (params != null) {
                    fillPreparedStatement(preparedStatement, params, resolver);
                    preparedStatement.addBatch();
                }
            }
        }
    }

    private static void fillPreparedStatement(PreparedStatement preparedStatement, Object[] params, TypeSQLResolver resolver) throws SQLException {
        var index = 1;
        for (var tempValue : params) {
            resolver.bind(preparedStatement, index, tempValue);
            index = index + 1;
        }
    }

    private static List<Long> getGeneratedKeys(PreparedStatement preparedStatement) throws SQLException {
        try (var resultSet = preparedStatement.getGeneratedKeys()) {
            var ids = new ArrayList<Long>();
            while (resultSet.next()) {
                ids.add(resultSet.getLong(1));
            }
            return ids;
        }
    }

    public static <T, X extends Throwable> T query(Connection con, SQL sql, ResultSetExtractor<T, X> extractor, TypeSQLResolver resolver) throws SQLException, X {
        try (var preparedStatement = con.prepareStatement(sql.sql(), TYPE_FORWARD_ONLY, CONCUR_READ_ONLY)) {
            fillParams(preparedStatement, sql, resolver);
            try (var resultSet = preparedStatement.executeQuery()) {
                return extractor.extract(resultSet, resolver);
            }
        }
    }

    public static UpdateResult update(Connection con, SQL sql, TypeSQLResolver resolver) throws SQLException {
        try (var preparedStatement = con.prepareStatement(sql.sql(), RETURN_GENERATED_KEYS)) {
            fillParams(preparedStatement, sql, resolver);
            long affectedItemsCount = preparedStatement.executeLargeUpdate();
            var generatedKeys = getGeneratedKeys(preparedStatement);
            return new UpdateResult(affectedItemsCount, generatedKeys);
        }
    }

    public static UpdateResult update(Connection con, BatchSQL batchSQL, TypeSQLResolver resolver) throws SQLException {
        try (var preparedStatement = con.prepareStatement(batchSQL.sql(), RETURN_GENERATED_KEYS)) {
            fillBatchParams(preparedStatement, batchSQL, resolver);
            long affectedItemsCount = 0L;
            var counts = preparedStatement.executeLargeBatch();
            for (long count : counts) {
                affectedItemsCount += count;
            }
            var generatedKeys = getGeneratedKeys(preparedStatement);
            return new UpdateResult(affectedItemsCount, generatedKeys);
        }
    }

    public static long execute(Connection con, SQL sql, TypeSQLResolver resolver) throws SQLException {
        try (var preparedStatement = con.prepareStatement(sql.sql(), RETURN_GENERATED_KEYS)) {
            fillParams(preparedStatement, sql, resolver);
            preparedStatement.execute();
            return preparedStatement.getLargeUpdateCount();
        }
    }

    public static <T, X extends Throwable> T query(Connection con, SQL sql, ResultSetExtractor<T, X> extractor) throws SQLException, X {
        return query(con, sql, extractor, DEFAULT_TYPE_SQL_RESOLVER);
    }

    public static UpdateResult update(Connection con, SQL sql) throws SQLException {
        return update(con, sql, DEFAULT_TYPE_SQL_RESOLVER);
    }

    public static UpdateResult update(Connection con, BatchSQL batchSQL) throws SQLException {
        return update(con, batchSQL, DEFAULT_TYPE_SQL_RESOLVER);
    }

    public static long execute(Connection con, SQL sql) throws SQLException {
        return execute(con, sql, DEFAULT_TYPE_SQL_RESOLVER);
    }

}
