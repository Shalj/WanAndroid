package com.shalj.wanandroid.ext

import androidx.paging.PagingState
import com.shalj.wanandroid.data.local.RemoteKey

suspend fun <Key : Any, Value : Any> PagingState<Key, Value>.currentKey(queryKey: suspend (value: Value) -> RemoteKey?): RemoteKey? {
    return anchorPosition?.let { position ->
        closestItemToPosition(position)?.let { value ->
            queryKey(value)
        }
    }
}

suspend fun <Key : Any, Value : Any> PagingState<Key, Value>.prevKey(queryKey: suspend (value: Value) -> RemoteKey?): RemoteKey? {
    return pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
        ?.let { value ->
            // Get the remote keys of the last item retrieved
            queryKey(value)
        }
}

suspend fun <Key : Any, Value : Any> PagingState<Key, Value>.nextKey(queryKey: suspend (value: Value) -> RemoteKey?): RemoteKey? {
    return pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
        ?.let { value ->
            // Get the remote keys of the last item retrieved
            queryKey(value)
        }
}