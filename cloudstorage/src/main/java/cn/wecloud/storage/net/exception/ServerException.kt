package cn.wecloud.storage.net.exception

/**
 * Created by Chauncey on 2019-05-14 10:22.
 * ServerException发生后，将自动转换为ResponseThrowable返回
 *
 */

class ServerException : RuntimeException() {
    internal var code: Int = 0
}