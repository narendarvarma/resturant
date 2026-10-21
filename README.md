# Abhilash Restaurant — Food Ordering & Restaurant Management Platform

> *"Delicious Food, Delivered Fresh."*

A production-oriented food ordering and restaurant dispatch platform inspired by Swiggy and Zomato, built exclusively for **Abhilash Restaurant**. This ecosystem integrates a Customer Mobile Ordering Application, a Kitchen/Restaurant Admin POS & Management Dashboard, and a Delivery Partner Dispatch Console into a unified Android native application with real-time state synchronization, authentic Hyderabadi culinary menus, Razorpay payment flows, and complete Supabase PostgreSQL backend migrations.

---

## 🌟 Key Features

### 1. Customer Mobile Application
- **Menu Discovery**: Curated categories for **Biryani**, **Indo-Chinese**, **South Indian Tiffins**, and **Refreshments**.
- **Portion & Add-on Customization**: Choose portion sizes (Single, Regular, Jumbo / Family Pack) and add-ons (Extra Mirchi Ka Salan, Onion Raita, Boiled Eggs, Double Masala, Podi Ghee Drizzle).
- **Special Cooking Instructions**: Direct kitchen notes per dish (e.g. *"less spicy, extra lemon slices"*).
- **Pure Veg & Price Filters**: Instant one-tap FSSAI-style Pure Veg toggle and price sorting (Low to High, High to Low).
- **Smart Shopping Cart**: Real-time subtotal, delivery fee calculation (Free delivery above ₹499), GST breakdown (5%), and promotional coupon discounts (`ABHILASH50`, `FEAST20`, `FLAT40`).
- **Interactive Razorpay Checkout**: Test gateway modal supporting UPI (Google Pay, PhonePe, Paytm, BHIM), Saved Cards, Net Banking, and Cash on Delivery (COD).
- **Live 6-Step Order Tracking**: Real-time status progression (Placed → Confirmed → Kitchen Prep → Ready → Out for Delivery → Delivered) with dynamic ETA, assigned delivery partner name, phone, and vehicle number.
- **Saved Addresses & Profile**: Manage multiple addresses (Home, Work, Other) with instant active selection.

### 2. Restaurant Admin Dashboard
- **Live Kitchen Queue**: Real-time order cards with Accept/Reject, "Start Kitchen Preparation", and "Mark Ready & Assign Delivery Partner" actions.
- **Analytics & Revenue**: Track today's orders, revenue in ₹, active kitchen tickets, completed orders, and top-selling dishes.
- **Menu Stock Management**: Live switches to mark any item Available or Sold Out on the fly, with inline price editing.
- **Operational Settings**: Toggle online ordering status (Open/Closed), configure minimum order value, base delivery fee, and free delivery thresholds.

### 3. Delivery Partner Console
- **Assigned Run Queue**: Drivers see assigned orders with customer name, contact phone, and full drop address.
- **One-Tap Status Progression**: Confirm "Food Picked Up from Restaurant" and advance to "Mark Order Delivered".
- **Payment Verification**: Clear badge indicating whether the order is prepaid online via UPI/Card or requires Cash on Delivery (COD) collection.

### 4. Interactive Role Switcher
- A discreet floating chip at the top right allows instant switching between **Customer**, **Restaurant Admin**, and **Delivery Partner** views on the same device so that an order placed by the customer can be accepted by the admin, assigned to a driver, picked up, and delivered in real time!

---

## 🏗️ Architecture & Technology Stack

| Layer | Technology |
|---|---|
| **Client Framework** | Android Native (Kotlin + Jetpack Compose) |
| **Design System** | Material Design 3 (M3) with custom Abhilash color tokens |
| **State Management** | MVVM with `ViewModel`, `StateFlow`, and reactive pipelines |
| **Launcher Icon** | Custom Adaptive Icon with handi pot & royal cutlery branding (`#F97316`) |
| **Database Schema** | Supabase PostgreSQL (`supabase_schema.sql` included) |
| **Security & RLS** | Zero-trust Row-Level Security policies with role-based permissions |
| **Monetary Precision** | Integer paise values (e.g. ₹280.00 = 28000 paise) preventing floating-point rounding errors |

---

## 🗄️ Database Schema & Supabase Migrations

The complete database migration script is located in `supabase_schema.sql`:

1. **`profiles`**: User profiles with `user_role_enum` (`customer`, `restaurant_admin`, `delivery_partner`).
2. **`categories`**: Menu categories with emoji icons and sort ordering.
3. **`products`**: Menu items with base prices in paise, FSSAI veg/non-veg flag, bestseller badges, and stock availability.
4. **`product_variants` & `product_addons`**: Portions and customizable add-on items.
5. **`addresses`**: Customer drop locations with labels and PIN codes.
6. **`orders` & `order_items`**: Full order lifecycle, item snapshots, delivery assignments, and payment statuses.
7. **`coupons`**: Discount rules with minimum spend and maximum caps.
8. **`delivery_staff`**: Registered restaurant drivers with vehicle numbers and availability status.
9. **`restaurant_settings`**: Operational hours, delivery fee thresholds, and tax percentages.

To deploy to your Supabase project:
1. Open your Supabase Dashboard → **SQL Editor**.
2. Paste the contents of `supabase_schema.sql` and run the script.
3. Enable Phone Authentication under **Authentication → Providers → Phone**.

---

## 🧪 Testing & Verification

- **Robolectric Local JVM Tests**: Run via `gradle :app:testDebugUnitTest`.
- **Verified Unit Tests**: Context assertions and string resources verified successfully.

---

## 🚀 Building the Project

### Running in Android Studio:
1. Open the repository root in Android Studio Ladybug / Meerkat or later.
2. Ensure Android SDK 36 is installed.
3. Run on connected device or emulator (`Shift + F10`).

### Build APK via CLI:
```bash
gradle :app:assembleDebug
```
The compiled APK will be generated at:
`app/build/outputs/apk/debug/app-debug.apk`
