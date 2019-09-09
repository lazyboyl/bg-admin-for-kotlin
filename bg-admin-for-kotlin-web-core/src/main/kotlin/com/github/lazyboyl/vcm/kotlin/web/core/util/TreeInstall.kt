package com.github.lazyboyl.vcm.kotlin.web.core.util

import com.github.lazyboyl.vcm.kotlin.web.core.dto.TreeDto
import java.util.ArrayList

class TreeInstall {

    companion object {
        /**
         * 功能描述：实现递归组装选中的菜单节点的数据
         * @param treeList 全部菜单节点的集合
         * @param treeMap
         * @return
         */
        fun installCheckTree(treeList: List<TreeDto>, treeMap: Map<String, Any>): List<TreeDto> {
            val trees = ArrayList<TreeDto>()
            for (roleTree in treeList) {
                if (0 == roleTree.parentTreeId) {
                    roleTree.children = getCheckChild(roleTree.treeId, treeList, treeMap)
                    // 用于防止子节点部分选中，而父节点全部选中
                    if (roleTree.children!!.size > 0) {
                        for (checkTree in roleTree.children!!) {
                            if (!checkTree.checked) {
                                roleTree.checked = false
                                break
                            }
                        }
                    }
                    trees.add(roleTree)
                }
            }
            return trees
        }

        /**
         * 功能描述：实现树形菜单数据的组装
         * @param treeList
         */
        fun installTree(treeList: List<TreeDto>): List<TreeDto> {
            val trees = ArrayList<TreeDto>()
            for (roleTree in treeList) {
                if (0 == roleTree.parentTreeId) {
                    roleTree.children = getChild(roleTree.treeId, treeList)
                    trees.add(roleTree)
                }
            }
            return trees
        }


        /**
         * 功能描述：递归遍历菜单节点
         * @param id
         * @param trees
         * @param treeMap
         * @return
         */
        private fun getCheckChild(id: Int?, trees: List<TreeDto>, treeMap: Map<String, Any>): List<TreeDto> {
            val childList = ArrayList<TreeDto>()
            for (tree in trees) {
                if (tree.parentTreeId === id) {
                    tree.children = getCheckChild(tree.treeId, trees, treeMap)
                    if (treeMap[tree.treeId.toString()] != null) {
                        tree.checked = true
                    } else {
                        tree.checked = false
                    }
                    if (tree.children!!.isNotEmpty()) {
                        for (checkTree in tree.children!!) {
                            if (!checkTree.checked) {
                                tree.checked = false
                                break
                            }
                        }
                    }
                    childList.add(tree)
                }
            }
            return childList
        }

        /**
         * 功能描述：递归遍历菜单节点
         * @param id
         * @param trees
         * @return
         */
        private fun getChild(id: Int?, trees: List<TreeDto>): List<TreeDto> {
            val childList = ArrayList<TreeDto>()
            for (tree in trees) {
                if (tree.parentTreeId === id) {
                    tree.children = getChild(tree.treeId, trees)
                    childList.add(tree)
                }
            }
            return childList
        }

    }

}