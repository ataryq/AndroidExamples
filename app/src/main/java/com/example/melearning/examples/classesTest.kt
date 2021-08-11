package com.example.melearning.examples

interface Printable {
    val mHead: String
    fun print()
}

open class Person(name: String = "Not specified", lastName: String = "Not specified",
                  override val mHead: String = "PersonLog"
) : Printable {
    private var mName: String = name
    var mLastName: String = lastName
        private set
    open var mAge : Int? = null
        get() {
            println("get age: $field")
            return field
        }
        set(value) {
            println("set age: $value")
            if(value == null) throw IllegalArgumentException("Input is null")
            if(value < 0) throw IllegalArgumentException("Age cannot be less 0")
            field = value
        }

    init {
        println("The person with name $name $lastName created");
    }

    constructor(name: String, lastName: String, age: Int): this(name, lastName) {
        mAge = age
    }

    override fun print() {
        val ageString = mAge?.let { "age is $mAge" } ?: ""

        println("The person is $mName $mLastName $ageString");
    }

    fun personData(): PersonData {
        return PersonData(mName, mAge?:0)
    }
}

open class Policeman(name: String, lastName: String, licenseId: Int)
    : Person(name, lastName) {

    override var mAge: Int? = null
    var mLicenseId: Int = 0

    init {
        mLicenseId = licenseId
    }
}

data class PolicemanData(var name: String, var lastName: String, var licenseId: Int):
    Policeman(name, lastName, licenseId)


data class PersonData(var name:String, var age:Int)

fun main3() {
    var pupil1 = Person(lastName = "Lincoln")
    pupil1 = Person("Avram","Lincoln")
    //pupil1.print()
    pupil1 = Person("Avram","Lincoln", 17)
    //pupil1.print()

    pupil1.mAge = 0
    pupil1.print()

    val userData = PersonData("User1", 1)
    val userData2 = PersonData("User1", 1)

    println(userData)
    println(userData == userData2)
    println("name: ${userData.component1()}, id: ${userData.component2()}")
    val (name, id) = userData
    println("name: $name, id: $id")

    println(pupil1.personData())

    val policeman = Policeman("Ariel", "Named", 1)
    policeman.mAge = 22
    println(policeman.personData())

    val personPrint: Printable = Person("1", "2", "head")
    personPrint.print()

    val listVariables: List<Any> = listOf(5, "five", 5.0)
    for (item in listVariables) {
        print("$item ")
    }
    println()

    val itemNumber: Int
    if(listVariables[0] is Int)
        itemNumber = listVariables[0] as Int

    //Cause an error
    //itemNumber = listVariables[1] as Int

    var itemString: String? = null
    //if not String return null without an error
    itemString = listVariables[1] as? String
}