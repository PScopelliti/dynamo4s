import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Item, Table}
import com.dynamo4s.DynamoClient
import org.mockito.Matchers.anyString
import org.mockito.Mockito.{verify, when}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Future

class DynamoClientTest extends FlatSpec with Matchers with MockitoSugar {

  trait Builder {
    val dynamoDB: DynamoDB = mock[DynamoDB]
    val table: Table = mock[Table]
    val item: Item = mock[Item]
  }

  it should "return a Finagle Future with Item when query dynamo" in new Builder {

    val tableName = "some_table_name"
    val hashKeyName = "some_hash_key_name"
    val hashKeyValue = "some_hash_key_value"

    // Set up mocks
    when(dynamoDB.getTable(anyString())).thenReturn(table)
    when(table.getItem(anyString(), anyString())).thenReturn(item)

    // Run fixture
    val sut = DynamoClient(dynamoDB).getItem(tableName)(hashKeyName)(hashKeyValue)

    // Verify mocks
    verify(table).getItem(hashKeyName, hashKeyValue)
    verify(dynamoDB).getTable(tableName)
  }

  it should "return a Scala Future with Item when query dynamo" in new Builder {

    // TODO find a better way to import implicits
    val tableName = "some_table_name"
    val hashKeyName = "some_hash_key_name"
    val hashKeyValue = "some_hash_key_value"

    // Set up mocks
    when(dynamoDB.getTable(anyString())).thenReturn(table)
    when(table.getItem(anyString(), anyString())).thenReturn(item)

    // Run fixture
    val sut: Future[Item] = DynamoClient(dynamoDB).getItem(tableName)(hashKeyName)(hashKeyValue)

    // Verify mocks
    verify(table).getItem(hashKeyName, hashKeyValue)
    verify(dynamoDB).getTable(tableName)
  }

}
