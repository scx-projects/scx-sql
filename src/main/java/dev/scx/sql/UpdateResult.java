package dev.scx.sql;

import java.util.List;

/// UpdateResult
///
/// @author scx567888
/// @version 0.0.1
public record UpdateResult(long affectedItemsCount, List<Long> generatedKeys) {

    public Long firstGeneratedKey() {
        return this.generatedKeys.size() > 0 ? this.generatedKeys.get(0) : null;
    }

}
