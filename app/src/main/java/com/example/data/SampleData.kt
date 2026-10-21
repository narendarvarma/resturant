package com.example.data

import com.example.R
import com.example.model.*

object SampleData {

    val categories = listOf(
        Category(
            id = "cat_biryani",
            name = "Biryani",
            description = "Authentic slow-cooked dum & fry biryanis",
            iconEmoji = "🍚",
            imageUrl = "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=500&auto=format&fit=crop&q=80"
        ),
        Category(
            id = "cat_chinese",
            name = "Chinese",
            description = "Indo-Chinese wok specials, rice & noodles",
            iconEmoji = "🥢",
            imageUrl = "https://images.unsplash.com/photo-1603133872878-684f208fb84b?w=500&auto=format&fit=crop&q=80"
        ),
        Category(
            id = "cat_tiffins",
            name = "South Indian Tiffins",
            description = "Crispy dosas, hot idlis & traditional breakfasts",
            iconEmoji = "🥞",
            imageUrl = "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=500&auto=format&fit=crop&q=80"
        ),
        Category(
            id = "cat_refreshments",
            name = "Refreshments",
            description = "Chilled beverages & fresh fruit refreshments",
            iconEmoji = "🥤",
            imageUrl = "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=500&auto=format&fit=crop&q=80"
        )
    )

    private val biryaniVariants = listOf(
        ProductVariant("v_single", "Single Portion", 0.0, true),
        ProductVariant("v_regular", "Regular (Serves 1-2)", 80.0),
        ProductVariant("v_family", "Jumbo / Family Pack (Serves 3-4)", 260.0)
    )

    private val biryaniAddons = listOf(
        ProductAddon("a_raita", "Extra Onion Raita", 30.0),
        ProductAddon("a_salan", "Extra Mirchi Ka Salan", 35.0),
        ProductAddon("a_egg", "Extra Boiled Egg (2 pcs)", 30.0),
        ProductAddon("a_masala", "Double Masala Layer", 40.0)
    )

    private val tiffinAddons = listOf(
        ProductAddon("a_sambar", "Extra Hot Sambar Bowl", 25.0),
        ProductAddon("a_ghee", "Ghee Podi Drizzle", 30.0),
        ProductAddon("a_chutney", "Special Peanut Chutney Dip", 20.0)
    )

