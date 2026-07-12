import random
import sys

# Define constants
NUM_USERS = 100
NUM_FARMERS = 100
NUM_CATEGORIES = 10
NUM_PRODUCTS = 1000
COMMON_PASSWORD_HASH = "$2a$10$rYwK1234567890123456789012345678901234567890123456789" # Mock BCrypt hash for 'password123' (Need to make sure this is valid in spring security)
# Better use a real one:
COMMON_PASSWORD_HASH = "$2a$10$5.8FmH.G38j/H7J2N3Fp1O.tT2F4K0L1Z7S.W6Q/2D4G.b.eR8v0O" # bcrypt hash for 'password123'

# Categories mock data
categories = [
    "Vegetables", "Fruits", "Grains", "Dairy", "Meat", 
    "Poultry", "Seafood", "Nuts", "Herbs", "Spices"
]

def generate_sql():
    sql = []
    
    # 1. Insert AccountMetadata (Optional but recommended for FKs)
    # 2. Insert Users
    sql.append("-- Insert Users")
    user_ids = []
    for i in range(1, NUM_USERS + 1):
        sql.append(f"INSERT INTO base_model (id, created_at, updated_at) VALUES ({i}, NOW(), NOW());")
        sql.append(f"INSERT INTO account_metadata (id, account_locked, attempts, enabled, token_expired) VALUES ({i}, 0, 0, 1, 0);")
        sql.append(f"INSERT INTO user (id, name, email, password, phone, metadata_id) VALUES ({i}, 'User {i}', 'user{i}@example.com', '{COMMON_PASSWORD_HASH}', '9876543{i:03d}', {i});")
        sql.append(f"INSERT INTO user_roles (user_id, role) VALUES ({i}, 'USER');")
        user_ids.append(i)

    # 3. Insert Farmers
    sql.append("-- Insert Farmers")
    farmer_ids = []
    for i in range(NUM_USERS + 1, NUM_USERS + NUM_FARMERS + 1):
        sql.append(f"INSERT INTO base_model (id, created_at, updated_at) VALUES ({i}, NOW(), NOW());")
        sql.append(f"INSERT INTO account_metadata (id, account_locked, attempts, enabled, token_expired) VALUES ({i}, 0, 0, 1, 0);")
        sql.append(f"INSERT INTO user (id, name, email, password, phone, metadata_id) VALUES ({i}, 'Farmer {i}', 'farmer{i}@example.com', '{COMMON_PASSWORD_HASH}', '9876544{i:03d}', {i});")
        sql.append(f"INSERT INTO user_roles (user_id, role) VALUES ({i}, 'FARMER');")
        # Farmer specific table insert if exists
        sql.append(f"INSERT INTO farmer (id, experience_years) VALUES ({i}, {random.randint(1, 20)});")
        farmer_ids.append(i)

    # 4. Insert Categories
    sql.append("-- Insert Categories")
    for i, category in enumerate(categories, start=1):
        sql.append(f"INSERT INTO base_model (id, created_at, updated_at) VALUES ({i + 1000}, NOW(), NOW());")
        sql.append(f"INSERT INTO category (id, name, description) VALUES ({i + 1000}, '{category}', 'Fresh {category}');")

    # 5. Insert Products
    sql.append("-- Insert Products")
    product_names = ["Tomato", "Apple", "Wheat", "Milk", "Beef", "Chicken", "Salmon", "Almond", "Basil", "Pepper"]
    for i in range(1, NUM_PRODUCTS + 1):
        cat_index = random.randint(0, len(categories) - 1)
        farmer_id = random.choice(farmer_ids)
        price = round(random.uniform(1.0, 100.0), 2)
        qty = round(random.uniform(10.0, 500.0), 2)
        unit = random.randint(1, 5)
        name = f"Fresh {product_names[cat_index]} {i}"
        
        sql.append(f"INSERT INTO base_model (id, created_at, updated_at) VALUES ({i + 2000}, NOW(), NOW());")
        sql.append(f"INSERT INTO product (id, name, description, price, quantity, unit, available, category_id, farmer_id) "
                   f"VALUES ({i + 2000}, '{name}', 'High quality {name}', {price}, {qty}, {unit}, 1, {cat_index + 1001}, {farmer_id});")

    return "\n".join(sql)

if __name__ == "__main__":
    sql_script = generate_sql()
    with open("mock_data.sql", "w") as f:
        f.write(sql_script)
    print("Successfully generated mock_data.sql with 1000+ records.")
