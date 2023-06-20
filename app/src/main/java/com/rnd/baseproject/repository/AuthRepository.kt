package com.rnd.baseproject.repository

import com.rnd.baseproject.database.dao.NoteDao
import com.rnd.baseproject.database.entity.Note
import com.rnd.baseproject.retrofit_api.request.AuthApi
import com.rnd.baseproject.retrofit_api.safeApiCall
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AuthRepository @Inject constructor(val api:AuthApi,private val noteDao:NoteDao) {

    suspend fun otpVerify(params: HashMap<String, Any>) = safeApiCall { api.otpVerify(params) }

    suspend fun createAccount(
        params: HashMap<String, RequestBody>,
        adhar_image: MultipartBody.Part? = null
    ) =
        safeApiCall { api.createAccount(params, adhar_image) }

    suspend fun addNote(note :Note) = noteDao.addNote(note)
    suspend fun getNotes() = noteDao.getAllNotes()
    suspend fun updateNote(note:Note) = noteDao.updateNote(note)
    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)


}