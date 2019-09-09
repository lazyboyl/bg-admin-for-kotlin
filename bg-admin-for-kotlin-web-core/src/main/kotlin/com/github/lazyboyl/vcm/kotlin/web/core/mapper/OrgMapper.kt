package com.github.lazyboyl.vcm.kotlin.web.core.mapper

import com.github.lazyboyl.vcm.kotlin.web.core.dto.OrgDto
import com.github.lazyboyl.vcm.kotlin.web.core.entity.Org
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface OrgMapper {

    /**
     * 功能描述：实现数据的转换
     *
     * @param entity
     * @return
     */
    @Mappings(Mapping(target = "title", source = "orgName"), Mapping(target = "orgId", source = "orgId"))
    fun orgToOrgDto(entity: Org): OrgDto


    /**
     * 功能描述：实现集合的数据的转换
     *
     * @param orgs
     * @return
     */
    fun orgsToOrgDto(orgs: List<Org>): List<OrgDto>

}