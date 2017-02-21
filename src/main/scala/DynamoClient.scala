import java.util.concurrent.Executors

import com.amazonaws.services.dynamodbv2.document._
import com.twitter.util.{Future, FuturePool}

class DynamoClient private(val dynamoDB: DynamoDB) {

  // Create a thread pool which adapts to load
  val futurePool = FuturePool(Executors.newCachedThreadPool())


  def getItem(tableName: String)(hashKeyName: String)(hashKeyValue: String): Future[Item] = {

    val table: Table = dynamoDB.getTable(tableName)

    futurePool(table.getItem(hashKeyName, hashKeyValue))

  }

  def deleteItem(tableName: String)(hashKeyName: String)(hashKeyValue: String): Future[DeleteItemOutcome] = {

    val table: Table = dynamoDB.getTable(tableName)

    futurePool(table.deleteItem(hashKeyName, hashKeyValue))

  }

  def getIndex(tableName: String)(indexName: String): Future[Index] = {

    val table: Table = dynamoDB.getTable(tableName)

    futurePool(table.getIndex(indexName))

  }

  def putItem(tableName: String)(item: Item): Future[PutItemOutcome] = {

    val table: Table = dynamoDB.getTable(tableName)

    futurePool(table.putItem(item))

  }

  def query(tableName: String)(keyAttribute: KeyAttribute): Future[ItemCollection[QueryOutcome]] = {

    val table: Table = dynamoDB.getTable(tableName)

    futurePool(table.query(keyAttribute))

  }

}


object DynamoClient {

  def apply(dynamoDB: DynamoDB) = new DynamoClient(dynamoDB)

}