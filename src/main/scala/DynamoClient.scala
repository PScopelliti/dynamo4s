import java.util.concurrent.Executors

import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Item, Table}
import com.twitter.util.{Future, FuturePool}

class DynamoClient private(val dynamoDB: DynamoDB) {

  // Create a thread pool which adapts to load
  val futurePool = FuturePool(Executors.newCachedThreadPool())


  def query(tableName: String, itemKey: String): Future[Item] = {

    val table: Table = dynamoDB.getTable(tableName)

    futurePool(table.getItem("key", itemKey))

  }

}


object DynamoClient {

  def apply(dynamoDB: DynamoDB) = new DynamoClient(dynamoDB)

}