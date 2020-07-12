package com.arbin.fastfood.model

import org.json.JSONArray

data class OrderedRestaurantDetails(
    val restaurantName: String,
    val orderedDate: String,
    val foodList: JSONArray
)