package com.example.barorganizer.database.dbo

data class RecipeDbo(val id: Long, val name: String, val description: String?, val url: String?,
                     val image: ByteArray?) {
}