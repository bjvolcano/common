package com.volcano.range.filter;

import com.alibaba.druid.sql.ast.SQLExpr;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.volcano.range.dto.RangeData;
import com.volcano.range.mapping.Mapping;
import com.volcano.range.mapping.SourceFromInfo;

/**
 * @author volcano
 * @version 1.0
 * @date 2020/9/23 17:46
 */
public class OrganizationRangeFilter extends AbstructRangeFilter {
    @Override
    public String getFilterWhereSql(Mapping mapping) {
        if(CollectionUtils.isEmpty(RangeData.getOrgIdsString()))
            return null;
        String sql=" ";
        if(!StringUtils.isEmpty(mapping.getAlias()))
            sql+=mapping.getAlias()+".";
        sql+=mapping.getField()+" in ("+String.join(",",RangeData.getOrgIdsString())+") ";
        return sql;
    }

    @Override
    public SQLExpr getFilterCondition(SourceFromInfo tableNameInfo) {
        return null;
    }
}
