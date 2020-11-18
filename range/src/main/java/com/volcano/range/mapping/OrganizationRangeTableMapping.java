package com.volcano.range.mapping;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;

import java.util.ArrayList;

/**
 * volcano
 * 此处暂时单模块运行，后期可以作为公共数据加载类，可以通过数据库拉取，作为全局的范围限制
 * 通过@EnableOrganzationRange 注解来开启是否    根据组织机构 过滤 本模块下的数据
 */
public class OrganizationRangeTableMapping extends ITableMapping {

    @Override
    public void initMapping() {
        mappings=new ArrayList<>();

        /*    xx模块    */
        /*示例*/
        mappings.add(new Mapping("表名","字段名"));

        /*    xx模块    */
        /*测试  申请表*/
        //mappings.add(new Mapping("oa_process_apply ","organization_id"));

    }

    public static void main(String[] args) {
        String sql="select count(1) from d par " +
                " INNER JOIN f pa " +
                " on par.xxx=pa.process_apply_id where par.aaa = ? " +
                " and bbb <> '0' and par.bbb=? " +
                "" +
                "" +
                " " +
                "";

        sql="select count(1) from b par inner join c a left join (select * from f where 1=1) where 1=1 order by xx limit 10";


        sql=" select * from ( select organization_id from a )  a ";
        ITableMapping m=new OrganizationRangeTableMapping();
        m.initMapping();

        sql = sql.toLowerCase().replaceAll("\n"," ").replaceAll("[\\s]+", " ");
        System.out.println(sql);
        //sql = m.filterOrgRange(sql);

        sql = sql.toLowerCase().replaceAll("\n"," ").replaceAll("[\\s]+", " ");
        System.out.println(sql);


        sql="and xxx in (1,3,5,6) ";
        SQLExpr sqlExpr= SQLUtils.toMySqlExpr(sql);
        System.out.println(SQLUtils.toSQLString(sqlExpr,"mysql"));
    }
}
