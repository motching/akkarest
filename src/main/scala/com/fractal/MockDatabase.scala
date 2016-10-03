package com.fractal

import scala.collection.mutable.{ArrayBuffer, Map => MMap}

object MockDatabase {

  private val transactionTable:ArrayBuffer[Int] = ArrayBuffer()

  private val userTable = List("sanyi")

  def readUser(userName: String):Boolean = {
    if (userTable.contains(userName)) {
      true
    } else {
      false
    }
  }

  def writeTrans (transID: Int) = {
    transactionTable.append(transID)
  }


  // def writeTrans (transIDs: List[Int]) = {
  //   transIDs map (transactionTable.append(_))
  // }

  def readTrans(transID: Int):List[Int] = {
    if (transID == 0) {
      transactionTable.toList
    } else {
      if (transactionTable.indexOf(transID) != -1) {
        List(transactionTable(transactionTable.indexOf(transID)))
      } else {
        List()
      }
    }
  }

  def deleteTrans(transID: Int) = {
    if (transactionTable.indexOf(transID) != -1) {
      transactionTable.remove(transactionTable.indexOf(transID))
    }
  }

}
