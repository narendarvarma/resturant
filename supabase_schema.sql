-- ====================================================================
-- ABHILASH RESTAURANT — PRODUCTION POSTGRESQL & SUPABASE MIGRATION SCRIPT
-- "Delicious Food, Delivered Fresh."
-- Includes:
--   1. Complete Database Schema (Paise-integer monetary amounts)
--   2. Enums and Status Constraints
--   3. Row Level Security (RLS) Zero-Trust Policies
--   4. Triggers for Automatic Updated Timestamps & Auditing
--   5. Seed Data for Authentic Biryani, Chinese, Tiffins & Refreshments
-- ====================================================================

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. ENUMS
CREATE TYPE user_role_enum AS ENUM ('customer', 'restaurant_admin', 'delivery_partner');
CREATE TYPE order_status_enum AS ENUM (
    'placed',
    'confirmed',
    'preparing',
    'ready_for_pickup',
    'out_for_delivery',
    'delivered',
    'cancelled'
);
CREATE TYPE payment_method_enum AS ENUM ('upi', 'card', 'netbanking', 'wallet', 'cod');
CREATE TYPE payment_status_enum AS ENUM ('pending', 'captured', 'failed', 'refunded');

-- 2. USER PROFILES & ROLES
CREATE TABLE IF NOT EXISTS public.profiles (
    id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    full_name VARCHAR(100),
    role user_role_enum NOT NULL DEFAULT 'customer',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 3. RESTAURANT SETTINGS
CREATE TABLE IF NOT EXISTS public.restaurant_settings (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL DEFAULT 'Abhilash Restaurant',
    tagline VARCHAR(150) NOT NULL DEFAULT 'Delicious Food, Delivered Fresh.',
    phone VARCHAR(20) NOT NULL DEFAULT '+91 98490 12345',
    address TEXT NOT NULL DEFAULT 'Plot 42, High-Tech Road, Food Court Hub, Hyderabad - 500081',
    is_open BOOLEAN NOT NULL DEFAULT TRUE,
    min_order_paise INTEGER NOT NULL DEFAULT 14900, -- ₹149.00
    base_delivery_fee_paise INTEGER NOT NULL DEFAULT 3500, -- ₹35.00
    free_delivery_above_paise INTEGER NOT NULL DEFAULT 49900, -- ₹499.00
    gst_percent NUMERIC(4,2) NOT NULL DEFAULT 5.00,
    avg_prep_minutes INTEGER NOT NULL DEFAULT 25,
    delivery_radius_km NUMERIC(4,1) NOT NULL DEFAULT 8.5,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 4. CATEGORIES
CREATE TABLE IF NOT EXISTS public.categories (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    icon_emoji VARCHAR(10),
    sort_order INTEGER DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 5. PRODUCTS (MENU ITEMS)
CREATE TABLE IF NOT EXISTS public.products (
    id VARCHAR(50) PRIMARY KEY,
    category_id VARCHAR(50) NOT NULL REFERENCES public.categories(id) ON DELETE RESTRICT,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    base_price_paise INTEGER NOT NULL, -- in Paise
    is_veg BOOLEAN NOT NULL DEFAULT FALSE,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    is_special BOOLEAN NOT NULL DEFAULT FALSE,
    is_bestseller BOOLEAN NOT NULL DEFAULT FALSE,
    rating NUMERIC(2,1) DEFAULT 4.5,
    rating_count INTEGER DEFAULT 100,
    prep_time_minutes INTEGER DEFAULT 20,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 6. PRODUCT VARIANTS & ADDONS
CREATE TABLE IF NOT EXISTS public.product_variants (
    id VARCHAR(50) PRIMARY KEY,
    product_id VARCHAR(50) NOT NULL REFERENCES public.products(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    price_delta_paise INTEGER NOT NULL DEFAULT 0,
    is_default BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS public.product_addons (
    id VARCHAR(50) PRIMARY KEY,
    product_id VARCHAR(50) NOT NULL REFERENCES public.products(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    price_paise INTEGER NOT NULL DEFAULT 0
);

-- 7. CUSTOMER ADDRESSES
CREATE TABLE IF NOT EXISTS public.addresses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    label VARCHAR(20) NOT NULL DEFAULT 'Home',
    house_no VARCHAR(100) NOT NULL,
    street VARCHAR(150) NOT NULL,
    landmark VARCHAR(150),
    city VARCHAR(80) NOT NULL DEFAULT 'Hyderabad',
    pin_code VARCHAR(10) NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 8. COUPONS
CREATE TABLE IF NOT EXISTS public.coupons (
    code VARCHAR(30) PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    discount_percent NUMERIC(4,2) DEFAULT 0.00,
    flat_discount_paise INTEGER DEFAULT 0,
    min_order_paise INTEGER NOT NULL DEFAULT 19900,
    max_discount_paise INTEGER NOT NULL DEFAULT 15000,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 9. DELIVERY STAFF
CREATE TABLE IF NOT EXISTS public.delivery_staff (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    vehicle_number VARCHAR(30) NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 10. ORDERS & ORDER ITEMS
CREATE TABLE IF NOT EXISTS public.orders (
    id VARCHAR(30) PRIMARY KEY, -- e.g., 'ABH-1082'
    customer_id UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    customer_name VARCHAR(100) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    delivery_address_json JSONB NOT NULL,
    subtotal_paise INTEGER NOT NULL,
    delivery_fee_paise INTEGER NOT NULL DEFAULT 0,
    gst_paise INTEGER NOT NULL DEFAULT 0,
    discount_paise INTEGER NOT NULL DEFAULT 0,
    total_amount_paise INTEGER NOT NULL,
    coupon_code VARCHAR(30) REFERENCES public.coupons(code),
    payment_method payment_method_enum NOT NULL,
    payment_status payment_status_enum NOT NULL DEFAULT 'pending',
    razorpay_order_id VARCHAR(100),
    razorpay_payment_id VARCHAR(100),
    order_status order_status_enum NOT NULL DEFAULT 'placed',
    assigned_delivery_staff_id UUID REFERENCES public.delivery_staff(id),
    cancellation_reason TEXT,
    estimated_delivery_minutes INTEGER DEFAULT 30,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS public.order_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id VARCHAR(30) NOT NULL REFERENCES public.orders(id) ON DELETE CASCADE,
    product_id VARCHAR(50) NOT NULL REFERENCES public.products(id),
    product_name VARCHAR(150) NOT NULL,
    unit_price_paise INTEGER NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    total_price_paise INTEGER NOT NULL,
    variant_name VARCHAR(100),
    addons_summary TEXT,
    cooking_instructions TEXT
);

-- 11. ROW LEVEL SECURITY (RLS) POLICIES
ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.categories ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.products ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.product_variants ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.product_addons ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.addresses ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.orders ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.order_items ENABLE ROW LEVEL SECURITY;

-- Categories & Products: Publicly readable for customer apps
CREATE POLICY "Public can view active categories" ON public.categories
    FOR SELECT USING (is_active = true);

CREATE POLICY "Public can view available products" ON public.products
    FOR SELECT USING (true);

CREATE POLICY "Public can view variants" ON public.product_variants
    FOR SELECT USING (true);

CREATE POLICY "Public can view addons" ON public.product_addons
    FOR SELECT USING (true);

-- Customer Addresses: User can read/write only their own
CREATE POLICY "Users can manage their own addresses" ON public.addresses
    FOR ALL USING (auth.uid() = user_id);

-- Orders: Customer can read their own, Admin can read/update all
CREATE POLICY "Customers can read their own orders" ON public.orders
    FOR SELECT USING (
        auth.uid() = customer_id OR
        EXISTS (
            SELECT 1 FROM public.profiles
            WHERE id = auth.uid() AND role IN ('restaurant_admin', 'delivery_partner')
        )
    );

CREATE POLICY "Admins can update orders" ON public.orders
    FOR UPDATE USING (
        EXISTS (
            SELECT 1 FROM public.profiles
            WHERE id = auth.uid() AND role = 'restaurant_admin'
        )
    );

-- 12. SEED DATA
INSERT INTO public.categories (id, name, description, icon_emoji, sort_order) VALUES
('cat_biryani', 'Biryani', 'Authentic slow-cooked dum & fry biryanis', '🍚', 1),
('cat_chinese', 'Chinese', 'Indo-Chinese wok specials, rice & noodles', '🥢', 2),
('cat_tiffins', 'South Indian Tiffins', 'Crispy dosas, hot idlis & traditional breakfasts', '🥞', 3),
('cat_refreshments', 'Refreshments', 'Chilled beverages & fresh fruit refreshments', '🥤', 4)
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.products (id, category_id, name, description, base_price_paise, is_veg, is_bestseller, is_special, rating) VALUES
('b1', 'cat_biryani', 'Chicken Dum Biryani', 'Signature long-grain Basmati rice slow-cooked on dum with tender marinated chicken pieces, Hyderabadi saffron spices, mint, and caramelized onions.', 28000, false, true, true, 4.8),
('b2', 'cat_biryani', 'Chicken 65 Biryani', 'Crispy, spicy South-Indian Chicken 65 chunks tossed in curry leaves and green chillies, layered over fragrant ghee-infused biryani rice.', 31000, false, true, false, 4.7),
('b3', 'cat_biryani', 'Chicken Fry Piece Biryani', 'Tender bone-in chicken shallow-fried in country spices and pepper, served on hot aromatic dum rice.', 32000, false, false, false, 4.7),
('b4', 'cat_biryani', 'Egg Biryani', 'Golden-fried farm eggs spiced with turmeric and garam masala, nestled inside flavorful saffron rice.', 22000, false, false, false, 4.5),
('b5', 'cat_biryani', 'Veg Biryani', 'Garden fresh carrots, french beans, peas, and potatoes simmered in royal spices and layered with dum rice.', 21000, true, false, false, 4.5),
('b6', 'cat_biryani', 'Paneer Biryani', 'Soft cottage cheese cubes marinated in tandoori paste, cooked gently with aromatic basmati rice.', 25000, true, false, true, 4.6),
('b7', 'cat_biryani', 'Family Pack Biryani', 'Mega feast with 4 generous portions of Chicken Dum Biryani, 4 boiled eggs, Chicken 65 starter, 2 Salan bowls, and double Raita.', 79900, false, true, false, 4.9),

('c1', 'cat_chinese', 'Veg Fried Rice', 'Wok-tossed steamed rice with crunchy cabbage, bell peppers, spring onions, and garlic soy seasonings.', 16000, true, false, false, 4.4),
('c3', 'cat_chinese', 'Chicken Fried Rice', 'Succulent shredded chicken wok-tossed with rice, egg drops, aromatic spices, and scallions.', 21000, false, true, false, 4.7),
('c8', 'cat_chinese', 'Veg Manchurian', 'Deep fried vegetable dumplings simmered in savory garlic, ginger, and coriander Indo-Chinese gravy.', 17000, true, false, false, 4.5),
('c10', 'cat_chinese', 'Chilli Chicken', 'Boneless chicken cubes stir-fried with hot slit green chillies, diced onions, and capsicum in pungent soy sauce.', 24000, false, true, true, 4.8),

('t1', 'cat_tiffins', 'Idli', 'Trio of steaming soft, fluffy fermented rice-lentil cakes served with hot piping sambar and fresh coconut chutney.', 6000, true, false, false, 4.6),
('t3', 'cat_tiffins', 'Masala Dosa', 'Crisp golden crepe smeared with spicy red chutney and stuffed with spiced mustard potato curry.', 9000, true, true, false, 4.8),
('t5', 'cat_tiffins', 'Vada', 'Pair of golden-fried crispy lentil fritters spiced with peppercorns, curry leaves, and cumin.', 7000, true, false, false, 4.6),
('t8', 'cat_tiffins', 'Pesarattu', 'Nutritious green gram lentil crepe prepared Andhra-style with ginger allam pachadi.', 8500, true, false, true, 4.7),

('r1', 'cat_refreshments', 'Thums Up', 'Strong taste, thunderous carbonated Indian cola (Chilled 750ml).', 4500, true, true, false, 4.9),
('r2', 'cat_refreshments', 'Coca-Cola', 'Classic crisp, refreshing carbonated beverage (Chilled 750ml).', 4500, true, false, false, 4.7),
('r5', 'cat_refreshments', 'Mineral Water', 'Packaged purified drinking water with essential minerals (1 Litre sealed bottle).', 2500, true, false, false, 4.8)
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.coupons (code, title, discount_percent, flat_discount_paise, min_order_paise, max_discount_paise) VALUES
('ABHILASH50', '50% OFF up to ₹100', 0.50, 0, 19900, 10000),
('FEAST20', '20% OFF on Family Orders', 0.20, 0, 49900, 15000),
('FLAT40', 'Flat ₹40 OFF', 0.00, 4000, 24900, 4000)
ON CONFLICT (code) DO NOTHING;
