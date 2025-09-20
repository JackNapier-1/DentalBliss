package com.example.dentalbliss.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.example.dentalbliss.ui.screens.UserRole
import com.google.mlkit.vision.face.Face
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FaceAuthManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("face_auth", Context.MODE_PRIVATE)
    private val _registeredFaces = MutableStateFlow<Map<String, UserRole>>(emptyMap())
    val registeredFaces: StateFlow<Map<String, UserRole>> = _registeredFaces.asStateFlow()

    init {
        // Load registered faces from SharedPreferences
        loadRegisteredFaces()
    }

    private fun loadRegisteredFaces() {
        val faces = mutableMapOf<String, UserRole>()
        prefs.all.forEach { (key, value) ->
            if (key.startsWith("face_")) {
                val role = UserRole.valueOf(value as String)
                faces[key.removePrefix("face_")] = role
            }
        }
        _registeredFaces.value = faces
    }

    fun registerFace(faceId: String, role: UserRole) {
        prefs.edit().putString("face_$faceId", role.name).apply()
        val updatedFaces = _registeredFaces.value.toMutableMap()
        updatedFaces[faceId] = role
        _registeredFaces.value = updatedFaces
    }

    fun verifyFace(faceId: String): UserRole? {
        return _registeredFaces.value[faceId]
    }

    fun isFaceRegistered(faceId: String): Boolean {
        return _registeredFaces.value.containsKey(faceId)
    }

    fun getFaceRole(faceId: String): UserRole? {
        return _registeredFaces.value[faceId]
    }

    fun clearRegisteredFaces() {
        prefs.edit().clear().apply()
        _registeredFaces.value = emptyMap()
    }

    companion object {
        @Volatile
        private var INSTANCE: FaceAuthManager? = null

        fun getInstance(context: Context): FaceAuthManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FaceAuthManager(context).also { INSTANCE = it }
            }
        }
    }
} 