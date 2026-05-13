# Namma-Mela: Digitizing Company Nataka

**Namma-Mela** is a modern, premium Android application designed to bridge the gap between traditional Indian folk theatre (Company Nataka) and contemporary digital ticketing ecosystems. Built with state-of-the-art Android technologies, it provides a seamless platform for theatre enthusiasts to discover, book, and engage with live stage performances.

---

## 1. Theoretical Analysis & Problem Statement

**The Problem:** Traditional folk theatre, such as the vibrant *Company Nataka* of Karnataka, holds immense cultural significance but suffers from a lack of digital infrastructure. Discovery relies heavily on word-of-mouth or physical posters, while ticketing is strictly offline. This disconnect limits audience reach, particularly among younger, digitally-native demographics, and complicates troupe management.

**The Solution:** Namma-Mela digitizes the entire theatrical lifecycle. It acts as an aggregator and a dedicated community hub. By applying a premium, minimalist User Experience (inspired by Apple’s design philosophy) to an inherently traditional art form, the application elevates the perceived value of the art while providing modern conveniences like real-time seat mapping, digital fan engagement, and analytics.

---

## 2. System Architecture

The application is built on a highly scalable, **Multi-Module Android Architecture** following **MVVM (Model-View-ViewModel)** and **Clean Architecture** principles.

*   **Modularization:** The codebase (~8,500 lines) is divided into logical feature modules (`:feature:home`, `:feature:booking`, `:feature:seatmap`, `:feature:studio`, `:feature:fanwall`, `:feature:profile`) and a common `:core` module. This enforces strict separation of concerns, improves build speeds, and allows for isolated feature development.
*   **State Management:** ViewModels manage UI state using Kotlin `StateFlow`. This reactive approach ensures that the UI (built with Jetpack Compose) automatically re-composes whenever underlying data changes, eliminating manual view updates and race conditions.
*   **Dependency Injection:** Dagger Hilt is utilized to inject dependencies (like Repositories, ViewModels, and Database instances) across the application, decoupling object creation from usage and drastically improving testability.

---

## 3. Technology Stack

*   **Language:** Kotlin 2.0.0 (Leveraging the latest Compose Compiler)
*   **UI Framework:** Jetpack Compose (Declarative UI)
*   **Architecture Components:** ViewModels, Lifecycle, Navigation Compose
*   **Dependency Injection:** Dagger Hilt (v2.50)
*   **Local Persistence:** Room Database (v2.6.1)
*   **Remote/Backend Integration:** Firebase (BOM 32.7.2 - Realtime Database, Authentication, Analytics)
*   **Image Loading:** Coil (v2.6.0) for asynchronous image fetching and caching.
*   **Typography:** Google Fonts Integration (utilizing the *Inter* font family as a modern, readable alternative to proprietary system fonts like San Francisco).

---

## 4. Core Features & UX Philosophy

### Premium Design Aesthetic
The application breaks away from conventional, cluttered ticketing apps. It employs a "Light Mode" aesthetic characterized by pure white surfaces, soft grey backgrounds, generous whitespace, large rounded corner radii (24dp), and high-contrast typography. This minimalist approach allows the vibrant, colorful theatrical posters to stand out.

### Key Modules
1.  **Home Feed:** A curated discovery engine highlighting "Featured Shows" and "All Productions."
2.  **Dynamic Seat Map:** An interactive, Compose-based seat selection grid that visually distinguishes between available, booked, and selected seats in real-time.
3.  **Fan Wall:** A dedicated community space for audience members to share experiences, drop comments, and "applaud" reviews, fostering a digital ecosystem around the physical plays.
4.  **Studio Dashboard:** A management interface for troupe administrators to track live occupancy via interactive donut charts, manage cast profiles, and upload promotional materials.
5.  **User Profile:** Tracks user booking history, digital tickets, and personal information.




## 5. Future Scope & Scalability

While the current foundation provides a robust, interactive frontend and localized mock-state management, the architecture is designed to scale:
*   **Payment Gateway Integration:** Integration of SDKs (like Razorpay or Stripe) into the `:feature:booking` module for actual financial transactions.
*   **Real-time Firebase Sync:** Activating the Firebase Realtime Database listeners to synchronize seat availability globally to prevent double-booking.
*   **Push Notifications:** Utilizing Firebase Cloud Messaging (FCM) to alert users of upcoming shows or last-minute ticket availability.

## 6. Snapshots

1.<img width="200" height="300" alt="6b116e4f-91b8-49fe-856d-d7d7e690e3fe" src="https://github.com/user-attachments/assets/2dc100c1-321e-45f8-a17a-9991094cbf33" />**Log In Page**
2.<img width="200" height="300" alt="372bcf55-7474-4336-9a9e-b0a5767cb552" src="https://github.com/user-attachments/assets/08bffc60-21cc-4815-a344-9a10e7ff5c88" />**Dashboard**
3.<img width="200" height="300" alt="669f72f3-8154-4a3e-b9d7-e1639428cc5b" src="https://github.com/user-attachments/assets/968cc99f-562f-44c2-abef-80a138009f06" />**Map Meta Data**






---

*Prepared for Project Presentation & Technical Review.*
