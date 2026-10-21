package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun FoodImageView(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    fallbackRes: Int? = null,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(12.dp)
) {
    val context = LocalContext.current
    var isError by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (fallbackRes != null && imageUrl.isBlank()) {
            Image(
                painter = painterResource(id = fallbackRes),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (imageUrl.isNotBlank() && !isError) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                onError = { isError = true },
                modifier = Modifier.fillMaxSize()
            )
        } else if (fallbackRes != null) {
            Image(
                painter = painterResource(id = fallbackRes),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                AbhilashPrimary.copy(alpha = 0.2f),
                                AbhilashSecondary.copy(alpha = 0.35f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = contentDescription,
                    tint = AbhilashPrimaryDark,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun VegIndicator(isVeg: Boolean, modifier: Modifier = Modifier) {
    val borderColor = if (isVeg) AbhilashGreen else AbhilashNonVeg
    Box(
        modifier = modifier
            .size(16.dp)
            .border(1.5.dp, borderColor, RoundedCornerShape(3.dp))
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isVeg) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(AbhilashGreen, CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(AbhilashNonVeg, RoundedCornerShape(1.dp))
            )
        }
    }
}

@Composable
fun RatingBadge(rating: Double, ratingCount: Int? = null) {
    Surface(
        color = Color(0xFF22C55E).copy(alpha = 0.15f),
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = String.format("%.1f", rating),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF15803D)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFF15803D),
                modifier = Modifier.size(11.dp)
            )
            if (ratingCount != null) {
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "($ratingCount)",
                    fontSize = 10.sp,
                    color = AbhilashMuted
                )
            }
        }
    }
}

@Composable
fun RoleSwitcherChip(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Surface(
            onClick = { expanded = true },
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, AbhilashPrimary.copy(alpha = 0.4f)),
            modifier = Modifier.testTag("role_switcher_chip")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = when (currentRole) {
                        UserRole.CUSTOMER -> Icons.Default.Person
                        UserRole.ADMIN -> Icons.Default.Restaurant
                        UserRole.DELIVERY -> Icons.Default.DeliveryDining
                    },
                    contentDescription = null,
                    tint = AbhilashPrimaryDark,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = currentRole.label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AbhilashPrimaryDark
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Switch role",
                    tint = AbhilashPrimaryDark,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            UserRole.values().forEach { role ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = when (role) {
                                    UserRole.CUSTOMER -> Icons.Default.Person
                                    UserRole.ADMIN -> Icons.Default.Restaurant
                                    UserRole.DELIVERY -> Icons.Default.DeliveryDining
                                },
                                contentDescription = null,
                                tint = if (role == currentRole) AbhilashPrimary else AbhilashCharcoal,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = role.label,
                                fontWeight = if (role == currentRole) FontWeight.Bold else FontWeight.Normal,
                                color = if (role == currentRole) AbhilashPrimary else AbhilashCharcoal
                            )
                        }
                    },
                    onClick = {
                        expanded = false
                        onRoleSelected(role)
                    }
                )
            }
        }
    }
}

