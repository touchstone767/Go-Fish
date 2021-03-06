/*
Controller Class
  Use these functions to manipulate the model
  otherwise, may put game in unintended state
  this is where error checking for legal moves is done
*/

package controller

import model._
import model.data._

class Controller(val model: Model) {
  private val mod = model.Game
  private val players = mod.data.players
  private val deck = mod.data.deck
  private val ai = mod.ai
  private val loader = mod.loader

  def get_deck: Deck = this.deck

  //check if players exist
  def isPlayerEmpty: Boolean = players.isEmpty
  def isPlayerNonEmpty: Boolean = !isPlayerEmpty

  //check if deck is empty
  def isDeckEmpty: Boolean = deck.isEmpty
  def isDeckNonEmpty: Boolean = !isDeckEmpty
  def getDeck_count: Int = deck.count

  def getTotalPlayer_cardCount: Int = {
    var c = 0; if(isPlayerNonEmpty) players.foreach(p =>
      c += p.getCardCount + p.get_points*4)
    c
  }

  //show game universe
  def showGameArea: String = 
    if(isPlayerEmpty) "None" else players.show + deck.show

  //show order of players
  def showPlayerOrder: String = if(isPlayerEmpty) "None" else players.toString

  //advance the order of the players
  def advanceOrder: Boolean = if(isPlayerEmpty) false else players.advanceOrder

  private def draw5 =
      players.foreach(p => for(x <- 0 until 5) p.addCard(deck.draw))

  private def initializeGame_bare = {
    players.initialize; deck.createNew; ai.initialize
  }

  //set conditions for new game
  def initializeGame = {initializeGame_bare; deck.shuffle; draw5}

  //same as initializeGame but dont shuffle deck
  def initializeGame_unshuffled = {initializeGame_bare; draw5}

  //is there a winner?
  def isWinner: Boolean = {
    if(isPlayerNonEmpty && isDeckEmpty) {
      players.foreach(p => if(p.getCardCount != 0) return false)
      true
    }

    else false
  }

  //check if a player has won the game
  def checkForWinner: String = if(isWinner) players.get_leader.toString else "None"

  def doMove_manuel(you: Player, them: Player, c_id: Int, console: Boolean = false): Boolean =
    if(isPlayerNonEmpty) {loader.load(you, them, c_id); true}
    else false

  def doMove(console: Boolean = false) =
    if(isPlayerEmpty) false
    else {
      if(console) mod.data.console = true
      ai.doMove
      mod.data.console = false
    }

  def doTurn(console: Boolean = false) = for(x <- 0 until players.length) doMove(console)

  def doGame(console: Boolean = false) = while(!isWinner) doMove(console)

  def set(p: Int, s: Int): Boolean = {
    if(isPlayerEmpty) return false
    val upperStrat = mod.ai.get_strat_idMap.maxBy(_._1)._1
    val upperP = players.length
    if(s >= 1 && s < upperStrat && p >= 0 && p < upperP) {
      ai.strat_playerMap += players.players_arry(p) -> ai.get_strat_idMap(s)
      true
    }
    else false
  }

  def showStrategies: String = {
    if(isPlayerEmpty) "None"
    else {
      var ans_str = ""; players.players_arry.foreach(p =>
        ans_str += s"$p\t-> " + ai.strat_playerMap(p) + "\n")
      ans_str.trim
    }
  }
}