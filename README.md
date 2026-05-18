# 🌸 Fiore Flower Shop

A comprehensive web-based flower bouquet management system built with **Java (J2EE)**, **JSP**, **CSS**, **MySQL**, following **MVC architecture**.

---

## 📁 Project Structure

```
fiore-flowershop/
├── pom.xml
├── fiore_db.sql                        ← Run this first
├── README.md
└── src/main/
    ├── java/com/fiore/
    │   ├── controller/                 ← Servlets (MVC Controllers)
    │   │   ├── HomeServlet.java
    │   │   ├── AuthServlet.java
    │   │   ├── ShopServlet.java
    │   │   ├── BouquetDetailServlet.java
    │   │   ├── OrderServlet.java
    │   │   ├── WishlistServlet.java
    │   │   ├── ProfileServlet.java
    │   │   ├── AboutServlet.java
    │   │   ├── ContactServlet.java
    │   │   └── admin/
    │   │       ├── AdminDashboardServlet.java
    │   │       ├── AdminBouquetServlet.java
    │   │       ├── AdminUserServlet.java
    │   │       ├── AdminOrderServlet.java
    │   │       └── AdminReportServlet.java
    │   ├── dao/                        ← Data Access Objects
    │   │   ├── UserDAO.java
    │   │   ├── BouquetDAO.java
    │   │   └── OrderDAO.java
    │   ├── entity/                     ← Model classes
    │   │   ├── User.java
    │   │   ├── Bouquet.java
    │   │   └── Order.java
    │   ├── service/                    ← Business logic layer
    │   │   ├── UserService.java
    │   │   ├── BouquetService.java
    │   │   └── OrderService.java
    │   ├── filter/                     ← Redirect management
    │   │   ├── AuthFilter.java
    │   │   └── AdminFilter.java
    │   └── util/
    │       ├── DBConnection.java
    │       ├── ValidationUtil.java
    │       └── PasswordUtil.java
    └── webapp/
        ├── WEB-INF/
        │   ├── web.xml
        │   └── views/
        │       ├── header.jsp
        │       ├── footer.jsp
        │       ├── home.jsp
        │       ├── shop.jsp
        │       ├── about.jsp
        │       ├── contact.jsp
        │       ├── login.jsp
        │       ├── register.jsp
        │       ├── user/
        │       │   ├── profile.jsp
        │       │   ├── my-orders.jsp
        │       │   └── wishlist.jsp
        │       ├── admin/
        │       │   ├── sidebar.jsp
        │       │   ├── dashboard.jsp
        │       │   ├── bouquets.jsp
        │       │   ├── bouquet-form.jsp
        │       │   ├── users.jsp
        │       │   ├── orders.jsp
        │       │   └── reports.jsp
        │       └── error/
        │           ├── 404.jsp
        │           ├── 403.jsp
        │           └── 500.jsp
        └── static/
            ├── css/style.css
            ├── js/main.js
            └── images/
                ├── logo.png            ← PUT YOUR LOGO HERE
                ├── banner.jpg          ← PUT YOUR BANNER HERE
                ├── bouquets/           ← PUT BOUQUET IMAGES HERE
                │   ├── default.jpg
                │   ├── rose-romance.jpg
                │   ├── eternal-blossoms.jpg
                │   ├── spring-garden.jpg
                │   ├── lavender-dreams.jpg
                │   ├── sunflower-delight.jpg
                │   ├── tulip-elegance.jpg
                │   ├── white-serenity.jpg
                │   └── tropical-burst.jpg
                └── team/               ← PUT TEAM PHOTOS HERE
                    ├── ashma.jpg
                    ├── shreya.jpg
                    ├── sadrishya.jpg
                    ├── supriya.jpg
                    └── sainma.jpg
```

---

## ⚙️ Prerequisites

| Tool | Version |
|------|---------|
| JDK | 11+ |
| Apache Tomcat | 9.x or 10.x |
| MySQL | 8.x |
| Maven | 3.6+ |
| IDE | IntelliJ IDEA / Eclipse / NetBeans |

---

## 🚀 Setup & Run Instructions

### Step 1 — Database

