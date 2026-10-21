# Abhilash Restaurant — Food Ordering & Restaurant Management Platform

A modern, high-fidelity native Android food ordering platform built for **Abhilash Restaurant** ("Delicious Food, Delivered Fresh.") inspired by Swiggy and Zomato. This comprehensive application provides three tightly integrated experiences: a customer mobile ordering app, an executive restaurant management dashboard, and a delivery partner dispatch console, linked with synchronized state, rich Indian culinary menus, real-time order lifecycle tracking, and realistic Razorpay/COD payment flows.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The following user preferences were confirmed during initial interactive clarification and will guide the implementation:

- **Full Native Android Scope**: Delivering complete customer, restaurant admin, and delivery partner views directly in native Kotlin and Jetpack Compose for immediate streaming preview and testing.
- **Interactive Role Switcher**: A persistent, discreet role switcher floating chip allowing instantaneous switching between **Customer**, **Restaurant Admin**, and **Delivery Partner** views so any stage of an order can be accepted, prepared, assigned, and delivered in real time.
- **Realistic Razorpay & COD Flow**: Complete payment checkout modal supporting UPI apps (Google Pay, PhonePe, Paytm), Credit/Debit cards, Net Banking, and Cash on Delivery with realistic verification and receipt snapshots.
- **Rich Menu Data**: Authentic Hyderabadi biryanis (Chicken Dum, 65, Fry Piece, Egg, Veg, Paneer, Family Pack), Chinese Indo-fusion dishes, South Indian tiffins (Idli, Dosa varieties, Vada, Poori), and chilled refreshments with customizable portion sizes and add-ons.

---

## 1. Overview & Core Concept

### What It Does
Abhilash Restaurant is an exclusive single-restaurant digital ordering ecosystem. Customers can browse curated categories, customize dishes with portions and add-ons, apply discount coupons, select delivery addresses, and track active orders across 7 distinct lifecycle stages (Placed → Confirmed → Preparing → Ready for Pickup → Out for Delivery → Delivered). Meanwhile, Restaurant Administrators can monitor incoming tickets, accept/reject orders, adjust menu availability and pricing, and assign deliveries to staff. Delivery partners can accept assigned runs and advance deliveries through to completion.

### Target Personas
1. **Food Customers**: Seeking quick discovery, rich food photography, Veg/Non-Veg indicators, portion selections, and transparent order tracking with live ETA.
2. **Restaurant Manager / Chef**: Requiring rapid order triage, kitchen preparation status tracking, menu management, and revenue analytics.
3. **Delivery Executive**: Requiring clear customer contact, delivery address, order item summaries, and status confirmation triggers.

### Key Value
Eliminates third-party aggregator commissions while providing customers with a Swiggy/Zomato-grade ordering experience and providing the restaurant with end-to-end operational visibility.

---

## 2. User Experience & Visual Design

### Key User Flows

```
[Customer Journey]
Splash / Onboarding ──> Home / Menu Discovery ──> Food Details & Add-ons ──> Cart
   │                                                                           │
   └───────────── Order Tracking <─── Razorpay / COD Checkout <────────────────┘

[Operational Journey (via Role Switcher)]
New Customer Order ──> Admin Dashboard (Accept & Cook) ──> Delivery Console (Pickup & Deliver)
```

