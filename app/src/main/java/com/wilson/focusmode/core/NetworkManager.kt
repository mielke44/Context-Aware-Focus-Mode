package com.wilson.focusmode.core

import com.wilson.focusmode.core.api.RESTApi
import com.wilson.focusmode.core.models.FocusSessionEntity
import com.wilson.focusmode.core.models.SessionDTO
import com.wilson.focusmode.core.models.toDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class NetworkManager(
    private val apiService: RESTApi
) {

    suspend fun syncSession(entity: FocusSessionEntity): Flow<Result<SessionDTO>> =
        executeApiRequest {
            apiService.syncSession(entity.toDto())
        }

    suspend fun getSession(sessionId: String): Flow<Result<SessionDTO>> =
        executeApiRequest {
            apiService.getSessionById(sessionId)
        }

    suspend fun fetchHistory(): Flow<Result<List<SessionDTO>>> =
        executeApiRequest {
            apiService.getAllSessions()
        }

    private fun <T> executeApiRequest(apiCall: suspend () -> Response<T>): Flow<Result<T>> =
        flow {
            val result = runCatching {
                val response = apiCall()
                if (response.isSuccessful) {
                    response.body() ?: throw Exception("Empty response body")
                } else {
                    throw Exception("API Error ${response.code()}: ${response.message()}")
                }
            }
            emit(result)
        }
}