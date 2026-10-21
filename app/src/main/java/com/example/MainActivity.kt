package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.RestaurantViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: RestaurantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AbhilashRestaurantTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: RestaurantViewModel) {
    val currentRole by viewModel.currentRole.collectAsState()
    val customerScreen by viewModel.customerScreen.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val selectedProductForDetail by viewModel.selectedProductForDetail.collectAsState()
    val isRazorpayModalOpen by viewModel.isRazorpayModalOpen.collectAsState()
    val snackMessage by viewModel.snackMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackMessage) {
        snackMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    // Back button handling
    BackHandler(enabled = currentRole == UserRole.CUSTOMER && customerScreen != CustomerScreen.HOME) {
        viewModel.navigateCustomer(CustomerScreen.HOME)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AbhilashPrimary,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.abhilash_logo_1792594329341),
                                contentDescription = "Abhilash Restaurant Logo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Abhilash Restaurant",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Delicious Food, Delivered Fresh.",
                                fontSize = 10.sp,
                                color = AbhilashPrimaryDark,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Top Interactive Role Switcher Chip
                    RoleSwitcherChip(
                        currentRole = currentRole,
                        onRoleSelected = { viewModel.switchRole(it) }
                    )
                }
            }
        },
        bottomBar = {
            if (currentRole == UserRole.CUSTOMER) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    // Floating Cart Summary Bar if items present and not on Cart screen
                    if (cartItems.isNotEmpty() && customerScreen != CustomerScreen.CART) {
                        val subtotal = viewModel.calculateSubtotal()
                        val totalQuantity = cartItems.sumOf { it.quantity }
                        Surface(
                            onClick = { viewModel.navigateCustomer(CustomerScreen.CART) },
                            color = AbhilashPrimary,
                            shape = RoundedCornerShape(12.dp),
                            shadowElevation = 6.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .height(50.dp)
                                .testTag("floating_cart_bar")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🛒", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "$totalQuantity ${if (totalQuantity == 1) "item" else "items"} | ₹${subtotal.toInt()}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Plus taxes & delivery charges",
                                            fontSize = 10.sp,
                                            color = Color.White.copy(alpha = 0.85f)
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "View Cart",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Customer Bottom Navigation Bar
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp
                    ) {
                        NavigationBarItem(
                            selected = customerScreen == CustomerScreen.HOME,
                            onClick = { viewModel.navigateCustomer(CustomerScreen.HOME) },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AbhilashPrimaryDark,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.testTag("nav_home")
                        )
                        NavigationBarItem(
                            selected = customerScreen == CustomerScreen.SEARCH,
                            onClick = { viewModel.navigateCustomer(CustomerScreen.SEARCH) },
                            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                            label = { Text("Search", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AbhilashPrimaryDark,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.testTag("nav_search")
                        )
                        NavigationBarItem(
                            selected = customerScreen == CustomerScreen.CART,
                            onClick = { viewModel.navigateCustomer(CustomerScreen.CART) },
                            icon = {
                                BadgedBox(badge = {
                                    if (cartItems.isNotEmpty()) {
                                        Badge(containerColor = AbhilashPrimary) {
                                            Text("${cartItems.sumOf { it.quantity }}")
                                        }
                                    }
                                }) {
                                    Icon(Icons.Default.ShoppingBag, contentDescription = "Cart")
                                }
                            },
                            label = { Text("Cart", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AbhilashPrimaryDark,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.testTag("nav_cart")
                        )
                        NavigationBarItem(
                            selected = customerScreen == CustomerScreen.ORDERS || customerScreen == CustomerScreen.TRACKING,
                            onClick = { viewModel.navigateCustomer(CustomerScreen.ORDERS) },
                            icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "Orders") },
                            label = { Text("Orders", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AbhilashPrimaryDark,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.testTag("nav_orders")
                        )
                        NavigationBarItem(
                            selected = customerScreen == CustomerScreen.PROFILE,
                            onClick = { viewModel.navigateCustomer(CustomerScreen.PROFILE) },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                            label = { Text("Profile", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AbhilashPrimaryDark,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.testTag("nav_profile")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRole) {
                UserRole.CUSTOMER -> {
                    when (customerScreen) {
                        CustomerScreen.HOME, CustomerScreen.CATEGORY -> CustomerHomeScreen(viewModel = viewModel)
                        CustomerScreen.SEARCH -> SearchScreen(viewModel = viewModel)
                        CustomerScreen.CART -> CartScreen(viewModel = viewModel)
                        CustomerScreen.TRACKING -> OrderTrackingScreen(viewModel = viewModel)
                        CustomerScreen.ORDERS -> OrderHistoryScreen(viewModel = viewModel)
                        CustomerScreen.PROFILE -> ProfileScreen(viewModel = viewModel)
                        CustomerScreen.ADDRESSES -> AddressScreen(viewModel = viewModel)
                        else -> CustomerHomeScreen(viewModel = viewModel)
                    }
                }
                UserRole.ADMIN -> {
                    AdminDashboardScreen(viewModel = viewModel)
                }
                UserRole.DELIVERY -> {
                    DeliveryPartnerScreen(viewModel = viewModel)
                }
            }
        }
    }

    // Product Customization Bottom Sheet
    if (selectedProductForDetail != null) {
        ProductCustomizationSheet(
            product = selectedProductForDetail!!,
            onDismiss = { viewModel.closeProductDetail() },
            onConfirmAdd = { variant, addons, qty, notes ->
                viewModel.addToCart(
                    product = selectedProductForDetail!!,
                    variant = variant,
                    addons = addons,
                    quantity = qty,
                    instructions = notes
                )
            }
        )
    }

    // Razorpay Payment Simulation Dialog
    if (isRazorpayModalOpen) {
        RazorpayPaymentDialog(
            totalAmount = viewModel.calculateGrandTotal(),
            onDismiss = { viewModel.closeRazorpayModal() },
            onPaymentSuccess = { paymentMethod ->
                viewModel.placeOrder(paymentMethod)
            }
        )
    }
}
