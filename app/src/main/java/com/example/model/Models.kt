package com.example.model

enum class UserRole(val label: String) {
    CUSTOMER("Customer"),
    ADMIN("Restaurant Admin"),
    DELIVERY("Delivery Partner")
}

enum class CustomerScreen {
    SPLASH,
    ONBOARDING,
    HOME,
    CATEGORY,
    SEARCH,
    CART,
    CHECKOUT,
    TRACKING,
    ORDERS,
    PROFILE,
    ADDRESSES
}

enum class AdminScreen {
    OVERVIEW,
    ORDERS,
    MENU,
    SETTINGS
}

data class Category(
    val id: String,
    val name: String,
    val description: String,
    val iconEmoji: String,
    val imageUrl: String = ""
)

data class ProductVariant(
    val id: String,
    val name: String,
    val priceDelta: Double = 0.0,
    val isDefault: Boolean = false
)

data class ProductAddon(
    val id: String,
    val name: String,
    val price: Double
)

data class Product(
    val id: String,
    val categoryId: String,
    val name: String,
    val description: String,
    val basePrice: Double,
    val isVeg: Boolean,
    val isAvailable: Boolean = true,
    val isSpecial: Boolean = false,
    val isBestseller: Boolean = false,
    val rating: Double = 4.6,
    val ratingCount: Int = 120,
    val preparationMinutes: Int = 20,
    val variants: List<ProductVariant> = emptyList(),
    val addons: List<ProductAddon> = emptyList(),
    val imageUrl: String = "",
    val localDrawableRes: Int? = null
)

data class CartItem(
    val cartItemId: String,
    val product: Product,
    val selectedVariant: ProductVariant?,
    val selectedAddons: List<ProductAddon>,
    val quantity: Int,
    val cookingInstructions: String = ""
) {
    val unitPrice: Double
        get() {
            val variantDelta = selectedVariant?.priceDelta ?: 0.0
            val addonTotal = selectedAddons.sumOf { it.price }
            return product.basePrice + variantDelta + addonTotal
        }

    val totalPrice: Double
        get() = unitPrice * quantity
}

enum class OrderStatus(val displayName: String, val stepIndex: Int) {
    PLACED("Order Placed", 0),
    CONFIRMED("Order Confirmed", 1),
    PREPARING("Preparing Food", 2),
    READY_FOR_PICKUP("Ready for Pickup", 3),
    OUT_FOR_DELIVERY("Out for Delivery", 4),
    DELIVERED("Delivered", 5),
    CANCELLED("Cancelled", -1)
}

enum class PaymentMethod(val title: String) {
    UPI("UPI (GPay / PhonePe / Paytm)"),
    CARD("Credit / Debit Card"),
    NETBANKING("Net Banking"),
    COD("Cash on Delivery")
}

enum class PaymentStatus {
    PENDING,
    PAID,
    FAILED
}

data class Address(
    val id: String,
    val label: String, // Home, Work, Other
    val houseNo: String,
    val street: String,
    val landmark: String,
    val city: String,
    val pinCode: String,
    val isDefault: Boolean = false
) {
    val fullFormattedAddress: String
        get() = "$houseNo, $street, Near $landmark, $city - $pinCode"
}

data class Coupon(
    val code: String,
    val title: String,
    val discountPercent: Double = 0.0,
    val flatDiscount: Double = 0.0,
    val minOrderAmount: Double = 299.0,
    val maxDiscount: Double = 150.0
)

data class Order(
    val id: String,
    val customerName: String,
    val customerPhone: String,
    val deliveryAddress: Address,
    val items: List<CartItem>,
    val subtotal: Double,
    val deliveryFee: Double,
    val gstAmount: Double,
    val discountAmount: Double,
    val totalAmount: Double,
    val paymentMethod: PaymentMethod,
    val paymentStatus: PaymentStatus,
    val orderStatus: OrderStatus,
    val deliveryPartnerName: String? = null,
    val deliveryPartnerPhone: String? = null,
    val deliveryPartnerVehicle: String? = null,
    val placedAtTime: String,
    val estimatedDeliveryMinutes: Int = 30,
    val cancellationReason: String? = null
)

data class RestaurantSettings(
    val name: String = "Abhilash Restaurant",
    val tagline: String = "Delicious Food, Delivered Fresh.",
    val phone: String = "+91 98490 12345",
    val address: String = "Plot 42, High-Tech Road, Food Court Hub, Hyderabad - 500081",
    val isOpen: Boolean = true,
    val minOrderValue: Double = 149.0,
    val baseDeliveryFee: Double = 35.0,
    val freeDeliveryAbove: Double = 499.0,
    val gstRate: Double = 0.05,
    val avgPrepMinutes: Int = 25,
    val deliveryRadiusKm: Double = 8.5
)

data class DeliveryStaff(
    val id: String,
    val name: String,
    val phone: String,
    val vehicleNumber: String,
    val isAvailable: Boolean = true
)