@Composable
fun ProductItemCard(
    product: Product,
    cartQuantity: Int,
    onAddToCart: () -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .testTag("product_card_${product.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(0.5.dp, AbhilashBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    VegIndicator(isVeg = product.isVeg)
                    Spacer(modifier = Modifier.width(6.dp))
                    if (product.isBestseller) {
                        Surface(
                            color = AbhilashGold.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "BESTSELLER",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFB45309),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "₹${product.basePrice.toInt()}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                RatingBadge(rating = product.rating, ratingCount = product.ratingCount)

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = AbhilashMuted,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Dish real food photograph with rounded corners
                FoodImageView(
                    imageUrl = product.imageUrl,
                    contentDescription = product.name,
                    fallbackRes = product.localDrawableRes,
                    modifier = Modifier.size(84.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (!product.isAvailable) {
                    Surface(
                        color = Color.LightGray.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "SOLD OUT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                } else if (cartQuantity == 0) {
                    Button(
                        onClick = onAddToCart,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("add_button_${product.id}")
                    ) {
                        Text("ADD", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.height(34.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            IconButton(
                                onClick = onDecrease,
                                modifier = Modifier.size(26.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Decrease",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "$cartQuantity",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )
                            IconButton(
                                onClick = onIncrease,
                                modifier = Modifier.size(26.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }

                if (product.variants.isNotEmpty() || product.addons.isNotEmpty()) {
                    Text(
                        text = "customisable",
                        fontSize = 10.sp,
                        color = AbhilashMuted,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCustomizationSheet(
    product: Product,
    onDismiss: () -> Unit,
    onConfirmAdd: (ProductVariant?, List<ProductAddon>, Int, String) -> Unit
) {
    var selectedVariant by remember {
        mutableStateOf(product.variants.firstOrNull { it.isDefault } ?: product.variants.firstOrNull())
    }
    val selectedAddons = remember { mutableStateListOf<ProductAddon>() }
    var quantity by remember { mutableStateOf(1) }
    var cookingInstructions by remember { mutableStateOf("") }

    val calculatedUnitPrice = product.basePrice +
            (selectedVariant?.priceDelta ?: 0.0) +
            selectedAddons.sumOf { it.price }
    val totalAmount = calculatedUnitPrice * quantity

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Food Photograph Banner
            FoodImageView(
                imageUrl = product.imageUrl,
                contentDescription = product.name,
                fallbackRes = product.localDrawableRes,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        VegIndicator(isVeg = product.isVeg)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Base: ₹${product.basePrice.toInt()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AbhilashPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Text(
                text = product.description,
                style = MaterialTheme.typography.bodySmall,
                color = AbhilashMuted,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Portion / Size Selection
            if (product.variants.isNotEmpty()) {
                Divider(modifier = Modifier.padding(vertical = 10.dp))
                Text(
                    text = "Select Portion Size",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))

                product.variants.forEach { variant ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedVariant = variant }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedVariant?.id == variant.id,
                                onClick = { selectedVariant = variant },
                                colors = RadioButtonDefaults.colors(selectedColor = AbhilashPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = variant.name, fontSize = 14.sp)
                        }
                        Text(
                            text = if (variant.priceDelta > 0) "+₹${variant.priceDelta.toInt()}" else "Free",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AbhilashMuted
                        )
                    }
                }
            }

            // Add-ons
            if (product.addons.isNotEmpty()) {
                Divider(modifier = Modifier.padding(vertical = 10.dp))
                Text(
                    text = "Optional Add-ons",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))

                product.addons.forEach { addon ->
                    val isChecked = selectedAddons.any { it.id == addon.id }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isChecked) selectedAddons.removeAll { it.id == addon.id }
                                else selectedAddons.add(addon)
                            }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { checked ->
                                    if (checked) selectedAddons.add(addon)
                                    else selectedAddons.removeAll { it.id == addon.id }
                                },
                                colors = CheckboxDefaults.colors(checkedColor = AbhilashPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = addon.name, fontSize = 14.sp)
                        }
                        Text(
                            text = "+₹${addon.price.toInt()}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AbhilashPrimaryDark
                        )
                    }
                }
            }

            // Cooking instructions
            Divider(modifier = Modifier.padding(vertical = 10.dp))
            Text(
                text = "Cooking Instructions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = cookingInstructions,
                onValueChange = { cookingInstructions = it },
                placeholder = { Text("E.g., less spicy, extra onions, packing advice...", fontSize = 13.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(85.dp),
                shape = RoundedCornerShape(10.dp)
            )

            // Quantity & Add button
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, AbhilashPrimary),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = AbhilashPrimary)
                        }
                        Text(
                            text = "$quantity",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        IconButton(
                            onClick = { quantity++ },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", tint = AbhilashPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        onConfirmAdd(selectedVariant, selectedAddons.toList(), quantity, cookingInstructions)
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("confirm_add_to_cart_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AbhilashPrimary)
                ) {
                    Text(
                        text = "Add Item • ₹${totalAmount.toInt()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun OrderStatusTimeline(currentStatus: OrderStatus) {
    val steps = listOf(
        OrderStatus.PLACED to "Order Placed",
        OrderStatus.CONFIRMED to "Confirmed",
        OrderStatus.PREPARING to "Kitchen Prep",
        OrderStatus.READY_FOR_PICKUP to "Ready",
        OrderStatus.OUT_FOR_DELIVERY to "Out for Delivery",
        OrderStatus.DELIVERED to "Delivered"
    )

    val currentIdx = currentStatus.stepIndex

    Column(modifier = Modifier.fillMaxWidth()) {
        steps.forEachIndexed { index, (status, label) ->
            val isCompleted = currentIdx > index
            val isCurrent = currentIdx == index

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            color = when {
                                isCompleted -> AbhilashGreen
                                isCurrent -> AbhilashPrimary
                                else -> Color.LightGray.copy(alpha = 0.5f)
                            },
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    } else if (isCurrent) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(Color.White, CircleShape)
                        )
                    } else {
                        Text(
                            text = "${index + 1}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = label,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                        color = if (isCurrent) AbhilashPrimaryDark else if (isCompleted) AbhilashCharcoal else AbhilashMuted,
                        fontSize = 14.sp
                    )
                    if (isCurrent) {
                        Text(
                            text = when (status) {
                                OrderStatus.PLACED -> "Restaurant is reviewing your order"
                                OrderStatus.CONFIRMED -> "Order accepted by chef"
                                OrderStatus.PREPARING -> "Food is cooking on dum in the kitchen"
                                OrderStatus.READY_FOR_PICKUP -> "Packed hot, awaiting delivery pickup"
                                OrderStatus.OUT_FOR_DELIVERY -> "Delivery partner on the way to your door"
                                OrderStatus.DELIVERED -> "Handed over fresh. Enjoy your meal!"
                                else -> ""
                            },
                            fontSize = 11.sp,
                            color = AbhilashMuted
                        )
                    }
                }
            }

            if (index < steps.size - 1) {
                Box(
                    modifier = Modifier
                        .padding(start = 13.dp)
                        .width(2.dp)
                        .height(14.dp)
                        .background(
                            if (isCompleted) AbhilashGreen else Color.LightGray.copy(alpha = 0.5f)
                        )
                )
            }
        }
    }
}

@Composable
fun RazorpayPaymentDialog(
    totalAmount: Double,
    onDismiss: () -> Unit,
    onPaymentSuccess: (PaymentMethod) -> Unit
) {
    var selectedMethod by remember { mutableStateOf(PaymentMethod.UPI) }
    var selectedUpiApp by remember { mutableStateOf("Google Pay") }
    var cardNumber by remember { mutableStateOf("4532 •••• •••• 8821") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Razorpay Checkout",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF0C2340) // Razorpay dark navy
                    )
                    Text(
                        text = "Secured 256-bit SSL Gateway",
                        fontSize = 11.sp,
                        color = AbhilashMuted
                    )
                }
                Surface(
                    color = Color(0xFF0284C7).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "TEST MODE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0284C7),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Amount to Pay", fontWeight = FontWeight.Medium, fontSize = 13.sp)
                        Text(
                            "₹${totalAmount.toInt()}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = AbhilashPrimaryDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("Select Payment Option", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))

                // UPI Option
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        1.dp,
                        if (selectedMethod == PaymentMethod.UPI) AbhilashPrimary else AbhilashBorder
                    ),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedMethod = PaymentMethod.UPI }
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedMethod == PaymentMethod.UPI,
                                onClick = { selectedMethod = PaymentMethod.UPI },
                                colors = RadioButtonDefaults.colors(selectedColor = AbhilashPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("UPI (Instant & Zero Fee)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }

                        if (selectedMethod == PaymentMethod.UPI) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 32.dp, top = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("Google Pay", "PhonePe", "Paytm").forEach { app ->
                                    val isSelected = selectedUpiApp == app
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        border = BorderStroke(1.dp, if (isSelected) AbhilashPrimary else AbhilashBorder),
                                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                                        modifier = Modifier.clickable { selectedUpiApp = app }
                                    ) {
                                        Text(
                                            text = app,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) AbhilashPrimaryDark else AbhilashCharcoal,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Cards Option
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        1.dp,
                        if (selectedMethod == PaymentMethod.CARD) AbhilashPrimary else AbhilashBorder
                    ),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedMethod = PaymentMethod.CARD }
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedMethod == PaymentMethod.CARD,
                                onClick = { selectedMethod = PaymentMethod.CARD },
                                colors = RadioButtonDefaults.colors(selectedColor = AbhilashPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Credit / Debit Cards", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }
                        if (selectedMethod == PaymentMethod.CARD) {
                            Text(
                                text = "Saved Card: $cardNumber (Visa)",
                                fontSize = 12.sp,
                                color = AbhilashMuted,
                                modifier = Modifier.padding(start = 36.dp, top = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Cash on Delivery
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        1.dp,
                        if (selectedMethod == PaymentMethod.COD) AbhilashPrimary else AbhilashBorder
                    ),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedMethod = PaymentMethod.COD }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedMethod == PaymentMethod.COD,
                            onClick = { selectedMethod = PaymentMethod.COD },
                            colors = RadioButtonDefaults.colors(selectedColor = AbhilashPrimary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text("Cash on Delivery (COD)", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("Pay cash to delivery executive at doorstep", fontSize = 11.sp, color = AbhilashMuted)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onPaymentSuccess(selectedMethod) },
                colors = ButtonDefaults.buttonColors(containerColor = AbhilashPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("razorpay_pay_button")
            ) {
                Text(
                    text = if (selectedMethod == PaymentMethod.COD) "Confirm COD Order" else "Pay ₹${totalAmount.toInt()} via Razorpay",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = AbhilashMuted)
            }
        }
    )
}
