package com.bugrui.jetpackproject.http

/**
 * @Author:            BugRui
 * @CreateDate:        2020/2/17 10:33
 * @Description:       Api数据源
 */
data class ApiResponse<T : Any?>(
    val code: Int,
    val data: T?,
    val msg: String? = null
) {

    /**
     * 请求成功
     */
    fun isSuccessful(): Boolean {
        return code == 1
    }

    companion object {

        fun <T> success(msg: String? = null, data: T?, code: Int = 1): ApiResponse<T> {
            return ApiResponse(
                code = code,
                data = data,
                msg = msg
            )
        }

        fun <T> error(msg: String?, data: T? = null, code: Int = 0): ApiResponse<T> {
            return ApiResponse(
                code = code,
                data = data,
                msg = msg
            )
        }
    }
}