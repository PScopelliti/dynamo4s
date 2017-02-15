import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Item, Table}
import org.mockito.Matchers.anyString
import org.mockito.Mockito.{verify, when}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

class DynamoClientTest extends FlatSpec with Matchers with MockitoSugar {

  trait Builder {
    val dynamoDB: DynamoDB = mock[DynamoDB]
    val table: Table = mock[Table]
    val item: Item = mock[Item]
  }

  it should "return a Scala Future with Item when query dynamo" in new Builder {

    val tableName = "some_table_name"
    val itemKey = "some_item_key"

    // Set up mocks
    when(dynamoDB.getTable(anyString())).thenReturn(table)
    when(table.getItem(anyString(), anyString())).thenReturn(item)

    // Run fixture
    val sut = DynamoClient(dynamoDB).query(tableName, itemKey)

    // Verify mocks
    verify(table).getItem("key", itemKey)
    verify(dynamoDB).getTable(tableName)
  }

}
