package com.fractal {

  object ApiDemo
  {
    def main(args: Array[String]):Unit = {
      WebServer.boot("localhost", 5678)
    }
  }
}
