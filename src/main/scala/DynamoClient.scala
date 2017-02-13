

class DynamoClient private(val value: Int) {

}


object DynamoClient {

  def apply(value: Int) = new DynamoClient(value)

}