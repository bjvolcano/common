package com.volcano.test.rangeConfig;

import com.volcano.range.mapping.ITableMapping;
import com.volcano.range.mapping.Mapping;

import java.util.ArrayList;

/**
 * volcano
 * 此处暂时单模块运行，后期可以作为公共数据加载类，可以通过数据库拉取，作为全局的范围限制
 * 通过@EnableOrganzationRange 注解来开启是否    根据组织机构 过滤 本模块下的数据
 */
public class OrganizationRangeExcludeTableMapping extends ITableMapping {

    @Override
    public void initMapping() {
        mappings=new ArrayList<>();

        /*    xx模块    */
        /*示例*/
        mappings.add(new Mapping("表名","字段名"));//当遇到该表时，本次不做过滤 和 映射配置 配合使用，此处不时必须，根据业务需要

        /*    xx模块    */
        /*测试  申请表*/
        //mappings.add(new Mapping("oa_process_apply ","organization_id"));

    }
}
