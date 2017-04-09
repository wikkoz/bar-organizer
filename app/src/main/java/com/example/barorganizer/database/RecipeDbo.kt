package com.example.barorganizer.database

data class RecipeDbo(val id: Long, val name: String, val description: String?, val url: String?,
                     val imgae: ByteArray?) {
}