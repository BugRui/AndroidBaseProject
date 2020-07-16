package com.bugrui.jetpackproject.utils.generator

/**
 * @Author: BugRui
 * @CreateDate: 2019/8/27 11:40
 * @Description: 文件名称生成器
 */
interface FileNameGenerator {

    fun generate(fileUrl: String): String
    fun generate(fileUrl: String, ending: String): String
}
