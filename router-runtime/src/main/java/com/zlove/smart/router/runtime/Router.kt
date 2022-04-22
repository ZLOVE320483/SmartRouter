package com.zlove.smart.router.runtime

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log

/**
 * Author by zlove, Email zlove.zhang@bytedance.com, Date on 2022/4/22.
 */
object Router {
    private const val TAG = "Router"
    private const val GENERATED_MAPPING = "com.zlove.smart.router.mapping.RouterMapping"
    private val mapping = HashMap<String, String>()

    fun init() {
        try {
            val clazz = Class.forName(GENERATED_MAPPING)
            val method = clazz.getMethod("get")
            val allMapping = method.invoke(null) as Map<String, String>
            if (allMapping.isNotEmpty()) {
                Log.i(TAG, "init: get all mapping:")
                allMapping.onEach {
                    Log.i(TAG, "    ${it.key} -> ${it.value}")
                }
                mapping.putAll(allMapping)
            }
        } catch (e: Throwable) {
            Log.i(TAG, "init: error while init router: $e")
        }
    }

    fun open(context: Context?, url: String?) {
        requireNotNull(context) {
            "open error, context must not be null."
        }
        requireNotNull(url) {
            "open error, url must not be null."
        }

        val uri = Uri.parse(url)
        val schema = uri.scheme
        val host = uri.host
        val path = uri.path
        var targetActivityClass = ""
        mapping.onEach {
            val rUri = Uri.parse(it.key)
            val rSchema = rUri.scheme
            val rHost = rUri.host
            val rPath = rUri.path

            if (rSchema == schema && rHost == host && rPath == path) {
                targetActivityClass = it.value
            }

            if (targetActivityClass == "") {
                Log.e(TAG, "open:     no destination found")
                return
            }

            // 2、解析URL里的参数，封装成一个 Bundle
            val bundle = Bundle()
            val query = uri.query
            query?.let { q ->
                if (q.length >= 3) {// a=b 至少三个字符
                    val args = q.split("&")
                    args.onEach { arg ->
                        val splits = arg.split("=")
                        bundle.putSerializable(splits[0], splits[1])
                    }
                }
            }

            // 3、打开对应的Activity，并传入参数
            try {
                val activity = Class.forName(targetActivityClass)
                val intent = Intent(context, activity)
                intent.putExtras(bundle)
                context.startActivity(intent)
            } catch (e: Throwable) {
                Log.e(TAG, "go: error while start activity: $targetActivityClass, e = $e")
            }
        }
    }

}