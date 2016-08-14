package com.archetype.util

import com.archetype.rest_dynamodb.Data
import org.scalatest._


class DynamoDbTest extends FlatSpec with Matchers with BeforeAndAfter {
  val dynamoDb = DynamoInstance.get
  dynamoDb.loadData()

  "get" should "get jim" in {
    assert(dynamoDb.get(1).get == Data("jim", 20))
  }

}
