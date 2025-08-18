package com.example.spender.core.data.service

import com.example.spender.feature.expense.data.remote.OcrRequest
import com.example.spender.feature.expense.data.remote.OcrResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface OcrApiService {
    @POST
    suspend fun analyzeReceipt(
        @Url url: String,
        @Header("X-OCR-SECRET") secretKey: String,
        @Body request: OcrRequest
    ): OcrResponse
}