package com.example.qrcodegenerator.model

class User {
    private var uid: String = ""
    private var userName: String = ""
    private var zucitechID: String = ""
    private var dateOfIssue:String = ""
    lateinit var deviceID :HashMap<String,Any>
    //private var previousDevice:String = ""

    constructor()

    constructor(uid: String, userName: String, zucitechID: String,dateOfIssue:String,deviceID:HashMap<String,Any>) {
        this.uid = uid
        this.userName = userName
        this.zucitechID = zucitechID
        this.dateOfIssue = dateOfIssue
        this.deviceID = deviceID
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

    fun getDateOfIssue():String?
    {
        return dateOfIssue
    }

    //-------------------------------------

    fun setDateOfIssue(dateOfIssue:String)
    {
        this.dateOfIssue = dateOfIssue
    }


    //-------------------------------------

    fun getdeviceID1():HashMap<String,Any>
    {
        return deviceID
    }


    fun setdeviceID1(deviceZid:HashMap<String,Any>)
    {
        this.deviceID = deviceID
    }




    //------------------------------------

//    fun getPreviousDevice():String?
//    {
//        return previousDevice
//    }
//
//
//    fun setPreviousDevice(previousDevice:String)
//    {
//        this.previousDevice = previousDevice
//    }
}