    val products = listOf(
        // ================= BIRYANI =================
        Product(
            id = "b1",
            categoryId = "cat_biryani",
            name = "Chicken Dum Biryani",
            description = "Signature long-grain Basmati rice slow-cooked on dum with tender marinated chicken pieces, Hyderabadi saffron spices, mint, and caramelized onions. Served with Mirchi Ka Salan and creamy Raita.",
            basePrice = 280.0,
            isVeg = false,
            isBestseller = true,
            isSpecial = true,
            rating = 4.8,
            ratingCount = 540,
            variants = biryaniVariants,
            addons = biryaniAddons,
            imageUrl = "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=500&auto=format&fit=crop&q=80",
            localDrawableRes = R.drawable.hero_biryani_banner_1792595380828
        ),
        Product(
            id = "b2",
            categoryId = "cat_biryani",
            name = "Chicken 65 Biryani",
            description = "Crispy, spicy South-Indian Chicken 65 chunks tossed in curry leaves and green chillies, layered over fragrant ghee-infused biryani rice.",
            basePrice = 310.0,
            isVeg = false,
            isBestseller = true,
            rating = 4.7,
            ratingCount = 380,
            variants = biryaniVariants,
            addons = biryaniAddons,
            imageUrl = "https://images.unsplash.com/photo-1633945274405-b6c8069047b0?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "b3",
            categoryId = "cat_biryani",
            name = "Chicken Fry Piece Biryani",
            description = "Tender bone-in chicken shallow-fried in country spices and pepper, served on hot aromatic dum rice.",
            basePrice = 320.0,
            isVeg = false,
            rating = 4.7,
            ratingCount = 290,
            variants = biryaniVariants,
            addons = biryaniAddons,
            imageUrl = "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "b4",
            categoryId = "cat_biryani",
            name = "Egg Biryani",
            description = "Golden-fried farm eggs spiced with turmeric and garam masala, nestled inside flavorful saffron rice.",
            basePrice = 220.0,
            isVeg = false,
            rating = 4.5,
            ratingCount = 190,
            variants = biryaniVariants,
            addons = biryaniAddons,
            imageUrl = "https://images.unsplash.com/photo-1701579231305-d84d8af9a3fd?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "b5",
            categoryId = "cat_biryani",
            name = "Veg Biryani",
            description = "Garden fresh carrots, french beans, peas, and potatoes simmered in royal spices and layered with dum rice.",
            basePrice = 210.0,
            isVeg = true,
            rating = 4.5,
            ratingCount = 220,
            variants = biryaniVariants,
            addons = listOf(
                ProductAddon("a_raita", "Extra Onion Raita", 30.0),
                ProductAddon("a_salan", "Extra Mirchi Ka Salan", 35.0)
            ),
            imageUrl = "https://images.unsplash.com/photo-1642821373181-696a54913e9a?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "b6",
            categoryId = "cat_biryani",
            name = "Paneer Biryani",
            description = "Soft, melt-in-mouth cottage cheese cubes marinated in tandoori paste, cooked gently with aromatic basmati rice.",
            basePrice = 250.0,
            isVeg = true,
            isSpecial = true,
            rating = 4.6,
            ratingCount = 270,
            variants = biryaniVariants,
            addons = listOf(
                ProductAddon("a_paneer", "Extra Paneer Cubes (4 pcs)", 50.0),
                ProductAddon("a_raita", "Extra Onion Raita", 30.0),
                ProductAddon("a_salan", "Extra Mirchi Ka Salan", 35.0)
            ),
            imageUrl = "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "b7",
            categoryId = "cat_biryani",
            name = "Family Pack Biryani",
            description = "Mega jumbo feast featuring 4 generous portions of Chicken Dum Biryani, 4 boiled eggs, Chicken 65 starter, 2 Salan bowls, and double Raita.",
            basePrice = 799.0,
            isVeg = false,
            isBestseller = true,
            rating = 4.9,
            ratingCount = 610,
            variants = emptyList(),
            addons = biryaniAddons,
            imageUrl = "https://images.unsplash.com/photo-1633945274405-b6c8069047b0?w=600&auto=format&fit=crop&q=80",
            localDrawableRes = R.drawable.hero_biryani_banner_1792595380828
        ),

        // ================= CHINESE =================
        Product(
            id = "c1",
            categoryId = "cat_chinese",
            name = "Veg Fried Rice",
            description = "Wok-tossed steamed rice with crunchy cabbage, bell peppers, spring onions, and garlic soy seasonings.",
            basePrice = 160.0,
            isVeg = true,
            rating = 4.4,
            ratingCount = 140,
            imageUrl = "https://images.unsplash.com/photo-1603133872878-684f208fb84b?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "c2",
            categoryId = "cat_chinese",
            name = "Egg Fried Rice",
            description = "Scrambled farm eggs stir-fried with fragrant rice, finely chopped veggies, and light soy pepper drizzle.",
            basePrice = 180.0,
            isVeg = false,
            rating = 4.6,
            ratingCount = 180,
            imageUrl = "https://images.unsplash.com/photo-1512058564366-18510be2db19?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "c3",
            categoryId = "cat_chinese",
            name = "Chicken Fried Rice",
            description = "Succulent shredded chicken wok-tossed with rice, egg drops, aromatic spices, and scallions.",
            basePrice = 210.0,
            isVeg = false,
            isBestseller = true,
            rating = 4.7,
            ratingCount = 310,
            imageUrl = "https://images.unsplash.com/photo-1603133872878-684f208fb84b?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "c4",
            categoryId = "cat_chinese",
            name = "Schezwan Fried Rice",
            description = "Fiery wok rice infused with spicy house Schezwan chili paste, roasted garlic, and choice veggies.",
            basePrice = 190.0,
            isVeg = true,
            rating = 4.6,
            ratingCount = 200,
            imageUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "c5",
            categoryId = "cat_chinese",
            name = "Veg Noodles",
            description = "Silky Hakka noodles tossed with julienne capsicum, shredded cabbage, carrots, and sesame oil.",
            basePrice = 160.0,
            isVeg = true,
            rating = 4.4,
            ratingCount = 165,
            imageUrl = "https://images.unsplash.com/photo-1585032226651-759b368d7246?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "c6",
            categoryId = "cat_chinese",
            name = "Egg Noodles",
            description = "Fresh wheat noodles tossed high on flame with fluffy scrambled egg, dark soy, and green chilies.",
            basePrice = 180.0,
            isVeg = false,
            rating = 4.5,
            ratingCount = 195,
            imageUrl = "https://images.unsplash.com/photo-1612927601601-6638404737ce?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "c7",
            categoryId = "cat_chinese",
            name = "Chicken Noodles",
            description = "Smoky pan-fried noodles laden with spicy chicken bits, crunchy veggies, and sweet-tangy Asian glaze.",
            basePrice = 210.0,
            isVeg = false,
            rating = 4.7,
            ratingCount = 290,
            imageUrl = "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "c8",
            categoryId = "cat_chinese",
            name = "Veg Manchurian",
            description = "Deep fried vegetable dumplings simmered in savory garlic, ginger, and coriander Indo-Chinese gravy.",
            basePrice = 170.0,
            isVeg = true,
            rating = 4.5,
            ratingCount = 230,
            imageUrl = "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "c9",
            categoryId = "cat_chinese",
            name = "Chicken Manchurian",
            description = "Crispy battered chicken spheres glazed in sweet, tangy, and spicy dark Manchurian sauce.",
            basePrice = 230.0,
            isVeg = false,
            rating = 4.6,
            ratingCount = 260,
            imageUrl = "https://images.unsplash.com/photo-1525755662778-989d0524087e?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "c10",
            categoryId = "cat_chinese",
            name = "Chilli Chicken",
            description = "Boneless chicken cubes stir-fried with hot slit green chillies, diced onions, and capsicum in pungent soy sauce.",
            basePrice = 240.0,
            isVeg = false,
            isBestseller = true,
            isSpecial = true,
            rating = 4.8,
            ratingCount = 420,
            imageUrl = "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=500&auto=format&fit=crop&q=80"
        ),

        // ================= SOUTH INDIAN TIFFINS =================
        Product(
            id = "t1",
            categoryId = "cat_tiffins",
            name = "Idli",
            description = "Trio of steaming soft, fluffy fermented rice-lentil cakes served with hot piping sambar and fresh coconut chutney.",
            basePrice = 60.0,
            isVeg = true,
            rating = 4.6,
            ratingCount = 310,
            addons = tiffinAddons,
            imageUrl = "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "t2",
            categoryId = "cat_tiffins",
            name = "Plain Dosa",
            description = "Golden, crispy traditional fermented crepe roasted with pure cow ghee. Served with coconut and tomato chutneys.",
            basePrice = 70.0,
            isVeg = true,
            rating = 4.5,
            ratingCount = 280,
            addons = tiffinAddons,
            imageUrl = "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "t3",
            categoryId = "cat_tiffins",
            name = "Masala Dosa",
            description = "Crisp golden crepe smeared with spicy red chutney and stuffed with spiced mustard potato curry.",
            basePrice = 90.0,
            isVeg = true,
            isBestseller = true,
            rating = 4.8,
            ratingCount = 450,
            addons = tiffinAddons,
            imageUrl = "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "t4",
            categoryId = "cat_tiffins",
            name = "Onion Dosa",
            description = "Thin savory crepe topped with finely chopped caramelized red onions and fresh green chillies.",
            basePrice = 85.0,
            isVeg = true,
            rating = 4.6,
            ratingCount = 220,
            addons = tiffinAddons,
            imageUrl = "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "t5",
            categoryId = "cat_tiffins",
            name = "Vada",
            description = "Pair of golden-fried crispy lentil fritters spiced with peppercorns, curry leaves, and cumin.",
            basePrice = 70.0,
            isVeg = true,
            rating = 4.6,
            ratingCount = 290,
            addons = tiffinAddons,
            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "t6",
            categoryId = "cat_tiffins",
            name = "Poori",
            description = "Pair of puffed, golden whole-wheat breads served with savory potato-chana masala sagu.",
            basePrice = 80.0,
            isVeg = true,
            rating = 4.5,
            ratingCount = 240,
            addons = tiffinAddons,
            imageUrl = "https://images.unsplash.com/photo-1626074353765-517a681e40be?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "t7",
            categoryId = "cat_tiffins",
            name = "Upma",
            description = "Wholesome roasted semolina cooked with ginger, mustard seeds, curry leaves, and crunchy cashew nuts.",
            basePrice = 60.0,
            isVeg = true,
            rating = 4.3,
            ratingCount = 110,
            addons = tiffinAddons,
            imageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "t8",
            categoryId = "cat_tiffins",
            name = "Pesarattu",
            description = "Nutritious green gram lentil crepe prepared Andhra-style, served with tangy ginger allam pachadi and coconut chutney.",
            basePrice = 85.0,
            isVeg = true,
            isSpecial = true,
            rating = 4.7,
            ratingCount = 195,
            addons = tiffinAddons,
            imageUrl = "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=500&auto=format&fit=crop&q=80"
        ),

        // ================= REFRESHMENTS =================
        Product(
            id = "r1",
            categoryId = "cat_refreshments",
            name = "Thums Up",
            description = "Strong taste, thunderous carbonated Indian cola (Chilled 750ml pet bottle).",
            basePrice = 45.0,
            isVeg = true,
            isBestseller = true,
            rating = 4.9,
            ratingCount = 500,
            imageUrl = "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "r2",
            categoryId = "cat_refreshments",
            name = "Coca-Cola",
            description = "Classic crisp, refreshing carbonated beverage (Chilled 750ml pet bottle).",
            basePrice = 45.0,
            isVeg = true,
            rating = 4.7,
            ratingCount = 310,
            imageUrl = "https://images.unsplash.com/photo-1554866585-cd94860890b7?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "r3",
            categoryId = "cat_refreshments",
            name = "Sprite",
            description = "Clear lemon-lime fizzy soda for immediate refreshment (Chilled 750ml).",
            basePrice = 45.0,
            isVeg = true,
            rating = 4.6,
            ratingCount = 240,
            imageUrl = "https://images.unsplash.com/photo-1625772299848-391b6a87d7b3?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "r4",
            categoryId = "cat_refreshments",
            name = "Fanta",
            description = "Fruity, bright orange flavored carbonated soda (Chilled 750ml).",
            basePrice = 45.0,
            isVeg = true,
            rating = 4.5,
            ratingCount = 180,
            imageUrl = "https://images.unsplash.com/photo-1624517452488-04869289c4ca?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "r5",
            categoryId = "cat_refreshments",
            name = "Mineral Water",
            description = "Packaged purified drinking water with essential minerals (1 Litre sealed bottle).",
            basePrice = 25.0,
            isVeg = true,
            rating = 4.8,
            ratingCount = 410,
            imageUrl = "https://images.unsplash.com/photo-1548839140-29a749e1bc4e?w=500&auto=format&fit=crop&q=80"
        ),
        Product(
            id = "r6",
            categoryId = "cat_refreshments",
            name = "Packaged Juices",
            description = "Chilled Real / Tropicana mixed fruit or mango pulpy juice (1 Litre tetra pack).",
            basePrice = 110.0,
            isVeg = true,
            rating = 4.6,
            ratingCount = 160,
            imageUrl = "https://images.unsplash.com/photo-1613478223719-2ab802602423?w=500&auto=format&fit=crop&q=80"
        )
    )

