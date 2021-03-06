package com.dynamo4s

import java.util.concurrent.Executors

import com.amazonaws.services.dynamodbv2.document._
import com.twitter.util.{Future, FuturePool}
import com.twitter.{util => twitter}

import scala.concurrent.{ExecutionContext, Promise}
import scala.util.{Failure, Success, Try}

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

  import scala.language.implicitConversions

  implicit def scalaToTwitterTry[T](t: Try[T]): twitter.Try[T] = t match {
    case Success(r) => twitter.Return(r)
    case Failure(ex) => twitter.Throw(ex)
  }

  implicit def twitterToScalaTry[T](t: twitter.Try[T]): Try[T] = t match {
    case twitter.Return(r) => Success(r)
    case twitter.Throw(ex) => Failure(ex)
  }

  implicit def twitterToScalaFuture[T](f: Future[T]): scala.concurrent.Future[T] = {
    val promise = Promise[T]()
    f.respond(promise complete _)
    promise.future
  }

  implicit def scalaToTwitterFuture[T](f: scala.concurrent.Future[T])(implicit ec: ExecutionContext): Future[T] = {
    val promise = twitter.Promise[T]()
    f.onComplete(promise update _)
    promise
  }

  def apply(dynamoDB: DynamoDB) = new DynamoClient(dynamoDB)
}