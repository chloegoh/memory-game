package com.example.memorygame

// only identifier is immutable
data class MemoryCard(val identifier: Int, var isFaceUp: Boolean = false, var isMatched: Boolean = false)