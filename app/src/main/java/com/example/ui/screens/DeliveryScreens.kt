package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrderStatus
import com.example.model.PaymentMethod
import com.example.ui.theme.*
import com.example.viewmodel.RestaurantViewModel

@Composable
fun DeliveryPartnerScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.orders.collectAsState()
    val deliveryStaff by viewModel.deliveryStaffList.collectAsState()
    val activeDriver = deliveryStaff.first()

    // Orders that are relevant to delivery: READY_FOR_PICKUP, OUT_FOR_DELIVERY, DELIVERED
    val activeDeliveries = orders.filter {
        it.orderStatus == OrderStatus.READY_FOR_PICKUP ||
                it.orderStatus == OrderStatus.OUT_FOR_DELIVERY
    }

    val completedDeliveries = orders.filter { it.orderStatus == OrderStatus.DELIVERED }

    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Driver Profile Card
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = AbhilashGreen.copy(alpha = 0.15f),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.DeliveryDining, contentDescription = null, tint = AbhilashGreen, modifier = Modifier.size(26.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = activeDriver.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(text = "Bike: ${activeDriver.vehicleNumber} • On Duty", fontSize = 11.sp, color = AbhilashMuted)
                        }
                    }

                    Surface(
                        color = AbhilashGreen.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "ONLINE",
                            color = AbhilashGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = AbhilashPrimary
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Active Deliveries (${activeDeliveries.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Completed (${completedDeliveries.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                }
            }
        }

        val currentList = if (selectedTab == 0) activeDeliveries else completedDeliveries

        if (currentList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = if (selectedTab == 0) "🛵" else "📦", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (selectedTab == 0) "No active runs assigned" else "No completed deliveries yet",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = if (selectedTab == 0) "When the admin marks food Ready & assigns a driver, orders appear here." else "Delivered orders will be recorded here.",
                        fontSize = 12.sp,
                        color = AbhilashMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(currentList, key = { it.id }) { order ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(0.5.dp, AbhilashBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Order #${order.id}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Surface(
                                    color = if (order.orderStatus == OrderStatus.DELIVERED) AbhilashGreen.copy(alpha = 0.15f) else AbhilashPrimary.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = order.orderStatus.displayName,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (order.orderStatus == OrderStatus.DELIVERED) AbhilashGreen else AbhilashPrimaryDark,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Divider(modifier = Modifier.padding(vertical = 10.dp), color = AbhilashBorder)

                            // Customer Details
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "Customer: ${order.customerName}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(text = "Phone: ${order.customerPhone}", fontSize = 12.sp, color = AbhilashMuted)
                                    Text(
                                        text = "📍 ${order.deliveryAddress.fullFormattedAddress}",
                                        fontSize = 12.sp,
                                        color = AbhilashCharcoal,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.showMessage("Calling customer at ${order.customerPhone}...") },
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = AbhilashGreen.copy(alpha = 0.15f))
                                ) {
                                    Icon(Icons.Default.Phone, contentDescription = "Call", tint = AbhilashGreen)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Payment mode
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Payment: ${order.paymentMethod.name}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    Text(
                                        text = if (order.paymentMethod == PaymentMethod.COD) "COLLECT CASH: ₹${order.totalAmount.toInt()}" else "ALREADY PAID ONLINE",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (order.paymentMethod == PaymentMethod.COD) Color(0xFFB45309) else AbhilashGreen
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Action buttons
                            if (order.orderStatus == OrderStatus.READY_FOR_PICKUP) {
                                Button(
                                    onClick = { viewModel.updateOrderStatus(order.id, OrderStatus.OUT_FOR_DELIVERY) },
                                    colors = ButtonDefaults.buttonColors(containerColor = AbhilashPrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.DirectionsBike, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Confirm Food Picked Up from Restaurant")
                                }
                            } else if (order.orderStatus == OrderStatus.OUT_FOR_DELIVERY) {
                                Button(
                                    onClick = { viewModel.updateOrderStatus(order.id, OrderStatus.DELIVERED) },
                                    colors = ButtonDefaults.buttonColors(containerColor = AbhilashGreen),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Mark Order Delivered to Customer")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
