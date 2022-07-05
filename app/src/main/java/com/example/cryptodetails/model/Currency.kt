package com.example.cryptodetails.model

data class Currency(
    val name: String,
    val fullName: String,
    val precision: String,
    val confirms: String,
    val contractAddress: String,
    val withdrawalMinSize: String,
    val withdrawalMinFee: String,
    val isWithdrawEnabled: Boolean,
    val isDepositEnabled: Boolean,
    val isMarginEnabled: Boolean,
    val isDebitEnabled: Boolean
)
/**
"currency":"CSP",
 *      "name":"CSP",
 *      "fullName":"Caspian",
 *      "precision":8,
 *      "confirms":12,
 *      "contractAddress":"0xa6446d655a0c34bc4f05042ee88170d056cbaf45",
 *      "withdrawalMinSize":"10000",
 *      "withdrawalMinFee":"5000",
 *      "isWithdrawEnabled":true,
 *      "isDepositEnabled":true,
 *      "isMarginEnabled":false,
 *      "isDebitEnabled":false
 */