package com.abg.constructorinterfacecompose.model

data class Element(
    val id: String,
    val type: Type,
    val text: String = "",
    val hint: String = "",
    val events: MutableList<ElementEvent>,
)

enum class Type(val type: String) {
    TEXTVIEW("TEXTVIEW"),
    EDITTEXT("EDITTEXT"),
    BUTTON("BUTTON"),
    SPINNER("SPINNER")
}