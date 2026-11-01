MERGE INTO products (pk, name, price) KEY (pk) VALUES ('S-01', 'Tuna Salad', 6.90);
MERGE INTO products (pk, name, price) KEY (pk) VALUES ('S-02', 'Italian Salad', 7.90);
MERGE INTO products (pk, name, price) KEY (pk) VALUES ('S-03', 'Romaine Salad', 8.90);
MERGE INTO products (pk, name, price) KEY (pk) VALUES ('P-10', 'Pizza Margarita', 5.50);
MERGE INTO products (pk, name, price) KEY (pk) VALUES ('P-11', 'Pizza Capricciosa', 7.50);
MERGE INTO products (pk, name, price) KEY (pk) VALUES ('P-12', 'Pizza Spinach and Feta', 7.00);

MERGE INTO customers (full_name, phone_number, street, postal_code, city) KEY (phone_number)
    VALUES ('Enrico Pallazzo', '+49 123 456789', 'Wasserstr. 123', '40302', 'Atlantis');
MERGE INTO customers (full_name, phone_number, street, postal_code, city) KEY (phone_number)
    VALUES ('Elizabeth Magie', '+1 77 551237', 'Schlossallee 1', '88776', 'Monopolhausen');
