package com.wilson.focusmode.core.api

import com.wilson.focusmode.core.models.SessionDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RESTApi {
    @POST("sessions")
    suspend fun syncSession(@Body session: SessionDTO): Response<SessionDTO>

    @GET("sessions")
    suspend fun getAllSessions(): Response<List<SessionDTO>>

    @GET("session/{id}")
    suspend fun getSessionById(@Path("id") id: String): Response<SessionDTO>
}