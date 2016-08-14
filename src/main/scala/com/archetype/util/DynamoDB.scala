package com.archetype.util

import awscala.Region
import awscala.dynamodbv2._
import com.amazonaws.services.dynamodbv2.model.{Condition => JCondition}
import com.amazonaws.services.{dynamodbv2 => aws}
import com.archetype.rest_dynamodb.{Data, Params2}
import com.google.gson.{Gson, GsonBuilder}

/**
 * Created by szymon.wartak on 14/08/2016.
 */

object DynamoInstance {
  var dynamoDb: Option[DynamoInstance] = None

  def get(): DynamoInstance = {
    dynamoDb.getOrElse {
      dynamoDb = Some(new DynamoInstance()); dynamoDb.get
    }
  }
}

class DynamoInstance(isTest: Boolean = true) {
  object TableColumns extends Enumeration {
    type TableColumns = Value
    val col_id, col_data = Value.toString
  }
  import TableColumns._

  // table-name, access key, secret key
  val creds: List[String] = scala.io.Source.fromURL(getClass.getResource("/dynamodb")).getLines().mkString.split(",").toList
  implicit val region = Region.EU_WEST_1
  implicit val dynamoDB = {
    if (isTest)
      DynamoDB.local()
    else {
      DynamoDB(creds(1), creds(2))
    }
  }
  val tableName = "dynamo-table"
  val table: Table = dynamoDB.table(tableName).getOrElse(createTable())

  def save(du: Params2) = dynamoDB.put(table, (col_data, Params2))

  import TableColumns._

  def get(id: Integer): Option[Data] =
    dynamoDB.get(table, id)
      .flatMap(x => x.attributes.find(_.name == col_data.toString))
      .map(x => gson.fromJson(x.value.s.get, classOf[Data]))

  // TODO: solve (threading?) issues - this doesn't initialize properly with spray
  def gson: Gson = new GsonBuilder().setPrettyPrinting.create

  // setup Dynamo table and populate
  def createTable(): Table = {
    dynamoDB.createTable(
      name = tableName,
      hashPK = col_id.toString -> AttributeType.Number
    )
    var tableOpt: Option[Table] = None
    do {
      println(s"getting table ${tableName}...")
      Thread.sleep(500)
      tableOpt = dynamoDB.table(tableName)
    } while (tableOpt.isEmpty)
    tableOpt.get
  }

  // uncomment for local server running with some test data
  def loadData(): Unit = {
    table.put(1, (col_data, gson.toJson(Data("jim", 20))))
    table.put(2, (col_data, gson.toJson(Data("jaw", 19))))
    table.put(3, (col_data, gson.toJson(Data("jay", 24))))
  }

  loadData()

}