    val sampleAddresses = listOf(
        Address(
            id = "addr_1",
            label = "Home",
            houseNo = "Flat 402, Sri Sai Nilayam",
            street = "Ayyappa Society Road, Madhapur",
            landmark = "Near Meridian School",
            city = "Hyderabad",
            pinCode = "500081",
            isDefault = true
        ),
        Address(
            id = "addr_2",
            label = "Work",
            houseNo = "Tower B, 4th Floor, Tech Hub",
            street = "Cyber Gateway, Hitech City Main Road",
            landmark = "Opposite Lemon Tree Hotel",
            city = "Hyderabad",
            pinCode = "500081",
            isDefault = false
        )
    )

    val coupons = listOf(
        Coupon(
            code = "ABHILASH50",
            title = "50% OFF up to ₹100",
            discountPercent = 0.50,
            flatDiscount = 0.0,
            minOrderAmount = 199.0,
            maxDiscount = 100.0
        ),
        Coupon(
            code = "FEAST20",
            title = "20% OFF on Family Orders",
            discountPercent = 0.20,
            flatDiscount = 0.0,
            minOrderAmount = 499.0,
            maxDiscount = 150.0
        ),
        Coupon(
            code = "FLAT40",
            title = "Flat ₹40 OFF",
            discountPercent = 0.0,
            flatDiscount = 40.0,
            minOrderAmount = 249.0,
            maxDiscount = 40.0
        )
    )

    val deliveryStaffList = listOf(
        DeliveryStaff("ds_1", "Ramesh Kumar", "+91 98765 43210", "TS 09 EA 4123", true),
        DeliveryStaff("ds_2", "Suresh Reddy", "+91 98765 43211", "TS 07 BD 8901", true),
        DeliveryStaff("ds_3", "Kiran Varma", "+91 98765 43212", "TS 08 FK 5567", true)
    )
}
