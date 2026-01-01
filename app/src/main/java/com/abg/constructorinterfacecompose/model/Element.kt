package com.abg.constructorinterfacecompose.model

data class Element(
    val id: String,
    val type: Type,
    val text: String = "",
    val hint: String = "",
    val events: MutableList<ElementEvent>,
)

enum class Type() {
    TEXTVIEW,
    EDITTEXT,
    BUTTON,
    SPINNER,
    CHECKBOX,
    OTHER
}