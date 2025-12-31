package com.abg.constructorinterfacecompose.model

import org.json.JSONObject

sealed class ElementEvent {
    data class ShowToast(val message: String) : ElementEvent()
    data class ShowDialog(val title: String, val message: String) : ElementEvent()
    data class CreateEntry(val tableName: String) : ElementEvent()
    data class DeleteEntry(val tableName: String) : ElementEvent()
    data class OpenTable(val tableName: String) : ElementEvent()
    data class GetTextFromEditText(val editTextId: Int) : ElementEvent()
    data class ChangeText(val newText: String) : ElementEvent()
    data class RunCustomCode(val code: String) : ElementEvent()
    data class MathOperation(
        val expression: String,
        val resultVar: String? = null,
        val resultTag: String? = null
    ) : ElementEvent()
    data class AddText(
        val newText: String,
        val resultVar: String? = null,
        val resultTag: String? = null
    ) : ElementEvent()
}

fun ElementEvent.info(): String {
    return when(this) {
        is ElementEvent.AddText -> "Add Text"
        is ElementEvent.ChangeText -> "Change Text"
        is ElementEvent.CreateEntry -> "Create Entry"
        is ElementEvent.DeleteEntry -> "Delete Entry"
        is ElementEvent.GetTextFromEditText -> "Get Text From Edit Text"
        is ElementEvent.OpenTable -> "Open Table"
        is ElementEvent.MathOperation -> "Math Operation"
        is ElementEvent.RunCustomCode -> "Run Custom Code"
        is ElementEvent.ShowDialog -> "Show Dialog"
        is ElementEvent.ShowToast -> "Show Toast"
    }
}

enum class ActionType(val position: Int) {
    NONE(0),
    TOAST(1),
    DIALOG(2),
    CREATE_ENTRY(3),
    OPEN_TABLE(4),
    CHANGE_TEXT(5),
    ADD_TEXT(6),
    MATH_OPERATION(7);

    companion object {
        fun fromPosition(position: Int): ActionType? {
            return ActionType.entries.find { it.position == position }
        }
    }
}

fun ElementEvent.toJson(): JSONObject {
    return JSONObject().apply {
        when (this@toJson) {
            is ElementEvent.ShowToast -> {
                put("type", "ShowToast")
                put("message", message)
            }

            is ElementEvent.ShowDialog -> {
                put("type", "ShowDialog")
                put("title", title)
                put("message", message)
            }

            is ElementEvent.CreateEntry -> {
                put("type", "CreateEntry")
                put("tableName", tableName)
            }

            is ElementEvent.DeleteEntry -> {
                put("type", "DeleteEntry")
                put("tableName", tableName)
            }

            is ElementEvent.OpenTable -> {
                put("type", "OpenTable")
                put("tableName", tableName)
            }

            is ElementEvent.GetTextFromEditText -> {
                put("type", "GetTextFromEditText")
                put("editTextId", editTextId)
            }

            is ElementEvent.ChangeText -> {
                put("type", "ChangeText")
                put("newText", newText)
            }

            is ElementEvent.AddText -> {
                put("type", "AddText")
                put("newText", newText)
                put("resultVar", resultVar)
                put("resultTag", resultTag)
            }

            is ElementEvent.RunCustomCode -> {
                put("type", "RunCustomCode")
                put("code", code)
            }

            is ElementEvent.MathOperation -> {
                put("type", "MathOperation")
                put("expression", expression)
                put("resultVar", resultVar)
                put("resultTag", resultTag)
            }
        }
    }
}

fun JSONObject.toElementEvent(): ElementEvent {
    val type = getString("type")
    return when (type) {
        "ShowToast" -> {
            val message = getString("message")
            ElementEvent.ShowToast(message)
        }

        "ShowDialog" -> {
            val title = getString("title")
            val message = getString("message")
            ElementEvent.ShowDialog(title, message)
        }

        "CreateEntry" -> {
            val tableName = getString("tableName")
            ElementEvent.CreateEntry(tableName)
        }

        "DeleteEntry" -> {
            val tableName = getString("tableName")
            ElementEvent.DeleteEntry(tableName)
        }

        "OpenTable" -> {
            val tableName = getString("tableName")
            ElementEvent.OpenTable(tableName)
        }

        "GetTextFromEditText" -> {
            val editTextId = getInt("editTextId")
            ElementEvent.GetTextFromEditText(editTextId)
        }

        "ChangeText" -> {
            val newText = getString("newText")
            ElementEvent.ChangeText(newText)
        }

        "RunCustomCode" -> {
            val code = getString("code")
            ElementEvent.RunCustomCode(code)
        }

        "MathOperation" -> {
            val expression = getString("expression")
            val resultTag = optString("resultTag", "")
            val resultVar = optString("resultVar", "")
            ElementEvent.MathOperation(expression, resultVar, resultTag)
        }

        "AddText" -> {
            val newText = getString("newText")
            val resultTag = optString("resultTag", "")
            val resultVar = optString("resultVar", "")
            ElementEvent.AddText(newText, resultVar, resultTag)
        }

        else -> throw kotlin.IllegalArgumentException("Unknown event type: $type")
    }
}
