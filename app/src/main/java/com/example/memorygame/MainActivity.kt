package com.example.memorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import com.example.memorygame.R.drawable.*

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var buttons: List<ImageButton>
    private lateinit var cards: List<MemoryCard>
    private var indexOfSingleSelectedCard: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val images = mutableListOf(ic_car, ic_cloud, ic_music, ic_smiley)
        // add the images again to create pairs
        images.addAll(images)
        // shuffle the order of images
        images.shuffle()

        buttons = listOf(imageButton, imageButton2, imageButton3, imageButton4, imageButton5, imageButton6, imageButton7, imageButton8)

        cards = buttons.indices.map { index ->
            MemoryCard(images[index])
        }

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                Log.i(TAG, "button clicked!")

                // update memory cards
                updateModels(index)
                // update UI
                updateViews()
            }
        }
    }

    private fun updateModels(index: Int) {
        val card = cards[index]

        // prevent users from undoing a flip
        if (card.isFaceUp) {
            Toast.makeText(this, "Invalid move!", Toast.LENGTH_SHORT).show()
            return
        }

        // card being flipped is first card
        if (indexOfSingleSelectedCard == null) {
            restoreCards()
            indexOfSingleSelectedCard = index
        }
        // card being flipped is second card
        else {
            checkForMatch(indexOfSingleSelectedCard!!, index)
            indexOfSingleSelectedCard = null
        }

        card.isFaceUp = !card.isFaceUp
    }

    private fun updateViews() {
        cards.forEachIndexed { index, card ->
            val button = buttons[index]

            button.setImageResource(if (card.isFaceUp) card.identifier else ic_default)
        }
    }

    private fun restoreCards() {
        // make sure all unmatched cards are face-down
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    private fun checkForMatch(indexOfSingleSelectedCard: Int, index: Int) {
        // matched
        if (cards[indexOfSingleSelectedCard].identifier == cards[index].identifier) {
            Toast.makeText(this, "Match found!", Toast.LENGTH_SHORT).show()
            cards[indexOfSingleSelectedCard].isMatched = true
            cards[index].isMatched = true
        }
    }
}
