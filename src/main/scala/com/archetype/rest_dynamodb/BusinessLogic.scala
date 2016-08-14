package com.archetype.rest_dynamodb

import com.archetype.util.DynamoInstance

/**
 * Created by szymon.wartak on 14/08/2016.
 */
class BusinessLogic {
  val dynamoDb = DynamoInstance.get

  def doIt(du: Params2) = {
    dynamoDb.save(du)
  }

  def get(id: Int) = {
    dynamoDb.get(id) match {
      case Some(data) => data
      case None => "{}"
    }
  }

}
