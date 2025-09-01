## Expense Tracker Android

A simple mobile app for tracking expenses.  
Tech stack: **Kotlin**, **MVVM**, **Room**

---

## 🚀 Features

- Add, edit, and delete expenses
- View expense history
- Categories support
- Home screen widget for quick overview

---

## 🏗️ Architecture

The project follows the **MVVM** architecture pattern with **Room Database** for local persistence.

---

## 🗄️ Database schema

```
@Entity(tableName = "expenses",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["category_id"],
        childColumns = ["category_id"]
    )])
data class Expense(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "value") val value: Double?,
    @ColumnInfo(name = "category_id") val categoryID: Int? = null,
    @ColumnInfo(name = "type") val expenseType: ExpenseType,
    @ColumnInfo(name = "created_at") val createdAt: Long
)

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("category_id")
    val categoryID: Int?,
    @ColumnInfo(name = "category_name") val categoryName: String
```

## 📸 Screenshots

<p align="center">
<img width="700" height="2992" alt="Image" src="https://github.com/user-attachments/assets/6ba933c0-066f-4065-bdf4-619cf358551f" />
<img width="1344" height="2992" alt="Image" src="https://github.com/user-attachments/assets/ff7a6759-b75a-4c34-9c98-a754e5e5fd8b" />
<img width="1344" height="2992" alt="Image" src="https://github.com/user-attachments/assets/fbc57a90-e5a8-4114-a35d-6d7366cc0159" />
<img width="1344" height="2992" alt="Image" src="https://github.com/user-attachments/assets/b631623b-152a-469c-96a6-80c4da4388da" />
<img width="1344" height="2992" alt="Image" src="https://github.com/user-attachments/assets/f5050881-cdaf-4c08-a257-76a3308fc829" />
</p>

## ⚙️ Installation

1. Clone the repository
```
git clone https://github.com/your-repo/expense-tracker.git
```
2. Open the project in Android Studio

3. Run on an emulator or a physical device