1. **Splash & Onboarding**: Animated warm branding with tagline *"Delicious Food, Delivered Fresh."*, quick onboarding cards highlighting fast delivery, and seamless phone login (+91 format).
2. **Menu Discovery & Filter**: Search bar with real-time text querying, quick filters (All, Pure Veg, Non-Veg, Today's Specials, Biryani, Chinese, Tiffins, Refreshments), and promotional carousel.
3. **Dish Customization Modal**: Bottom sheet with size variants (Single, Regular, Jumbo / Family Pack), optional add-ons (Extra Raita, Salan, Boiled Egg, Double Masala), special cooking instructions note, and sticky Add-to-Cart bar.
4. **Cart & Dynamic Bill Breakdown**: Live subtotal, delivery fee calculation (free delivery above ₹499), GST breakdown, promotional coupon applicator (`ABHILASH50`, `WELCOMEFREE`), and address selection.
5. **Interactive Razorpay Checkout**: Modal simulating Razorpay checkout with UPI intent selection, card entry, and Cash on Delivery with security safeguards.
6. **Live Order Tracking**: Visual 7-step timeline, restaurant contact details, delivery executive assignment info, and live status progress.
7. **Admin Dashboard**: Live metrics (Today's Orders, Sales in ₹, Active Kitchen Queue, Completed), Order Action Cards (Accept, Move to Preparing, Mark Ready, Assign Driver), and Menu Editor (Toggle Availability, Update Prices).
8. **Delivery Partner Console**: Assigned order queue, customer address details, quick contact action, and one-tap "Picked Up" and "Delivered" actions.

### Visual Identity & Theme
- **Primary Color**: Vibrant Culinary Orange (`#F97316`)
- **Secondary / Accent**: Deep Warm Orange (`#EA580C`) and Emerald Green (`#16A34A`) for Vegetarian badges and success states
- **Background**: Soft Warm Cream (`#FFF9F3`) and Clean Surface White (`#FFFFFF`)
- **Text & Hierarchy**: Charcoal Black (`#1F2937`) for high legibility, Warm Muted Grey (`#6B7280`) for secondary metadata
- **Badges**: Standard FSSAI-style green square with green dot (Pure Veg) and brown square with brown triangle (Non-Veg)
- **Shape Language**: Smooth rounded cards (16dp corner radius), pill-shaped category chips, elevated floating cart summary, and clean Material 3 surfaces

---

## 3. Key Product Decisions & Trade-Offs

| Decision | Selected Approach | Why | Alternatives Considered |
|---|---|---|---|
| **Platform Target** | Native Android (Kotlin + Jetpack Compose) | AI Studio execution environment runs native Android with streaming emulator for instant live verification. | Flutter Web (cannot run natively in current container) |
| **Testing Experience** | Top Floating Role Switcher | Enables testing the entire customer-to-kitchen-to-doorstep lifecycle on a single device without multi-phone sync issues. | Hidden admin menu (slows down demo and review) |
| **State Persistence** | Reactive In-Memory & StateFlow Repository with Room-ready Schema | Instant reactivity, zero latency across simulated roles, with complete exportable Supabase PostgreSQL SQL schema provided for production deployment. | Remote backend dependence (would fail if offline or without live credentials) |
| **Payment Flow** | Simulated Razorpay Sheet | Provides authentic Razorpay UX (UPI IDs, card simulation, and COD) while keeping demo zero-risk and testable immediately. | Static checkout button (fails to show real payment UX) |

---

## 4. Technical Architecture & Data Strategy

### System Architecture

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Abhilash Restaurant App                         │
│                                                                        │
│   ┌───────────────────┐ ┌───────────────────┐ ┌────────────────────┐   │
│   │   Customer UI     │ │    Admin Hub      │ │  Delivery Console  │   │
│   │ (Home, Cart, Track) │ │ (Queue, Menu, Rev)│ │ (Assigned, Update) │   │
│   └─────────▲─────────┘ └─────────▲─────────┘ └─────────▲──────────┘   │
│             │                     │                     │              │
│   ┌─────────┴─────────────────────┴─────────────────────┴──────────┐   │
│   │              RestaurantViewModel / State Hub                   │   │
│   │   - Active User Role (Customer / Admin / Delivery)             │   │
│   │   - Reactive Cart State & Price Calculators                    │   │
│   │   - Order Repository & Status Transitions                      │   │
│   │   - Live Menu Catalog & Stock Management                       │   │
│   └───────────────────────────────▲────────────────────────────────┘   │
│                                   │                                    │
│   ┌───────────────────────────────┴────────────────────────────────┐   │
│   │                   Data & Persistence Layer                     │   │
│   │   - Mock / Seed Catalog (Hyderabadi Biryani, Chinese, Tiffins) │   │
│   │   - Local Order Journal & Status History                       │   │
│   │   - Exportable Supabase PostgreSQL Migration Script (SQL)      │   │
│   └────────────────────────────────────────────────────────────────┘   │
└────────────────────────────────────────────────────────────────────────┘
```

### Core Entities & State

- **`Product`**: ID, title, description, category, basePrice, isVeg, isAvailable, imageUrl, variants (Portions), addOns.
- **`CartItem`**: Product reference, selectedVariant, selectedAddOns, quantity, specialInstructions, calculatedItemTotal.
- **`Order`**: OrderId, customerName, customerPhone, deliveryAddress, items, subtotal, deliveryFee, tax, discount, totalAmount, paymentMethod, paymentStatus, orderStatus (PLACED, CONFIRMED, PREPARING, READY_FOR_PICKUP, OUT_FOR_DELIVERY, DELIVERED, CANCELLED), deliveryPartnerName, deliveryPartnerPhone, placedAt, estimatedDeliveryMinutes.
- **`RestaurantSettings`**: Name, tagline, isOpen, minOrderValue, deliveryFee, freeDeliveryThreshold, contactPhone, address.

### Production Supabase Database Artifact
In addition to the working interactive Android application, a production-grade Supabase SQL migration script (`supabase_schema.sql`) will be packaged containing complete table schemas (`profiles`, `categories`, `products`, `orders`, `order_items`, `payments`, `delivery_assignments`), Row-Level-Security (RLS) policies, and backend validation triggers.

---

## Verification Plan

### Automated Compilation & Build
- Verify successful compilation with `compile_applet`.
- Verify absence of missing imports, unused resources, or broken Jetpack Compose bindings.

### Functional Verification Checklist
1. **Customer Flow**: Browse menu → filter by Pure Veg → open Chicken Dum Biryani → select "Family Pack" + "Extra Salan" → Add to Cart → Apply coupon `ABHILASH50` → Proceed to Checkout → Complete Razorpay UPI payment.
2. **Order Lifecycle**: Observe new order created with active tracking screen (Placed).
3. **Role Switch to Admin**: Switch role to Admin → See new order appear in Live Orders queue → Tap "Accept Order" → Tap "Start Preparing" → Tap "Mark Ready" → Assign to delivery staff "Ramesh (Delivery Partner)".
4. **Role Switch to Delivery**: Switch role to Delivery Partner → View assigned order → Tap "Out for Delivery" → Tap "Order Delivered".
5. **Customer Verification**: Switch back to Customer → Confirm tracking updates to "Delivered" with complete receipt details.
