package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RestaurantViewModel : ViewModel() {

    private val _currentRole = MutableStateFlow(UserRole.CUSTOMER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _customerScreen = MutableStateFlow(CustomerScreen.HOME)
    val customerScreen: StateFlow<CustomerScreen> = _customerScreen.asStateFlow()

    private val _adminScreen = MutableStateFlow(AdminScreen.ORDERS)
    val adminScreen: StateFlow<AdminScreen> = _adminScreen.asStateFlow()

    private val _categories = MutableStateFlow(SampleData.categories)
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _products = MutableStateFlow(SampleData.products)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _activeCategory = MutableStateFlow<Category?>(null)
    val activeCategory: StateFlow<Category?> = _activeCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isPureVegOnly = MutableStateFlow(false)
    val isPureVegOnly: StateFlow<Boolean> = _isPureVegOnly.asStateFlow()

    private val _selectedProductForDetail = MutableStateFlow<Product?>(null)
    val selectedProductForDetail: StateFlow<Product?> = _selectedProductForDetail.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _appliedCoupon = MutableStateFlow<Coupon?>(null)
    val appliedCoupon: StateFlow<Coupon?> = _appliedCoupon.asStateFlow()

    private val _savedAddresses = MutableStateFlow(SampleData.sampleAddresses)
    val savedAddresses: StateFlow<List<Address>> = _savedAddresses.asStateFlow()

    private val _deliveryAddress = MutableStateFlow(SampleData.sampleAddresses.first())
    val deliveryAddress: StateFlow<Address> = _deliveryAddress.asStateFlow()

    private val _restaurantSettings = MutableStateFlow(RestaurantSettings())
    val restaurantSettings: StateFlow<RestaurantSettings> = _restaurantSettings.asStateFlow()

    private val _deliveryStaffList = MutableStateFlow(SampleData.deliveryStaffList)
    val deliveryStaffList: StateFlow<List<DeliveryStaff>> = _deliveryStaffList.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _activeTrackingOrderId = MutableStateFlow<String?>(null)
    val activeTrackingOrderId: StateFlow<String?> = _activeTrackingOrderId.asStateFlow()

    private val _isRazorpayModalOpen = MutableStateFlow(false)
    val isRazorpayModalOpen: StateFlow<Boolean> = _isRazorpayModalOpen.asStateFlow()

    private val _customerName = MutableStateFlow("Narendar V")
    val customerName: StateFlow<String> = _customerName.asStateFlow()

    private val _customerPhone = MutableStateFlow("+91 98490 56789")
    val customerPhone: StateFlow<String> = _customerPhone.asStateFlow()

    private val _snackMessage = MutableStateFlow<String?>(null)
    val snackMessage: StateFlow<String?> = _snackMessage.asStateFlow()

    init {
        // Seed initial past order for demo
        seedInitialOrder()
    }

    private fun seedInitialOrder() {
        val initialProduct = SampleData.products.first()
        val item = CartItem(
            cartItemId = "cart_seed_1",
            product = initialProduct,
            selectedVariant = initialProduct.variants.find { it.id == "v_regular" },
            selectedAddons = listOf(SampleData.products.first().addons.first()),
            quantity = 1,
            cookingInstructions = "Medium spicy, please send extra lemon slices."
        )
        val initialOrder = Order(
            id = "ABH-1082",
            customerName = "Narendar V",
            customerPhone = "+91 98490 56789",
            deliveryAddress = SampleData.sampleAddresses.first(),
            items = listOf(item),
            subtotal = item.totalPrice,
            deliveryFee = 35.0,
            gstAmount = item.totalPrice * 0.05,
            discountAmount = 0.0,
            totalAmount = item.totalPrice + 35.0 + (item.totalPrice * 0.05),
            paymentMethod = PaymentMethod.UPI,
            paymentStatus = PaymentStatus.PAID,
            orderStatus = OrderStatus.PREPARING,
            deliveryPartnerName = "Ramesh Kumar",
            deliveryPartnerPhone = "+91 98765 43210",
            deliveryPartnerVehicle = "TS 09 EA 4123",
            placedAtTime = "Today, 12:45 PM",
            estimatedDeliveryMinutes = 25
        )
        _orders.value = listOf(initialOrder)
        _activeTrackingOrderId.value = initialOrder.id
    }

    // Role Switcher
    fun switchRole(role: UserRole) {
        _currentRole.value = role
        showMessage("Switched to ${role.label} view")
    }

    // Navigation
    fun navigateCustomer(screen: CustomerScreen) {
        _customerScreen.value = screen
    }

    fun navigateAdmin(screen: AdminScreen) {
        _adminScreen.value = screen
    }

    fun selectCategory(category: Category?) {
        _activeCategory.value = category
        if (category != null) {
            _customerScreen.value = CustomerScreen.CATEGORY
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun togglePureVeg() {
        _isPureVegOnly.value = !_isPureVegOnly.value
    }

    fun openProductDetail(product: Product) {
        _selectedProductForDetail.value = product
    }

    fun closeProductDetail() {
        _selectedProductForDetail.value = null
    }

    // Cart Operations
    fun addToCart(
        product: Product,
        variant: ProductVariant? = product.variants.firstOrNull { it.isDefault },
        addons: List<ProductAddon> = emptyList(),
        quantity: Int = 1,
        instructions: String = ""
    ) {
        val current = _cartItems.value.toMutableList()
        val existingIndex = current.indexOfFirst {
            it.product.id == product.id &&
                    it.selectedVariant?.id == variant?.id &&
                    it.selectedAddons.map { a -> a.id }.sorted() == addons.map { a -> a.id }.sorted()
        }

        if (existingIndex >= 0) {
            val existing = current[existingIndex]
            current[existingIndex] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            val newItem = CartItem(
                cartItemId = "cart_${UUID.randomUUID().toString().take(6)}",
                product = product,
                selectedVariant = variant,
                selectedAddons = addons,
                quantity = quantity,
                cookingInstructions = instructions
            )
            current.add(newItem)
        }
        _cartItems.value = current
        showMessage("Added ${product.name} to Cart")
    }

    fun updateCartQuantity(cartItemId: String, newQty: Int) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.cartItemId == cartItemId }
        if (index >= 0) {
            if (newQty <= 0) {
                current.removeAt(index)
                showMessage("Item removed from cart")
            } else {
                current[index] = current[index].copy(quantity = newQty)
            }
            _cartItems.value = current
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _appliedCoupon.value = null
    }

    fun applyCoupon(coupon: Coupon) {
        val subtotal = calculateSubtotal()
        if (subtotal < coupon.minOrderAmount) {
            showMessage("Coupon requires minimum order of ₹${coupon.minOrderAmount.toInt()}")
            return
        }
        _appliedCoupon.value = coupon
        showMessage("Applied coupon: ${coupon.code}")
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
        showMessage("Coupon removed")
    }

    fun setDeliveryAddress(address: Address) {
        _deliveryAddress.value = address
    }

    fun addAddress(address: Address) {
        val current = _savedAddresses.value.toMutableList()
        current.add(address)
        _savedAddresses.value = current
        _deliveryAddress.value = address
        showMessage("Address saved successfully")
    }

    // Calculations
    fun calculateSubtotal(): Double {
        return _cartItems.value.sumOf { it.totalPrice }
    }

    fun calculateDeliveryFee(): Double {
        val subtotal = calculateSubtotal()
        if (subtotal == 0.0) return 0.0
        val settings = _restaurantSettings.value
        return if (subtotal >= settings.freeDeliveryAbove) 0.0 else settings.baseDeliveryFee
    }

    fun calculateGst(): Double {
        return calculateSubtotal() * _restaurantSettings.value.gstRate
    }

    fun calculateDiscount(): Double {
        val subtotal = calculateSubtotal()
        val coupon = _appliedCoupon.value ?: return 0.0
        return if (coupon.discountPercent > 0) {
            (subtotal * coupon.discountPercent).coerceAtMost(coupon.maxDiscount)
        } else {
            coupon.flatDiscount.coerceAtMost(coupon.maxDiscount)
        }
    }

    fun calculateGrandTotal(): Double {
        val sub = calculateSubtotal()
        if (sub == 0.0) return 0.0
        return sub + calculateDeliveryFee() + calculateGst() - calculateDiscount()
    }

    // Payment & Order Placement
    fun openRazorpayModal() {
        if (_cartItems.value.isEmpty()) {
            showMessage("Your cart is empty")
            return
        }
        _isRazorpayModalOpen.value = true
    }

    fun closeRazorpayModal() {
        _isRazorpayModalOpen.value = false
    }

    fun placeOrder(paymentMethod: PaymentMethod) {
        val subtotal = calculateSubtotal()
        if (subtotal < _restaurantSettings.value.minOrderValue) {
            showMessage("Minimum order value is ₹${_restaurantSettings.value.minOrderValue.toInt()}")
            return
        }

        val orderNum = (1000 + (1..8999).random()).toString()
        val orderId = "ABH-$orderNum"
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val placedTime = "Today, " + timeFormat.format(Date())

        val newOrder = Order(
            id = orderId,
            customerName = _customerName.value,
            customerPhone = _customerPhone.value,
            deliveryAddress = _deliveryAddress.value,
            items = _cartItems.value,
            subtotal = subtotal,
            deliveryFee = calculateDeliveryFee(),
            gstAmount = calculateGst(),
            discountAmount = calculateDiscount(),
            totalAmount = calculateGrandTotal(),
            paymentMethod = paymentMethod,
            paymentStatus = if (paymentMethod == PaymentMethod.COD) PaymentStatus.PENDING else PaymentStatus.PAID,
            orderStatus = OrderStatus.PLACED,
            placedAtTime = placedTime,
            estimatedDeliveryMinutes = _restaurantSettings.value.avgPrepMinutes + 10
        )

        val updatedOrders = _orders.value.toMutableList()
        updatedOrders.add(0, newOrder)
        _orders.value = updatedOrders
        _activeTrackingOrderId.value = orderId
        _isRazorpayModalOpen.value = false
        clearCart()
        _customerScreen.value = CustomerScreen.TRACKING
        showMessage("Order $orderId placed successfully!")
    }

    fun trackOrder(orderId: String) {
        _activeTrackingOrderId.value = orderId
        _customerScreen.value = CustomerScreen.TRACKING
    }

    fun reorder(order: Order) {
        val newCart = order.items.map {
            it.copy(cartItemId = "cart_${UUID.randomUUID().toString().take(6)}")
        }
        _cartItems.value = newCart
        _customerScreen.value = CustomerScreen.CART
        showMessage("Items from order ${order.id} added to cart")
    }

    // Admin & Operational Status Transitions
    fun updateOrderStatus(orderId: String, newStatus: OrderStatus, reason: String? = null) {
        val current = _orders.value.toMutableList()
        val index = current.indexOfFirst { it.id == orderId }
        if (index >= 0) {
            val order = current[index]
            current[index] = order.copy(
                orderStatus = newStatus,
                cancellationReason = reason ?: order.cancellationReason
            )
            _orders.value = current
            showMessage("Order $orderId updated: ${newStatus.displayName}")
        }
    }

    fun assignDeliveryStaff(orderId: String, staff: DeliveryStaff) {
        val current = _orders.value.toMutableList()
        val index = current.indexOfFirst { it.id == orderId }
        if (index >= 0) {
            val order = current[index]
            current[index] = order.copy(
                deliveryPartnerName = staff.name,
                deliveryPartnerPhone = staff.phone,
                deliveryPartnerVehicle = staff.vehicleNumber,
                orderStatus = if (order.orderStatus == OrderStatus.PREPARING || order.orderStatus == OrderStatus.CONFIRMED) {
                    OrderStatus.READY_FOR_PICKUP
                } else order.orderStatus
            )
            _orders.value = current
            showMessage("Assigned ${staff.name} to order $orderId")
        }
    }

    // Admin Menu Controls
    fun adminToggleProductAvailability(productId: String) {
        val current = _products.value.toMutableList()
        val index = current.indexOfFirst { it.id == productId }
        if (index >= 0) {
            val p = current[index]
            current[index] = p.copy(isAvailable = !p.isAvailable)
            _products.value = current
            showMessage("${p.name} is now ${if (!p.isAvailable) "Available" else "Out of Stock"}")
        }
    }

    fun adminUpdateProductPrice(productId: String, newPrice: Double) {
        val current = _products.value.toMutableList()
        val index = current.indexOfFirst { it.id == productId }
        if (index >= 0) {
            current[index] = current[index].copy(basePrice = newPrice)
            _products.value = current
            showMessage("Price updated for ${current[index].name}")
        }
    }

    fun adminUpdateProductImage(productId: String, newImageUrl: String) {
        val current = _products.value.toMutableList()
        val index = current.indexOfFirst { it.id == productId }
        if (index >= 0) {
            current[index] = current[index].copy(imageUrl = newImageUrl)
            _products.value = current
            showMessage("Updated photo for ${current[index].name}")
        }
    }

    fun adminUpdateSettings(settings: RestaurantSettings) {
        _restaurantSettings.value = settings
        showMessage("Restaurant settings updated")
    }

    fun showMessage(msg: String) {
        _snackMessage.value = msg
    }

    fun clearMessage() {
        _snackMessage.value = null
    }
}
