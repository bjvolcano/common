package com.volcano.test.rangeConfig;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.volcano.range.dto.IRangeData;
import com.volcano.range.filter.AbstructRangeFilter;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.volcano.range.mapping.Mapping;
import com.volcano.range.mapping.SourceFromInfo;

import java.util.List;

/**
 * @author volcano
 * @version 1.0
 * @date 2020/9/23 17:46
 */

public class OrganizationRangeFilter extends AbstructRangeFilter {
    private OrganizationRangeRangeData rangeData;

    public OrganizationRangeFilter(OrganizationRangeRangeData rangeData) {
        this.rangeData = rangeData;
    }

    @Override
    public String getFilterWhereSql(Mapping mapping) {
        List ids = rangeData.getOrgIdsString();
        if (ids==null)
            return null;
        String sql = " ";
        if (!StringUtils.isEmpty(mapping.getAlias()))
            sql += mapping.getAlias() + ".";

        sql += mapping.getField() + " in (" + String.join(",", ids) + ") ";
        return sql;
    }

}
