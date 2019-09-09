package com.github.lazyboyl.vcm.kotlin.web.core.util

import com.github.lazyboyl.vcm.kotlin.web.core.dto.OrgCascaderDto
import com.github.lazyboyl.vcm.kotlin.web.core.dto.OrgDto
import com.github.lazyboyl.vcm.kotlin.web.core.entity.Org
import java.util.ArrayList

class OrgInstall {

    companion object {
        /**
         * 功能描述：实现递归组装选中的菜单节点的数据
         *
         * @param orgList 全部菜单节点的集合
         * @param orgMap  当前被选中的数据
         * @return 返回组装好的数据
         */
        fun installCheckOrg(orgList: List<OrgDto>, orgMap: Map<String, Any>): List<OrgDto> {
            val trees = ArrayList<OrgDto>()
            for (orgDto in orgList) {
                if (0 == orgDto.parentOrgId) {
                    orgDto.children = getCheckChild(orgDto.orgId, orgList, orgMap)
                    // 用于防止子节点部分选中，而父节点全部选中
                    if (orgDto.children!!.isNotEmpty()) {
                        for (checkDto in orgDto.children!!) {
                            if (!checkDto.checked) {
                                orgDto.checked = true
                                break
                            }
                        }
                    }
                    trees.add(orgDto)
                }
            }
            return trees
        }

        /**
         * 功能描述：实现树形菜单组织架构的数据的组装
         *
         * @param orgList 需要组装的组织架构的数据
         * @return 返回组装好的数据
         */
        fun installOrgCascader(orgList: List<Org>): List<OrgCascaderDto> {
            val orgCascaderDtos = ArrayList<OrgCascaderDto>()
            var orgCascaderDto: OrgCascaderDto? = null
            for (org in orgList) {
                orgCascaderDto = OrgCascaderDto()
                orgCascaderDto.value = org.orgId.toString()
                orgCascaderDto.label = org.orgName
                if (0 == org.parentOrgId) {
                    orgCascaderDto!!.children = getCascaderChild(org.orgId, orgList)
                    orgCascaderDtos.add(orgCascaderDto)
                }
            }
            return orgCascaderDtos
        }

        /**
         * 功能描述：递归遍历组织架构节点
         *
         * @param id 父节点ID
         * @param orgList 组织架构数据
         * @return 递归组装数据
         */
        private fun getCascaderChild(id: Int?, orgList: List<Org>): List<OrgCascaderDto> {
            val childList = ArrayList<OrgCascaderDto>()
            var orgCascaderDto: OrgCascaderDto?
            for (org in orgList) {
                orgCascaderDto = OrgCascaderDto()
                orgCascaderDto.value = org.orgId.toString()
                orgCascaderDto.label = org.orgName
                if (org.parentOrgId === id!!.toInt()) {
                    orgCascaderDto.children = getCascaderChild(org.orgId, orgList)
                    childList.add(orgCascaderDto)
                }
            }
            return childList
        }

        /**
         * 功能描述：实现树形菜单组织架构的数据的组装
         *
         * @param orgDtoList 需要组装的组织架构的数据
         * @return 返回组装好的数据
         */
        fun installOrg(orgDtoList: List<OrgDto>): List<OrgDto> {
            val orgDtos = ArrayList<OrgDto>()
            for (orgDto in orgDtoList) {
                if (0 == orgDto.parentOrgId) {
                    orgDto.children = getChild(orgDto.orgId, orgDtoList)
                    orgDtos.add(orgDto)
                }
            }
            return orgDtos
        }


        /**
         * 功能描述：递归遍历菜单节点
         *
         * @param id      父节点ID
         * @param orgDtos 组织架构的集合
         * @param OrgMap  当前被选中的组织架构
         * @return 递归实现组装数据
         */
        private fun getCheckChild(id: Int?, orgDtos: List<OrgDto>, OrgMap: Map<String, Any>): List<OrgDto> {
            val childList = ArrayList<OrgDto>()
            for (orgDto in orgDtos) {
                if (orgDto.parentOrgId === id!!.toInt()) {
                    orgDto.children = getCheckChild(orgDto.orgId, orgDtos, OrgMap)
                    if (OrgMap[orgDto.orgId.toString()] != null) {
                        orgDto.checked = true
                    } else {
                        orgDto.checked = false
                    }
                    if (orgDto.children!!.isNotEmpty()) {
                        for (checkOrg in orgDto.children!!) {
                            if (!checkOrg.checked) {
                                orgDto.checked = false
                                break
                            }
                        }
                    }
                    childList.add(orgDto)
                }
            }
            return childList
        }

        /**
         * 功能描述：递归遍历组织架构节点
         *
         * @param id 父节点ID
         * @param orgDtos 组织架构数据
         * @return 递归组装数据
         */
        fun getChild(id: Int?, orgDtos: List<OrgDto>): List<OrgDto> {
            val childList = ArrayList<OrgDto>()
            for (orgDto in orgDtos) {
                if (orgDto.parentOrgId === id!!.toInt()) {
                    orgDto.children = getChild(orgDto.orgId, orgDtos)
                    childList.add(orgDto)
                }
            }
            return childList
        }
    }
}