```bash
mysql -u root -p < fiore_db.sql
```

Or open MySQL Workbench, paste the contents of `fiore_db.sql`, and execute.

**Default credentials created:**
- Admin: `admin@fiore.com` / `admin123`
- User:  `priya@example.com` / `user1234`

---

### Step 2 — Configure DB Password

Open `src/main/java/com/fiore/util/DBConnection.java` and update:

```java
private static final String PASSWORD = "your_mysql_password_here";
```

---

### Step 3 — Add Your Images

Place your images in the correct folders:

| Image | Folder | Filename |
|-------|--------|----------|
| Logo | `static/images/` | `logo.png` |
| Home Banner | `static/images/` | `banner.jpg` |
| Bouquet photos | `static/images/bouquets/` | e.g. `rose-romance.jpg` |
| Team photos | `static/images/team/` | `ashma.jpg`, `shreya.jpg`, etc. |
| Default (fallback) | `static/images/bouquets/` | `default.jpg` |

To **enable the banner** on the home page, open `home.jsp` and uncomment:
```jsp
<%-- <img src="${pageContext.request.contextPath}/static/images/banner.jpg"
         alt="Fiore Banner" class="hero-banner-img"> --%>
```

---

### Step 4 — Build

```bash
mvn clean package
```

---

### Step 5 — Deploy to Tomcat

Copy the generated `target/fiore-flowershop.war` to your Tomcat `webapps/` folder, then start Tomcat:

```bash
# Linux / macOS
$CATALINA_HOME/bin/startup.sh

# Windows
%CATALINA_HOME%\bin\startup.bat
```

---

### Step 6 — Access the Application

| URL | Description |
|-----|-------------|
| `http://localhost:8080/fiore-flowershop/home` | Public home page |
| `http://localhost:8080/fiore-flowershop/shop` | Browse bouquets |
| `http://localhost:8080/fiore-flowershop/about` | About us |
| `http://localhost:8080/fiore-flowershop/contact` | Contact form |
| `http://localhost:8080/fiore-flowershop/login` | Login |
| `http://localhost:8080/fiore-flowershop/register` | Register |
| `http://localhost:8080/fiore-flowershop/admin/dashboard` | Admin panel |

---

## 🔐 Authentication & Roles

| Role | Access |
|------|--------|
| **Guest** | Home, Shop, About, Contact, Login, Register |
| **User (approved)** | + Profile, My Orders, Wishlist, Place Orders |
| **Admin** | + Full Admin Panel (CRUD bouquets, manage users & orders, reports) |

New user registrations are **pending** by default. Admin must approve them from the dashboard.

---

## 📋 Features Implemented

- ✅ MVC Architecture (Servlets + JSP + Service + DAO)
- ✅ Role-based authentication (Admin / User)
- ✅ Session management & redirect filters
- ✅ Admin: Create / Read / Update / Delete bouquets
- ✅ Admin: Approve / Reject / Delete users
- ✅ Admin: View & update all orders
- ✅ Admin: Sales vs availability reports
- ✅ User: Register (pending approval), Login
- ✅ User: Browse & search bouquets by name / category
- ✅ User: Place orders with quantity & special note
- ✅ User: Order history
- ✅ User: Session-based wishlist
- ✅ User: Profile management & password change
- ✅ Input validation (server-side + client-side hints)
- ✅ BCrypt password hashing
- ✅ Duplicate phone/email check on registration
- ✅ Custom error pages (404, 403, 500)
- ✅ Responsive design (CSS Flexbox + media queries, no Bootstrap)
- ✅ About Us page with 5 team members
- ✅ Contact form with validation
- ✅ Image placeholders for logo, banner, bouquets, team photos

---

## 🧰 Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | JSP, CSS3 (Flexbox, Media Queries), Vanilla JS |
| Backend | Java 11, J2EE Servlets, JSTL |
| Database | MySQL 8 |
| Security | BCrypt (jBCrypt), Session-based auth |
| Build | Maven |
| Server | Apache Tomcat 9/10 |

---

*Fiore Flower Shop — Where Every Petal Tells a Story 🌸*
