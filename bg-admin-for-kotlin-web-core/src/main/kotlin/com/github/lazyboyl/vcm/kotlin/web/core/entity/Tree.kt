package com.github.lazyboyl.vcm.kotlin.web.core.entity

import java.util.*
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

/**
 * 类描述：菜单的实体
 * @author linzf
 * @since 2019-09-04
 */
@Table(name = "t_tree")
class Tree(treeCode: String, treeName: String, treeType: String, treeState: String, parentTreeName: String, parentTreeId: Int, crtDate: Date, powerPath: String) {

    /**
     * 菜单流水ID
     */
    @Id
    @Column(name = "treeId")
    @GeneratedValue(generator = "JDBC")
    var treeId: Int? = null

    /**
     * 菜单名字
     */
    @Column(name = "treeName")
    var treeName: String? = treeName

    /**
     * 菜单编码
     */
    @Column(name = "treeCode")
    var treeCode: String? = treeCode

    /**
     * 菜单状态【1：可用；2：禁用】
     */
    @Column(name = "treeState")
    var treeState: String? = treeState

    /**
     * 菜单类型【1：菜单节点；2：按钮节点】
     */
    @Column(name = "treeType")
    var treeType: String? = treeType

    /**
     * 父节点编号
     */
    @Column(name = "parentTreeId")
    var parentTreeId: Int? = parentTreeId

    /**
     * 父节点名称
     */
    @Column(name = "parentTreeName")
    var parentTreeName: String? = parentTreeName

    /**
     * 创建时间
     */
    @Column(name = "crtDate")
    var crtDate: Date? = crtDate

    /**
     * 菜单流水完整路径(1.2.3)
     */
    @Column(name = "fullPath")
    var fullPath: String? = null

    /**
     * 权限路径采用小写的冒号分隔
     */
    @Column(name = "powerPath")
    var powerPath: String? = powerPath

}