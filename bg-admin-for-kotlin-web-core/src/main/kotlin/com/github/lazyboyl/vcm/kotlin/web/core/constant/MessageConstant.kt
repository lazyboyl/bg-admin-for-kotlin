package com.github.lazyboyl.vcm.kotlin.web.core.constant

class MessageConstant {

    companion object {
        /**
         * 消息类型为系统消息
         */
        val MESSAGE_TYPE_SYSTEM = "1"

        /**
         * 消息类型为其他消息
         */
        val MESSAGE_TYPE_OTHER = "2"

        /**
         * 消息已经删除
         */
        val REMOVE_STATE_REMOVE = "0"

        /**
         * 消息正常
         */
        val REMOVE_STATE_NORMAL = "1"

        /**
         * 阅读状态-已读
         */
        val READE_STATE_READE = "2"

        /**
         * 阅读状态-未读
         */
        val READE_STATE_NOT_READE = "1"
    }

}