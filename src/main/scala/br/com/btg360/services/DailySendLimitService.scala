package br.com.btg360.services

class DailySendLimitService {

  //val redis = new RedisClientService().connect

  private var _limit: Int = 1

  private def limit: Int = _limit

  private def limit_=(value: Int): Unit = {
    _limit = value
  }


  def filter(users: List[String] = List()): List[String] = {

  }

}


DailySendLimitService.limit(3).filter(list)

