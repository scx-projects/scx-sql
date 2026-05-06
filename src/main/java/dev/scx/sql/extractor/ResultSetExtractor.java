package dev.scx.sql.extractor;

import dev.scx.function.Function1Void;
import dev.scx.reflect.FieldInfo;
import dev.scx.sql.TypeSQLResolver;
import dev.scx.sql.extractor.bean.BeanBuilder;
import dev.scx.sql.extractor.bean.BeanConsumerExtractor;
import dev.scx.sql.extractor.bean.BeanExtractor;
import dev.scx.sql.extractor.bean.BeanListExtractor;
import dev.scx.sql.extractor.map.MapBuilder;
import dev.scx.sql.extractor.map.MapConsumerExtractor;
import dev.scx.sql.extractor.map.MapExtractor;
import dev.scx.sql.extractor.map.MapListExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.scx.sql.TypeSQLResolver.DEFAULT_TYPE_SQL_RESOLVER;
import static dev.scx.sql.extractor.map.MapExtractor.DEFAULT_MAP_EXTRACTOR;
import static dev.scx.sql.extractor.map.MapListExtractor.DEFAULT_MAP_LIST_EXTRACTOR;

/// ResultSetExtractor
///
/// @author scx567888
/// @version 0.0.1
public interface ResultSetExtractor<T, X extends Throwable> {

    // ************************************ Map 相关 ************************************

    static ResultSetExtractor<Map<String, Object>, RuntimeException> ofMap() {
        return DEFAULT_MAP_EXTRACTOR;
    }

    static ResultSetExtractor<Map<String, Object>, RuntimeException> ofMap(MapBuilder mapBuilder) {
        return new MapExtractor(mapBuilder);
    }

    static ResultSetExtractor<Map<String, Object>, RuntimeException> ofMap(Supplier<Map<String, Object>> mapSupplier) {
        return new MapExtractor(MapBuilder.of(mapSupplier));
    }

    static ResultSetExtractor<Map<String, Object>, RuntimeException> ofMap(Function<String, String> mapKeyMapping) {
        return new MapExtractor(MapBuilder.of(mapKeyMapping));
    }

    static ResultSetExtractor<List<Map<String, Object>>, RuntimeException> ofMapList() {
        return DEFAULT_MAP_LIST_EXTRACTOR;
    }

    static ResultSetExtractor<List<Map<String, Object>>, RuntimeException> ofMapList(MapBuilder mapBuilder) {
        return new MapListExtractor(mapBuilder);
    }

    static ResultSetExtractor<List<Map<String, Object>>, RuntimeException> ofMapList(Supplier<Map<String, Object>> mapSupplier) {
        return new MapListExtractor(MapBuilder.of(mapSupplier));
    }

    static ResultSetExtractor<List<Map<String, Object>>, RuntimeException> ofMapList(Function<String, String> mapKeyMapping) {
        return new MapListExtractor(MapBuilder.of(mapKeyMapping));
    }

    static <X extends Throwable> ResultSetExtractor<Void, X> ofMapConsumer(Function1Void<Map<String, Object>, X> consumer) {
        return new MapConsumerExtractor<>(MapBuilder.of(), consumer);
    }

    static <X extends Throwable> ResultSetExtractor<Void, X> ofMapConsumer(MapBuilder mapBuilder, Function1Void<Map<String, Object>, X> consumer) {
        return new MapConsumerExtractor<>(mapBuilder, consumer);
    }

    static <X extends Throwable> ResultSetExtractor<Void, X> ofMapConsumer(Supplier<Map<String, Object>> mapSupplier, Function1Void<Map<String, Object>, X> consumer) {
        return new MapConsumerExtractor<>(MapBuilder.of(mapSupplier), consumer);
    }

    static <X extends Throwable> ResultSetExtractor<Void, X> ofMapConsumer(Function<String, String> mapKeyMapping, Function1Void<Map<String, Object>, X> consumer) {
        return new MapConsumerExtractor<>(MapBuilder.of(mapKeyMapping), consumer);
    }

    // ************************************ Bean 相关 ************************************

    static <C> ResultSetExtractor<C, RuntimeException> ofBean(Class<C> clazz) {
        return new BeanExtractor<>(BeanBuilder.of(clazz));
    }

    static <C> ResultSetExtractor<C, RuntimeException> ofBean(Class<C> clazz, Function<FieldInfo, String> fieldColumnLabelMapping) {
        return new BeanExtractor<>(BeanBuilder.of(clazz, fieldColumnLabelMapping));
    }

    static <C> ResultSetExtractor<C, RuntimeException> ofBean(BeanBuilder<C> beanBuilder) {
        return new BeanExtractor<>(beanBuilder);
    }

    static <C> ResultSetExtractor<List<C>, RuntimeException> ofBeanList(Class<C> clazz) {
        return new BeanListExtractor<>(BeanBuilder.of(clazz));
    }

    static <C> ResultSetExtractor<List<C>, RuntimeException> ofBeanList(Class<C> clazz, Function<FieldInfo, String> fieldColumnLabelMapping) {
        return new BeanListExtractor<>(BeanBuilder.of(clazz, fieldColumnLabelMapping));
    }

    static <C> ResultSetExtractor<List<C>, RuntimeException> ofBeanList(BeanBuilder<C> beanBuilder) {
        return new BeanListExtractor<>(beanBuilder);
    }

    static <C, X extends Throwable> ResultSetExtractor<Void, X> ofBeanConsumer(Class<C> clazz, Function1Void<C, X> consumer) {
        return new BeanConsumerExtractor<>(BeanBuilder.of(clazz), consumer);
    }

    static <C, X extends Throwable> ResultSetExtractor<Void, X> ofBeanConsumer(Class<C> clazz, Function<FieldInfo, String> fieldColumnLabelMapping, Function1Void<C, X> consumer) {
        return new BeanConsumerExtractor<>(BeanBuilder.of(clazz, fieldColumnLabelMapping), consumer);
    }

    static <C, X extends Throwable> ResultSetExtractor<Void, X> ofBeanConsumer(BeanBuilder<C> beanBuilder, Function1Void<C, X> consumer) {
        return new BeanConsumerExtractor<>(beanBuilder, consumer);
    }

    // ************************************ SingleValue 相关 ************************************

    static <C> ResultSetExtractor<C, RuntimeException> ofSingleValue(Class<C> clazz) {
        return new SingleValueExtractor<>(clazz);
    }

    static <C> ResultSetExtractor<C, RuntimeException> ofSingleValue(String columnLabel, Class<C> clazz) {
        return new SingleValueExtractor<>(columnLabel, clazz);
    }

    static <C> ResultSetExtractor<C, RuntimeException> ofSingleValue(int columnIndex, Class<C> clazz) {
        return new SingleValueExtractor<>(columnIndex, clazz);
    }

    /// extract 必须在执行期间同步消费 ResultSet, 不得在返回后继续持有或使用它
    T extract(ResultSet rs, TypeSQLResolver resolver) throws SQLException, X;

    default T extract(ResultSet rs) throws SQLException, X {
        return extract(rs, DEFAULT_TYPE_SQL_RESOLVER);
    }

}
