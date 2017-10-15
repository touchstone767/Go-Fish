package controller

import model._
import model.playing_cards._

class Deckctl (deck: Deck) {
  
  //return a shuffled deck
  private def shuffle(d: Deck): Deck = new Deck ++= util.Random.shuffle(d)
  
  //return a new unshuffled deck
  private def getNewDeck: Deck = {
    val newDeck = new Deck
    for (card_id <- deck.card_ids) for (suit <- deck.card_suits)
      newDeck.enqueue(Card(card_id, suit))
    newDeck
  }
  
  //initiate a new deck
  def init: Deck = {
    deck.clear
    deck ++= shuffle(getNewDeck)
  }
  
  //count number of cards
  def count: Int = deck.length
  
  //draw a card; does check if deck empty
  def draw: Card = deck.dequeue
  
  //text show of cards
  def show: String = deck.toString
  
  def isEmpty: Boolean = deck.isEmpty
  def nonEmpty: Boolean = deck.nonEmpty
}