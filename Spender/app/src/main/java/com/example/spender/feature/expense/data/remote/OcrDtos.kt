package com.example.spender.feature.expense.data.remote

import kotlinx.serialization.Serializable

//@Serializable
//data class OcrRequest(
//    val version: String = "V2",
//    val requestId: String,
//    val timestamp: Long = System.currentTimeMillis(),
//    val images: List<OcrImageRequest>
//)
//
//@Serializable
//data class OcrImageRequest(
//    val format: String,
//    val name: String = "receipt",
//    val data: String
//)
//
//@Serializable
//data class OcrResponse(val images: List<OcrImage>)
//
//@Serializable
//data class OcrImage(
//    val receipt: OcrReceipt?,
//    val inferResult: String,
//)
//
//@Serializable
//data class OcrReceipt(val result: OcrResult?)
//
//@Serializable
//data class OcrResult(
//    val storeInfo: StoreInfo?,
//    val paymentInfo: PaymentInfo?,
//    val totalPrice: TotalPrice?
//)
//
//@Serializable
//data class StoreInfo(val name: OcrField?)
//
//@Serializable
//data class PaymentInfo(val date: OcrField?)
//
//@Serializable
//data class TotalPrice(val price: OcrField?)
//
//@Serializable
//data class OcrField(val text: String)
@Serializable
data class OcrRequest(
    val version: String = "V2",
    val requestId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val images: List<OcrImageRequest>
)

@Serializable
data class OcrImageRequest(
    val format: String,
    val name: String = "receipt",
    val data: String
)

@Serializable
data class OcrResponse(
    val version: String,
    val requestId: String,
    val timestamp: Long,
    val images: List<OcrImage>
)

@Serializable
data class OcrImage(
    val uid: String? = null,
    val name: String? = null,
    val inferResult: String,
    val message: String? = null,
    val receipt: OcrReceipt? = null
)

@Serializable
data class OcrReceipt(
    val result: OcrResult? = null
)

@Serializable
data class OcrResult(
    val storeInfo: StoreInfo? = null,
    val paymentInfo: PaymentInfo? = null,
    val totalPrice: TotalPrice? = null
)

@Serializable
data class StoreInfo(
    val name: OcrField? = null
)

@Serializable
data class PaymentInfo(
    val date: OcrField? = null
)

@Serializable
data class TotalPrice(
    val price: OcrField? = null
)

@Serializable
data class OcrField(
    val text: String,
    val confidenceScore: Float? = null
)