package com.example.bibliotheque

import android.os.Parcel
import android.os.Parcelable

data class CartItem(
    val book: Book,
    var quantity: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Book::class.java.classLoader)!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(book, flags)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        return this.book.equals((other as CartItem).book)
    }

    companion object CREATOR : Parcelable.Creator<CartItem> {
        override fun createFromParcel(parcel: Parcel): CartItem {
            return CartItem(parcel)
        }

        override fun newArray(size: Int): Array<CartItem?> {
            return arrayOfNulls(size)
        }
    }
}