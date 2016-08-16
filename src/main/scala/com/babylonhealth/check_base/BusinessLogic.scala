package com.babylonhealth.check_base

import com.google.gson.{Gson, GsonBuilder}
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.{Document, Field, TextField}
import org.apache.lucene.index.{DirectoryReader, IndexWriter, IndexWriterConfig}
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.RAMDirectory


class BusinessLogic {
//  import com.babylonhealth.check_base._

  val index = new QAIndex
  // search KN cache (json)
  def search(userProps: UserProps)(term: String): Seq[KnowledgeNode] = {
    index.search(term).take(10)
  }

  // search neo4j
  // add KN to neo4j
}


class QAIndex {
  val gson: Gson = new GsonBuilder().setPrettyPrinting.create
  val qaArrayClass = Class.forName("[L" + classOf[KnowledgeNodeRaw].getName + ";")
  // load KN from json
  val qaStr = io.Source.fromURL(getClass.getClassLoader.getResource("questions_and_answers.json")).getLines().mkString
  val qas =
    gson.fromJson(qaStr, qaArrayClass).asInstanceOf[Array[KnowledgeNodeRaw]]
      .map(x => KnowledgeNodeRaw(x.question, x.answers))


  // pump data into (in memory) Lucene index
  private val store = new RAMDirectory()
  private val indexWriter = new IndexWriter(store, new IndexWriterConfig(new StandardAnalyzer()))

  private def addQA(qa: KnowledgeNodeRaw): Unit = {
    val doc = new Document()
    doc.add(new TextField("qa", s"${qa()._1.text} ${qa()._2.map(_.text).mkString(" ")}", Field.Store.YES))
    indexWriter.addDocument(doc)
  }
  qas.foreach(qa => addQA(qa))
  indexWriter.commit()

  // create searchers for REST API
  private val indexSearcher = new IndexSearcher(DirectoryReader.open(store))
  private val parser = new QueryParser("qa", new StandardAnalyzer())

  def search(term: String): Array[KnowledgeNodeRaw] = {
    indexSearcher
      .search(parser.parse(s"""qa:${term}"""), 100)
      .scoreDocs
      .map(s => qas(s.doc))
  }


}
