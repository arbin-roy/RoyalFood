package com.arbin.fastfood.model

data class OrderedRestaurantDetails(
    val restaurantName: String,
    val orderedDate: String,
    val menuList: ArrayList<MenuItem>
)