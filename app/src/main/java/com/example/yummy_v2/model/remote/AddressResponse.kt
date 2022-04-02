package com.example.yummy_v2.model.remote

data class AddressResponse(
    val results : ResultResponse,
) {
    data class ResultResponse(
        val common : CommonResponse,
        val juso : ArrayList<JusoResponse>
    ) {
        data class CommonResponse(
            val totalCount: String,
            val currentPage: String,
            val countPerPage: String,
            val errorCode: String,
            val errorMessage: String,
        )

        data class JusoResponse(
            val roadAddr: String,
            val roadAddrPart1: String,
            val roadAddrPart2: String,
            val jibunAddr: String,
            val engAddr: String,
            val zipNo: String,
            val admCd: String,
            val rnMgtSn: String,
            val bdMgtSn: String,
            val detBdNmList: String,
            val bdNm: String
        )
    }
}