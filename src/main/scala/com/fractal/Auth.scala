package com.fractal

object Auth {

  def transAuth(userName:String, transID:Int) = {
    if (transID != 999 && MockDatabase.readUser(userName)) {
      true
    } else {
      false
    }
  }
}
