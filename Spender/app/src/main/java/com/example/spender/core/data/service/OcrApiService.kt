package com.example.spender.core.data.service

import com.example.spender.feature.expense.data.remote.OcrRequest
import com.example.spender.feature.expense.data.remote.OcrResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OcrApiService {
    @POST("custom/v1/45107/dbd70982eab31f5fe9359fc9eca09ff78dc4112aab8ce4beaf993016e30b6325/document/receipt")
    suspend fun analyzeReceipt(
        @Header("X-OCR-SECRET") secretKey: String,
        @Body request: OcrRequest
    ): OcrResponse
}