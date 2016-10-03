package com.fractal

import MockDatabase.writeTrans
import MockDatabase.readTrans
import MockDatabase.deleteTrans
import MockDatabase.readUser
import Auth.transAuth

object RequestHandler {

  def getAllTransactions (userName:String) = {
    getTransaction(userName, 0)
  }

  def getTransaction(userName: String, transID:Int):String = {
   if (transAuth(userName, transID)) {
     val allTransIDs:List[Int] = readTrans(transID)
     val res:String = allTransIDs.length match {
       case 0 => "No transactions found."
       case 1 => "Showing transaction for " +
         userName +
         ", ID: " +
         allTransIDs.head.toString +
         "."
       case _ => "Existing transactions for " +
         userName +
         ": " +
         allTransIDs.head.toString +
         allTransIDs.tail.foldLeft(""){(s, i) =>
           s + ", " + i.toString
         } +
         "."
     }
     res
   } else {
     "Authentication error"
   }
  }

  def executeTransaction(userName: String, transID:Int) = {
    if (transAuth(userName, transID)) {
      if (readTrans(transID).length == 1) {
        //if executing successful
        deleteTrans(transID)
        "Transaction " + transID.toString + " executed."
      } else {
        "Transaction not found"
      }
    } else {
     "Authentication error"
    }
  }

  def deleteTransaction(userName: String, transID: Int) = {
    if (transAuth(userName, transID)) {
      deleteTrans(transID)
      "Transaction " + transID.toString + " canceled."
    } else {
      "Authentication error"
    }
  }

  def modifyTransaction(userName:String, transID:Int, newID:Int) = {
    if (transAuth(userName, transID)) {
      deleteTrans(transID)
      writeTrans(newID)
      "Transaction " + transID.toString + " modified. New ID: " + newID.toString + "."
    } else {
      "Authentication error"
    }
  }

  def queueTransaction(userName: String) = {
    if (transAuth(userName, 0)) {
      val transIDs = readTrans(0)
      val newID = transIDs.max + 1
      writeTrans(newID)
      "Transaction queued with ID: " + newID.toString + "."
     } else {
       "Authentication error"
     }
  }

  def deleteAllTransactions(userName:String) = {
    updateAllTransactions(userName, List())
  }

  def updateAllTransactions(userName: String, newIDs:List[Int]) ={
    val transIDs = readTrans(0)
    transIDs map (id => if(transAuth(userName,id)){deleteTrans(id)})
    if (newIDs.length == 0) {
      "Transaction list updated, contains no transactions now."
    } else {
      newIDs map writeTrans
      "Transaction list uploaded: " +
         newIDs.head.toString +
         newIDs.tail.foldLeft(""){(s, i) =>
           s + ", " + i.toString
         } +
      "."
    }
  }
}
