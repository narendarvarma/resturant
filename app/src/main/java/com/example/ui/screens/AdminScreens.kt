package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.RestaurantViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val adminScreen by viewModel.adminScreen.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Admin Top Navigation Tabs
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Restaurant Manager Hub",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = AbhilashPrimaryDark
                        )
                        Text(
                            text = "Abhilash Restaurant • Kitchen & POS Console",
                            fontSize = 11.sp,
                            color = AbhilashMuted
                        )
                    }
                }

                TabRow(
                    selectedTabIndex = adminScreen.ordinal,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = AbhilashPrimary
                ) {
                    Tab(
                        selected = adminScreen == AdminScreen.ORDERS,
                        onClick = { viewModel.navigateAdmin(AdminScreen.ORDERS) },
                        text = { Text("Live Orders", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                        icon = { Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                    Tab(
                        selected = adminScreen == AdminScreen.OVERVIEW,
                        onClick = { viewModel.navigateAdmin(AdminScreen.OVERVIEW) },
                        text = { Text("Analytics", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                        icon = { Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                    Tab(
                        selected = adminScreen == AdminScreen.MENU,
                        onClick = { viewModel.navigateAdmin(AdminScreen.MENU) },
                        text = { Text("Menu Stock", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                        icon = { Icon(Icons.Default.RestaurantMenu, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                    Tab(
                        selected = adminScreen == AdminScreen.SETTINGS,
                        onClick = { viewModel.navigateAdmin(AdminScreen.SETTINGS) },
                        text = { Text("Settings", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                        icon = { Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                }
            }
        }

        when (adminScreen) {
            AdminScreen.ORDERS -> AdminOrdersTab(viewModel = viewModel)
            AdminScreen.OVERVIEW -> AdminOverviewTab(viewModel = viewModel)
            AdminScreen.MENU -> AdminMenuTab(viewModel = viewModel)
            AdminScreen.SETTINGS -> AdminSettingsTab(viewModel = viewModel)
        }
    }
}

@Composable
fun AdminOrdersTab(viewModel: RestaurantViewModel) {
    val orders by viewModel.orders.collectAsState()
    val deliveryStaff by viewModel.deliveryStaffList.collectAsState()

    var selectedOrderForAssign by remember { mutableStateOf<Order?>(null) }

    if (orders.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No incoming orders currently.")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(orders, key = { it.id }) { order ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                        if (order.orderStatus == OrderStatus.PLACED) 1.5.dp else 0.5.dp,
                        if (order.orderStatus == OrderStatus.PLACED) AbhilashPrimary else AbhilashBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Order #${order.id}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text(text = "Customer: ${order.customerName} (${order.customerPhone})", fontSize = 12.sp, color = AbhilashMuted)
                            }

                            Surface(
                                color = when (order.orderStatus) {
                                    OrderStatus.PLACED -> Color(0xFFFEF08A)
                                    OrderStatus.PREPARING -> Color(0xFFFED7AA)
                                    OrderStatus.READY_FOR_PICKUP -> Color(0xFFBAE6FD)
                                    OrderStatus.DELIVERED -> Color(0xFFBBF7D0)
                                    else -> Color(0xFFE5E7EB)
                                },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = order.orderStatus.displayName,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = when (order.orderStatus) {
                                        OrderStatus.PLACED -> Color(0xFF854D0E)
                                        OrderStatus.PREPARING -> Color(0xFF9A3412)
                                        OrderStatus.READY_FOR_PICKUP -> Color(0xFF0369A1)
                                        OrderStatus.DELIVERED -> Color(0xFF15803D)
                                        else -> Color(0xFF374151)
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 10.dp), color = AbhilashBorder)

                        // Items
                        order.items.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${item.quantity}x ${item.product.name}" + (if (item.selectedVariant != null) " (${item.selectedVariant.name})" else ""),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text("₹${item.totalPrice.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            if (item.cookingInstructions.isNotBlank()) {
                                Text(
                                    text = "Cooking Note: \"${item.cookingInstructions}\"",
                                    fontSize = 11.sp,
                                    color = AbhilashPrimaryDark,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Drop Address: ${order.deliveryAddress.fullFormattedAddress}",
                            fontSize = 11.sp,
                            color = AbhilashMuted
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Kitchen Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            when (order.orderStatus) {
                                OrderStatus.PLACED -> {
                                    Button(
                                        onClick = { viewModel.updateOrderStatus(order.id, OrderStatus.CONFIRMED) },
                                        colors = ButtonDefaults.buttonColors(containerColor = AbhilashGreen),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Accept Order", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                    OutlinedButton(
                                        onClick = { viewModel.updateOrderStatus(order.id, OrderStatus.CANCELLED, "Kitchen at capacity") },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Reject", fontSize = 12.sp, color = Color.Red)
                                    }
                                }
                                OrderStatus.CONFIRMED -> {
                                    Button(
                                        onClick = { viewModel.updateOrderStatus(order.id, OrderStatus.PREPARING) },
                                        colors = ButtonDefaults.buttonColors(containerColor = AbhilashPrimary),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Start Preparing in Kitchen", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                OrderStatus.PREPARING -> {
                                    Button(
                                        onClick = { selectedOrderForAssign = order },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Mark Ready & Assign Driver", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                OrderStatus.READY_FOR_PICKUP -> {
                                    Surface(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Awaiting Pickup by ${order.deliveryPartnerName ?: "Driver"}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AbhilashPrimaryDark,
                                            modifier = Modifier.padding(10.dp)
                                        )
                                    }
                                }
                                OrderStatus.OUT_FOR_DELIVERY -> {
                                    Text(
                                        text = "Out for delivery with ${order.deliveryPartnerName} (${order.deliveryPartnerPhone})",
                                        fontSize = 12.sp,
                                        color = AbhilashMuted
                                    )
                                }
                                OrderStatus.DELIVERED -> {
                                    Text("Delivered successfully. Paid ₹${order.totalAmount.toInt()}", fontSize = 12.sp, color = AbhilashGreen, fontWeight = FontWeight.Bold)
                                }
                                OrderStatus.CANCELLED -> {
                                    Text("Order was cancelled.", fontSize = 12.sp, color = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (selectedOrderForAssign != null) {
        val targetOrder = selectedOrderForAssign!!
        AlertDialog(
            onDismissRequest = { selectedOrderForAssign = null },
            title = { Text("Assign Delivery Staff") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Select a delivery partner for Order #${targetOrder.id}:", fontSize = 13.sp)
                    deliveryStaff.forEach { staff ->
                        Card(
                            onClick = {
                                viewModel.assignDeliveryStaff(targetOrder.id, staff)
                                selectedOrderForAssign = null
                            },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = staff.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(text = "${staff.vehicleNumber} • ${staff.phone}", fontSize = 11.sp, color = AbhilashMuted)
                                }
                                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = AbhilashPrimary)
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { selectedOrderForAssign = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AdminOverviewTab(viewModel: RestaurantViewModel) {
    val orders by viewModel.orders.collectAsState()
    val totalRevenue = orders.filter { it.orderStatus != OrderStatus.CANCELLED }.sumOf { it.totalAmount }
    val preparingCount = orders.count { it.orderStatus == OrderStatus.PREPARING }
    val completedCount = orders.count { it.orderStatus == OrderStatus.DELIVERED }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(text = "Today's Performance", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        // Metrics Grid
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminMetricCard(
                    title = "Total Orders",
                    value = "${orders.size}",
                    icon = Icons.Default.Receipt,
                    color = AbhilashPrimary,
                    modifier = Modifier.weight(1f)
                )
                AdminMetricCard(
                    title = "Revenue",
                    value = "₹${totalRevenue.toInt()}",
                    icon = Icons.Default.CurrencyRupee,
                    color = AbhilashGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AdminMetricCard(
                    title = "Kitchen In-Prep",
                    value = "$preparingCount",
                    icon = Icons.Default.SoupKitchen,
                    color = Color(0xFFF59E0B),
                    modifier = Modifier.weight(1f)
                )
                AdminMetricCard(
                    title = "Delivered",
                    value = "$completedCount",
                    icon = Icons.Default.CheckCircle,
                    color = Color(0xFF0284C7),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(0.5.dp, AbhilashBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Top Selling Dishes", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    listOf(
                        "Chicken Dum Biryani" to "48 orders today",
                        "Family Pack Biryani" to "26 orders today",
                        "Masala Dosa" to "34 orders today",
                        "Chilli Chicken" to "22 orders today"
                    ).forEach { (dish, stats) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = dish, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                            Text(text = stats, fontSize = 12.sp, color = AbhilashGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminMetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(0.5.dp, AbhilashBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = title, fontSize = 11.sp, color = AbhilashMuted)
        }
    }
}

@Composable
fun AdminMenuTab(viewModel: RestaurantViewModel) {
    val products by viewModel.products.collectAsState()

    var editingProduct by remember { mutableStateOf<Product?>(null) }
    var editPriceText by remember { mutableStateOf("") }
    var editingImageProduct by remember { mutableStateOf<Product?>(null) }
    var editImageUrlText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Manage Menu Stock, Pricing & Photography (${products.size} items)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(products, key = { it.id }) { product ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(0.5.dp, AbhilashBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        FoodImageView(
                            imageUrl = product.imageUrl,
                            contentDescription = product.name,
                            fallbackRes = product.localDrawableRes,
                            modifier = Modifier.size(54.dp),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                VegIndicator(isVeg = product.isVeg)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = product.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Text(
                                text = "₹${product.basePrice.toInt()}",
                                fontSize = 12.sp,
                                color = AbhilashPrimaryDark,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            editingImageProduct = product
                            editImageUrlText = product.imageUrl
                        }) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Edit Photo", tint = AbhilashPrimary, modifier = Modifier.size(20.dp))
                        }

                        IconButton(onClick = {
                            editingProduct = product
                            editPriceText = product.basePrice.toInt().toString()
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Price", tint = AbhilashMuted, modifier = Modifier.size(18.dp))
                        }

                        Switch(
                            checked = product.isAvailable,
                            onCheckedChange = { viewModel.adminToggleProductAvailability(product.id) },
                            colors = SwitchDefaults.colors(checkedThumbColor = AbhilashGreen)
                        )
                    }
                }
            }
        }
    }

    if (editingProduct != null) {
        val p = editingProduct!!
        AlertDialog(
            onDismissRequest = { editingProduct = null },
            title = { Text("Update Price: ${p.name}") },
            text = {
                OutlinedTextField(
                    value = editPriceText,
                    onValueChange = { editPriceText = it },
                    label = { Text("Price in INR (₹)") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newP = editPriceText.toDoubleOrNull()
                        if (newP != null && newP > 0) {
                            viewModel.adminUpdateProductPrice(p.id, newP)
                        }
                        editingProduct = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AbhilashPrimary)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingProduct = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (editingImageProduct != null) {
        val p = editingImageProduct!!
        AlertDialog(
            onDismissRequest = { editingImageProduct = null },
            title = { Text("Update Dish Photo: ${p.name}") },
            text = {
                Column {
                    Text("Enter image URL or select verified photo:", fontSize = 12.sp, color = AbhilashMuted)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editImageUrlText,
                        onValueChange = { editImageUrlText = it },
                        label = { Text("Image URL") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editImageUrlText.isNotBlank()) {
                            viewModel.adminUpdateProductImage(p.id, editImageUrlText)
                        }
                        editingImageProduct = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AbhilashPrimary)
                ) {
                    Text("Save Photo")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingImageProduct = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AdminSettingsTab(viewModel: RestaurantViewModel) {
    val settings by viewModel.restaurantSettings.collectAsState()

    var isOpen by remember { mutableStateOf(settings.isOpen) }
    var minOrder by remember { mutableStateOf(settings.minOrderValue.toInt().toString()) }
    var deliveryFee by remember { mutableStateOf(settings.baseDeliveryFee.toInt().toString()) }
    var freeAbove by remember { mutableStateOf(settings.freeDeliveryAbove.toInt().toString()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(text = "Restaurant Operational Settings", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(0.5.dp, AbhilashBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Online Ordering Status", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = if (isOpen) "Accepting online orders" else "Restaurant closed for orders", fontSize = 12.sp, color = AbhilashMuted)
                        }
                        Switch(checked = isOpen, onCheckedChange = { isOpen = it })
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(
                        value = minOrder,
                        onValueChange = { minOrder = it },
                        label = { Text("Minimum Order Value (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = deliveryFee,
                        onValueChange = { deliveryFee = it },
                        label = { Text("Base Delivery Fee (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = freeAbove,
                        onValueChange = { freeAbove = it },
                        label = { Text("Free Delivery Threshold (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val updated = settings.copy(
                                isOpen = isOpen,
                                minOrderValue = minOrder.toDoubleOrNull() ?: settings.minOrderValue,
                                baseDeliveryFee = deliveryFee.toDoubleOrNull() ?: settings.baseDeliveryFee,
                                freeDeliveryAbove = freeAbove.toDoubleOrNull() ?: settings.freeDeliveryAbove
                            )
                            viewModel.adminUpdateSettings(updated)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AbhilashPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Settings")
                    }
                }
            }
        }
    }
}
