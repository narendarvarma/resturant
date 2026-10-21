package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.RestaurantViewModel
import kotlinx.coroutines.launch

@Composable
fun CustomerHomeScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val categories by viewModel.categories.collectAsState()
    val products by viewModel.products.collectAsState()
    val activeCategory by viewModel.activeCategory.collectAsState()
    val isPureVegOnly by viewModel.isPureVegOnly.collectAsState()
    val deliveryAddress by viewModel.deliveryAddress.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val settings by viewModel.restaurantSettings.collectAsState()

    val filteredProducts = remember(products, activeCategory, isPureVegOnly) {
        products.filter { p ->
            (activeCategory == null || p.categoryId == activeCategory?.id) &&
                    (!isPureVegOnly || p.isVeg)
        }
    }

    val listState = androidx.compose.foundation.lazy.rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Top Location & Status Header
        item {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.navigateCustomer(CustomerScreen.ADDRESSES) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = AbhilashPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = deliveryAddress.label,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = AbhilashMuted
                                    )
                                }
                                Text(
                                    text = deliveryAddress.fullFormattedAddress,
                                    fontSize = 11.sp,
                                    color = AbhilashMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Restaurant Open/Closed Status
                        Surface(
                            color = if (settings.isOpen) AbhilashGreen.copy(alpha = 0.15f) else Color.Red.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(
                                            if (settings.isOpen) AbhilashGreen else Color.Red,
                                            CircleShape
                                        )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (settings.isOpen) "Open Now" else "Closed",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (settings.isOpen) AbhilashGreen else Color.Red
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Search Trigger Card
                    Surface(
                        onClick = { viewModel.navigateCustomer(CustomerScreen.SEARCH) },
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("search_trigger_input")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = AbhilashMuted,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Search for Chicken Dum Biryani, Noodles, Dosa...",
                                fontSize = 13.sp,
                                color = AbhilashMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // Prominent Abhilash Restaurant Hero Section
        item {
            AbhilashHeroSection(
                onOrderNowClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(index = 4)
                    }
                    viewModel.selectCategory(null)
                    viewModel.showMessage("Browsing Abhilash Restaurant menu")
                }
            )
        }

        // Photo-Driven Category Discovery
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "What's on your mind?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (activeCategory != null) {
                        Text(
                            text = "View All",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AbhilashPrimaryDark,
                            modifier = Modifier.clickable { viewModel.selectCategory(null) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories) { cat ->
                        CategoryPhotoCard(
                            category = cat,
                            isSelected = activeCategory?.id == cat.id,
                            onClick = {
                                viewModel.selectCategory(if (activeCategory?.id == cat.id) null else cat)
                            }
                        )
                    }
                }
            }
        }

        // Promotional Banners Carousel
        item {
            Spacer(modifier = Modifier.height(14.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    PromoBannerCard(
                        title = "Abhilash Dum Biryani Festival",
                        subtitle = "50% OFF up to ₹100 • Code ABHILASH50",
                        gradient = Brush.horizontalGradient(listOf(Color(0xFFEA580C), Color(0xFFF97316))),
                        badge = "POPULAR"
                    )
                }
                item {
                    PromoBannerCard(
                        title = "Crispy South Indian Tiffins",
                        subtitle = "Fresh Ghee Dosas & Steaming Idlis from ₹60",
                        gradient = Brush.horizontalGradient(listOf(Color(0xFF047857), Color(0xFF10B981))),
                        badge = "MORNING SPECIAL"
                    )
                }
                item {
                    PromoBannerCard(
                        title = "Free Delivery Above ₹499",
                        subtitle = "Delivered Fresh To Your Doorstep in 30 mins",
                        gradient = Brush.horizontalGradient(listOf(Color(0xFF0369A1), Color(0xFF0EA5E9))),
                        badge = "FAST DELIVERY"
                    )
                }
            }
        }

        // Filter Row: Categories + Pure Veg toggle
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Explore Menu",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                // Pure Veg Toggle
                Surface(
                    onClick = { viewModel.togglePureVeg() },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isPureVegOnly) AbhilashGreen.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        1.dp,
                        if (isPureVegOnly) AbhilashGreen else AbhilashBorder
                    ),
                    modifier = Modifier.testTag("pure_veg_toggle")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VegIndicator(isVeg = true)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pure Veg",
                            fontSize = 12.sp,
                            fontWeight = if (isPureVegOnly) FontWeight.Bold else FontWeight.Medium,
                            color = if (isPureVegOnly) AbhilashGreen else AbhilashCharcoal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Categories horizontal list
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = activeCategory == null,
                        onClick = { viewModel.selectCategory(null) },
                        label = { Text("All Dishes (${products.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AbhilashPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                items(categories) { cat ->
                    FilterChip(
                        selected = activeCategory?.id == cat.id,
                        onClick = {
                            viewModel.selectCategory(if (activeCategory?.id == cat.id) null else cat)
                        },
                        label = { Text("${cat.iconEmoji} ${cat.name}") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AbhilashPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Active filter status count
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredProducts.size} items available",
                    fontSize = 12.sp,
                    color = AbhilashMuted,
                    fontWeight = FontWeight.Medium
                )
                if (activeCategory != null || isPureVegOnly) {
                    Text(
                        text = "Clear filters",
                        fontSize = 12.sp,
                        color = AbhilashPrimaryDark,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            viewModel.selectCategory(null)
                            if (isPureVegOnly) viewModel.togglePureVeg()
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Products List
        items(filteredProducts, key = { it.id }) { product ->
            val cartItem = cartItems.find { it.product.id == product.id }
            val quantity = cartItem?.quantity ?: 0

            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                ProductItemCard(
                    product = product,
                    cartQuantity = quantity,
                    onAddToCart = {
                        if (product.variants.isNotEmpty() || product.addons.isNotEmpty()) {
                            viewModel.openProductDetail(product)
                        } else {
                            viewModel.addToCart(product)
                        }
                    },
                    onIncrease = {
                        if (cartItem != null) {
                            viewModel.updateCartQuantity(cartItem.cartItemId, cartItem.quantity + 1)
                        }
                    },
                    onDecrease = {
                        if (cartItem != null) {
                            viewModel.updateCartQuantity(cartItem.cartItemId, cartItem.quantity - 1)
                        }
                    },
                    onClick = {
                        viewModel.openProductDetail(product)
                    }
                )
            }
        }
    }
}

@Composable
fun HeroTag(text: String) {
    Surface(
        color = Color.White.copy(alpha = 0.9f),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(0.5.dp, AbhilashBorder)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = AbhilashCharcoal,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun CategoryPhotoCard(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            if (isSelected) 1.5.dp else 0.5.dp,
            if (isSelected) AbhilashPrimary else AbhilashBorder
        ),
        modifier = Modifier
            .width(112.dp)
            .height(115.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            FoodImageView(
                imageUrl = category.imageUrl,
                contentDescription = category.name,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                Text(
                    text = category.iconEmoji,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = category.name,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (isSelected) AbhilashPrimaryDark else AbhilashCharcoal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun AbhilashHeroSection(
    onOrderNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1917)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, AbhilashPrimary.copy(alpha = 0.25f))
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val isExpanded = maxWidth > 600.dp

            if (isExpanded) {
                // Tablet / Desktop: Rich banner with Biryani background & horizontal scrim
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.hero_biryani_banner_1792595380828),
                        contentDescription = "Signature Hyderabadi Dum Biryani at Abhilash Restaurant",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )

                    // Directional gradient to guarantee crystal-clear text contrast on left
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.horizontalGradient(
                                    0.0f to Color(0xFF0F172A).copy(alpha = 0.96f),
                                    0.45f to Color(0xFF18181B).copy(alpha = 0.88f),
                                    0.75f to Color(0xFF18181B).copy(alpha = 0.40f),
                                    1.0f to Color.Transparent
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.65f)
                            .padding(24.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = AbhilashPrimary,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "🔥 HYDERABAD'S CULINARY GEM",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                                Surface(
                                    color = Color.Black.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = AbhilashGold, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("4.8 (1,500+ Reviews)", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Abhilash Restaurant",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )

                            Text(
                                text = "Delicious Food, Delivered Fresh.",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFFED7AA)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Authentic Hyderabadi Dum Biryani, Indo-Chinese sizzlers, and golden South Indian tiffins cooked with 100% pure ghee and royal spices.",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.85f),
                                lineHeight = 18.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = onOrderNowClick,
                                colors = ButtonDefaults.buttonColors(containerColor = AbhilashPrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .height(44.dp)
                                    .testTag("hero_order_now_button")
                            ) {
                                Text("Order Now", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }

                            Surface(
                                color = Color.White.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Bolt, contentDescription = null, tint = Color(0xFFFDBA74), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("⚡ 30 Mins Delivery", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            } else {
                // Mobile: Edge-to-edge Biryani photography background with vertical scrim
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.hero_biryani_banner_1792595380828),
                        contentDescription = "Signature Dum Biryani at Abhilash Restaurant",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )

                    // Cinematic multi-stage vertical gradient: preserves appetizing biryani at top, dark contrast for text at bottom
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    0.0f to Color.Black.copy(alpha = 0.35f),
                                    0.35f to Color(0xFF1C1917).copy(alpha = 0.65f),
                                    0.70f to Color(0xFF18181B).copy(alpha = 0.90f),
                                    1.0f to Color(0xFF0F172A).copy(alpha = 0.98f)
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top Header Tags
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = AbhilashPrimary,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "🔥 HYDERABAD'S SPECIALTY",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Surface(
                                color = Color.Black.copy(alpha = 0.65f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = AbhilashGold,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "4.8 (1,500+)",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Bottom Prominent Branding & CTA
                        Column {
                            Text(
                                text = "Abhilash Restaurant",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = (-0.5).sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Delicious Food, Delivered Fresh.",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFED7AA)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Slow-cooked Hyderabadi Dum Biryanis, sizzling Chinese & crispy Dosas.",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Surface(
                                        color = Color.White.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "⚡ 30 Mins",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    Surface(
                                        color = Color.White.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "🍃 Pure Ghee",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Button(
                                    onClick = onOrderNowClick,
                                    colors = ButtonDefaults.buttonColors(containerColor = AbhilashPrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                                    modifier = Modifier
                                        .height(38.dp)
                                        .testTag("hero_order_now_button")
                                ) {
                                    Text("Order Now", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        Icons.Default.ArrowDownward,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PromoBannerCard(
    title: String,
    subtitle: String,
    gradient: Brush,
    badge: String
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = badge,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun SearchScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val products by viewModel.products.collectAsState()
    val isPureVegOnly by viewModel.isPureVegOnly.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()

    var sortByPriceAsc by remember { mutableStateOf<Boolean?>(null) }

    val searchResults = remember(products, searchQuery, isPureVegOnly, sortByPriceAsc) {
        var list = products.filter {
            (searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true) ||
                    it.description.contains(searchQuery, ignoreCase = true) ||
                    it.categoryId.contains(searchQuery, ignoreCase = true)) &&
                    (!isPureVegOnly || it.isVeg)
        }
        if (sortByPriceAsc != null) {
            list = if (sortByPriceAsc == true) list.sortedBy { it.basePrice } else list.sortedByDescending { it.basePrice }
        }
        list
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Search Input Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.navigateCustomer(CustomerScreen.HOME) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = { Text("Search biryani, fried rice, dosa...") },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("search_query_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Sort & Veg Filters
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = isPureVegOnly,
                        onClick = { viewModel.togglePureVeg() },
                        label = { Text("Pure Veg") },
                        leadingIcon = { VegIndicator(isVeg = true) }
                    )
                    FilterChip(
                        selected = sortByPriceAsc == true,
                        onClick = {
                            sortByPriceAsc = if (sortByPriceAsc == true) null else true
                        },
                        label = { Text("Price: Low to High") }
                    )
                    FilterChip(
                        selected = sortByPriceAsc == false,
                        onClick = {
                            sortByPriceAsc = if (sortByPriceAsc == false) null else false
                        },
                        label = { Text("Price: High to Low") }
                    )
                }
            }
        }

        if (searchResults.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🍽️", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No dishes found",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Try searching for 'Biryani', 'Fried Rice', or 'Dosa'",
                        fontSize = 13.sp,
                        color = AbhilashMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(searchResults, key = { it.id }) { product ->
                    val cartItem = cartItems.find { it.product.id == product.id }
                    val quantity = cartItem?.quantity ?: 0

                    ProductItemCard(
                        product = product,
                        cartQuantity = quantity,
                        onAddToCart = {
                            if (product.variants.isNotEmpty() || product.addons.isNotEmpty()) {
                                viewModel.openProductDetail(product)
                            } else {
                                viewModel.addToCart(product)
                            }
                        },
                        onIncrease = {
                            if (cartItem != null) {
                                viewModel.updateCartQuantity(cartItem.cartItemId, cartItem.quantity + 1)
                            }
                        },
                        onDecrease = {
                            if (cartItem != null) {
                                viewModel.updateCartQuantity(cartItem.cartItemId, cartItem.quantity - 1)
                            }
                        },
                        onClick = { viewModel.openProductDetail(product) }
                    )
                }
            }
        }
    }
}

@Composable
fun CartScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()
    val deliveryAddress by viewModel.deliveryAddress.collectAsState()
    val settings by viewModel.restaurantSettings.collectAsState()

    val subtotal = viewModel.calculateSubtotal()
    val deliveryFee = viewModel.calculateDeliveryFee()
    val gst = viewModel.calculateGst()
    val discount = viewModel.calculateDiscount()
    val total = viewModel.calculateGrandTotal()

    if (cartItems.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "🛒", fontSize = 56.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Your Cart is Empty",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Add delicious Hyderabadi Dum Biryani, Tiffins or Chinese to feast!",
                    fontSize = 13.sp,
                    color = AbhilashMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
                )
                Button(
                    onClick = { viewModel.navigateCustomer(CustomerScreen.HOME) },
                    colors = ButtonDefaults.buttonColors(containerColor = AbhilashPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Explore Menu", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Cart Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateCustomer(CustomerScreen.HOME) }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "Your Food Cart",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${cartItems.sumOf { it.quantity }} items from Abhilash Restaurant",
                        fontSize = 12.sp,
                        color = AbhilashMuted
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Free delivery progress banner
            item {
                Surface(
                    color = if (subtotal >= settings.freeDeliveryAbove) Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (subtotal >= settings.freeDeliveryAbove) "🎉 Free Delivery Unlocked!" else "Add ₹${(settings.freeDeliveryAbove - subtotal).toInt()} more for FREE Delivery!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (subtotal >= settings.freeDeliveryAbove) Color(0xFF15803D) else Color(0xFFB45309)
                        )
                    }
                }
            }

            // Cart Items
            items(cartItems, key = { it.cartItemId }) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(0.5.dp, AbhilashBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(modifier = Modifier.weight(1f)) {
                                VegIndicator(isVeg = item.product.isVeg)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = item.product.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    if (item.selectedVariant != null) {
                                        Text(
                                            text = item.selectedVariant.name,
                                            fontSize = 11.sp,
                                            color = AbhilashPrimaryDark,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    if (item.selectedAddons.isNotEmpty()) {
                                        Text(
                                            text = "+ " + item.selectedAddons.joinToString { it.name },
                                            fontSize = 11.sp,
                                            color = AbhilashMuted
                                        )
                                    }
                                    if (item.cookingInstructions.isNotBlank()) {
                                        Text(
                                            text = "Note: \"${item.cookingInstructions}\"",
                                            fontSize = 11.sp,
                                            color = Color.DarkGray,
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                        )
                                    }
                                }
                            }

                            // Stepper
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.height(30.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 2.dp)
                                ) {
                                    IconButton(
                                        onClick = { viewModel.updateCartQuantity(item.cartItemId, item.quantity - 1) },
                                        modifier = Modifier.size(26.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Minus", modifier = Modifier.size(14.dp))
                                    }
                                    Text(
                                        text = "${item.quantity}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                    IconButton(
                                        onClick = { viewModel.updateCartQuantity(item.cartItemId, item.quantity + 1) },
                                        modifier = Modifier.size(26.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Plus", modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = AbhilashBorder)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Item Total",
                                fontSize = 12.sp,
                                color = AbhilashMuted
                            )
                            Text(
                                text = "₹${item.totalPrice.toInt()}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Coupon Section
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(0.5.dp, AbhilashBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Discount, contentDescription = null, tint = AbhilashPrimary, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (appliedCoupon != null) "Coupon: ${appliedCoupon?.code}" else "Apply Restaurant Coupon",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            if (appliedCoupon != null) {
                                TextButton(onClick = { viewModel.removeCoupon() }) {
                                    Text("Remove", color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        if (appliedCoupon != null) {
                            Text(
                                text = "You saved ₹${discount.toInt()} on this order!",
                                fontSize = 12.sp,
                                color = AbhilashGreen,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("ABHILASH50", "FEAST20", "FLAT40").forEach { code ->
                                    val couponObj = com.example.data.SampleData.coupons.find { it.code == code }
                                    if (couponObj != null) {
                                        Surface(
                                            onClick = { viewModel.applyCoupon(couponObj) },
                                            shape = RoundedCornerShape(6.dp),
                                            border = BorderStroke(1.dp, AbhilashPrimary.copy(alpha = 0.5f)),
                                            color = MaterialTheme.colorScheme.primaryContainer
                                        ) {
                                            Text(
                                                text = code,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = AbhilashPrimaryDark,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bill Breakdown
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(0.5.dp, AbhilashBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Bill Details",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        BillRow(title = "Item Total", value = "₹${subtotal.toInt()}")
                        BillRow(
                            title = "Delivery Partner Fee",
                            value = if (deliveryFee == 0.0) "FREE" else "₹${deliveryFee.toInt()}",
                            isHighlight = deliveryFee == 0.0
                        )
                        BillRow(title = "Government Taxes & GST (5%)", value = "₹${gst.toInt()}")
                        if (discount > 0) {
                            BillRow(
                                title = "Coupon Discount (${appliedCoupon?.code})",
                                value = "-₹${discount.toInt()}",
                                isDiscount = true
                            )
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = AbhilashBorder)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "To Pay",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "₹${total.toInt()}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = AbhilashPrimaryDark
                            )
                        }
                    }
                }
            }

            // Delivery Address Confirmation Card
            item {
                Card(
                    onClick = { viewModel.navigateCustomer(CustomerScreen.ADDRESSES) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(0.5.dp, AbhilashBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Home, contentDescription = null, tint = AbhilashPrimary)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Delivering to ${deliveryAddress.label}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = deliveryAddress.fullFormattedAddress,
                                    fontSize = 11.sp,
                                    color = AbhilashMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        Text("CHANGE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AbhilashPrimary)
                    }
                }
            }
        }

        // Sticky Checkout Button
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            border = BorderStroke(0.5.dp, AbhilashBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "₹${total.toInt()}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = AbhilashPrimaryDark
                    )
                    Text(
                        text = "TOTAL PAYABLE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AbhilashMuted
                    )
                }

                Button(
                    onClick = { viewModel.openRazorpayModal() },
                    colors = ButtonDefaults.buttonColors(containerColor = AbhilashPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("proceed_to_checkout_button")
                ) {
                    Text(
                        text = "Proceed to Pay",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun BillRow(title: String, value: String, isDiscount: Boolean = false, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 12.sp, color = if (isDiscount) AbhilashGreen else AbhilashMuted)
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = if (isDiscount || isHighlight) FontWeight.Bold else FontWeight.Medium,
            color = if (isDiscount || isHighlight) AbhilashGreen else AbhilashCharcoal
        )
    }
}

@Composable
fun OrderTrackingScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.orders.collectAsState()
    val activeTrackingId by viewModel.activeTrackingOrderId.collectAsState()
    val order = orders.find { it.id == activeTrackingId } ?: orders.firstOrNull()

    if (order == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No active order to track.")
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.navigateCustomer(CustomerScreen.HOME) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Live Order Tracking",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    color = AbhilashPrimary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = order.id,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = AbhilashPrimaryDark,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Live ETA Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, AbhilashPrimary.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (order.orderStatus == OrderStatus.DELIVERED) "Order Delivered!" else "Arriving in ~${order.estimatedDeliveryMinutes} mins",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = AbhilashPrimaryDark
                        )
                        Text(
                            text = order.orderStatus.displayName,
                            fontSize = 12.sp,
                            color = AbhilashCharcoal,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        text = when (order.orderStatus) {
                            OrderStatus.DELIVERED -> "✅"
                            OrderStatus.OUT_FOR_DELIVERY -> "🛵"
                            OrderStatus.PREPARING -> "👨‍🍳"
                            else -> "⏱️"
                        },
                        fontSize = 36.sp
                    )
                }
            }
        }

        // Timeline Step Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(0.5.dp, AbhilashBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Order Progress",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    OrderStatusTimeline(currentStatus = order.orderStatus)
                }
            }
        }

        // Delivery Partner Card
        if (order.deliveryPartnerName != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(0.5.dp, AbhilashBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.DeliveryDining, contentDescription = null, tint = AbhilashPrimaryDark)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = order.deliveryPartnerName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Vehicle: ${order.deliveryPartnerVehicle ?: "Bike"}",
                                    fontSize = 11.sp,
                                    color = AbhilashMuted
                                )
                            }
                        }

                        IconButton(
                            onClick = { viewModel.showMessage("Calling ${order.deliveryPartnerName}...") },
                            colors = IconButtonDefaults.iconButtonColors(containerColor = AbhilashGreen.copy(alpha = 0.15f))
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", tint = AbhilashGreen)
                        }
                    }
                }
            }
        }

        // Items Summary
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(0.5.dp, AbhilashBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Order Summary (${order.items.size} items)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    order.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item.quantity}x ${item.product.name}",
                                fontSize = 13.sp,
                                color = AbhilashCharcoal
                            )
                            Text(
                                text = "₹${item.totalPrice.toInt()}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = AbhilashBorder)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Paid via ${order.paymentMethod.name}", fontSize = 12.sp, color = AbhilashMuted)
                        Text("Total: ₹${order.totalAmount.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AbhilashPrimaryDark)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderHistoryScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.orders.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Past Orders",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📜", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No past orders yet", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Your placed orders will show up here.", fontSize = 12.sp, color = AbhilashMuted)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orders, key = { it.id }) { order ->
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
                                Column {
                                    Text(text = order.id, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(text = order.placedAtTime, fontSize = 11.sp, color = AbhilashMuted)
                                }
                                Surface(
                                    color = when (order.orderStatus) {
                                        OrderStatus.DELIVERED -> AbhilashGreen.copy(alpha = 0.15f)
                                        OrderStatus.CANCELLED -> Color.Red.copy(alpha = 0.15f)
                                        else -> AbhilashPrimary.copy(alpha = 0.15f)
                                    },
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = order.orderStatus.displayName,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when (order.orderStatus) {
                                            OrderStatus.DELIVERED -> AbhilashGreen
                                            OrderStatus.CANCELLED -> Color.Red
                                            else -> AbhilashPrimaryDark
                                        },
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Divider(modifier = Modifier.padding(vertical = 10.dp), color = AbhilashBorder)

                            order.items.forEach { item ->
                                Text(
                                    text = "${item.quantity}x ${item.product.name}",
                                    fontSize = 12.sp,
                                    color = AbhilashCharcoal
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total: ₹${order.totalAmount.toInt()}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )

                                Row {
                                    OutlinedButton(
                                        onClick = { viewModel.trackOrder(order.id) },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        modifier = Modifier.height(34.dp)
                                    ) {
                                        Text("Track", fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = { viewModel.reorder(order) },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = AbhilashPrimary),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        modifier = Modifier.height(34.dp)
                                    ) {
                                        Text("Reorder", fontSize = 12.sp, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val name by viewModel.customerName.collectAsState()
    val phone by viewModel.customerPhone.collectAsState()
    val settings by viewModel.restaurantSettings.collectAsState()

    var showSupportDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Customer profile header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(0.5.dp, AbhilashBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = AbhilashPrimary, modifier = Modifier.size(28.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(text = name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(text = phone, fontSize = 12.sp, color = AbhilashMuted)
                        Surface(
                            color = AbhilashGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = "VERIFIED CUSTOMER",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = AbhilashGreen,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // Profile Menu Options
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(0.5.dp, AbhilashBorder)
            ) {
                Column {
                    ProfileMenuRow(
                        icon = Icons.Default.LocationOn,
                        title = "Saved Delivery Addresses",
                        subtitle = "Manage home, work, and other delivery drop locations",
                        onClick = { viewModel.navigateCustomer(CustomerScreen.ADDRESSES) }
                    )
                    Divider(color = AbhilashBorder)
                    ProfileMenuRow(
                        icon = Icons.Default.Receipt,
                        title = "Order History & Invoices",
                        subtitle = "View completed and past dining orders",
                        onClick = { viewModel.navigateCustomer(CustomerScreen.ORDERS) }
                    )
                    Divider(color = AbhilashBorder)
                    ProfileMenuRow(
                        icon = Icons.Default.Headphones,
                        title = "Help & Customer Support",
                        subtitle = "Call restaurant desk or report an issue with an order",
                        onClick = { showSupportDialog = true }
                    )
                    Divider(color = AbhilashBorder)
                    ProfileMenuRow(
                        icon = Icons.Default.Description,
                        title = "Terms, FSSAI & Privacy Policy",
                        subtitle = "Food safety standards and customer terms",
                        onClick = { showTermsDialog = true }
                    )
                }
            }
        }

        // Restaurant Info
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = settings.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = settings.tagline, fontSize = 12.sp, color = AbhilashPrimaryDark)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = settings.address, fontSize = 11.sp, color = AbhilashMuted)
                    Text(text = "Desk: ${settings.phone}", fontSize = 11.sp, color = AbhilashMuted)
                }
            }
        }
    }

    if (showSupportDialog) {
        AlertDialog(
            onDismissRequest = { showSupportDialog = false },
            title = { Text("Abhilash Restaurant Support") },
            text = {
                Column {
                    Text("For active order inquiries, table reservations, or feedback:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("📞 Phone: +91 98490 12345", fontWeight = FontWeight.Bold)
                    Text("✉️ Email: support@abhilashrestaurant.com", fontWeight = FontWeight.Bold)
                    Text("⏰ Support Hours: 11:00 AM – 11:30 PM", fontSize = 12.sp, color = AbhilashMuted)
                }
            },
            confirmButton = {
                Button(onClick = { showSupportDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = { Text("Terms & FSSAI Standards") },
            text = {
                Column {
                    Text("• FSSAI Lic No: 13622011000421")
                    Text("• All dishes prepared fresh using 100% natural spices.")
                    Text("• Cancellations allowed before the kitchen begins food preparation.")
                    Text("• Delivery within 8.5 km radius of Madhapur/Hitech City.")
                }
            },
            confirmButton = {
                Button(onClick = { showTermsDialog = false }) {
                    Text("Understood")
                }
            }
        )
    }
}

@Composable
fun ProfileMenuRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = AbhilashPrimary)
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text(text = subtitle, fontSize = 11.sp, color = AbhilashMuted)
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = AbhilashMuted)
    }
}
