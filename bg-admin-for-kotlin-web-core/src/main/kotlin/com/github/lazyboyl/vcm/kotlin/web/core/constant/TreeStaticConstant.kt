package com.github.lazyboyl.vcm.kotlin.web.core.constant

class TreeStaticConstant {

    companion object {
        /**
         * 菜单状态为正常
         */
        const val TREE_STATE_NORMAL = "1"

        /**
         * 菜单类型为菜单节点
         */
        const val TREE_TYPE_MENU = "1"

        /**
         * 菜单类型为按钮节点
         */
        const val TREE_TYPE_BUTTON = "2"


        /**
         * 验证是否通过的标识
         */
        const val CHECK_TREE_TIP = "success"

        /**
         * 验证通过
         */
        const val CHECK_TREE_PASS = "pass"

        /**
         * 验证不通过
         */
        const val CHECK_TREE_UN_PASS = "unPass"
    }

}