package com.babylonhealth.check_base


case class UserProps(language: String, gender: String)

case class Question(qid: Int, text: String, `type`: QuestionType.Value)

case class Answer(aid: Int, order: Int, text: String)

object QuestionType extends Enumeration {
  val multiple_choice, yes_no, action = Value
}

///////////////////////////////////////////
// Knowledge Node types. Templates defined in Knowledge node case classes themselves
object KnowledgeNodeType extends Enumeration {
  val raw = Value
}

object KnowledgeFieldType extends Enumeration {
  val condition, qualifier, freeText = Value
}

/**
 *
 * @param typ The template
 * @param fields Tells you how to structure q&a text along with the language gen
 */
abstract class KnowledgeNode(typ: KnowledgeNodeType.Value,
                             fields: List[KnowledgeField]) {
  def apply(props: UserProps): (Question, Array[Answer])
}

case class KnowledgeField(typ: KnowledgeFieldType.Value, value: Any)

///////////////////////////////////////////
// The Knowledge Node templates

case class KnowledgeNodeRaw(question: Question, answers: Array[Answer])
  extends KnowledgeNode(KnowledgeNodeType.raw, List(KnowledgeField(KnowledgeFieldType.freeText, ""))) {

  def apply(props: UserProps = UserProps("en", "male")) = (question, answers)
}


///////////////////////////////////////////
// language generation

