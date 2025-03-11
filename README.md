
# Task Management App

## Overview
This app is designed to manage a list of tasks. It utilizes **Jetpack Compose** for the UI, **Hilt** for dependency injection, and follows **Clean Architecture** principles for scalability and maintainability. The app supports basic CRUD operations, with a focus on displaying a list of tasks and showing details of a selected task.

## Tech Stack
- **Kotlin**: Programming language
- **Jetpack Compose**: UI framework
- **Hilt**: Dependency Injection
- **Flow**: Asynchronous data streams (for data loading)
- **Room**: For task persistence
- **Coroutines**: For asynchronous tasks

---

## Setup Instructions

### 1. Clone the Repository


### 2. Open in Android Studio
Open the project in Android Studio, ensuring your **Gradle** and **Kotlin** versions are up to date.

### 3. Add Dependencies
Make sure you have the required dependencies in your `build.gradle.kts` files.

#### **`build.gradle.kts` (app module)**:
```kotlin
dependencies {
    // Jetpack Compose
    implementation("androidx.compose.ui:ui:1.4.0")
    implementation("androidx.compose.material:material:1.4.0")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Hilt DI
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-android-compiler:2.50")

    // ViewModel & LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
}
```

### 4. Enable Hilt
In your `AndroidManifest.xml`, ensure you have the following:

```xml
<application
    android:name=".MyApplication"
    ... >
</application>
```

And in `MyApplication.kt`, annotate your application class with `@HiltAndroidApp`:
```kotlin
@HiltAndroidApp
class MyApplication : Application()
```

### 5. Sync the Project
After updating the dependencies and configurations, sync the project with Gradle files.

---

## Architecture Overview

This project follows **Clean Architecture** principles to ensure a **scalable** and **maintainable** application. Here's a breakdown of how the code is structured:

### 1. **Domain Layer**
- **Entities/Models**: The core business models, such as `Task`.
- **Use Cases**: Encapsulates business logic. For example:
    - `GetAllTasksUseCase`: Fetches all tasks.
    - `GetTaskByIdUseCase`: Fetches a task by its ID.
- **Repository Interface**: Defines the contract for data access (`TaskRepository`).

### 2. **Data Layer**
- **Repository Implementation**: Implements data fetching logic, whether from a local database (e.g., Room) or a remote server (API). Example: `TaskRepositoryImpl`.
- **Data Sources**: Defines how data is retrieved. For simplicity, a mock implementation is used in `TaskRepositoryImpl`.

### 3. **Presentation Layer**
- **ViewModel**: Contains logic to handle data and prepare it for the UI. `TaskViewModel` interacts with `GetAllTasksUseCase` and `GetTaskByIdUseCase` to fetch data.
- **UI (Jetpack Compose)**: Composables that display the UI. `TaskScreen` displays the list of tasks, and clicking on a task will show its details.

---

## Key Features

### **Get All Tasks**:
The `GetAllTasksUseCase` fetches all tasks from the repository and updates the UI in real-time using **Flow**. It supports a reactive UI that will automatically update when the task list changes.

### **Get Task by ID**:
The `GetTaskByIdUseCase` fetches a specific task by its ID. When a task is clicked in the list, the app shows its details.

### **Dependency Injection with Hilt**:
The app uses **Hilt** for dependency injection, ensuring that components like repositories, use cases, and view models are properly injected where needed, making the code modular and easy to test.

---

## Design Rationale

### **Clean Architecture**
The app follows the **Clean Architecture** pattern, which divides the project into distinct layers:
- **Domain**: Holds the business logic and core models.
- **Data**: Manages data fetching, including repository and data sources.
- **Presentation**: Manages UI-related tasks, including ViewModel and Composables.

This separation of concerns ensures that the codebase is **scalable**, **testable**, and **easy to maintain**.

### **Use of Flow**
We leverage **Flow** for data streams, allowing for real-time updates in the UI. This is especially useful in cases where data changes frequently, like when tasks are added, updated, or removed.

### **Hilt for Dependency Injection**
**Hilt** is used to simplify dependency injection, reducing boilerplate code. By using **constructor injection**, we ensure that the dependencies are properly managed by the system and are easy to mock during unit testing.

### **Jetpack Compose**
We use **Jetpack Compose** for building the UI declaratively. Compose allows us to write concise UI code that is easy to read and maintain, while also supporting modern features like **recomposition** and **state management**.


