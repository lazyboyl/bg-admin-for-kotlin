package com.github.lazyboyl.vcm.kotlin.web.core.mapper

import com.github.lazyboyl.vcm.kotlin.web.core.dto.TreeDto
import com.github.lazyboyl.vcm.kotlin.web.core.entity.Tree
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * @author linzf
 * @since 2019/5/5
 * 类描述：菜单实体快速转换的实现
 */
@Mapper(componentModel = "spring")
interface TreeMapper {

    /**
     * 功能描述：实现数据的转换
     *
     * @param entity
     * @return
     */
    @Mappings(Mapping(target = "title", source = "treeName"), Mapping(target = "treeId", source = "treeId"))
    fun treeToTreeDto(entity: Tree): TreeDto

    /**
     * 功能描述：实现集合的数据的转换，这边先设置实体之间的转换集合的设置才会实现
     *
     * @param trees
     * @return
     */
    fun treesToTreeDTO(trees: List<Tree>): List<TreeDto>

}