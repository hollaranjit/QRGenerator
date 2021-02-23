package com.example.qrcodegenerator.model

class User {
    private var uid: String = ""
    private var userName: String = ""
    private var zucitechID: String = ""
    private var deviceID: String = ""
    private var dateOfIssue:String = ""

    constructor()

    constructor(uid: String, userName: String, zucitechID: String,deviceID: String,dateOfIssue:String) {
        this.uid = uid
        this.userName = userName
        this.zucitechID = zucitechID
        this.deviceID = deviceID
        this.dateOfIssue = dateOfIssue

    }

    //--------------------------------



    //--------------------------------

    fun getUID():String?
    {
        return uid
    }


    fun setUID(uid:String)
    {
        this.uid = uid
    }

    //---------------------------------

    fun getUserName():String?
    {
        return userName
    }


    fun setUserName(userName:String)
    {
        this.userName = userName
    }


    //---------------------------------

    fun getZucitechID():String?
    {
        return zucitechID
    }


    fun setZucitechID(zucitechID:String)
    {
        this.zucitechID = zucitechID
    }

    //---------------------------------

    fun getDeviceID():String?
    {
        return deviceID
    }


    fun setDeviceID(deviceID:String)
    {
        this.deviceID = deviceID
    }

    fun getDateOfIssue():String?
    {
        return dateOfIssue
    }

    //-------------------------------------

    fun setDateOfIssue(dateOfIssue:String)
    {
        this.dateOfIssue = dateOfIssue
    }

}