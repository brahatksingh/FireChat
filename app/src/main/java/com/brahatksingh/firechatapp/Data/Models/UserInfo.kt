package com.brahatksingh.firechatapp.Data.Models

data class UserInfo(var email : String ,var userId : String , var name : String,var profilePicUrl : String) {
    constructor() : this("email_from_class","username_from_class","name_from_class","pic_url")
}