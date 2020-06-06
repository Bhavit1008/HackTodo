package com.example.schedulertodo.models

data class RegistrationResponse(val error:Boolean,val message:String , val users: Restritation_Users,var token:String)
